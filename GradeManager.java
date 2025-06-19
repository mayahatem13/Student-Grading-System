import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GradeManager {
    private Map<Student, Map<Course, String>> grades = new HashMap<>();

    public void assignGrade(Student student, Course course, String grade) {
        grades.computeIfAbsent(student, k -> new HashMap<>()).put(course, grade);
    }

    public String getStudentGrade(Student student, Course course) {
        Map<Course, String> studentGrades = grades.get(student);
    
        if (studentGrades != null) {
            String grade = studentGrades.get(course);
            if (grade != null) {
                return grade; 
            } else {
                return "No grade found for course: " + course.getCourseName();
            }
        } else {
            return "No grades found for student: " + student.getStudentName();
        }
    }


    public Map<Student, Map<Course, String>> getGrades() {
        return grades;
    }

    public Map<Student, String> viewCourseGrades(Course course) {
        Map<Student, String> courseGrades = new HashMap<>();

        for (Map.Entry<Student, Map<Course, String>> entry : grades.entrySet()) {
            Student student = entry.getKey();
            Map<Course, String> studentGrades = entry.getValue();

            if (studentGrades.containsKey(course)) {
                String grade = studentGrades.get(course);
                courseGrades.put(student, grade);
            }
        }
        return courseGrades;
    }

    public double calculateGPA(Student student) {
        Map<Course, String> studentGrades = grades.get(student);
        if (studentGrades == null || studentGrades.isEmpty()) {
            Logger.getLogger(GradeManager.class.getName()).warning("No grades found for student: " + student.getStudentName());
            return 0;
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<Course, String> entry : studentGrades.entrySet()) {
            Course course = entry.getKey();
            String grade = entry.getValue();
            double points = convertGradeToPoints(grade);
            totalPoints += points * course.getCredits();
            totalCredits += course.getCredits();
        }

        if (totalCredits == 0) {
            System.out.println("Error: total credit hours is zero.");
            return 0;
        }

        double gpa = totalPoints / totalCredits;
        System.out.printf("GPA for %s (%s): %.2f%n", student.getStudentName(), student.getStudentID(), gpa);
        return gpa;
    }
    private double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A":
                return 4.0;
            case "B":
                return 3.0;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                System.out.println("Invalid grade: " + grade);
                return 0.0;
        }
    }
    public String GenerateReportCard(Student student) {
        Map<Course, String> studentGrades = grades.get(student);

        if (studentGrades == null || studentGrades.isEmpty()) {
            return "No grades found for student: " + student.getStudentName();
        }

        String report = "Report Card for " + student.getStudentName() + ":\n";
        int totalCredits = 0;

        for (Map.Entry<Course, String> entry : studentGrades.entrySet()) {
            Course course = entry.getKey();
            String grade = entry.getValue();

            if (grade != null) {
                report += "Course: " + course.getCourseName() +
                          " | Credits: " + course.getCredits() +
                          " | Grade: " + grade + "\n";
                totalCredits += course.getCredits();
            } else {
                report += "No grade found for course: " + course.getCourseName() + "\n";
            }
        }

        report += "Total Credits: " + totalCredits + "\n";


        double gpa = calculateGPA(student);
        report +=  "GPA: " + String.format("%.2f", gpa);
        return report;}
}
