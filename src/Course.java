/**
 * Represents a single course in the catalog.
 * Holds all details shown on the course-detail screen.
 */
public class Course {

    // ── Fields ──────────────────────────────────────────────────────────────
    private final String code;           // e.g.  "MATH 101"
    private final String name;           // e.g.  "Mathematics I"
    private final String description;   // short blurb shown on the card
    private final int    creditHours;   // 1 – 6
    private final String schedule;      // e.g.  "Mon / Wed / Fri"
    private final String instructor;    // full name of the professor
    private final int    maxStudents;   // room / section capacity
    private       int    enrolledStudents; // current head-count (mutable)
    private final String icon;          // Unicode symbol used in the card header

    // ── Constructor ──────────────────────────────────────────────────────────
    public Course(String code,
                  String name,
                  String description,
                  int    creditHours,
                  String schedule,
                  String instructor,
                  int    maxStudents,
                  int    enrolledStudents,
                  String icon) {

        this.code             = code;
        this.name             = name;
        this.description      = description;
        this.creditHours      = creditHours;
        this.schedule         = schedule;
        this.instructor       = instructor;
        this.maxStudents      = maxStudents;
        this.enrolledStudents = enrolledStudents;
        this.icon             = icon;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getCode()             { return code; }
    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public int    getCreditHours()      { return creditHours; }
    public String getSchedule()         { return schedule; }
    public String getInstructor()       { return instructor; }
    public int    getMaxStudents()      { return maxStudents; }
    public int    getEnrolledStudents() { return enrolledStudents; }
    public String getIcon()             { return icon; }

    // ── Derived helpers ──────────────────────────────────────────────────────
    /** How many seats are still open. */
    public int getAvailableSeats() {
        return maxStudents - enrolledStudents;
    }

    /** True when there is at least one free seat. */
    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    /**
     * Enrolls one student.
     * @throws IllegalStateException if the course is already full.
     */
    public void enroll() {
        if (!hasAvailableSeats())
            throw new IllegalStateException("Course \"" + name + "\" is full.");
        enrolledStudents++;
    }

    @Override
    public String toString() {
        return code + " – " + name +
               " | " + creditHours + " cr | " +
               enrolledStudents + "/" + maxStudents + " students";
    }
}
