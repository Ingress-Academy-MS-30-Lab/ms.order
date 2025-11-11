package az.ingress.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* az.ingress.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        logger.info("ActionLog.{}.START", methodName);
        try {
            Object result = joinPoint.proceed();
            logger.info("ActionLog.{}.SUCCESS", methodName);
            return result;
        } catch (Exception ex) {
            logger.error("ActionLog.{}.FAILED - Error: {}",
                    methodName,
                    ex.getMessage(), ex);
            throw ex;
        }
    }


}