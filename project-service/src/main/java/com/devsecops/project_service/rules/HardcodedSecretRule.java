package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class HardcodedSecretRule implements SecurityRule {
    private static final Pattern PATTERN = Pattern.compile("password\\s*=\\s*\".+\"", Pattern.CASE_INSENSITIVE);

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        if (unit.getMethodBody() != null && PATTERN.matcher(unit.getMethodBody()).find()) {
            return RuleResult.fail("Found potential hardcoded secret/password assignment", Severity.HIGH, "HardcodedSecretRule");
        }
        return RuleResult.pass("HardcodedSecretRule");
    }
}
