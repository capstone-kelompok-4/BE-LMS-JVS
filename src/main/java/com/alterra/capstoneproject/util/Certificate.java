package com.alterra.capstoneproject.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Certificate {
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 35, Font.BOLD);
    private static Font itaFont = new Font(Font.FontFamily.HELVETICA, 16, Font.ITALIC);
    private static Font nameFont = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD);
    private static Font tcFont = new Font(Font.FontFamily.HELVETICA, 20);

    public static ByteArrayInputStream getCertificate(CourseTaken courseTaken) {
        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph paragraph = new Paragraph();

            paragraph.add(new Paragraph(" "));
            document.add(paragraph);

            paragraph.add(new Paragraph(" "));
            document.add(paragraph);
            
            paragraph.add(new Paragraph(" "));
            document.add(paragraph);

            paragraph = new Paragraph("Certificate of Completion", catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));

            document.add(paragraph);

            paragraph = new Paragraph("This to certify that,", itaFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            
            document.add(paragraph);

            paragraph = new Paragraph(courseTaken.getUser().getName().toUpperCase(), nameFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(new Paragraph(" "));
            
            document.add(paragraph);

            paragraph = new Paragraph("has completed the " + courseTaken.getRequestType().toString().toLowerCase(), itaFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(new Paragraph(" "));
            
            document.add(paragraph);

            paragraph = new Paragraph("\"" + courseTaken.getCourseTake().getName().toUpperCase() + "\"", tcFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));
            
            document.add(paragraph);

            document.close();

        } catch (DocumentException e) {
            log.error("Create certificate failed");
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
