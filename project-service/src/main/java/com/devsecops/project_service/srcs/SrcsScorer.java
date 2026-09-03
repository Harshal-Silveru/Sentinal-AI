package com.devsecops.project_service.srcs;

import com.devsecops.project_service.entity.Finding;
import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SrcsScorer {

    public static class ScoredUnit {
        public final MethodUnit unit;
        public final double finalScore;
        public final int ruleHits;
        public final boolean apiUsageFlag;
        public final boolean inputExposureFlag;
        public final boolean authRelevanceFlag;
        public final double complexityFactor;

        public ScoredUnit(MethodUnit unit, double finalScore, int ruleHits, boolean apiUsageFlag, boolean inputExposureFlag, boolean authRelevanceFlag, double complexityFactor) {
            this.unit = unit;
            this.finalScore = finalScore;
            this.ruleHits = ruleHits;
            this.apiUsageFlag = apiUsageFlag;
            this.inputExposureFlag = inputExposureFlag;
            this.authRelevanceFlag = authRelevanceFlag;
            this.complexityFactor = complexityFactor;
        }
    }

    public List<ScoredUnit> scoreAndSelect(List<MethodUnit> units, List<Finding> allFindings) {
        if (units.isEmpty()) return Collections.emptyList();

        Map<MethodUnit, Integer> hitCounts = new HashMap<>();
        for (MethodUnit unit : units) {
            hitCounts.put(unit, 0);
        }

        for (Finding f : allFindings) {
            for (MethodUnit u : units) {
                if (u.getFilePath().equals(f.getFilePath()) && u.getClassName().equals(f.getClassName()) && u.getMethodName().equals(f.getMethodName())) {
                    hitCounts.put(u, hitCounts.get(u) + 1);
                    break;
                }
            }
        }

        int maxHits = hitCounts.values().stream().max(Integer::compareTo).orElse(0);

        List<ScoredUnit> scoredUnits = new ArrayList<>();

        for (MethodUnit unit : units) {
            int hits = hitCounts.get(unit);
            double ruleHitsNorm = maxHits > 0 ? (double) hits / maxHits : 0.0;

            boolean apiUsage = computeApiUsageFlag(unit);
            boolean inputExposure = computeInputExposureFlag(unit);
            boolean authRelevance = computeAuthRelevanceFlag(unit);

            double raw = 0.35 * ruleHitsNorm + 0.20 * (apiUsage ? 1 : 0) + 0.20 * (inputExposure ? 1 : 0) + 0.15 * (authRelevance ? 1 : 0);

            double complexity = unit.getCyclomaticComplexity();
            double complexityFactor = 0.5 + 0.5 * Math.min(complexity, 10.0) / 10.0;
            complexityFactor = Math.max(0.5, Math.min(complexityFactor, 1.0));

            double finalScore = raw * complexityFactor;
            scoredUnits.add(new ScoredUnit(unit, finalScore, hits, apiUsage, inputExposure, authRelevance, complexityFactor));
        }

        scoredUnits.sort((a, b) -> Double.compare(b.finalScore, a.finalScore));

        scoredUnits.sort((a, b) -> Double.compare(b.finalScore, a.finalScore));
        return scoredUnits;
    }

    private boolean computeApiUsageFlag(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body == null) return false;
        String lowerBody = body.toLowerCase();
        return lowerBody.contains("jdbc") || lowerBody.contains("file") || lowerBody.contains("socket") ||
               lowerBody.contains("httpclient") || lowerBody.contains("messagedigest") || lowerBody.contains("cipher") ||
               lowerBody.contains("processbuilder") || lowerBody.contains("invoke(");
    }

    private boolean computeInputExposureFlag(MethodUnit unit) {
        List<String> annotations = unit.getAnnotations();
        if (annotations != null) {
            if (annotations.stream().anyMatch(a -> a.contains("RequestParam") || a.contains("RequestBody") || a.contains("PathVariable") || a.contains("Mapping"))) {
                return true;
            }
        }
        return false;
    }

    private boolean computeAuthRelevanceFlag(MethodUnit unit) {
        String body = unit.getMethodBody();
        if (body == null) body = "";
        String lowerBody = body.toLowerCase();

        List<String> annotations = unit.getAnnotations();
        boolean annMatch = annotations != null && annotations.stream().anyMatch(a -> a.contains("PreAuthorize") || a.contains("Secured"));

        return annMatch || lowerBody.contains("userdetails") || lowerBody.contains("password") || lowerBody.contains("token") || lowerBody.contains("auth");
    }
}
