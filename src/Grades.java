public class Grades {
    private double week7;      // Out of 30
    private double week12;     // Out of 20
    private double courseWork; // Out of 10
    private double finalExam;  // Out of 40
    private boolean finalized;

    // Constructor for a brand-new registration (all zeroes)
    public Grades() {
        this.week7 = 0.0;
        this.week12 = 0.0;
        this.courseWork = 0.0;
        this.finalExam = 0.0;
        this.finalized = false;
    }

    // Constructor for loading existing grades from the database
    public Grades(double week7, double week12, double courseWork, double finalExam) {
        this.week7 = week7;
        this.week12 = week12;
        this.courseWork = courseWork;
        this.finalExam = finalExam;
    }

    // Calculate total on the fly
    public double getTotalMark() {
        return week7 + week12 + courseWork + finalExam;
    }

    public String getLetterGrade() {
        double total = Math.round(getTotalMark()); // Rounding to avoid 89.9 missing an A-
        if (total >= 97) return "A+";
        if (total >= 93) return "A";
        if (total >= 89) return "A-";
        if (total >= 84) return "B+";
        if (total >= 80) return "B";
        if (total >= 76) return "B-";
        if (total >= 73) return "C+";
        if (total >= 70) return "C";
        if (total >= 67) return "C-";
        if (total >= 64) return "D+";
        if (total >= 60) return "D";
        return "F";
    }

    // Getters and Setters
    public double getWeek7() { return week7; }
    public void setWeek7(double week7) { this.week7 = week7; }

    public double getWeek12() { return week12; }
    public void setWeek12(double week12) { this.week12 = week12; }

    public double getCourseWork() { return courseWork; }
    public void setCourseWork(double courseWork) { this.courseWork = courseWork; }

    public double getFinalExam() { return finalExam; }
    public void setFinalExam(double finalExam) { this.finalExam = finalExam; }

    public boolean isFinalized() { return finalized; }
    public void setFinalized(boolean finalized) { this.finalized = finalized; }
}