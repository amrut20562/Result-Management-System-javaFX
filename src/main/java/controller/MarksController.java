package controller;

import app.DBConnection;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Student;
import model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private ComboBox<Subject> subjectComboBox;

    @FXML
    private TextField marksField;

    @FXML
    public void initialize() {
        loadStudents();
        loadSubjects();
    }

    private void loadStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT * FROM students ORDER BY roll_no";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getInt("semester"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
            studentComboBox.setItems(students);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load students: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadSubjects() {
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        String query = "SELECT * FROM subjects ORDER BY subject_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getInt("id"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("credits")
                ));
            }
            subjectComboBox.setItems(subjects);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load subjects: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void saveMarks() {
        Student selectedStudent = studentComboBox.getValue();
        Subject selectedSubject = subjectComboBox.getValue();
        String marksText = marksField.getText().trim();

        if (selectedStudent == null || selectedSubject == null || marksText.isEmpty()) {
            showAlert("Validation Error", "Please select student, subject, and enter marks.", Alert.AlertType.WARNING);
            return;
        }

        int marks;
        try {
            marks = Integer.parseInt(marksText);
            if (marks < 0 || marks > 100) {
                showAlert("Validation Error", "Marks must be between 0 and 100.", Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Marks must be a valid number.", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // First check if marks exist for this student and subject
            String checkQuery = "SELECT id FROM marks WHERE student_id = ? AND subject_id = ?";
            boolean exists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, selectedStudent.getId());
                checkStmt.setInt(2, selectedSubject.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                // Update
                String updateQuery = "UPDATE marks SET marks = ? WHERE student_id = ? AND subject_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, marks);
                    updateStmt.setInt(2, selectedStudent.getId());
                    updateStmt.setInt(3, selectedSubject.getId());
                    updateStmt.executeUpdate();
                    showAlert("Success", "Marks updated successfully!", Alert.AlertType.INFORMATION);
                }
            } else {
                // Insert
                String insertQuery = "INSERT INTO marks (student_id, subject_id, marks) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, selectedStudent.getId());
                    insertStmt.setInt(2, selectedSubject.getId());
                    insertStmt.setInt(3, marks);
                    insertStmt.executeUpdate();
                    showAlert("Success", "Marks added successfully!", Alert.AlertType.INFORMATION);
                }
            }
            marksField.clear();
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not save marks: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goHome() {
        Main.setRoot("home");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
