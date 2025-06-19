import java.util.Objects;

public class Student {
    private String name;
    private int studentID;
    private int courseCode;

    public Student(String name, int studentID) {
        this.name = name;
        this.studentID = studentID;
    }

    public int getEnrolledCourse() {
        return courseCode;
    }
    
    public void setEnrolledCourse(int courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentName() {
        return name;
    }

    public int getStudentID() {
        return studentID;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentID + ", Name: " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return this.studentID == other.studentID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentID);
    }
}
