package com.finwise.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * FinWise Chat REST API Models — Volume 1, Chapter 1
 *
 * <p>Java 21 records for request/response data transfer objects.
 * Records are ideal for AI request/response DTOs:
 * <ul>
 *   <li>Immutable by default — thread-safe for concurrent LLM calls</li>
 *   <li>Compact syntax — less boilerplate than traditional POJOs</li>
 *   <li>Auto-generated equals/hashCode/toString</li>
 *   <li>Works perfectly with Spring's JSON serialization</li>
 * </ul>
 */
public class ChatModels {

    /**
     * Incoming chat request from the user.
     *
     * <p>Validation constraints prevent:
     * <ul>
     *   <li>Empty messages (no tokens wasted on blank prompts)</li>
     *   <li>Excessively long messages (context window protection)</li>
     * </ul>
     *
     * @param message The user's natural language question or request
     * @param sessionId Optional session identifier for conversation tracking (Chapter 5)
     */
    public record ChatRequest(
            @NotBlank(message = "Message cannot be blank")
            @Size(max = 4000, message = "Message cannot exceed 4000 characters")
            String message,

            String sessionId  // Used in Chapter 5 (Conversational Memory)
    ) {
        // Compact constructor — called by the record's canonical constructor
        public ChatRequest {
            // Sanitize whitespace
            if (message != null) {
                message = message.strip();
            }
            // Default sessionId if not provided
            if (sessionId == null || sessionId.isBlank()) {
                sessionId = "default";
            }
        }
    }

    /**
     * Outgoing chat response to the user.
     *
     * @param reply     The AI-generated response text
     * @param sessionId The session ID echoed back for client-side tracking
     * @param model     Which LLM model generated this response (transparency)
     */
    public record ChatResponse(
            String reply,
            String sessionId,
            String model
    ) {}

    /**
     * Health check response for the AI service.
     *
     * @param status   "ok" | "degraded" | "down"
     * @param provider Which LLM provider is active ("openai" | "ollama")
     * @param latencyMs How long the health check ping took in milliseconds
     */
    public record HealthResponse(
            String status,
            String provider,
            long latencyMs
    ) {}

    /**
     * Structured financial advice response (Chapter 3).
     * Used by BeanOutputConverter to parse JSON.
     */
    public record FinancialAdviceResponse(
            String summary,
            List<String> actionItems,
            String riskLevel
    ) {}
}
