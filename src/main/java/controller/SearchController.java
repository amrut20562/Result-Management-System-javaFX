package controller;

import app.DBConnection;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private TableView<Student> searchTable;
    @FXML private TableColumn<Student, Integer> idCol;
    @FXML private TableColumn<Student, String> rollNoCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, Integer> semesterCol;
    @FXML private TableColumn<Student, Void> actionCol;

    private ObservableList<Student> searchResults = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        rollNoCol.setCellValueFactory(cellData -> cellData.getValue().rollNoProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        semesterCol.setCellValueFactory(cellData -> cellData.getValue().semesterProperty().asObject());

        addActionButtonsToTable();
    }

    @FXML
    private void performSearch() {
        String queryText = searchField.getText().trim();
        if (queryText.isEmpty()) {
            showAlert("Validation", "Please enter a name or roll number to search.", Alert.AlertType.WARNING);
            return;
        }

        searchResults.clear();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR roll_no LIKE ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + queryText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                searchResults.add(new Student(
                        rs.getInt("id"),
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getInt("semester"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

            searchTable.setItems(searchResults);
            
            if (searchResults.isEmpty()) {
                showAlert("Search Result", "No students found for: " + queryText, Alert.AlertType.INFORMATION);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to search: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addActionButtonsToTable() {
        actionCol.setCellFactory(param -> new TableCell<Student, Void>() {
            private final Button viewBtn = new Button("View");
            private final Button pdfBtn = new Button("PDF");
            private final HBox pane = new HBox(10, viewBtn, pdfBtn);

            {
                viewBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                pdfBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                viewBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    Main.loadResultScene(student.getId());
                });

                pdfBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    Main.downloadPDF(student.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
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
