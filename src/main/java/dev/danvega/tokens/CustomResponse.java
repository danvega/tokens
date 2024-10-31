package dev.danvega.tokens;

import org.springframework.ai.chat.metadata.Usage;

public record CustomResponse(String content, Usage usage) {
}
