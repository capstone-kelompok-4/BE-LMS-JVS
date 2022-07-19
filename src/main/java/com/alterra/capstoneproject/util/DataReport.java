package com.alterra.capstoneproject.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataReport {
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD); 
    private static Font detFont = new Font(Font.FontFamily.HELVETICA, 11); 
    
    public static ByteArrayInputStream courseReport(CourseTaken courseTakens) {
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            log.info("Add title and detail");
            Paragraph paragraph = new Paragraph();

            paragraph = new Paragraph("Report " + courseTakens.getCourseTake().getName(), catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setIndentationLeft(65);
            paragraph.setIndentationRight(65);
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));

            document.add(paragraph);

            paragraph = new Paragraph("NAME :   " + courseTakens.getUser().getName().toUpperCase(), detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            
            document.add(paragraph);
            
            paragraph = new Paragraph("TYPE :   " + courseTakens.getRequestType(), detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);            
            
            document.add(paragraph);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = courseTakens.getTakenAt().format(formatter);
            
            paragraph = new Paragraph("TAKEN AT :   " + formatDateTime, detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.add(new Paragraph(" "));
            
            document.add(paragraph);

            log.info("Add table");

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 4, 4, 1});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("No.", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Section", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Material", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Score", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            log.info("Add table cells");

            PdfPCell cell;

            for(int i=0; i<courseTakens.getReports().size(); i++) {
                cell = new PdfPCell(new Phrase(String.valueOf(i+1)));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(
                    new Phrase(courseTakens.getReports().get(i).getSectionReport().getNumber()
                    + " - " + courseTakens.getReports().get(i).getSectionReport().getName()));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(
                    new Phrase(courseTakens.getReports().get(i).getMaterial().getType()
                    + " - " + courseTakens.getReports().get(i).getMaterial().getName()));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(courseTakens.getReports().get(i).getScore())));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            log.error("Create data report failed");
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
