import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JButton addCourseButton, addStudentButton, showStudentsButton, showCoursesButton, assignGradeButton, viewStudentGradesButton, viewCourseGradeButton, reportCardButton, exitButton;

    // Data structures to store courses, students, and grades
    private HashMap<Integer, Course> courses = new HashMap<>();
    private HashMap<Integer, Student> students = new HashMap<>();
    GradeManager gradeManager = new GradeManager();
    ArrayList<Course> enrolledCourses = new ArrayList<>();


    private JTextArea reportTextArea;
    private JScrollPane reportScrollPane;

    public GUI() {
        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize reusable components for report card
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setLineWrap(true);
        reportTextArea.setWrapStyleWord(true);
        reportScrollPane = new JScrollPane(reportTextArea);
        reportScrollPane.setPreferredSize(new Dimension(400, 300));

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 1));

        addCourseButton = new JButton("Add Course");
        addStudentButton = new JButton("Add Student");
        showStudentsButton = new JButton("Show Students");
        showCoursesButton = new JButton("Show Courses");
        assignGradeButton = new JButton("Assign Grade");
        viewStudentGradesButton = new JButton("Student Grade");
        viewCourseGradeButton = new JButton("Course Grades");
        reportCardButton = new JButton("Generate Report Card");
        exitButton = new JButton("Exit");

        buttonPanel.add(addCourseButton);
        buttonPanel.add(showCoursesButton);
        buttonPanel.add(addStudentButton);
        buttonPanel.add(showStudentsButton);
        buttonPanel.add(assignGradeButton);
        buttonPanel.add(viewStudentGradesButton);
        buttonPanel.add(viewCourseGradeButton);
        buttonPanel.add(reportCardButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.WEST);

        addCourseButton.addActionListener(this);
        addStudentButton.addActionListener(this);
        showStudentsButton.addActionListener(this);
        showCoursesButton.addActionListener(this);
        assignGradeButton.addActionListener(this);
        viewStudentGradesButton.addActionListener(this);
        viewCourseGradeButton.addActionListener(this);
        reportCardButton.addActionListener(this);
        exitButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCourseButton) {
            String courseName = JOptionPane.showInputDialog(this, "Enter course name:");
            try {
                if (courseName == null || courseName.trim().isEmpty() || !courseName.matches("[a-zA-Z ]+")) {
                    textArea.append("Course name must contain only letters and spaces.\n");
                    return;
                }
                int courseCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter course Code:"));
                int credits = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter course credits:"));
                if (courses.containsKey(courseCode)) {
                    textArea.append("Course with Code " + courseCode + " already exists.\n");
                } else {
                    Course course = new Course(courseName.trim(), courseCode, credits);
                    courses.put(courseCode, course);
                    textArea.append("Course added successfully\n");
                }
            } catch (NumberFormatException ex) {
                textArea.append("Invalid input for course Code or credits. Please enter numeric values.\n");
            }
        } else if (e.getSource() == showCoursesButton) {
            if (courses.isEmpty()) {
                textArea.append("No courses available.\n");
            } else {
                textArea.append("List of courses:\n");
                for (Course course : courses.values()) {
                    textArea.append(course.toString() + "\n");
                }
            }
        } else if (e.getSource() == addStudentButton) {
            String studentName = JOptionPane.showInputDialog(this, "Enter student name:");
            try {
                if (studentName == null || studentName.trim().isEmpty() || !studentName.matches("[a-zA-Z ]+")) {
                    textArea.append("Student name cannot be empty.\n");
                    return;
                }
                int studentID = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Student ID:"));
                // Check if the student ID already exists
                if (students.containsKey(studentID)) {
                    textArea.append("Student with ID " + studentID + " already exists.\n");
                    return;
                }
                int numberofcourses = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the number of courses enrolled in:"));
                if (numberofcourses <= 0) {
                    textArea.append("Invalid number of courses. Please enter a positive number.\n");
                    return;
                } else if (numberofcourses > 10) {
                    textArea.append("Invalid number of courses. A student cannot enroll in more than 10 courses.\n");
                    return;
                }

                // Create the student once
                Student student = new Student(studentName.trim(), studentID);

                // Enroll the student in courses
                for (int i = 0; i < numberofcourses; i++) {
                    int courseCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Course Code:"));
                    Course course = courses.get(courseCode);
                    if (course != null) {
                        gradeManager.assignGrade(student, course, null); // Enroll the student in the course with no grade initially
                    } else {
                        textArea.append("Course with code \"" + courseCode + "\" doesn't exist.\n");
                        return;
                    }
                }
                students.put(studentID, student); // Add the student to the map

                textArea.append("Student added successfully and enrolled in courses.\n");
            } catch (NumberFormatException ex) {
                textArea.append("Invalid input for student name, ID, or enrolled courses codes. Please try again.\n");
            }
            
        } else if (e.getSource() == showStudentsButton) {
            if (students.isEmpty()) {
                textArea.append("No students available.\n");
            } else {
                textArea.append("List of students and their enrolled courses:\n");
                for (Student student : students.values()) {
                    textArea.append("Student: " + student.getStudentName() + " (ID: " + student.getStudentID() + ")\n");
                    Map<Course, String> studentGrades = gradeManager.getGrades().get(student);

                    if (studentGrades != null && !studentGrades.isEmpty()) {
                        textArea.append("  Enrolled Courses:\n");
                        for (Course course : studentGrades.keySet()) {
                            textArea.append("    - " + course.getCourseName() + " (Code: " + course.getCourseCode() + ")\n");
                        }
                    } else {
                        textArea.append("  No courses enrolled.\n");
                    }
                }
            }
                        
        } else if (e.getSource() == assignGradeButton) {
            try {
                int studentID = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Student ID:"));
                Student student = students.get(studentID);
                if (student == null) {
                    textArea.append("Student with ID " + studentID + " not found.\n");
                    return;
                }
                int courseCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Course Code:"));
                Course course = courses.get(courseCode);
                if (course == null) {
                    textArea.append("Course with code " + courseCode + " not found.\n");
                    return;
                }
                String grade = JOptionPane.showInputDialog(this, "Enter grade for " + student.getStudentName() + ":");
                if (grade == null || grade.trim().isEmpty()) {
                    textArea.append("Grade cannot be empty.\n");
                    return;
                }
                gradeManager.assignGrade(student, course, grade);
                textArea.append("Grade assigned successfully.\n");
            } catch (NumberFormatException ex) {
                textArea.append("Invalid input for student ID or course code. Please enter numeric values.\n");
            }
            
        } else if (e.getSource() == viewStudentGradesButton) {
            try {
                int studentID = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Student ID:"));
                Student student = students.get(studentID);
                if (student == null) {
                    textArea.append("Student with ID " + studentID + " not found.\n");
                    return;
                }
                int courseCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Course Code:"));
                Course course = courses.get(courseCode);
                if (course == null) {
                    textArea.append("Course with code " + courseCode + " not found.\n");
                    return;
                }
                String grade = gradeManager.getStudentGrade(student, course);
                textArea.append("Grade for " + student.getStudentName() + " in " + course.getCourseName() + ": " + grade + "\n");
            } catch (NumberFormatException ex) {
                textArea.append("Invalid input for student ID. Please enter a numeric value.\n");
            }
        } else if (e.getSource() == viewCourseGradeButton) {
            try {
                int courseCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Course Code:"));
                Course course = courses.get(courseCode);
                if (course == null) {
                    textArea.append("Course with code " + courseCode + " not found.\n");
                    return;
                }
                Map<Student, String> courseGrades = gradeManager.viewCourseGrades(course);
                if (courseGrades.isEmpty()) {
                    textArea.append("No grades found for course: " + course.getCourseName() + "\n");
                } else {
                    textArea.append("Grades for course: " + course.getCourseName() + "\n");
                    for (Map.Entry<Student, String> entry : courseGrades.entrySet()) {
                        Student student = entry.getKey();
                        String grade = entry.getValue();
                        textArea.append("Student: " + student.getStudentName() + " | Grade: " + grade + "\n");
                    }
                }
                } catch (NumberFormatException ex) {
                textArea.append("Invalid input for course code. Please enter a numeric value.\n");
            }
        } else if (e.getSource() == reportCardButton) {
            String inputId = JOptionPane.showInputDialog(this, "Enter Student ID to generate report card:");
            if (inputId == null) {
                textArea.append("Invalid Input.\n");
                return;
            }
            try {
                int studentId = Integer.parseInt(inputId);
                if (!students.containsKey(studentId)) {
                    JOptionPane.showMessageDialog(this, "Student not found. Please try again.");
                    return;
                }
        
                Student found = students.get(studentId);
                String report = gradeManager.GenerateReportCard(found);
        
                JTextArea textArea = new JTextArea(report);
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
        
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
        
                JOptionPane.showMessageDialog(this, scrollPane, "Report Card", JOptionPane.INFORMATION_MESSAGE);
        
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a numeric value.");
            }
        } else if (e.getSource() == exitButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }  
    public static void main(String[] args) throws IOException {
        new GUI();
    }
}
