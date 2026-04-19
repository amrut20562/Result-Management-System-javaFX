package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultRow {
    private final StringProperty subjectCode;
    private final StringProperty subjectName;
    private final IntegerProperty marksObtained;
    private final IntegerProperty maxMarks;

    public ResultRow(String subjectCode, String subjectName, int marksObtained, int maxMarks) {
        this.subjectCode = new SimpleStringProperty(subjectCode);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.marksObtained = new SimpleIntegerProperty(marksObtained);
        this.maxMarks = new SimpleIntegerProperty(maxMarks);
    }

    public String getSubjectCode() { return subjectCode.get(); }
    public StringProperty subjectCodeProperty() { return subjectCode; }

    public String getSubjectName() { return subjectName.get(); }
    public StringProperty subjectNameProperty() { return subjectName; }

    public int getMarksObtained() { return marksObtained.get(); }
    public IntegerProperty marksObtainedProperty() { return marksObtained; }

    public int getMaxMarks() { return maxMarks.get(); }
    public IntegerProperty maxMarksProperty() { return maxMarks; }
}
