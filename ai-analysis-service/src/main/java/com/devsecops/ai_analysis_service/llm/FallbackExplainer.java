package com.devsecops.ai_analysis_service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class FallbackExplainer implements LlmClient {

    @Override
    public LlmResult explain(String contextPayload, JsonNode findings) {
        StringBuilder explanation = new StringBuilder("Deterministic Fallback Explanation:\n");
        double riskScore = 0;
        
        if (findings != null && findings.isArray() && findings.size() > 0) {
            for (JsonNode finding : findings) {
                String rule = finding.path("ruleName").asText("Unknown");
                String severity = finding.path("severity").asText("LOW");
                String msg = finding.path("message").asText("");
                String method = finding.path("methodName").asText("");
                
                explanation.append("- [").append(severity).append("] ").append(rule).append(" in method '").append(method).append("': ").append(msg).append("\n");
                
                if ("CRITICAL".equalsIgnoreCase(severity)) riskScore += 30;
                else if ("HIGH".equalsIgnoreCase(severity)) riskScore += 20;
                else if ("MEDIUM".equalsIgnoreCase(severity)) riskScore += 10;
                else riskScore += 5;
            }
        } else {
            explanation.append("No security vulnerabilities detected by rule engine.");
        }
        
        riskScore = Math.min(riskScore, 100.0);
        
        return new LlmResult(explanation.toString(), riskScore, "fallback");
    }
}
