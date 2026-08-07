package com.finwise.chat;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatModels.ChatResponse> chat(@Valid @RequestBody ChatModels.ChatRequest request) {
        ChatModels.ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    // New streaming endpoint
    @PostMapping(value = "/stream", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@Valid @RequestBody ChatModels.ChatRequest request) {
        return chatService.streamChat(request.message());
    }

    // New structured output endpoint
    @GetMapping("/summary")
    public ChatService.FinancialConceptSummary getSummary(@RequestParam String concept) {
        return chatService.getStructuredSummary(concept);
    }

    // New structured advice endpoint for Chapter 3
    @PostMapping(value = "/structured", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatModels.FinancialAdviceResponse getStructuredAdvice(@Valid @RequestBody ChatModels.ChatRequest request) {
        return chatService.getStructuredAdvice(request.message());
    }
}
