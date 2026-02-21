package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.model.CV;
import com.cvbuilder.model.User;
import com.cvbuilder.service.CVService;
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
 * SERVLET — DownloadPDFServlet
 *
 * Flux : Servlet → CVService.findByIdForUser() → CVDAO.findById() → génère PDF avec iText 5
 */
@WebServlet("/cv/download")
public class DownloadPDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String cvId = req.getParameter("id");
        if (cvId == null || cvId.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        User user = (User) session.getAttribute("user");

        // CVService vérifie la propriété ET charge via CVDAO
        CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
        CV cv = cvService.findByIdForUser(cvId, user.getUsername());

        if (cv == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
            "attachment; filename=\"CV_" + cv.getFullName().replace(" ", "_") + ".pdf\"");

        try {
            generatePDF(cv, resp.getOutputStream());
        } catch (DocumentException e) {
            throw new ServletException("Erreur génération PDF", e);
        }
    }

    private void generatePDF(CV cv, OutputStream out) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        PdfWriter.getInstance(document, out);
        document.open();

        BaseColor accentColor;
        switch (cv.getTemplateChoice()) {
            case 2:  accentColor = new BaseColor(15, 118, 110);  break;
            case 3:  accentColor = new BaseColor(124, 58, 237);  break;
            default: accentColor = new BaseColor(37, 99, 235);   break;
        }

        Font nameFont    = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.WHITE);
        Font titleFont   = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, accentColor);
        Font normalFont  = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.DARK_GRAY);
        Font boldFont    = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
        Font smallFont   = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);

        // Header
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(accentColor);
        headerCell.setPadding(20);
        headerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph namePara = new Paragraph(cv.getFullName().toUpperCase(), nameFont);
        namePara.setAlignment(Element.ALIGN_CENTER);
        headerCell.addElement(namePara);

        if (nonEmpty(cv.getTitle())) {
            Paragraph tp = new Paragraph(cv.getTitle(), titleFont);
            tp.setAlignment(Element.ALIGN_CENTER);
            tp.setSpacingBefore(5);
            headerCell.addElement(tp);
        }

        StringBuilder contact = new StringBuilder();
        if (nonEmpty(cv.getEmail()))     contact.append(cv.getEmail());
        if (nonEmpty(cv.getTelephone())) contact.append("  |  ").append(cv.getTelephone());
        if (nonEmpty(cv.getVille()))     contact.append("  |  ").append(cv.getVille());
        if (contact.length() > 0) {
            Font cf = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(200, 220, 255));
            Paragraph cp = new Paragraph(contact.toString(), cf);
            cp.setAlignment(Element.ALIGN_CENTER);
            cp.setSpacingBefore(8);
            headerCell.addElement(cp);
        }

        header.addCell(headerCell);
        document.add(header);
        document.add(Chunk.NEWLINE);

        // Résumé
        if (nonEmpty(cv.getResume())) {
            addSection(document, "À PROPOS", sectionFont);
            Paragraph rp = new Paragraph(cv.getResume(), normalFont);
            rp.setSpacingBefore(4);
            document.add(rp);
            addDivider(document, accentColor);
        }

        // Expériences
        List<String> experiences = cv.getExperiences();
        if (experiences != null && !experiences.isEmpty()) {
            addSection(document, "EXPÉRIENCES PROFESSIONNELLES", sectionFont);
            for (String exp : experiences) {
                String[] p = exp.split("\\|", -1);
                String poste = p.length > 0 ? p[0] : "";
                String entreprise = p.length > 1 ? p[1] : "";
                String debut = p.length > 2 ? p[2] : "";
                String fin   = p.length > 3 ? p[3] : "";
                String desc  = p.length > 4 ? p[4] : "";

                Paragraph pp = new Paragraph(poste, boldFont);
                pp.setSpacingBefore(8);
                document.add(pp);

                String dateStr = debut + (nonEmpty(fin) ? " - " + fin : " - Présent");
                Paragraph ep = new Paragraph(entreprise + "  |  " + dateStr, smallFont);
                ep.setSpacingBefore(2);
                document.add(ep);

                if (nonEmpty(desc)) {
                    Paragraph dp = new Paragraph(desc, normalFont);
                    dp.setSpacingBefore(3);
                    document.add(dp);
                }
            }
            addDivider(document, accentColor);
        }

        // Formation
        List<String> formations = cv.getFormations();
        if (formations != null && !formations.isEmpty()) {
            addSection(document, "FORMATION", sectionFont);
            for (String form : formations) {
                String[] p = form.split("\\|", -1);
                String diplome = p.length > 0 ? p[0] : "";
                String ecole   = p.length > 1 ? p[1] : "";
                String annee   = p.length > 2 ? p[2] : "";
                String desc    = p.length > 3 ? p[3] : "";

                Paragraph dp = new Paragraph(diplome, boldFont);
                dp.setSpacingBefore(8);
                document.add(dp);

                Paragraph ep = new Paragraph(ecole + (nonEmpty(annee) ? "  |  " + annee : ""), smallFont);
                ep.setSpacingBefore(2);
                document.add(ep);

                if (nonEmpty(desc)) {
                    document.add(new Paragraph(desc, normalFont));
                }
            }
            addDivider(document, accentColor);
        }

        // Compétences & Langues
        if (nonEmpty(cv.getCompetences()) || nonEmpty(cv.getLangues())) {
            addSection(document, "COMPÉTENCES & LANGUES", sectionFont);
            if (nonEmpty(cv.getCompetences()))
                document.add(new Paragraph("Compétences: " + cv.getCompetences(), normalFont));
            if (nonEmpty(cv.getLangues())) {
                Paragraph lp = new Paragraph("Langues: " + cv.getLangues(), normalFont);
                lp.setSpacingBefore(4);
                document.add(lp);
            }
            addDivider(document, accentColor);
        }

        // Loisirs
        if (nonEmpty(cv.getLoisirs())) {
            addSection(document, "CENTRES D'INTÉRÊT", sectionFont);
            document.add(new Paragraph(cv.getLoisirs(), normalFont));
        }

        document.close();
    }

    private void addSection(Document doc, String title, Font font) throws DocumentException {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(12);
        p.setSpacingAfter(4);
        doc.add(p);
    }

    private void addDivider(Document doc, BaseColor color) throws DocumentException {
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
