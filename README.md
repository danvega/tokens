# Spring AI Tokens Project

Track and monitor token usage in your Spring AI applications with ease. This project demonstrates how to implement token usage tracking and logging using Spring AI, AspectJ, and Spring Boot.

## Overview

This Spring Boot application showcases the integration of Spring AI with Anthropic's API, featuring automatic token usage tracking and logging through AspectJ. It provides endpoints to interact with AI models while monitoring token consumption, making it perfect for applications that need to track AI resource usage.

## Project Requirements

- Java 23
- Maven
- Spring Boot 3.3.5
- Spring AI 1.0.0-M3
- Anthropic API Key

## Key Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-anthropic-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

## Getting Started

1. Set your Anthropic API key in `application.properties`:
```properties
spring.ai.anthropic.api-key=your-api-key
```

2. Build the project:
```bash
./mvnw clean install
```

## Features

### 1. Token Usage Tracking
The application includes three endpoints demonstrating different approaches to token usage monitoring:

```java
@GetMapping("/")
public String tokensString() {
    return chatClient.prompt(PROMPT).call().content();
}

@GetMapping("/tokens")
public ChatResponse tokensFull() {
    return chatClient.prompt(PROMPT).call().chatResponse();
}

@GetMapping("/tokens-custom")
public CustomResponse tokens() {
    ChatResponse chatResponse = chatClient.prompt(PROMPT).call().chatResponse();
    return new CustomResponse(
        chatResponse.getResult().getOutput().getContent(),
        chatResponse.getMetadata().getUsage()
    );
}
```

### 2. Aspect-Oriented Token Logging
The project implements automatic token usage logging using AspectJ:

```java
@Around("tokensMethods()")
public Object logTokenUsage(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    logger.info("Entering method: {}", methodName);
    
    Object result = joinPoint.proceed();
    if (result instanceof CustomResponse response) {
        logger.info("Token usage for {}: {}", methodName, response.usage());
    }
    
    return result;
}
```

## API Endpoints

| Endpoint | Description | Response Type |
|----------|-------------|---------------|
| GET `/` | Returns AI response content | String |
| GET `/tokens` | Returns full chat response with metadata | ChatResponse |
| GET `/tokens-custom` | Returns custom response with usage stats | CustomResponse |

## Example Usage

### Making a Request
```bash
curl http://localhost:8080/tokens-custom
```

### Sample Response
```json
{
    "content": "Here's a fun fact about Java: The language was originally named 'Oak' after a tree outside James Gosling's office.",
    "usage": {
        "promptTokens": 5,
        "generationTokens": 24,
        "totalTokens": 29
    }
}
```

## Logging Output
The application automatically logs token usage for each request:
```
2024-03-21 10:15:23.456 INFO  [main] Entering method: tokens
2024-03-21 10:15:23.789 INFO  [main] Token usage for tokens: Usage[promptTokens=5, generationTokens=24, totalTokens=29]
2024-03-21 10:15:23.790 INFO  [main] Exiting method: tokens (execution time: 334ms)
```

## Best Practices

1. **Token Monitoring**
    - Use the custom endpoint for detailed token usage tracking
    - Monitor token consumption patterns through logs
    - Set up alerts for unusual token usage

2. **Error Handling**
    - The aspect handles exceptions and logs errors automatically
    - Implement additional error handling as needed for your use case