package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;

public interface SecurityRule {
    RuleResult evaluate(MethodUnit unit);
}
