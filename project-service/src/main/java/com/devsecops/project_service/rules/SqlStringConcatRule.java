package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

@Component
public class SqlStringConcatRule implements SecurityRule {
    
    @Override
    public RuleResult evaluate(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body != null) {
            boolean hasSqlExecution = body.contains("executeQuery") || body.contains("executeUpdate") 
                    || body.contains("createQuery") || body.contains("createNativeQuery") 
                    || body.contains("jdbcTemplate.query");
            boolean hasConcat = body.contains(" + ") || body.contains(".concat(");
            boolean hasSelectOrUpdate = body.toLowerCase().contains("select ") || body.toLowerCase().contains("update ") 
                    || body.toLowerCase().contains("delete ") || body.toLowerCase().contains("insert ");
            
            if (hasSqlExecution && hasConcat && hasSelectOrUpdate) {
                return RuleResult.fail("Potential SQL Injection via string concatenation", Severity.CRITICAL, "SqlStringConcatRule");
            }
        }
        return RuleResult.pass("SqlStringConcatRule");
    }
}
