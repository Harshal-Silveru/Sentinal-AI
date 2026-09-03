package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MissingAuthAnnotationRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        List<String> annotations = unit.getAnnotations();
        if (annotations == null) {
            return RuleResult.pass("MissingAuthAnnotationRule");
        }

        boolean hasRequestMapping = annotations.stream().anyMatch(a -> a.contains("Mapping")); // @GetMapping, @PostMapping, etc.
        boolean hasAuthAnnotation = annotations.stream().anyMatch(a -> a.contains("PreAuthorize") || a.contains("Secured") || a.contains("RolesAllowed"));

        if (hasRequestMapping && !hasAuthAnnotation) {
            String methodName = unit.getMethodName().toLowerCase();
            if (methodName.contains("delete") || methodName.contains("admin") || methodName.contains("user") || methodName.contains("update")) {
                return RuleResult.fail("Sensitive endpoint missing authorization annotation", Severity.HIGH, "MissingAuthAnnotationRule");
            }
        }

        return RuleResult.pass("MissingAuthAnnotationRule");
    }
}
