import java.util.ArrayList;

public class Student extends Person {

    private String major;
    private ArrayList<Course> registeredCourses;

    public Student(int id, String name, String email, String major) {

        super(id, name, email);

        this.major = major;
        this.registeredCourses = new ArrayList<>();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public ArrayList<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerCourse(Course course) {
        registeredCourses.add(course);
    }

    public void dropCourse(Course course) {
        registeredCourses.remove(course);
    }
}