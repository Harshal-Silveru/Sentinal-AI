package com.devsecops.project_service.rules;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Component;

@Component
public class DisabledSslVerificationRule implements SecurityRule {

    @Override
    public RuleResult evaluate(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body != null) {
            boolean hasTrustManager = body.contains("X509TrustManager");
            boolean hasHostnameVerifier = body.contains("HostnameVerifier");
            boolean returnsTrue = body.contains("return true;") || body.contains("return true");
            
            if ((hasTrustManager || hasHostnameVerifier) && returnsTrue) {
                return RuleResult.fail("Disabled SSL/TLS verification detected", Severity.HIGH, "DisabledSslVerificationRule");
            }
        }
        return RuleResult.pass("DisabledSslVerificationRule");
    }
}
