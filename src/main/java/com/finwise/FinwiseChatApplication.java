package com.finwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FinWise Chat Application — Volume 1, Chapter 1
 *
 * <p>The entry point for the FinWise AI financial assistant.
 *
 * <p>This is a standard Spring Boot application. Spring AI auto-configures
 * itself based on the dependencies on the classpath and the properties
 * in application.yml. No special @Enable* annotations are required.
 *
 * <p>Run modes:
 * <ul>
 *   <li>Default: OpenAI (requires OPENAI_API_KEY env var)</li>
 *   <li>Local:   Ollama  (run with --spring.profiles.active=local)</li>
 * </ul>
 */
@SpringBootApplication
public class FinwiseChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinwiseChatApplication.class, args);
    }
}
