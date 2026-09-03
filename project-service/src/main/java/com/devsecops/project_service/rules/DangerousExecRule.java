package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

@Component
public class DangerousExecRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body != null) {
            if (body.contains("Runtime.getRuntime().exec(") || body.contains("new ProcessBuilder(")) {
                return RuleResult.fail("Dangerous OS command execution detected", Severity.HIGH, "DangerousExecRule");
            }
        }
        return RuleResult.pass("DangerousExecRule");
    }
}
