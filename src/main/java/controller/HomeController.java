package controller;

import app.DBConnection;
import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeController {

    @FXML private Label totalStudentsLabel;
    @FXML private Label totalMarksLabel;
    @FXML private Label avgMarksLabel;
    @FXML private Label passPercentageLabel;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        String query = "SELECT " +
                "COUNT(DISTINCT s.id) as total_students, " +
                "COUNT(m.id) as total_marks_entries, " +
                "AVG(m.marks) as avg_marks, " +
                "SUM(CASE WHEN m.marks >= 40 THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(m.id), 0) as pass_percentage " +
                "FROM students s " +
                "LEFT JOIN marks m ON s.id = m.student_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int totalStudents = rs.getInt("total_students");
                int totalMarks = rs.getInt("total_marks_entries");
                double avgMarks = rs.getDouble("avg_marks");
                double passPercentage = rs.getDouble("pass_percentage");

                totalStudentsLabel.setText(String.valueOf(totalStudents));
                totalMarksLabel.setText(String.valueOf(totalMarks));
                avgMarksLabel.setText(String.format("%.1f", avgMarks));
                passPercentageLabel.setText(String.format("%.1f%%", passPercentage));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToViewStudents() {
        Main.setRoot("students");
    }

    @FXML
    private void goToEnterMarks() {
        Main.setRoot("enter_marks");
    }

    @FXML
    private void goToSearch() {
        Main.setRoot("search");
    }

    @FXML
    private void goToAnalysis() {
        Main.setRoot("analysis");
    }

    @FXML
    private void goToAddStudent() {
        Main.setRoot("add_student");
    }
}
