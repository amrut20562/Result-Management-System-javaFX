package controller;

import app.DBConnection;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ResultRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultController {

    @FXML private Label studentNameLabel;
    @FXML private Label rollNoLabel;
    @FXML private Label semesterLabel;
    
    @FXML private TableView<ResultRow> marksTable;
    @FXML private TableColumn<ResultRow, String> codeCol;
    @FXML private TableColumn<ResultRow, String> nameCol;
    @FXML private TableColumn<ResultRow, Integer> marksCol;
    @FXML private TableColumn<ResultRow, Integer> maxMarksCol;

    @FXML private Label totalSubjectsLabel;
    @FXML private Label totalMarksLabel;
    @FXML private Label percentageLabel;
    @FXML private Label gradeLabel;
    @FXML private Label statusLabel;

    private int currentStudentId;

    @FXML
    public void initialize() {
        codeCol.setCellValueFactory(cellData -> cellData.getValue().subjectCodeProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().subjectNameProperty());
        marksCol.setCellValueFactory(cellData -> cellData.getValue().marksObtainedProperty().asObject());
        maxMarksCol.setCellValueFactory(cellData -> cellData.getValue().maxMarksProperty().asObject());
    }

    public void loadStudentResult(int studentId) {
        this.currentStudentId = studentId;
        ObservableList<ResultRow> rows = FXCollections.observableArrayList();
        int totalObtained = 0;
        int subjectCount = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // Load student info
            String studentQuery = "SELECT roll_no, name, semester FROM students WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(studentQuery)) {
                pstmt.setInt(1, studentId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    rollNoLabel.setText(rs.getString("roll_no"));
                    studentNameLabel.setText(rs.getString("name"));
                    semesterLabel.setText(String.valueOf(rs.getInt("semester")));
                }
            }

            // Load marks info
            String marksQuery = "SELECT s.subject_code, s.subject_name, m.marks FROM marks m JOIN subjects s ON m.subject_id = s.id WHERE m.student_id = ? ORDER BY s.subject_code";
            try (PreparedStatement pstmt = conn.prepareStatement(marksQuery)) {
                pstmt.setInt(1, studentId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int marks = rs.getInt("marks");
                    rows.add(new ResultRow(rs.getString("subject_code"), rs.getString("subject_name"), marks, 100));
                    totalObtained += marks;
                    subjectCount++;
                }
            }

            marksTable.setItems(rows);

            if (subjectCount > 0) {
                double percentage = ((double) totalObtained / (subjectCount * 100)) * 100;
                
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

                totalSubjectsLabel.setText(subjectCount + " out of 6");
                totalMarksLabel.setText(totalObtained + " / " + (subjectCount * 100));
                percentageLabel.setText(String.format("%.2f%%", percentage));
                gradeLabel.setText(grade);
                statusLabel.setText(status);

                // Quick styling for status
                if (status.equals("PASS")) {
                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else {
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }
            } else {
                totalSubjectsLabel.setText("0");
                totalMarksLabel.setText("0 / 0");
                percentageLabel.setText("0.00%");
                gradeLabel.setText("N/A");
                statusLabel.setText("No marks found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load result: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void downloadPDF() {
        if (currentStudentId > 0) {
            Main.downloadPDF(currentStudentId);
        }
    }

    @FXML
    private void goBack() {
        Main.setRoot("students"); // Go back to student list
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
