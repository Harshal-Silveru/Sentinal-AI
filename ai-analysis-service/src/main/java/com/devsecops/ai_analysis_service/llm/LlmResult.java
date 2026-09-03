package com.devsecops.ai_analysis_service.llm;

public class LlmResult {
    private String explanation;
    private double riskScore;
    private String source;

    public LlmResult(String explanation, double riskScore, String source) {
        this.explanation = explanation;
        this.riskScore = riskScore;
        this.source = source;
    }

    public String getExplanation() { return explanation; }
    public double getRiskScore() { return riskScore; }
    public String getSource() { return source; }
}
