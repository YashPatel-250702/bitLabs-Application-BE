package com.talentstream.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectAop {

    @Before("execution(* com.talentstream.service.*.*(..))")
    public void printMessageBeforeMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Method execution starting: " + className + "." + methodName + "()");
    }

    @After("execution(* com.talentstream.service.*.*(..))")
    public void printMessageAfterMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Method execution completed: " + className + "." + methodName + "()");
    }

    @Around("execution(* com.talentstream.service.*.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        
        System.out.println("Executing method: " + className + "." + methodName + "()");

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            System.out.println("Exception occurred in method: " + className + "." + methodName + "() - " + ex.getMessage());
            throw ex;
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution completed: " + className + "." + methodName + "() in " + (endTime - startTime) + " ms");

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.talentstream.service.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        
        System.out.println("Exception in method: " + className + "." + methodName + "()");
        System.out.println("Exception message: " + ex.getMessage());
    }
}
