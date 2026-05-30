import java.util.ArrayList;
import java.util.HashMap;

public class Student extends Person {

    private double gpa;
    private int term; // Which term/semester they are in

    private ArrayList<Course> registeredCourses;

    // A dictionary linking "CourseCode" to their specific "Grades"
    private HashMap<String, Grades> courseGrades;

    // Updated Constructor
    public Student(int id, String name, String email, double gpa, int term) {
        super(id, name, email);

        this.gpa = gpa;
        this.term = term;

        this.registeredCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>(); // Initialize the dictionary
    }

    // --- Getters & Setters ---
    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    // --- Grade Management Methods ---

    // Grabs the student's grades for a specific class
    public Grades getGradesForCourse(String courseCode) {
        return courseGrades.get(courseCode);
    }

    // Used by the database reader to load existing grades into the student's profile
    public void loadExistingGrades(String courseCode, Grades grades) {
        courseGrades.put(courseCode, grades);
    }

    // --- Course Registration ---

    public boolean addCourse(Course course) {
        if(registeredCourses.contains(course)){
            System.out.println("STUDENT.JAVA: Tried to add " + course.getCode() + " but it is already registered.");
            return false;
        }

        registeredCourses.add(course);
        System.out.println("STUDENT.JAVA: Successfully added " + course.getCode() + " to registeredCourses list.");

        // Ensure courseGrades is not null (safety check!)
        if (this.courseGrades == null) {
            System.out.println("STUDENT.JAVA WARNING: courseGrades HashMap was null! Initializing now.");
            this.courseGrades = new HashMap<>();
        }

        courseGrades.put(course.getCode(), new Grades());
        System.out.println("STUDENT.JAVA: Created a fresh Grades sheet for " + course.getCode() + " in the HashMap.");

        return true;
    }

    public ArrayList<Course> getRegisteredCourses(){
        return registeredCourses;
    }
}