package com.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Aspect
@Component
public class ApplicationMonitor {

    @Autowired
    HttpServletRequest request;

    @Value("${api.performance.threshold:#{20}}")
    private long apiPerfomanceMaxThreshold;

    @Autowired
    private ApplicationContext context;

    private boolean isTracingEnabled = false;


    @PostConstruct
    private void init(){
        try{
            ApplicationTracerManager enableLogManager = context.getBean(ApplicationTracerManager.class);
            if(Objects.nonNull(enableLogManager)){
                isTracingEnabled = enableLogManager.isTracingEnabled();
            }
        }catch (Exception e){
            isTracingEnabled = false;
        }
        System.out.println(isTracingEnabled);
    }

    //@Before("execution(public * com.springboot.controller.ConfigurationPropertyController+.saveConfig(..))")
   /* public void beforeFindById(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        Student student = (Student) args[0];
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!1111 "+student.toString());
        System.out.println("############# "+methodName+"##################"+declaringTypeName);

    }

    @After("execution(public * com.springboot.controller.ConfigurationPropertyController+.saveConfig(..))")
    public void AfterFindById(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        System.out.println("@@@@@@@@@@@@@@@@@@@ "+methodName+"@@@@@@@@@@@@@@@@@22 "+declaringTypeName);

    }

    @Before("execution(public * com.springboot.controller.ConfigurationPropertyController+.saveConfig(..)) && args(student) && target(service)")
    public void beforeFindById2(Student student , ConfigurationPropertyController service) throws Exception {
        service.saveConfig(student);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!1111 "+student.toString());
    }

    @AfterReturning(value = "execution(public * com.springboot.controller.ConfigurationPropertyController+.saveConfig(..))", returning = "student")
    public void AfterFindById2(JoinPoint joinPoint,Student student) {
        String methodName = joinPoint.getSignature().getName();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        System.out.println("In Student : "+student.toString());
        System.out.println("@@@@@@@@@@@@@@@@@@@ "+methodName+"@@@@@@@@@@@@@@@@@22 "+declaringTypeName);

    }

    //@Around("execution(public * com.springboot.controller.ConfigurationPropertyController+.saveConfig(..)) && args(student) && target(service)")
    public void TestAroundFindById(ProceedingJoinPoint joinPoint,Student student , ConfigurationPropertyController service) {
        System.out.println("In TestAroundFindById");
        Object o = null;
        try {
            o = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println("In exception");
        }
        System.out.println("@@@ Result is "+o);

    }*/

    //@Around("@annotation(EnableRestCallLogs)")
    //@Around("@annotation(EnableRestCallLogs)")
    @Around("classLevelAnnotations()")
    public Object getLogDetails(ProceedingJoinPoint joinPoint) throws Throwable {

        if(!isTracingEnabled){
            return joinPoint.proceed();
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@ is "+apiPerfomanceMaxThreshold);
        String methodEntryMsg = null,methodExitMsg = null,requestedPath = null, methodType=null;;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        Annotation annotation = getAnnotationText(Class.forName(className));

        if(Objects.nonNull(annotation) && annotation instanceof LogRestCallExecutionTime){
            LogRestCallExecutionTime log = (LogRestCallExecutionTime) annotation;
            //methodEntryMsg = log.methodEntryMsg();
            methodExitMsg = log.methodExitMsg();
        }
        String methodName = methodSignature.getMethod().getName();
        System.out.println(methodEntryMsg+" "+className+"."+methodName);
        Instant startTime = Instant.now();
        HttpStatus httpStatus=null;
        Object o = null;
        long elapsedTime;

        getMethodAttributes(methodSignature,joinPoint.getArgs());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        requestedPath = request.getContextPath();
        methodType = request.getMethod();
        try{
            o = joinPoint.proceed();
        }catch(Exception e){
            httpStatus = extractHttpStatus(e);
            throw e;
        }finally {
            if(Objects.isNull(httpStatus)){
                httpStatus = extractHttpStatus(o);
            }
            elapsedTime = Duration.between(startTime, Instant.now()).toMillis();
            System.out.println(methodExitMsg+" "+className+"."+methodName);
        }
        System.out.println(methodExitMsg+" "+className+"."+methodName);
        System.out.println("Total Elapsed Time : {} "+elapsedTime + "ms");
        if(elapsedTime>apiPerfomanceMaxThreshold){
            System.out.println("Total Elapsed Time : {} "+elapsedTime + "ms");
        }
       /* LOGGER.info("Class Name: {}, Method Name: {}, Additional Message: {}, Elapsed Time: {}ms",
                className, methodName, additionalMessage, elapsedTime);*/
        return o;
    }

    @Pointcut("execution(* (@com.springboot.aop.LogRestCallExecutionTime *).*(..))")
    public void classLevelAnnotations() {}


    private Annotation getAnnotationText(Class reflectClass) {
        if (reflectClass.isAnnotationPresent(LogRestCallExecutionTime.class)) {
            Annotation a = reflectClass.getAnnotation(LogRestCallExecutionTime.class);
            LogRestCallExecutionTime annotation = (LogRestCallExecutionTime) a;
            return annotation;
        }
        return null;
    }

    private HttpStatus extractHttpStatus(Object response) {
        if (Objects.nonNull(response) && response instanceof ResponseEntity) {
            return ((ResponseEntity<?>)response).getStatusCode();
        }
        return null;
    }

    // Helper method to extract HTTP status from the exception
    private HttpStatus extractHttpStatus(Throwable ex) {
        if(ex instanceof HttpStatusCodeException) {
            return ((HttpStatusCodeException)ex).getStatusCode();
        }
        return null;
    }

    private void getParameters(){

    }

    private void getMethodAttributes(MethodSignature methodSignature , Object[] args){
        List<String> parametrList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        if(Objects.nonNull(methodSignature)){
            parametrList = Arrays.stream(methodSignature.getParameterNames()).collect(Collectors.toList());
        }
        if(Objects.nonNull(args)){
            valueList = Arrays.stream(args).collect(Collectors.toList());
        }
        Map<String ,Object> map = new HashMap<>();
        int count = 0;

        for(String s : parametrList){
            map.put(s,valueList.get(count));
            count++;
        }


    }

}

/*
    @Pointcut("execution(@com.test.MyAnnotation * *.*(..))")
    public void annotatedMethod() {}

    @Pointcut("execution(* (@com.test.MyAnnotation *).*(..))")
    public void annotatedClass() {}

    @Pointcut("execution(* *(..)) && target(com.test.MyAnnotable)")
    public void implementedInterface() {}

    @Around("annotatedMethod() || annotatedClass() || implementedInterface()")
    public Object aop(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("AOP IS WORKING");
        return pjp.proceed;
    }
*/
