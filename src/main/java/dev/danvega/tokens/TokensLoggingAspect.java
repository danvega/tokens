package dev.danvega.tokens;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokensLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(TokensLoggingAspect.class);

    @Pointcut("within(dev.danvega.tokens.TokenController)")
    public void tokensMethods() {}

    @Around("tokensMethods()")
    public Object logTokenUsage(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Entering method: {}", methodName);
        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
            if (result instanceof CustomResponse response) {
                logger.info("Token usage for {}: {}", methodName, response.usage());
            } else if (result instanceof ChatResponse response) {
                logger.info("Token usage for {}: {}", methodName, response.getMetadata().getUsage());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error in {}: {}", methodName, e.getMessage());
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Exiting method: {} (execution time: {}ms)", methodName, executionTime);
        }
    }
}
