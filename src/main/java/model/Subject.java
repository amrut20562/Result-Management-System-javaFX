package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Subject {
    private final IntegerProperty id;
    private final StringProperty subjectCode;
    private final StringProperty subjectName;
    private final IntegerProperty credits;

    public Subject(int id, String subjectCode, String subjectName, int credits) {
        this.id = new SimpleIntegerProperty(id);
        this.subjectCode = new SimpleStringProperty(subjectCode);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.credits = new SimpleIntegerProperty(credits);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getSubjectCode() { return subjectCode.get(); }
    public StringProperty subjectCodeProperty() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode.set(subjectCode); }

    public String getSubjectName() { return subjectName.get(); }
    public StringProperty subjectNameProperty() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName.set(subjectName); }

    public int getCredits() { return credits.get(); }
    public IntegerProperty creditsProperty() { return credits; }
    public void setCredits(int credits) { this.credits.set(credits); }

    @Override
    public String toString() {
        return subjectCode.get() + " - " + subjectName.get();
    }
}
