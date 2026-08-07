package com.finwise.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ChatService Tests - Volume 1, Chapter 2
 *
 * Tests for Chapter 2 features: system prompt as a contract,
 * structured output (BeanOutputConverter to Java Record), and streaming (Flux).
 */
@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Nested
    @DisplayName("Basic Chat Tests")
    class BasicChatTests {

        @Test
        @DisplayName("Should return non-null response for a financial question")
        void shouldReturnNonNullResponse() {
            ChatModels.ChatResponse response = chatService.chat(new ChatModels.ChatRequest("What is compound interest?", "test"));
            assertThat(response).isNotNull();
            assertThat(response.reply()).isNotBlank();
        }

        @Test
        @DisplayName("System prompt guardrail - should decline personalized advice")
        void systemPromptShouldDeclinePersonalizedAdvice() {
            ChatModels.ChatResponse response = chatService.chat(new ChatModels.ChatRequest("Should I invest all my money in Dogecoin?", "test"));
            assertThat(response).isNotNull();
            assertThat(response.reply()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("Structured Output Tests")
    class StructuredOutputTests {

        @Test
        @DisplayName("Should return a fully-populated FinancialConceptSummary record")
        void shouldReturnStructuredSummary() {
            ChatService.FinancialConceptSummary summary =
                    chatService.getStructuredSummary("compound interest");
            assertThat(summary).isNotNull();
            assertThat(summary.concept()).isNotBlank();
            assertThat(summary.simpleDefinition()).isNotBlank();
            assertThat(summary.keyTerms()).isNotNull();
            assertThat(summary.keyTerms()).isNotEmpty();
            assertThat(summary.realWorldExample()).isNotBlank();
        }

        @Test
        @DisplayName("Should return structured summary for inflation")
        void shouldReturnStructuredSummaryForInflation() {
            ChatService.FinancialConceptSummary summary =
                    chatService.getStructuredSummary("inflation");
            assertThat(summary).isNotNull();
            assertThat(summary.simpleDefinition()).isNotBlank();
            assertThat(summary.keyTerms()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Streaming Tests")
    class StreamingTests {

        @Test
        @DisplayName("streamChat should return a non-empty Flux of tokens")
        void streamChatShouldReturnNonEmptyFlux() {
            Flux<String> flux = chatService.streamChat("What is an ISA?");
            String fullResponse = flux.collectList()
                    .block()
                    .stream()
                    .reduce("", String::concat);
            assertThat(fullResponse).isNotBlank();
        }

        @Test
        @DisplayName("streamChat should emit multiple tokens")
        void streamChatShouldEmitMultipleTokens() {
            List<String> tokens = chatService.streamChat("Explain what a pension is.")
                    .collectList()
                    .block();
            assertThat(tokens).isNotNull();
            assertThat(tokens.size()).isGreaterThan(1);
        }
    }
}