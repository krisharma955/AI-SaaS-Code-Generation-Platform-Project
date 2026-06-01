package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import reactor.core.publisher.Flux;

public interface AiGenerationService {

    Flux<String> streamResponse(String message, Long projectId);

}
