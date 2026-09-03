package com.devsecops.project_service.parsing;

import java.util.List;
import java.util.Set;

public class MethodUnit {
    private String filePath;
    private String className;
    private String methodName;
    private String methodBody;
    private int lineNumber;
    private List<String> annotations;
    private Set<String> calledMethodNames;
    private int cyclomaticComplexity;

    public MethodUnit() {}

    public MethodUnit(String filePath, String className, String methodName, String methodBody, int lineNumber, List<String> annotations, Set<String> calledMethodNames, int cyclomaticComplexity) {
        this.filePath = filePath;
        this.className = className;
        this.methodName = methodName;
        this.methodBody = methodBody;
        this.lineNumber = lineNumber;
        this.annotations = annotations;
        this.calledMethodNames = calledMethodNames;
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getMethodBody() { return methodBody; }
    public void setMethodBody(String methodBody) { this.methodBody = methodBody; }

    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public List<String> getAnnotations() { return annotations; }
    public void setAnnotations(List<String> annotations) { this.annotations = annotations; }

    public Set<String> getCalledMethodNames() { return calledMethodNames; }
    public void setCalledMethodNames(Set<String> calledMethodNames) { this.calledMethodNames = calledMethodNames; }

    public int getCyclomaticComplexity() { return cyclomaticComplexity; }
    public void setCyclomaticComplexity(int cyclomaticComplexity) { this.cyclomaticComplexity = cyclomaticComplexity; }
}
