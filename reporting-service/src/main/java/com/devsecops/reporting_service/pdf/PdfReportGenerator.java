package com.devsecops.reporting_service.pdf;

import com.devsecops.reporting_service.dto.AnalysisResponse;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfReportGenerator {

    public byte[] generateReport(AnalysisResponse analysis) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("AI-Powered Secure DevSecOps Platform - Scan Report", titleFont));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Scan ID: " + analysis.getScanId(), normalFont));
            document.add(new Paragraph("Risk Score: " + analysis.getRiskScore(), normalFont));
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Analysis Summary:", headerFont));
            document.add(new Paragraph(analysis.getExplanation(), normalFont));
            
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
