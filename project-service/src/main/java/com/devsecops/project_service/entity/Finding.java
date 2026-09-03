package com.devsecops.project_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "findings")
public class Finding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scanId;

    @Column(nullable = false)
    private String ruleName;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String methodName;

    public Finding() {}

    public Finding(Long scanId, String ruleName, String message, String severity, String filePath, String className, String methodName) {
        this.scanId = scanId;
        this.ruleName = ruleName;
        this.message = message;
        this.severity = severity;
        this.filePath = filePath;
        this.className = className;
        this.methodName = methodName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getScanId() { return scanId; }
    public void setScanId(Long scanId) { this.scanId = scanId; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
}
