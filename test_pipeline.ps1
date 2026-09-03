$ErrorActionPreference = 'Stop'

Write-Host "1. Getting JWT Token from user-service..."
$loginBody = @{ email = "admin@devsecops.local"; password = "admin123" } | ConvertTo-Json
$loginRes = Invoke-RestMethod -Uri "http://localhost:8081/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
$token = $loginRes.token
$headers = @{ Authorization = "Bearer $token" }

Write-Host "2. Creating Project 1..."
$proj1Body = @{ projectName = "Demo 1 Project"; userId = 1 } | ConvertTo-Json
$proj1 = Invoke-RestMethod -Uri "http://localhost:8082/api/projects" -Method Post -Body $proj1Body -ContentType "application/json" -Headers $headers
$proj1Id = $proj1.id

Write-Host "3. Uploading demo1.zip to Project 1..."
curl.exe -X POST "http://localhost:8082/api/projects/$proj1Id/upload" -H "Authorization: Bearer $token" -F "file=@.\demo1.zip"

Write-Host "4. Triggering Scan 1..."
$scan1Body = @{ projectId = $proj1Id } | ConvertTo-Json
$scan1 = Invoke-RestMethod -Uri "http://localhost:8082/api/scans" -Method Post -Body $scan1Body -ContentType "application/json" -Headers $headers

Write-Host "5. Fetching Selected Code Stats for Scan 1..."
$stats1 = Invoke-RestMethod -Uri "http://localhost:8082/api/scans/$($scan1.id)/selected-code" -Method Get -Headers $headers

Write-Host "6. Creating Project 2..."
$proj2Body = @{ projectName = "Demo 2 Vulnerable"; userId = 1 } | ConvertTo-Json
$proj2 = Invoke-RestMethod -Uri "http://localhost:8082/api/projects" -Method Post -Body $proj2Body -ContentType "application/json" -Headers $headers
$proj2Id = $proj2.id

Write-Host "7. Uploading demo2.zip to Project 2..."
curl.exe -X POST "http://localhost:8082/api/projects/$proj2Id/upload" -H "Authorization: Bearer $token" -F "file=@.\demo2.zip"

Write-Host "8. Triggering Scan 2..."
$scan2Body = @{ projectId = $proj2Id } | ConvertTo-Json
$scan2 = Invoke-RestMethod -Uri "http://localhost:8082/api/scans" -Method Post -Body $scan2Body -ContentType "application/json" -Headers $headers

Write-Host "9. Fetching Selected Code Stats for Scan 2..."
$stats2 = Invoke-RestMethod -Uri "http://localhost:8082/api/scans/$($scan2.id)/selected-code" -Method Get -Headers $headers

Write-Host "----------------------------------------------------"
Write-Host "SIDE BY SIDE STATS (Scan 1 vs Scan 2)"
Write-Host "----------------------------------------------------"
Write-Host "Total Files: $($stats1.stats.totalFiles) vs $($stats2.stats.totalFiles)"
Write-Host "Total Methods: $($stats1.stats.totalMethods) vs $($stats2.stats.totalMethods)"
Write-Host "Security Relevant: $($stats1.stats.securityRelevantMethods) vs $($stats2.stats.securityRelevantMethods)"
Write-Host "High Priority: $($stats1.stats.highPriorityMethods) vs $($stats2.stats.highPriorityMethods)"
Write-Host "Selected for AI: $($stats1.stats.selectedForAi) vs $($stats2.stats.selectedForAi)"
Write-Host "Context Expanded: $($stats1.stats.contextExpandedCount) vs $($stats2.stats.contextExpandedCount)"
Write-Host "Code Reduction: $($stats1.stats.pctCodeReduction)% vs $($stats2.stats.pctCodeReduction)%"
Write-Host "----------------------------------------------------"

Write-Host "10. Running AI Analysis for Scan 2 (Should use LLM source)..."
$analysisBody = @{ scanId = $scan2.id; projectId = $proj2Id } | ConvertTo-Json
$analysis = Invoke-RestMethod -Uri "http://localhost:8083/api/analyses" -Method Post -Body $analysisBody -ContentType "application/json" -Headers $headers

Write-Host "AI Source: $($analysis.source)"
Write-Host "AI Risk Score: $($analysis.riskScore)"
Write-Host "AI Explanation: $($analysis.explanation)"
