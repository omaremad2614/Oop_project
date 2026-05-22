import java.util.ArrayList;

public class CourseCatalog {

    private ArrayList<Course> courses;

    public CourseCatalog() {

        courses = new ArrayList<>();

        addDefaultCourses();
    }

    private void addDefaultCourses() {

        courses.add(
                new Course(
                        "CS101",
                        "Object Oriented Programming",
                        3
                )
        );

        courses.add(
                new Course(
                        "CS102",
                        "Database Systems",
                        3
                )
        );

        courses.add(
                new Course(
                        "CS103",
                        "Data Structures",
                        3
                )
        );

        courses.add(
                new Course(
                        "CS104",
                        "Computer Networks",
                        3
                )
        );

        courses.add(
                new Course(
                        "CS105",
                        "Operating Systems",
                        3
                )
        );
    }

    public ArrayList<Course> getCourses() {

        return courses;
    }

    public void addCourse(Course course) {

        courses.add(course);
    }

    public void removeCourse(Course course) {

        courses.remove(course);
    }
}