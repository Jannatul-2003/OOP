import java.util.*;

class Database {
    public static List<Student> students = new ArrayList<>();

    public Database() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Rank> getRankList(String courseName) {
        List<Rank> ranks = new ArrayList<>();

        for (Student student : students) {
            double studentGrade = 0.0;
            for (Course c : student.totalCourses) {
                if (c.courseName.equals(courseName)) {
                    studentGrade = c.grade;
                    break;
                }
            }
            Rank rank = new Rank();
            rank.student = student;
            rank.grade = studentGrade;
            ranks.add(rank);
        }

        ranks.sort((r1, r2) -> Double.compare(r2.grade, r1.grade)); // Sort in descending order

        return ranks;
    }

    public List<Student> getComprehensiveList(String courseName) {
        List<Student> std = new ArrayList<>();

        for (Student student : students) {
            for (Course c : student.totalCourses) {
                if (c.courseName.equals(courseName)) {
                    std = c.getStudents();
                    return std;
                    // break;
                }
            }
        }
        return std;
    }

    class Rank {
        Student student;
        double grade;
    }

    class Student {
        protected String name;
        protected String roll;
        private String email;
        protected List<Course> totalCourses;
        private List<Course> majorCourses;
        private Course optionalCourse;
        private double cgpa;

        public Student() {
            this.cgpa = 0.0;
            System.out.println("Enter Name: ");
            Student_Database.scanner.nextLine();
            this.name = Student_Database.scanner.nextLine();
            System.out.println("Enter Roll: ");
            this.roll = Student_Database.scanner.nextLine();
            System.out.println("Enter Email: ");
            this.email = Student_Database.scanner.nextLine();
            this.totalCourses = new ArrayList<>();
            this.majorCourses = new ArrayList<>();
            clear();
            TotalCourses(this);
            addStudent(this);
        }

        public void addMajorCourse(Course course) {
            majorCourses.add(course);
            totalCourses.add(course);
            course.enrollStudent(this);
            course.courseType = "Major";
            course.credits = 3;
        }

        public void setOptionalCourse(Course course) {
            optionalCourse = course;
            totalCourses.add(course);
            course.enrollStudent(this);
            course.courseType = "Optional";
            course.credits = 1.5;
        }

        public double getCGPA() {
            return cgpa;
        }

    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // class TotalCourses{
    public void TotalCourses(Student student) {
        System.out.println("Select Major Courses: ");

        for (int i = 0; i < 3; i++) {
            System.out.println("Select Three Major Courses:");
            for (int j = 1; j < Student_Database.ara.length; j++) {
                System.out.println(j + ". " + Student_Database.ara[j]);
            }
            int query = Student_Database.scanner.nextInt();
            Course course;
            switch (query) {
                case 1:
                    course = new ArtificialIntelligence(student);
                    break;
                case 2:
                    course = new Security(student);
                    break;
                case 3:
                    course = new OperationResearch(student);
                    break;
                case 4:
                    course = new Networking(student);
                    break;
                default:
                    course = new EmbeddedSystems(student);
                    break;
            }
            EvaluationCombination(course);
            student.addMajorCourse(course);
            clear();
        }
        System.out.println("Select One Optional Course: ");
        for (int j = 1; j < Student_Database.ara.length; j++) {
            System.out.println(j + ". " + Student_Database.ara[j]);
        }
        int query = Student_Database.scanner.nextInt();
        Course course;
        switch (query) {
            case 1:
                course = new ArtificialIntelligence(student);
                break;
            case 2:
                course = new Security(student);
                break;
            case 3:
                course = new OperationResearch(student);
                break;
            case 4:
                course = new Networking(student);
                break;
            default:
                course = new EmbeddedSystems(student);
                break;
        }
        EvaluationCombination(course);
        student.setOptionalCourse(course);
        // System.out.println("Hahaa");
        for (Course c : student.totalCourses) {
            student.cgpa += c.grade * c.credits;
        }
        student.cgpa /= 10.5;
    }
    // }

    abstract class Course {
        protected String courseName;
        protected String courseType;
        protected double credits;
        protected List<Student> students;
        private double marks;
        private double grade;

        public Course() {
            this.students = new ArrayList<>();
        }

        public void enrollStudent(Student student) {
            students.add(student);
        }

        public void setMarks(double marks) {
            this.marks = marks;
            Grade g = new Grade();
            this.grade = g.point(marks);
            return;
        }

        public List<Student> getStudents() {
            return students;
        }

    }

    class ArtificialIntelligence extends Course {
        public ArtificialIntelligence() {
            this.courseName = "ArtificialIntelligence";
        }

        public ArtificialIntelligence(Student student) {
            this.courseName = "ArtificialIntelligence";
        }

    }

    class Security extends Course {
        public Security() {
            this.courseName = "Security";
        }

        public Security(Student student) {
            this.courseName = "Security";
        }

    }

    class OperationResearch extends Course {
        public OperationResearch() {
            this.courseName = "OperationResearch";
        }

        public OperationResearch(Student student) {
            this.courseName = "OperationResearch";
        }

    }

    class Networking extends Course {
        public Networking() {
            this.courseName = "Networking";
        }

        public Networking(Student student) {
            this.courseName = "Networking";
        }

    }

    class EmbeddedSystems extends Course {
        public EmbeddedSystems() {
            this.courseName = "EmbeddedSystems";
        }

        public EmbeddedSystems(Student student) {
            this.courseName = "EmbeddedSystems";
        }

    }

    class Grade {
        public double point(double marks) {
            int p;
            double g;
            if (marks >= 80)
                p = 80;
            else {
                p = (int) marks / 5;
                p *= 5;
            }
            g = (double) p / 20;
            return g;
        }
    }

    // class EvaluationCombination {
    // Course course;
    int arr[] = new int[3];

    public void EvaluationCombination(Course course) {

        int arr1[] = { 0, 30, 20, 40 };
        int arr2[] = { 0, 10, 5 };
        int arr3[] = { 0, 60, 70 };

        boolean isOn = true;
        while (isOn) {
            System.out.println("Marks for MidTerm:\n1. 30\n2. 20\n3. 40\nChoose one: ");
            int q = Student_Database.scanner.nextInt();
            int m = arr1[q];
            arr[0] = m;
            System.out.println(
                    "Marks for Regular Assessment:\n1.Assignment of 10\n2.Assignment of 5 and Attendance of 5\nChoose one: ");
            q = Student_Database.scanner.nextInt();
            m += arr2[q];
            arr[1] = arr2[q];
            System.out.println("Marks for Final:\n1. 60\n2. 70\nChoose one: ");
            q = Student_Database.scanner.nextInt();
            m += arr3[q];
            arr[2] = arr3[q];
            if (m == 100) {
                Evaluation(course, arr);
                isOn = false;
                // System.out.println("Hahaa");
                break;
            } else {
                System.out.println("Marks should be 100. Try again.");
            }
        }
        return;
    }

    // class Evaluation {
    public void Evaluation(Course course, int arr[]) {
        System.out.println("Marks for MidTerm: ");
        double l = Student_Database.scanner.nextDouble();
        while (l > arr[0]) {
            System.out.println("Marks should not be greater than " + arr[0] + ". Try again.");
            l = Student_Database.scanner.nextDouble();
        }
        double m = l;
        if (arr[1] == 5) {
            System.out.println("Attendance: ");
            double a = Student_Database.scanner.nextDouble();
            while (a > 5) {
                System.out.println("Attendance should not be greater than 5. Try again.");
                a = Student_Database.scanner.nextDouble();
            }
            m += a;
        }
        System.out.println("Marks for Regular Assessment: ");
        l = Student_Database.scanner.nextDouble();

        while (l > arr[1]) {
            System.out.println("Marks should not be greater than " + arr[1] + ". Try again.");
            l = Student_Database.scanner.nextDouble();
        }
        m += l;
        System.out.println("Marks for Final: ");
        l = Student_Database.scanner.nextDouble();
        while (l > arr[2]) {
            System.out.println("Marks should not be greater than " + arr[2] + ". Try again.");
            l = Student_Database.scanner.nextDouble();
        }
        m += l;
        course.setMarks(m);
        return;
    }

    class EvaluationComponent {
        private String name;
        private double marks;

        public EvaluationComponent(String name, double marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    class Midterm extends EvaluationComponent {
        public Midterm(double marks) {
            super("Midterm", marks);
        }
    }

    class RegularAssessment extends EvaluationComponent {
        public RegularAssessment(double marks) {
            super("RegularAssessment", marks);
        }
    }

    class Final extends EvaluationComponent {
        public Final(double marks) {
            super("Final", marks);
        }
    }

    class overallRank {
        public overallRank() {
            students.sort((s1, s2) -> Double.compare(s2.getCGPA(), s1.getCGPA()));
            for (int i = 0; i < students.size(); i++) {
                System.out.println(i + 1 + ". Name: " + students.get(i).name + " CGPA: " + students.get(i).getCGPA());
            }
        }
    }
}

public class Student_Database {
    public static Scanner scanner = new Scanner(System.in);
    public static String ara[] = { "", "ArtificialIntelligence", "Security", "OperationResearch", "Networking",
            "Embedded Systems" };

    public static void main(String[] args) {
        int query;
        boolean isRunning = true;
        Database db = new Database();
        while (isRunning) {
            System.out.println(
                    "1. Add Student\n2. See Overall Rank List\n3. Course Based Rank List\n4. Get Comprehensive List\n5. Exit\nChoose one:");
            query = scanner.nextInt();
            switch (query) {
                case 1:
                    Database.Student student = db.new Student();
                    student = null;
                    // System.out.println("Hahaa");
                    break;
                case 2:
                    Database.overallRank o = db.new overallRank();
                    o = null;
                    break;
                case 3:
                    System.out.println("Select Course: ");
                    for (int j = 1; j < ara.length; j++) {
                        System.out.println(j + ". " + ara[j]);
                    }
                    int q = scanner.nextInt();
                    List<Database.Rank> r = db.getRankList(ara[q]);
                    for (int i = 0; i < r.size(); i++) {
                        System.out.println(i + 1 + ". Name: " + r.get(i).student.name + " Grade: " + r.get(i).grade);
                    }
                    break;
                case 4:
                    System.out.println("Select Course: ");
                    for (int j = 1; j < ara.length; j++) {
                        System.out.println(j + ". " + ara[j]);
                    }
                    int p = scanner.nextInt();
                    List<Database.Student> s = db.getComprehensiveList(ara[p]);
                    for (int i = 0; i < s.size(); i++) {
                        System.out.println(i + 1 + ". Name: " + s.get(i).name);
                    }
                    break;
                case 5:
                    isRunning = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid Input. Try again.");
            }
        }
    }
}
