package com.finwise.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    // Define our expected output structure as a Java Record
    public record FinancialConceptSummary(
            String concept,
            String simpleDefinition,
            List<String> keyTerms,
            String realWorldExample
    ) {}

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                // Add the system prompt as the contract
                .defaultSystem("""
                    You are FinWise Chat, a premium financial assistant.
                    
                    TONE AND STYLE:
                    - Professional, calm, and reassuring.
                    - Never use exclamation marks.
                    - Never use slang.
                    
                    CONSTRAINTS:
                    - Do not provide specific investment advice or stock tips.
                    - Do not calculate taxes.
                    - If asked for personalized advice, politely decline.
                    """)
                .build();
    }

    public ChatModels.ChatResponse chat(ChatModels.ChatRequest request) {
        var response = chatClient.prompt()
                .user(request.message())
                .call()
                .chatResponse();
                
        return new ChatModels.ChatResponse(
                response.getResult().getOutput().getText(),
                request.sessionId(),
                response.getMetadata().getModel()
        );
    }

    public Flux<String> streamChat(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    public FinancialConceptSummary getStructuredSummary(String concept) {
        var converter = new BeanOutputConverter<>(FinancialConceptSummary.class);
        String format = converter.getFormat();

        String response = chatClient.prompt()
                .user(u -> u.text("Explain {concept}. \n{format}")
                            .param("concept", concept)
                            .param("format", format))
                .call()
                .content();

        return converter.convert(response);
    }

    public ChatModels.FinancialAdviceResponse getStructuredAdvice(String message) {
        var converter = new BeanOutputConverter<>(ChatModels.FinancialAdviceResponse.class);

        return chatClient.prompt()
                .user(u -> u.text(message)
                            .text(converter.getFormat())) // Inject schema instructions
                .call()
                .entity(converter); // Parse response into the Record
    }
}
