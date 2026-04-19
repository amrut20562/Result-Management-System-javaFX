package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Marks {
    private final IntegerProperty id;
    private final IntegerProperty studentId;
    private final IntegerProperty subjectId;
    private final IntegerProperty marks;

    public Marks(int id, int studentId, int subjectId, int marks) {
        this.id = new SimpleIntegerProperty(id);
        this.studentId = new SimpleIntegerProperty(studentId);
        this.subjectId = new SimpleIntegerProperty(subjectId);
        this.marks = new SimpleIntegerProperty(marks);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public int getStudentId() { return studentId.get(); }
    public IntegerProperty studentIdProperty() { return studentId; }
    public void setStudentId(int studentId) { this.studentId.set(studentId); }

    public int getSubjectId() { return subjectId.get(); }
    public IntegerProperty subjectIdProperty() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId.set(subjectId); }

    public int getMarks() { return marks.get(); }
    public IntegerProperty marksProperty() { return marks; }
    public void setMarks(int marks) { this.marks.set(marks); }
}
