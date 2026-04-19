package controller;

import app.DBConnection;
import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStudentController {

    @FXML
    private TextField rollNoField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private void saveStudent() {
        String rollNo = rollNoField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (rollNo.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.WARNING);
            return;
        }

        String query = "INSERT INTO students (roll_no, name, email, phone, semester) VALUES (?, ?, ?, ?, 4)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Student added successfully!", Alert.AlertType.INFORMATION);
                clearFields();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate")) {
                showAlert("Database Error", "A student with this Roll No already exists.", Alert.AlertType.ERROR);
            } else {
                showAlert("Database Error", "Could not save student: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void clearFields() {
        rollNoField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
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
