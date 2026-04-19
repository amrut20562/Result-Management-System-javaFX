package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final IntegerProperty id;
    private final StringProperty rollNo;
    private final StringProperty name;
    private final IntegerProperty semester;
    private final StringProperty email;
    private final StringProperty phone;

    public Student(int id, String rollNo, String name, int semester, String email, String phone) {
        this.id = new SimpleIntegerProperty(id);
        this.rollNo = new SimpleStringProperty(rollNo);
        this.name = new SimpleStringProperty(name);
        this.semester = new SimpleIntegerProperty(semester);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getRollNo() { return rollNo.get(); }
    public StringProperty rollNoProperty() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo.set(rollNo); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public int getSemester() { return semester.get(); }
    public IntegerProperty semesterProperty() { return semester; }
    public void setSemester(int semester) { this.semester.set(semester); }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String email) { this.email.set(email); }

    public String getPhone() { return phone.get(); }
    public StringProperty phoneProperty() { return phone; }
    public void setPhone(String phone) { this.phone.set(phone); }

    @Override
    public String toString() {
        // Helpful for combobox representation if needed
        return rollNo.get() + " - " + name.get();
    }
}
