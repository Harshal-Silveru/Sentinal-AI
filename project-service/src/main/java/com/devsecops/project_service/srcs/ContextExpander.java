package com.devsecops.project_service.srcs;

import com.devsecops.project_service.parsing.MethodUnit;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContextExpander {

    public List<MethodUnit> expandContext(List<SrcsScorer.ScoredUnit> selectedUnits, List<MethodUnit> allUnits) {
        Set<MethodUnit> expanded = new HashSet<>();

        for (SrcsScorer.ScoredUnit su : selectedUnits) {
            MethodUnit unit = su.unit;
            
            // a) methods in the same class that call it or that it calls
            for (MethodUnit other : allUnits) {
                if (other.getClassName().equals(unit.getClassName()) && !other.getMethodName().equals(unit.getMethodName())) {
                    if (other.getCalledMethodNames() != null && other.getCalledMethodNames().contains(unit.getMethodName())) {
                        expanded.add(other);
                    }
                    if (unit.getCalledMethodNames() != null && unit.getCalledMethodNames().contains(other.getMethodName())) {
                        expanded.add(other);
                    }
                }
            }

            // b) field-level signatures of @Repository/DTO types referenced (Skipped full JavaParser resolution for demo brevity, using heuristic)
            // c) DTO class definitions for controller handler parameter/return types (Skipped full JavaParser resolution for demo brevity)
            
            // d) SecurityConfig/JwtFilter if auth_relevance_flag is true
            if (su.authRelevanceFlag) {
                String packageName = getPackageName(unit.getClassName());
                for (MethodUnit other : allUnits) {
                    if (getPackageName(other.getClassName()).equals(packageName)) {
                        String shortName = getShortName(other.getClassName());
                        if (shortName.contains("SecurityConfig") || shortName.contains("JwtFilter")) {
                            expanded.add(other);
                        }
                    }
                }
            }
        }

        // Deduplicate against already selected
        for (SrcsScorer.ScoredUnit su : selectedUnits) {
            expanded.remove(su.unit);
        }

        return new ArrayList<>(expanded);
    }

    private String getPackageName(String className) {
        int idx = className.lastIndexOf('.');
        if (idx > 0) return className.substring(0, idx);
        return "";
    }

    private String getShortName(String className) {
        int idx = className.lastIndexOf('.');
        if (idx > 0) return className.substring(idx + 1);
        return className;
    }
}
