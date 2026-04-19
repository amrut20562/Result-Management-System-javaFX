package app;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {

    public static void generatePDF(int studentId) {
        // Query to get student details
        String studentQuery = "SELECT roll_no, name, semester FROM students WHERE id = ?";
        // Query to get marks
        String marksQuery = "SELECT s.subject_code, s.subject_name, m.marks FROM marks m JOIN subjects s ON m.subject_id = s.id WHERE m.student_id = ? ORDER BY s.subject_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stdStmt = conn.prepareStatement(studentQuery);
             PreparedStatement mkStmt = conn.prepareStatement(marksQuery)) {

            stdStmt.setInt(1, studentId);
            ResultSet stdRs = stdStmt.executeQuery();

            if (!stdRs.next()) {
                showAlert("Error", "Student not found!");
                return;
            }

            String rollNo = stdRs.getString("roll_no");
            String name = stdRs.getString("name");
            int semester = stdRs.getInt("semester");

            // Setup PDF destination
            String dest = "Result_" + name.replace(" ", "_") + ".pdf";
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
            document.open();

            // Add Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("B.E (Artificial Intelligence & Data Science) 4th Semester - Result Card", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Add Student Info
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            
            document.add(new Paragraph("Student Name: " + name, normalFont));
            document.add(new Paragraph("Roll Number: " + rollNo, normalFont));
            document.add(new Paragraph("Semester: " + semester, normalFont));
            
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
            document.add(new Paragraph("Date Generated: " + date, normalFont));
            document.add(new Paragraph("\n"));

            // Add Marks Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 3f, 1.5f, 1.5f});

            // Table Header
            table.addCell(new PdfPCell(new Phrase("Subject Code", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Subject Name", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Marks Obtained", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Max Marks", boldFont)));

            mkStmt.setInt(1, studentId);
            ResultSet mkRs = mkStmt.executeQuery();

            int totalMarks = 0;
            int count = 0;

            while (mkRs.next()) {
                table.addCell(mkRs.getString("subject_code"));
                table.addCell(mkRs.getString("subject_name"));
                int marks = mkRs.getInt("marks");
                table.addCell(String.valueOf(marks));
                table.addCell("100");
                totalMarks += marks;
                count++;
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            if (count > 0) {
                double percentage = ((double) totalMarks / (count * 100)) * 100;
                String grade;
                String status = percentage >= 40 ? "PASS" : "FAIL";

                if (percentage >= 90) grade = "O (Outstanding)";
                else if (percentage >= 80) grade = "A+ (Excellent)";
                else if (percentage >= 70) grade = "A (Very Good)";
                else if (percentage >= 60) grade = "B+ (Good)";
                else if (percentage >= 50) grade = "B (Above Average)";
                else if (percentage >= 45) grade = "C (Average)";
                else if (percentage >= 40) grade = "P (Pass)";
                else grade = "F (Fail)";

                PdfPTable summary = new PdfPTable(2);
                summary.setWidthPercentage(50);
                summary.setHorizontalAlignment(Element.ALIGN_LEFT);

                summary.addCell(new Phrase("Total Subjects", boldFont));
                summary.addCell(count + " out of 6");

                summary.addCell(new Phrase("Total Marks Obtained", boldFont));
                summary.addCell(totalMarks + " / " + (count * 100));

                summary.addCell(new Phrase("Percentage", boldFont));
                summary.addCell(String.format("%.2f%%", percentage));

                summary.addCell(new Phrase("Grade", boldFont));
                summary.addCell(grade);

                summary.addCell(new Phrase("Status", boldFont));
                summary.addCell(status);

                document.add(summary);
            }

            document.add(new Paragraph("\n\n"));
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);
            document.add(new Paragraph("This is a computer-generated result card. No signature required.", footerFont));

            document.close();
            
            showAlert("Success", "PDF Generated Successfully!\nSaved as: " + dest);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not generate PDF: " + e.getMessage());
        }
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
