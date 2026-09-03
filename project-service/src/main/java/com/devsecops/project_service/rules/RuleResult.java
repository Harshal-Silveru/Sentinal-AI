package com.devsecops.project_service.rules;

public class RuleResult {
    private boolean hit;
    private String message;
    private Severity severity;
    private String ruleName;

    public RuleResult(boolean hit, String message, Severity severity, String ruleName) {
        this.hit = hit;
        this.message = message;
        this.severity = severity;
        this.ruleName = ruleName;
    }

    public static RuleResult pass(String ruleName) {
        return new RuleResult(false, null, null, ruleName);
    }

    public static RuleResult fail(String message, Severity severity, String ruleName) {
        return new RuleResult(true, message, severity, ruleName);
    }

    public boolean isHit() { return hit; }
    public String getMessage() { return message; }
    public Severity getSeverity() { return severity; }
    public String getRuleName() { return ruleName; }
}
