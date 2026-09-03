package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

@Component
public class WeakHashAlgorithmRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body != null) {
            if (body.contains("MessageDigest.getInstance(\"MD5\")") || 
                body.contains("MessageDigest.getInstance(\"SHA1\")") ||
                body.contains("MessageDigest.getInstance(\"SHA-1\")")) {
                return RuleResult.fail("Use of weak hash algorithm (MD5/SHA1)", Severity.MEDIUM, "WeakHashAlgorithmRule");
            }
        }
        return RuleResult.pass("WeakHashAlgorithmRule");
    }
}
