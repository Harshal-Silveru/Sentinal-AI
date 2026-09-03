package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissiveCorsRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        List<String> annotations = unit.getAnnotations();
        if (annotations != null) {
            for (String annotation : annotations) {
                if (annotation.contains("CrossOrigin") && annotation.contains("\"*\"")) {
                    return RuleResult.fail("Permissive CORS policy detected (origins = \"*\")", Severity.MEDIUM, "PermissiveCorsRule");
                }
            }
        }
        String body = unit.getMethodBody();
        if (body != null && body.contains(".allowedOrigins(\"*\")")) {
            return RuleResult.fail("Permissive CORS policy detected in config", Severity.MEDIUM, "PermissiveCorsRule");
        }
        return RuleResult.pass("PermissiveCorsRule");
    }
}
