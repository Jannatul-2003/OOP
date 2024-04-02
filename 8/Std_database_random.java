import java.util.stream.Collectors;
import java.util.*;

class Student {
    protected String name;
    protected int roll;
    protected String email;
    protected List<Course> totalCourses;
    private List<Course> majorCourses;
    private Course optionalCourse;
    private double cgpa;
    private double total_marks;

    public Student(String name, int roll, String email) {
        this.cgpa = this.total_marks = 0.0;
        this.name = name;
        this.roll = roll;
        this.email = email;
        this.totalCourses = new ArrayList<>();
        this.majorCourses = new ArrayList<>();
    }

    public void addMajorCourse(Course course) {
        Course c = new Course(course.courseName);
        majorCourses.add(c);
        totalCourses.add(c);
        course.enrollStudent(this);
        c.courseType = "Major";
        c.credits = 3;
    }

    public void setOptionalCourse(Course course) {
        optionalCourse = new Course(course.courseName);
        totalCourses.add(optionalCourse);
        course.enrollStudent(this);
        optionalCourse.courseType = "Optional";
        optionalCourse.credits = 1.5;
    }

    public double getCGPA() {
        return cgpa;
    }

    public double getTotalMarks() {
        return total_marks;
    }

    public void setTotalMarks(double total_marks) {
        this.total_marks = total_marks;
    }

    public void setCGPA(double cgpa) {
        this.cgpa = cgpa;
    }

    public Course getCourse(String courseName) {
        for (Course c : totalCourses) {
            if (c.courseName.equals(courseName)) {
                return c;
            }
        }
        return null;
    }

    public double getCourseGrade(String courseName) {
        for (Course c : totalCourses) {
            if (c.courseName.equals(courseName)) {
                return c.getGrade();
            }
        }
        return 0.0;
    }

    public List<Course> sortCourses() {
        Collections.sort(majorCourses, (c1, c2) -> {
            return c1.courseName.compareTo(c2.courseName);
        });
        return majorCourses;
    }

    public void studentInfo() {
        System.out.print("Name   : " + name + "\nRoll   : " + roll + "\nEmail  : " + email + "\nCourses:\n         Major: ");
        List<Course> majrCourses = sortCourses();
        int a = 1;
        for (Course c : majrCourses) {
            if (a != 1)
                System.out.print(", ");
            System.out.print(c.courseName);
            a = 0;
        }
        System.out.println(".\n         Minor: " + optionalCourse.courseName + ".");
    }
}

class Course {
    protected String courseName;
    protected String courseType;
    protected double credits;
    protected List<Student> students;
    protected List<EvaluationComponent> evaluationComponents;
    private double marks;
    private double grade;

    public Course(String courseName) {
        this.courseName = courseName;
        this.students = new ArrayList<>();
        this.evaluationComponents = new ArrayList<>();
    }

    public void enrollStudent(Student student) {
        students.add(student);
    }

    public void addEvaluationComponent(EvaluationComponent evaluationComponent) {
        evaluationComponents.add(evaluationComponent);
    }

    public void setMarks(double marks) {
        this.marks = marks;
        GradeCalculator G = new GradeCalculator();
        G.calculateGrade(this);
    }

    public double getObtainedMarks() {
        return marks;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<EvaluationComponent> getEvaluationComponents() {
        return evaluationComponents;
    }

    public boolean isInvaild() {
        int sum=0;
        for (EvaluationComponent e : evaluationComponents)
            sum+=e.weightage;
        if (sum == 100)
            return false;
        return true;
    }
}

class EvaluationComponent {
    protected String EvaluationName;
    protected double weightage;

    public EvaluationComponent(String EvaluationName, double weightage) {
        this.EvaluationName = EvaluationName;
        this.weightage = weightage;
    }

}

class GradeCalculator {
    private List<Student> students;
    private List<Course> courses;

    public GradeCalculator(List<Student> students, List<Course> courses) {
        this.students = students;
        this.courses = courses;
    }

    public GradeCalculator() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public void getComprehensiveList(String courseName) {
        List<Student> comprehensiveList = new ArrayList<>();
        for (Course c : courses) {
            if (c.courseName.equals(courseName)) {
                comprehensiveList = c.getStudents();
                break;
            }
        }
        System.out.println("________Comprehensive List of " + courseName + "________\n\n");
        int i = 1;
        for (Student s : comprehensiveList) {
            System.out.println("*****No. " + i + "*****");
            s.studentInfo();
            i++;
            System.out.println("Assessment Criteria: ");
            Course c = s.getCourse(courseName);
            for (EvaluationComponent e : c.evaluationComponents)
                System.out.println("         " + e.EvaluationName + " " + e.weightage);
            System.out.println();
        }

    }

    public void calculateGrade(Course c) {
        double obtainedMarks = c.getObtainedMarks();
        double grade = 0.0;
        int m[] = { 80, 75, 70, 65, 60, 55, 50, 45, 40, 0 };
        double g[] = { 4, 3.75, 3.5, 3.25, 3, 2.75, 2.5, 2.25, 2, 0 };
        for (int i = 0; i < 10; i++) {
            if (obtainedMarks >= m[i]) {
                grade = g[i];
                break;
            }
        }
        c.setGrade(grade);
    }

    public void calculateTotalGPA(Student s) {
        double totalMarks = 0;
        totalMarks = s.totalCourses.stream().mapToDouble(course -> course.getObtainedMarks()).sum();
        double totalGPA = 0;
        totalGPA = s.totalCourses.stream().mapToDouble(course -> course.getGrade() * course.credits).sum();
        totalGPA /= s.totalCourses.stream().mapToDouble(course -> course.credits).sum();
        s.setCGPA(totalGPA);
        s.setTotalMarks(totalMarks);
    }

    public List<Student> overAllRank() {
        Comparator<Student> c = Comparator.comparing(Student::getCGPA).reversed().thenComparing(Student::getTotalMarks);
        ArrayList<Student> temp = students.stream().sorted(c).collect(Collectors.toCollection(ArrayList::new));
        return temp;
    }

    public List<Student> getCourseRankList(String courseName) {
        List<Student> coursewise = new ArrayList<>();
        List<Student> std = new ArrayList<>();
        for (Course c : courses) {
            if (c.courseName.equals(courseName)) {
                std = c.getStudents();
                break;
            }
        }
        int size = std.size();
        for (int i = 0; i < size; i++) {
            Student s = std.get(i);
            insertDescending(coursewise, s, courseName);
        }
        return coursewise;
    }

    public static void insertDescending(List<Student> list, Student s, String courseName) {
        int index = 0;
        Course cs = s.getCourse(courseName);

        for (int i = 0; i < list.size(); i++) {
            Course c = cs;
            Student s1 = list.get(i);
            Course c1 = s1.getCourse(courseName);

            if ((c != null && c1 != null) && (c.getGrade() > c1.getGrade()
                    || (c.getGrade() == c1.getGrade() && s.getTotalMarks() > s1.getTotalMarks()))) {
                index = i;
                break;
            }
            index = i + 1;
        }
        if (cs != null)
            list.add(index, s);
    }
}

public class Std_database_random {
    public static Random random = new Random();
    public static String ara[] = { "Artificial Intelligence", "Embedded Systems", "Networking", "Operation Research",
            "Security" };
    public static final String[] FIRST_NAMES = { "John", "Jane", "Michael", "Emily", "William", "Sophia", "James",
            "Olivia", "Daniel", "Emma" };
    public static final String[] LAST_NAMES = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
            "Wilson", "Moore", "Taylor" };
    public static int mid[] = { 30, 20, 40 };
    public static int regular[] = { 10, 5 };
    public static int ct[] = { 0, 5 };
    public static int final_marks[] = { 60, 70 };
    // public static int project[]={0,5};

    public static void main(String[] args) {
        ArrayList<Student> sStudents = new ArrayList<>();
        ArrayList<Course> sCourses = new ArrayList<>();
        for (String courseName : ara) {
            sCourses.add(new Course(courseName));
        }
        final int N =20;
        // Course c1 = new Course("Human Computer Interaction");
        // sCourses.add(c1); //Your code flexible enough to incorporate new courses

        for (int i = 0; i < N; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String email = firstName.toLowerCase() + "_" + lastName.toLowerCase() + "@gmail.com";
            Student student = new Student(firstName + " " + lastName, i + 1, email);
            int arr[] = { 0, 0, 0, 0, 0 };
            int r;
            for (int j = 0; j < 4; j++) {
                do {
                    r = random.nextInt(sCourses.size());
                } while (arr[r] > 0);// for unique random number
                arr[r]++;
                Course c = sCourses.get(r);
                if (j == 3)
                    student.setOptionalCourse(c);
                else
                    student.addMajorCourse(c);
                Course course = student.getCourse(c.courseName);
                do {
                    course.getEvaluationComponents().clear();//clear() method in Java simply removes all the elements from the list.
                    course.addEvaluationComponent(new EvaluationComponent("MidTerm", mid[random.nextInt(mid.length)]));
                    course.addEvaluationComponent(new EvaluationComponent("Regular", regular[random.nextInt(regular.length)]));
                    course.addEvaluationComponent(new EvaluationComponent("Final", final_marks[random.nextInt(final_marks.length)]));
                    course.addEvaluationComponent(new EvaluationComponent("CT", ct[random.nextInt(ct.length)]));
                    // course.addEvaluationComponent(new EvaluationComponent("Project",project_mark)); 
                    //code flexible enough to incorporate new evaluation metric
                } while (course.isInvaild());
                // for each student for each course, randomly picked an evaluation system maintaining the constraint of having 100 in each course
                int sz=course.getEvaluationComponents().size();
                double marks = 0;
                for (int k = 0; k < sz; k++) {
                    int bound = (int) (course.getEvaluationComponents().get(k).weightage);
                    double m = -1.0;
                        if (bound == 0)
                        {
                            m = 0;
                            continue;
                    }
                    while (m < 0 || m > bound)// randomly assigned marks for each course, for each evaluation metric within the bound
                    {
                        m = random.nextDouble();
                        m *= 100;
                    }
                    marks += m;
                }
                course.setMarks(marks);
            }
            sStudents.add(student);
        }
        int r = random.nextInt(sCourses.size());
        GradeCalculator G = new GradeCalculator(sStudents, sCourses);
        G.getComprehensiveList(sCourses.get(r).courseName);
//Executed a function call to print a comprehensive list containing all the information relevant their assessment criteria.
    
        for (Student s : sStudents) {
            for (Course c : s.totalCourses)
                G.calculateGrade(c);
                //System.out.println("Grade of " + s.name + " in " + c.courseName + " is " + c.getGrade() );}
                // G.calculateTotalGPA(s);
                // System.out.println("CGPA of " + s.name + " is " + s.getCGPA());
        }
//Executed a function call that would calculate the grade for each student for each course
        
        for (Student s : sStudents) {
            G.calculateTotalGPA(s);
        }
//Executed a function call to calculate the total grade for each student
        
        List<Student> overallRank = G.overAllRank();
        System.out.println("______________Overall Rank______________\n\n");
        int i = 1;
        for (Student s : overallRank) {
            System.out.println("*****No. " + i + "*****");
            s.studentInfo();
            System.out.println("CGPA   : " + s.getCGPA() + "\nMarks  : " + s.getTotalMarks() + "\n");
            i++;
        }
//Executed a function call to print the students as per their total grade and then total number in descending order.
        //for(Course c:sCourses)
        Course c = sCourses.get(random.nextInt(sCourses.size()));
        List<Student> courseRank = G.getCourseRankList(c.courseName);
        System.out.println("______________Rank in " + c.courseName + "______________\n\n");
        i = 1;
        for (Student s : courseRank) {
            System.out.println("*****No. " + i + "*****");
            s.studentInfo();
            System.out.println("Grade   : " + s.getCourseGrade(c.courseName) + "\nMarks  : " + s.getTotalMarks() + "\n");
            i++;
        }
//Executed a function call to print a course-based ranking in descending order and total number is tie breaker.
    }
}
