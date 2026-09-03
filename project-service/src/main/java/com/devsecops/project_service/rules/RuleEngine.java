package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngine {

    private final List<SecurityRule> rules;

    public RuleEngine(List<SecurityRule> rules) {
        this.rules = rules;
    }

    public List<RuleResult> evaluateAll(MethodUnit unit) {
        List<RuleResult> results = new ArrayList<>();
        for (SecurityRule rule : rules) {
            results.add(rule.evaluate(unit));
        }
        return results;
    }
}
