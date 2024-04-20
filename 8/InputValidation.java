import java.util.*;
import java.util.regex.*;

class FirstNameAbsence extends Exception {
    private String detail;

    public FirstNameAbsence() {
        detail = "First name is missing";
    }

    public String toString() {
        return "FirstNameAbsence: " + detail;
    }
}

class LastNameAbsence extends Exception {
    private String detail;

    public LastNameAbsence() {
        detail = "Last name is missing";
    }

    public String toString() {
        return "LastNameAbsence: " + detail;
    }
}

class NoNamePresent extends Exception {
    private String detail;

    public NoNamePresent() {
        detail = "Both first and last names are missing";
    }

    public String toString() {
        return "NoNamePresent: " + detail;
    }
}

class BlankEmailField extends Exception {
    private String detail;

    public BlankEmailField() {
        detail = "Email field is blank";
    }

    public String toString() {
        return "BlankEmailField: " + detail;
    }
}

class AbsenceofGmailSuffix extends Exception {
    private String detail;

    public AbsenceofGmailSuffix() {
        detail = "Email must end with @gmail.com";
    }

    public String toString() {
        return "AbsenceofGmailSuffix: " + detail;
    }
}

class NotProperlyFormatedEmailPrefix extends Exception {
    private String detail;

    public NotProperlyFormatedEmailPrefix() {
        detail = "Email must have a proper prefix of lowercase letters and digits";
    }

    public String toString() {
        return "NotProperlyFormatedEmailPrefix: " + detail;
    }
}

class BlankNIDPassportField extends Exception {
    private String detail;

    public BlankNIDPassportField() {
        detail = "Both NID and passport fields are blank";
    }

    public String toString() {
        return "BlankNIDPassportField: " + detail;
    }
}

class UnexpectedNIDFormat extends Exception {
    private String detail;

    public UnexpectedNIDFormat() {
        detail = "NID must be an 11-character string of digits";
    }

    public String toString() {
        return "UnexpectedNIDFormat: " + detail;
    }
}

class UnexpectedPassportFormat extends Exception {
    private String detail;

    public UnexpectedPassportFormat() {
        detail = "Passport must have 2 uppercase letters followed by 7 digits";
    }

    public String toString() {
        return "UnexpectedPassportFormat: " + detail;
    }
}

class WrongBirthdateFormat extends Exception {
    private String detail;

    public WrongBirthdateFormat() {
        detail = "Birthdate must be in the format 'Date Month Year' (e.g., 12 January 2023)";
    }

    public String toString() {
        return "WrongBirthdateFormat: " + detail;
    }
}

class BirthdateBlank extends Exception {
    private String detail;

    public BirthdateBlank() {
        detail = "Birthdate field is blank";
    }

    public String toString() {
        return "BirthdateBlank: " + detail;
    }
}

class BlankAddressField extends Exception {
    private String detail;

    public BlankAddressField() {
        detail = "Address field is blank";
    }

    public String toString() {
        return "BlankAddressField: " + detail;
    }
}

class InvalidAddressPart extends Exception {
    private String detail;

    public InvalidAddressPart() {
        detail = "Address must contain 3 parts: P1, P2, P3";
    }

    public String toString() {
        return "InvalidAddressPart: " + detail;
    }
}

class InvalidAddressPart1 extends Exception {
    private String detail;

    public InvalidAddressPart1() {
        detail = "Part1 would consist of digits, punctuation (/), and Upper case English letters. (e.g., 124/A)";
    }

    public String toString() {
        return "InvalidAddressPart1: " + detail;
    }
}

class InvalidAddressPart2 extends Exception {
    private String detail;

    public InvalidAddressPart2() {
        detail = "Part2 would consist of Upper case and Lower case English letters and space. (e.g., Shaheed Faruq)";
    }

    public String toString() {
        return "InvalidAddressPart2: " + detail;
    }
}

class InvalidAddressPart3 extends Exception {
    private String detail;

    public InvalidAddressPart3() {
        detail = "Part3 would be a substring, denoting major divisions of Bangladesh. (e.g., Dhaka)";
    }

    public String toString() {
        return "InvalidAddressPart3: " + detail;
    }
}

class Person {
    String firstName;
    String middleName;
    String lastName;
    String email;
    String nid;
    String passport;
    String birthdate;
    String address;

    public Person() {
    }

    public Person(String firstName, String middleName, String lastName, String email, String nid, String passport,
            String birthdate, String address) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.nid = nid;
        this.passport = passport;
        this.birthdate = birthdate;
        this.address = address;
    }
}

public class InputValidation {
    static List<Person> personList = new ArrayList<>();
    public static Scanner sc = new Scanner(System.in);

    public static String[] nameException(String firstName, String lastName) {
        String[] name = new String[2];
        name[0] = firstName;
        name[1] = lastName;

        try {
            if (firstName == null || firstName.isEmpty()) {
                throw new FirstNameAbsence();
            }
        } catch (FirstNameAbsence e) {
            name[0] = e.toString();
            // System.out.println(e);
        }

        try {
            if (lastName == null || lastName.isEmpty()) {
                throw new LastNameAbsence();
            }
        } catch (LastNameAbsence e) {
            name[1] = e.toString();
            // System.out.println(e);
        }

        try {
            if ((firstName == null || firstName.isEmpty()) && (lastName == null || lastName.isEmpty())) {
                NoNamePresent e = new NoNamePresent();
                e.initCause(new LastNameAbsence());
                throw e;
            }
        } catch (NoNamePresent e) {
            name[1] = e.toString() + "\nCause:" + e.getCause();
            // System.out.println(e);
        }
        return name;
    }

    public static String emailException(String email) {
        if (email != null && !email.isEmpty()) {
            String ans = "";
            String emailPattern = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]+@gmail\\.com$";
            try {
                if (!Pattern.matches(emailPattern, email)) {
                    if (!email.endsWith("@gmail.com")) {
                        throw new AbsenceofGmailSuffix();
                    }
                    throw new NotProperlyFormatedEmailPrefix();
                }
            } catch (AbsenceofGmailSuffix e) {
                ans = e.toString();
                // System.out.println(e);
            } catch (NotProperlyFormatedEmailPrefix e) {
                ans = e.toString();
                // System.out.println(e);
            }
            try {
                if (!email.endsWith("@gmail.com") && !email.contains("^(?=.*[a-z])(?=.*\\d)[a-z\\d]"))
                    throw new NotProperlyFormatedEmailPrefix();
            } catch (NotProperlyFormatedEmailPrefix e) {
                ans += " " + e.toString();
            }
            return ans;
        }
        try {
            if (email == null || email.isEmpty()) 
                throw new BlankEmailField();
        } catch (BlankEmailField e) {
            return e.toString();
            // System.out.println(e);
        }
        return email;
    }

    public static String nidException(String nid) {
        try {
            if (!nid.matches("\\d{11}"))
                throw new UnexpectedNIDFormat();
        } catch (UnexpectedNIDFormat e) {
            return e.toString();
            // System.out.println(e);
        }
        return nid;
    }

    public static String passportException(String passport) {
        try {
            if (!passport.matches("[A-Z]{2}\\d{7}")) 
                throw new UnexpectedPassportFormat();
        } catch (UnexpectedPassportFormat e) {
            return e.toString();
            // System.out.println(e);
        }
        return passport;
    }

    public static String BlankNIDPassportException(String nid, String passport) {
        try {
            if ((nid == null || nid.isEmpty()) && (passport == null || passport.isEmpty())) 
                throw new BlankNIDPassportField();
        } catch (BlankNIDPassportField e) {
            return e.toString();
            // System.out.println(e);
        }
        return null;
    }

    public static String BirthdateException(String birthdate) {
        try {
            if (birthdate == null || birthdate.isEmpty())
                throw new BirthdateBlank();
        } catch (BirthdateBlank e) {
            return e.toString();
            // System.out.println(e);
        }
        String birthdatePattern = "(?:[1-9]|[12][0-9]|3[01]) [A-Za-z]+ \\d{4}";
        try {
            if (!Pattern.matches(birthdatePattern, birthdate))
                throw new WrongBirthdateFormat();
        } catch (WrongBirthdateFormat e) {
            return e.toString();
            // System.out.println(e);
        }
        return birthdate;
    }

    public static String[] splitAddress(String address) {
        String[] parts = new String[3];
        int firstSpaceIndex = address.indexOf(' ');
        int lastSpaceIndex = address.lastIndexOf(' ');

        parts[0] = address.substring(0, firstSpaceIndex);
        parts[1] = address.substring(firstSpaceIndex + 1, lastSpaceIndex);
        parts[2] = address.substring(lastSpaceIndex + 1);
        return parts;
    }

    public static String AddressException(String address) {
        try {
            if (address == null || address.isEmpty()) {
                throw new BlankAddressField();
            }
        } catch (BlankAddressField e) {
            return e.toString();
            // System.out.println(e);
        }
        String[] addressParts = splitAddress(address);
        try {
            if (addressParts[0] == null || addressParts[1] == null || addressParts[2] == null) {
                throw new InvalidAddressPart();
            }
        } catch (InvalidAddressPart e) {
            return e.toString();
            // System.out.println(e);
        }
        String ans = "";
        String p1Pattern = "[A-Z0-9/]+";
        try {
            if (!Pattern.matches(p1Pattern, addressParts[0]))
                throw new InvalidAddressPart1();
        } catch (InvalidAddressPart1 e) {
            ans += e.toString();
            // System.out.println(e);
        }
        String p2Pattern = "[A-Za-z ]+";
        try {
            if (!Pattern.matches(p2Pattern, addressParts[1]))
                throw new InvalidAddressPart2();
        } catch (InvalidAddressPart2 e) {
            ans += " " + e.toString();
            // System.out.println(e);
        }
        List<String> validDivisions = Arrays.asList("Dhaka", "Chottogram", "Barishal", "Khulna", "Sylhet", "Rajshahi",
                "Mymensingh", "Rangpur");
        try {
            if (!validDivisions.contains(addressParts[2]))
                throw new InvalidAddressPart3();
        } catch (InvalidAddressPart3 e) {
            ans += " " + e.toString();
            // System.out.println(e);
        }
        if (ans != "")
            return ans;
        return address;
    }

    public static void addPerson(String firstName, String middleName, String lastName, String email, String nid,
            String passport, String birthdate, String address) {
        try {
            // Name-related exceptions
            String[] name = new String[2];
            name = nameException(firstName, lastName);
            firstName = name[0];
            lastName = name[1];
            // Email-related exceptions
            email = emailException(email);
            // NID or Passport-related exceptions
            if (nid != null && !nid.isEmpty())
                nid = nidException(nid);

            else if (passport != null && !passport.isEmpty())
                passport = passportException(passport);

            else if ((nid == null || nid.isEmpty()) && (passport == null || passport.isEmpty())) {
                nid = passport = BlankNIDPassportException(nid, passport);
            }

            // Birthdate-related exceptions
            birthdate = BirthdateException(birthdate);

            // Address-related exceptions
            address = AddressException(address);

            Person person = new Person(firstName, middleName, lastName, email, nid, passport, birthdate, address);
            personList.add(person);
            System.out.println("Person added successfully!");

        } catch (Exception e) {
            System.out.println("Error while adding person: " + e);
        } finally {
            System.out.println("Attempt to add person completed.");
        }
    }

    public static void queryPerson(int index) {
        try {
            if (index < 0 || index >= personList.size()) {
                throw new IllegalArgumentException("Invalid index: " + index);
            }

            Person person = personList.get(index);
            System.out.println("Person at index " + index + ":");
            System.out.println("Name: " + person.firstName + " " + person.middleName + " " + person.lastName);
            System.out.println("Email: " + person.email);
            System.out.println("NID: " + person.nid);
            System.out.println("Passport: " + person.passport);
            System.out.println("Birthdate: " + person.birthdate);
            System.out.println("Address: " + person.address);

        } catch (Exception e) {
            System.out.println("Error while querying person: " + e);
        } finally {
            System.out.println("Query attempt completed.");
        }
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception{
        boolean isRunning = true;
        int query;
        try{
        while (isRunning) {
            System.out.println("1. Add a new person");
            System.out.println("2. Get Persons Info");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            query = sc.nextInt();
            sc.nextLine();
            switch (query) {                
                case 1:
                    /**/System.out.println("First Name: ");
                    String fName = sc.nextLine();
                    System.out.println("Middle Name: ");
                    String mName = sc.nextLine();
                    System.out.println("Last Name: ");
                    String lName = sc.nextLine();
                    System.out.println("Email: ");
                    String email = sc.nextLine();
                    System.out.println("NID: ");
                    String nid = sc.nextLine();
                    System.out.println("Passport");
                    String passport = sc.nextLine();
                    System.out.println("Birthdate");
                    String birthDate = sc.nextLine();
                    System.out.println("Address");
                    String address = sc.nextLine();
                    clear();
                    addPerson(fName, mName, lName, email, nid, passport, birthDate, address);/**/
                    /*addPerson("John", "Doe", "", "johndoe@gmail.com", "12345678901", "", "12 January 2023",
                    "124/A Shaheed Faruq Dhaka");
            addPerson("Jane", "", "Doe", "janedoe@invalid.com", "", "BD1234567", "15 March 1985",
                    "45BC Nilkhet Sylhet");
            addPerson("", "", "", "", "", "", "", "");*/
                    break;
                case 2:
                    System.out.println("See the info of person whose serial no is: ");
                    int c = sc.nextInt();
                    c = c - 1;
                    clear();
                    queryPerson(c);
                    break;
                case 3:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
        catch(Exception e){
        System.out.println("Error: " + e);   }   
    }
}
