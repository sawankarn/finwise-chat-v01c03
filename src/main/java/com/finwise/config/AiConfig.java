package com.finwise.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Spring AI Configuration — Volume 1, Chapter 1
 *
 * <p>Configures the {@link ChatClient} beans used by the FinWise Chat application.
 *
 * <p>Two configurations are provided:
 * <ul>
 *   <li><b>Default</b> — OpenAI configuration with production-oriented settings</li>
 *   <li><b>Local</b>   — Ollama configuration for local development (no API key needed)</li>
 * </ul>
 *
 * <h2>Why Configure ChatClient as a Bean?</h2>
 * <p>Spring AI auto-configures a {@link ChatClient.Builder} based on the dependencies
 * on the classpath. We define this {@code @Configuration} class to:
 * <ol>
 *   <li>Add default advisors (logging, token counting, etc.)</li>
 *   <li>Set default options (temperature, max tokens)</li>
 *   <li>Apply the FinWise system prompt once (not in every service class)</li>
 * </ol>
 *
 * <h2>Chapter Evolution</h2>
 * This class grows throughout Volume 1:
 * <ul>
 *   <li>Chapter 1: Basic ChatClient with system prompt + logging advisor</li>
 *   <li>Chapter 5: Add MemoryAdvisor for conversational memory</li>
 *   <li>Chapter 6: Add resilience (circuit breakers via Resilience4j)</li>
 *   <li>Chapter 7: Add SemanticCacheAdvisor for cost management</li>
 *   <li>Chapter 9: Add token counting and cost attribution advisors</li>
 *   <li>Chapter 10: Add PII scrubbing and audit logging advisors</li>
 * </ul>
 */
@Configuration
public class AiConfig {

    // ═══════════════════════════════════════════════════════════════════════
    // SYSTEM PROMPT
    // Centralised here so it's applied consistently across all profiles.
    // In production, this would come from a prompt management service
    // with versioning support (Chapter 5, Section 5.2).
    // ═══════════════════════════════════════════════════════════════════════
    private static final String FINWISE_SYSTEM_PROMPT = """
            You are FinWise Assistant, an intelligent and warm financial advisor
            for FinWise customers in the United Kingdom.

            YOUR PURPOSE:
            Help FinWise customers understand financial concepts, navigate
            FinWise products, and make better-informed financial decisions.

            YOUR RULES (regulatory compliance — non-negotiable):
            1. Never promise or imply specific investment returns
            2. Never advise on specific stock picks or trading decisions
            3. Always recommend professional advice for decisions over £10,000
            4. Never process, store, or repeat sensitive PII:
               - Account numbers, sort codes
               - National Insurance numbers
               - Passwords or PINs
               - Full card numbers
            5. If uncertain about a fact, say so clearly
            6. Cite that information may be outdated when relevant

            YOUR TONE:
            Warm, clear, professional, and jargon-free.
            Like a knowledgeable friend — not a robot, not a salesperson.

            RESPONSE FORMAT:
            - Keep responses concise (2–4 paragraphs maximum for most questions)
            - Use bullet points for lists of steps or options
            - Use plain English — avoid unnecessary financial jargon
            - End with a helpful follow-up question when appropriate
            """;

    /**
     * Primary ChatClient bean — used when running with OpenAI (default profile).
     *
     * <p>Advisors are middleware that wrap every LLM call.
     * {@link SimpleLoggerAdvisor} logs all prompts and responses at DEBUG level —
     * essential for development debugging. In production, replace with a
     * structured audit logger (Chapter 10).
     *
     * @param builder Auto-configured by Spring AI based on application.yml
     * @return Fully configured ChatClient for FinWise
     */
    @Bean
    @Profile("!local")  // Active for all profiles EXCEPT "local"
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(FINWISE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        // Logs prompts + responses at DEBUG level.
                        // NEVER use in production with real user data!
                        // Chapter 10 shows the production-safe alternative.
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    /**
     * Local development ChatClient bean — used with -Dspring.profiles.active=local.
     *
     * <p>Identical configuration but activated by the "local" Spring profile,
     * which switches the underlying model to Ollama via application-local.yml.
     *
     * <p>Local development benefits:
     * <ul>
     *   <li>Zero API cost — model runs on your machine</li>
     *   <li>Zero latency for network — model is local</li>
     *   <li>Privacy — no data leaves your machine</li>
     *   <li>Offline development — works without internet</li>
     * </ul>
     *
     * @param builder Auto-configured for Ollama when "local" profile is active
     * @return ChatClient backed by a local Ollama model
     */
    @Bean
    @Profile("local")
    public ChatClient localChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(FINWISE_SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
