package controller;

import app.DBConnection;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalysisController {

    @FXML private BarChart<String, Number> subjectBarChart;
    @FXML private PieChart gradePieChart;

    @FXML
    public void initialize() {
        // Prevent animated chart from glitching on load depending on JavaFX versions
        subjectBarChart.setAnimated(false);
        gradePieChart.setAnimated(false);
        
        loadSubjectAverages();
        loadGradeDistribution();
    }

    private void loadSubjectAverages() {
        String query = "SELECT s.subject_name, AVG(m.marks) as avg_marks " +
                       "FROM marks m JOIN subjects s ON m.subject_id = s.id " +
                       "GROUP BY s.id ORDER BY avg_marks DESC";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Marks");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                if (subjectName.length() > 15) {
                    subjectName = subjectName.substring(0, 15) + "...";
                }
                double avgMarks = rs.getDouble("avg_marks");
                series.getData().add(new XYChart.Data<>(subjectName, avgMarks));
            }

            subjectBarChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load bar chart: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadGradeDistribution() {
        String query = "SELECT " +
                "CASE " +
                "  WHEN marks >= 90 THEN 'O' " +
                "  WHEN marks >= 80 THEN 'A+' " +
                "  WHEN marks >= 70 THEN 'A' " +
                "  WHEN marks >= 60 THEN 'B+' " +
                "  WHEN marks >= 50 THEN 'B' " +
                "  WHEN marks >= 40 THEN 'P' " +
                "  ELSE 'F' " +
                "END as grade, COUNT(*) as count " +
                "FROM marks GROUP BY grade";

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String grade = rs.getString("grade");
                int count = rs.getInt("count");
                pieChartData.add(new PieChart.Data(grade + " (" + count + ")", count));
            }

            gradePieChart.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load pie chart: " + e.getMessage(), Alert.AlertType.ERROR);
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
