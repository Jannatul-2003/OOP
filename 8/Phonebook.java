import java.util.*;

class Contact {
    String name;
    String number;

    Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }
}

public class Phonebook {
    static Scanner sc = new Scanner(System.in);

    static void print(String s) {
        System.out.println(s);
    }

    static boolean lexicographically_smaller(String s1, String s2) {
        int n = (s1.length() < s2.length()) ? s1.length() : s2.length();
        for (int i = 0; i < n; i++) {
            if (s1.charAt(i) < s2.charAt(i))
                return true;

            else if (s1.charAt(i) > s2.charAt(i))
                return false;
        }
        if (s1.length() < s2.length())
            return true;
        return false;
    }

    static String Contact_num(String number) {
        String phone = new String();
        String phone_num = new String();
        int j = 0;
        for (j = 0; j < number.length(); j++) {
            if ((number.charAt(j) >= '0' && number.charAt(j) <= '9') || (number.charAt(j) == '+'))
                break;
        }
        if (number.charAt(j) != '+')
            phone += "+880";

        for (; j < number.length(); j++) {
            if ((number.charAt(j) >= '0' && number.charAt(j) <= '9') || (number.charAt(j) == '+'))
                phone += number.charAt(j);
        }

        for (int i = 0; i < phone.length(); i++) {
            if (i == 4 || i == 12)
                phone_num += " ";
            if (i == 9)
                phone_num += "-";
            phone_num += phone.charAt(i);
        }
        return phone_num;
    }

    static String Contact_name(String name) {
        String name1 = new String();
        String result = new String();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ')
                continue;
            if (i == 0 || name.charAt(i - 1) == ' ')
                name1 += (name.charAt(i) >= 'a' && name.charAt(i) <= 'z') ? (char) ((int) name.charAt(i) - 32)
                        : name.charAt(i);
            else
                name1 += (name.charAt(i) >= 'a' && name.charAt(i) <= 'z') ? name.charAt(i)
                        : (char) ((int) name.charAt(i) + 32);
        }
        for (int i = 0; i < name1.length(); i++) {
            result += name1.charAt(i);
            if (i != name1.length() - 1 && name1.charAt(i + 1) >= 'A' && name1.charAt(i + 1) <= 'Z')
                result += " ";
        }
        return result;
    }

    static boolean containsSubstring(String mainString, String subString) {
        int n = mainString.length();
        int m = subString.length();
        String s1 = new String();
        String s2 = new String();
        for (int i = 0; i < n; i++)
            s1 += (mainString.charAt(i) >= 'a' && mainString.charAt(i) <= 'z')
                    ? (char) ((int) mainString.charAt(i) - 32)
                    : mainString.charAt(i);
        for (int i = 0; i < m; i++)
            s2 += (subString.charAt(i) >= 'a' && subString.charAt(i) <= 'z') ? (char) ((int) subString.charAt(i) - 32)
                    : subString.charAt(i);
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && s1.charAt(i + j) == s2.charAt(j)) {
                j++;
            }
            if (j == m) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int K;
        K = sc.nextInt();
        sc.nextLine();
        Contact arr[] = new Contact[K];
        for (int i = 0; i < K; i++) {
            String name = new String();
            name = sc.nextLine();
            String number = new String();
            number = sc.nextLine();
            name = Contact_name(name);
            // print(name);
            number = Contact_num(number);
            // print(number);
            arr[i] = new Contact(name, number);
        }
        String sub = new String();
        sub = sc.nextLine();
        ArrayList<Contact> res = new ArrayList<Contact>();
        for (int i = 0; i < K; i++) {
            if (containsSubstring(arr[i].name, sub)) {
                String name = new String();
                name = arr[i].name;
                String number = new String();
                number = arr[i].number;
                // print(name+" "+number);
                res.add(new Contact(name, number));
                // print(res[j].name+" "+res[j].number+" "+j);
            }
        }
        for (int i = 0; i < res.size(); i++) {
            for (int j = i + 1; j < res.size(); j++) {
                if (lexicographically_smaller(res.get(j).name, res.get(i).name)) {
                    Contact temp = new Contact(res.get(i).name, res.get(i).number);
                    res.set(i, res.get(j));
                    res.set(j, temp);
                }
            }
        }
        for (int i = 0; i < res.size(); i++) {
            print(res.get(i).name + " " + res.get(i).number);
        }

    }
}
