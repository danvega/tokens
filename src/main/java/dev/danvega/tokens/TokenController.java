package dev.danvega.tokens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private final ChatClient chatClient;
    private final String PROMPT = "Tell me a fun fact about Java";

    public TokenController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/")
    public String tokensString() {
        return chatClient.prompt(PROMPT)
                .call()
                .content();
    }

    @GetMapping("/tokens")
    public ChatResponse tokensFull() {
        return chatClient.prompt(PROMPT)
                .call()
                .chatResponse();
    }

    @GetMapping("/tokens-custom")
    public CustomResponse tokens() {
        ChatResponse chatResponse = chatClient.prompt(PROMPT).call().chatResponse();

        /*
                log.info("Tokens for this call - Prompt: {}, Generation: {}, Total: {}",
                chatResponse.getMetadata().getUsage().getPromptTokens(),
                chatResponse.getMetadata().getUsage().getGenerationTokens(),
                chatResponse.getMetadata().getUsage().getTotalTokens());
         */

        return new CustomResponse(chatResponse.getResult().getOutput().getContent(),chatResponse.getMetadata().getUsage());
    }

}
