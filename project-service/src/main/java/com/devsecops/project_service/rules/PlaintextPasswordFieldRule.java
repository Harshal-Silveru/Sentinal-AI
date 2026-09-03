package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

@Component
public class PlaintextPasswordFieldRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body != null) {
            // Very naive check for method bodies assigning passwords without encryption
            if (body.toLowerCase().contains("password") && body.contains("=") && !body.toLowerCase().contains("bcrypt")) {
                return RuleResult.fail("Potential plaintext password assignment detected", Severity.MEDIUM, "PlaintextPasswordFieldRule");
            }
        }
        return RuleResult.pass("PlaintextPasswordFieldRule");
    }
}
