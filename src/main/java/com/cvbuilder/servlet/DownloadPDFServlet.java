package com.cvbuilder.servlet;

import com.cvbuilder.model.CV;
import com.cvbuilder.model.User;
import com.cvbuilder.util.FileStorage;
import com.cvbuilder.util.StorageFactory;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * CONTROLLER - Downloads the CV as a PDF.
 *
 * GET /cv/download?id=xxx → generates PDF and sends it to browser
 *
 * We use iText library to create the PDF.
 * The PDF style changes based on the template choice.
 */
@WebServlet("/cv/download")
public class DownloadPDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String cvId = request.getParameter("id");
        if (cvId == null || cvId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        FileStorage storage = StorageFactory.getStorage(getServletContext());
        CV cv = storage.loadCV(cvId);

        if (cv == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Security: make sure user owns this CV
        User user = (User) session.getAttribute("user");
        if (!cv.getOwnerUsername().equals(user.getUsername())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Set response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"CV_" + cv.getFullName().replace(" ", "_") + ".pdf\"");

        // Generate PDF
        try {
            generatePDF(cv, response.getOutputStream());
        } catch (DocumentException e) {
            throw new ServletException("Error generating PDF", e);
        }
    }

    private void generatePDF(CV cv, OutputStream out) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        PdfWriter.getInstance(document, out);
        document.open();

        // Colors based on template
        BaseColor accentColor;
        switch (cv.getTemplateChoice()) {
            case 2: accentColor = new BaseColor(15, 118, 110);  break; // Teal
            case 3: accentColor = new BaseColor(124, 58, 237);  break; // Purple
            default: accentColor = new BaseColor(37, 99, 235);  break; // Blue
        }

        // Fonts
        Font nameFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.WHITE);
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, accentColor);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.DARK_GRAY);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);

        // ---- HEADER ----
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(accentColor);
        headerCell.setPadding(20);
        headerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph namePara = new Paragraph(cv.getFullName().toUpperCase(), nameFont);
        namePara.setAlignment(Element.ALIGN_CENTER);
        headerCell.addElement(namePara);

        if (cv.getTitle() != null && !cv.getTitle().isEmpty()) {
            Paragraph titlePara = new Paragraph(cv.getTitle(), titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingBefore(5);
            headerCell.addElement(titlePara);
        }

        // Contact info in header
        StringBuilder contact = new StringBuilder();
        if (nonEmpty(cv.getEmail())) contact.append(cv.getEmail());
        if (nonEmpty(cv.getTelephone())) contact.append("  |  ").append(cv.getTelephone());
        if (nonEmpty(cv.getVille())) contact.append("  |  ").append(cv.getVille());
        if (contact.length() > 0) {
            Font contactFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(200, 220, 255));
            Paragraph contactPara = new Paragraph(contact.toString(), contactFont);
            contactPara.setAlignment(Element.ALIGN_CENTER);
            contactPara.setSpacingBefore(8);
            headerCell.addElement(contactPara);
        }

        header.addCell(headerCell);
        document.add(header);
        document.add(Chunk.NEWLINE);

        // ---- RÉSUMÉ ----
        if (nonEmpty(cv.getResume())) {
            addSection(document, "À PROPOS", sectionFont);
            Paragraph resumePara = new Paragraph(cv.getResume(), normalFont);
            resumePara.setSpacingBefore(4);
            document.add(resumePara);
            addDivider(document, accentColor);
        }

        // ---- EXPÉRIENCES ----
        List<String> experiences = cv.getExperiences();
        if (experiences != null && !experiences.isEmpty()) {
            addSection(document, "EXPÉRIENCES PROFESSIONNELLES", sectionFont);
            for (String exp : experiences) {
                String[] parts = exp.split("\\|", -1);
                String poste = parts.length > 0 ? parts[0] : "";
                String entreprise = parts.length > 1 ? parts[1] : "";
                String dateDebut = parts.length > 2 ? parts[2] : "";
                String dateFin = parts.length > 3 ? parts[3] : "";
                String desc = parts.length > 4 ? parts[4] : "";

                Paragraph postePara = new Paragraph(poste, boldFont);
                postePara.setSpacingBefore(8);
                document.add(postePara);

                String dateStr = dateDebut + (nonEmpty(dateFin) ? " - " + dateFin : " - Présent");
                Paragraph entPara = new Paragraph(entreprise + "  |  " + dateStr, smallFont);
                entPara.setSpacingBefore(2);
                document.add(entPara);

                if (nonEmpty(desc)) {
                    Paragraph descPara = new Paragraph(desc, normalFont);
                    descPara.setSpacingBefore(3);
                    document.add(descPara);
                }
            }
            addDivider(document, accentColor);
        }

        // ---- FORMATION ----
        List<String> formations = cv.getFormations();
        if (formations != null && !formations.isEmpty()) {
            addSection(document, "FORMATION", sectionFont);
            for (String form : formations) {
                String[] parts = form.split("\\|", -1);
                String diplome = parts.length > 0 ? parts[0] : "";
                String ecole = parts.length > 1 ? parts[1] : "";
                String annee = parts.length > 2 ? parts[2] : "";
                String desc = parts.length > 3 ? parts[3] : "";

                Paragraph diplomePara = new Paragraph(diplome, boldFont);
                diplomePara.setSpacingBefore(8);
                document.add(diplomePara);

                Paragraph ecolePara = new Paragraph(ecole + (nonEmpty(annee) ? "  |  " + annee : ""), smallFont);
                ecolePara.setSpacingBefore(2);
                document.add(ecolePara);

                if (nonEmpty(desc)) {
                    Paragraph descPara = new Paragraph(desc, normalFont);
                    descPara.setSpacingBefore(3);
                    document.add(descPara);
                }
            }
            addDivider(document, accentColor);
        }

        // ---- COMPÉTENCES & LANGUES ----
        boolean hasSkills = nonEmpty(cv.getCompetences()) || nonEmpty(cv.getLangues());
        if (hasSkills) {
            addSection(document, "COMPÉTENCES & LANGUES", sectionFont);
            if (nonEmpty(cv.getCompetences())) {
                document.add(new Paragraph("Compétences: " + cv.getCompetences(), normalFont));
            }
            if (nonEmpty(cv.getLangues())) {
                Paragraph langPara = new Paragraph("Langues: " + cv.getLangues(), normalFont);
                langPara.setSpacingBefore(4);
                document.add(langPara);
            }
            addDivider(document, accentColor);
        }

        // ---- LOISIRS ----
        if (nonEmpty(cv.getLoisirs())) {
            addSection(document, "CENTRES D'INTÉRÊT", sectionFont);
            document.add(new Paragraph(cv.getLoisirs(), normalFont));
        }

        document.close();
    }

    private void addSection(Document doc, String title, Font font) throws DocumentException {
        Paragraph section = new Paragraph(title, font);
        section.setSpacingBefore(12);
        section.setSpacingAfter(4);
        doc.add(section);
    }

    private void addDivider(Document doc, BaseColor color) throws DocumentException {
        // iText 5 compatible divider using a thin colored table cell
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.setSpacingBefore(4);
        line.setSpacingAfter(6);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(color);
        cell.setFixedHeight(1f);
        cell.setBorder(Rectangle.NO_BORDER);
        line.addCell(cell);
        doc.add(line);
    }

    private boolean nonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}