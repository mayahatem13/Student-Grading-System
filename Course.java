import java.util.Objects;

public class Course {
    private String courseName;
    private int courseCode;
    private int credits;


    public Course(String courseName, int courseCode, int credits){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return "Course Code: " + courseCode + ", Name: " + courseName + ", Credits: " + credits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course)) return false;
        Course other = (Course) obj;
        return this.courseCode == other.courseCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
