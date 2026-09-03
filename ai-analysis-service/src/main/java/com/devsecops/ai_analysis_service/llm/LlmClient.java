package com.devsecops.ai_analysis_service.llm;

import com.fasterxml.jackson.databind.JsonNode;

public interface LlmClient {
    LlmResult explain(String contextPayload, JsonNode findings);
}
