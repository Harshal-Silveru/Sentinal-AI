package com.devsecops.project_service.dto;

import com.devsecops.project_service.parsing.MethodUnit;
import java.util.List;

public class SelectedCodeResponse {
    
    private List<MethodUnit> selectedUnits;
    private List<MethodUnit> expandedUnits;
    private Stats stats;

    public SelectedCodeResponse() {}

    public SelectedCodeResponse(List<MethodUnit> selectedUnits, List<MethodUnit> expandedUnits, Stats stats) {
        this.selectedUnits = selectedUnits;
        this.expandedUnits = expandedUnits;
        this.stats = stats;
    }

    public List<MethodUnit> getSelectedUnits() { return selectedUnits; }
    public void setSelectedUnits(List<MethodUnit> selectedUnits) { this.selectedUnits = selectedUnits; }

    public List<MethodUnit> getExpandedUnits() { return expandedUnits; }
    public void setExpandedUnits(List<MethodUnit> expandedUnits) { this.expandedUnits = expandedUnits; }

    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }

    public static class Stats {
        public int totalFiles;
        public int totalMethods;
        public int securityRelevantMethods;
        public int highPriorityMethods;
        public int selectedForAi;
        public int contextExpandedCount;
        public double pctCodeReduction;
    }
}
