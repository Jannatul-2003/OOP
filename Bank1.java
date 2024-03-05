import java.util.*;

class Bank {
    private String BankName;
    private List<Employee> ListNameEmployees;
    private List<Customer> ListNameCustomers;

    public Bank(String BankName) {
        this.BankName = BankName;
        this.ListNameEmployees = new ArrayList<>();
        this.ListNameCustomers = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        ListNameEmployees.add(employee);
    }

    public void addCustomer(Customer customer) {
        ListNameCustomers.add(customer);
    }

    public String getBankName() {// private is only accessible within the class
        return BankName;
    }
}

// client
class Client {// client is of two types customer and employee
    protected String name;
    protected String email;
    protected String bankName;
    protected String phoneNumber;

    public Client(String name, String email, String bankName, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.bankName = bankName;
        this.phoneNumber = phoneNumber;
    }
}

// Employee
class Employee extends Client {// Employee os of three types manager, officer and trainee
    protected String position;

    public Employee(String name, String email, String bankName, String phoneNumber, String position) {
        super(name, email, bankName, phoneNumber);// super is used to call the constructor of Client class
        this.position = position;
    }
}

class Manager extends Employee {
    public Manager(String name, String email, String bankName, String phoneNumber) {
        super(name, email, bankName, phoneNumber, "Manager");
    }
}

class Officer extends Employee {
    public Officer(String name, String email, String bankName, String phoneNumber) {
        super(name, email, bankName, phoneNumber, "Officer");
    }
}

class Trainee extends Employee {
    public Trainee(String name, String email, String bankName, String phoneNumber) {
        super(name, email, bankName, phoneNumber, "Trainee");
    }
}
// Emplyoee

// Customer
class Customer extends Client {// customer is of two types SinglePerson and Organization
    private String accountNumber;

    public Customer(String name, String email, String bankName, String phoneNumber, String accountNumber) {
        super(name, email, bankName, phoneNumber);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {// private is only accessible within the class
        return accountNumber;
    }
}

class SinglePerson extends Customer {

    private String BIN;

    public SinglePerson(String name, String email, String bankName, String phoneNumber, String accountNumber) {
        super(name, email, bankName, phoneNumber, accountNumber);
    }

    public String getBin() {
        return BIN;
    }

    public void setBin(String Bin) {
        this.BIN = Bin;
    }
}

class Organization extends Customer {
    private String TIN;

    public Organization(String name, String email, String bankName, String phoneNumber, String accountNumber) {
        super(name, email, bankName, phoneNumber, accountNumber);
    }

    public String getTin() {
        return TIN;
    }

    public void setTin(String Tin) {
        this.TIN = Tin;
    }
}
// Customer
// Client

// Account
class Account {// account is of two types savings and salary
    private String accountNumber;
    protected String accountType;
    private double balance;
    private double investmentPeriod;
    protected double taxRate = 0.2;

    public Account(String accountNumber, String accountType, double balance, double investmentPeriod) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.investmentPeriod = investmentPeriod;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getInvestmentPeriod() {
        return investmentPeriod;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: " + balance);
        } else {
            System.out.println("Insufficient funds for withdrawal.");
        }
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: " + balance);
    }
}

class Savings extends Account {
    private double interestRate = 0.025;
    private double taxCut = 0.05;

    public Savings(String accountNumber, double balance, double investmentPeriod) {
        super(accountNumber, "Savings", balance, investmentPeriod);
        balance = balance + balance * this.interestRate * investmentPeriod
                - balance * (super.taxRate - this.taxCut) * investmentPeriod;
        super.setBalance(balance);
    }

}

class Salary extends Account {
    private double interestRate = 0.02;
    private double taxCut = 0.1;

    public Salary(String accountNumber, double balance, double investmentPeriod) {
        super(accountNumber, "Salary", balance, investmentPeriod);
        balance = balance + balance * this.interestRate * investmentPeriod
                - balance * (super.taxRate - this.taxCut) * investmentPeriod;
        super.setBalance(balance);
    }
}

class SendMoneyToAccount {
    private Account destinationAccount;

    public SendMoneyToAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public void SendMoney(double amount) {
        destinationAccount.deposit(amount);
    }

    public void SendMoneyOptions() {
        System.out.println("Send Money To Account by:");
        System.out.println("1.BkashWallet");
        System.out.println("2.EFT");
        System.out.println("3.BankReceipt");
        System.out.println("4.Exit");
        System.out.println("Enter your choice:");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice != 4) {
            System.out.println("Enter the amount to send:");
            double amount = input.nextDouble();
            input.close();
            if (choice != 4)
                SendMoney(amount);
        } else {
            System.out.println("Thank you for using the bank system.");
        }
    }

}

class Withdraw {
    private Account sourceAccount;

    public Withdraw(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public void WithdrawMoney(double amount) {
        sourceAccount.withdraw(amount);
    }

    public void withdrawOptions() {
        System.out.println("Withdraw by:");
        System.out.println("1.DirectCheck");
        System.out.println("2.BkashWallet");
        System.out.println("3.CreditCard");
        System.out.println("4.Exit");
        System.out.println("Enter your choice:");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice != 4) {
            System.out.println("Enter the amount to withdraw:");
            double amount = input.nextDouble();
            input.close();
            WithdrawMoney(amount);
        } else {
            System.out.println("Thank you for using the bank system.");
        }
    }
}

public class Bank1 {
    public static void main(String[] args) {
        System.out.println("Welcome to the bank system");
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the bank name: ");
        String bankName = sc.next();
        Bank bank = new Bank(bankName);

        System.out.println("Enter the name: ");
        String name = sc.next();
        System.out.println("Enter the email: ");
        String email = sc.next();
        System.out.println("Enter the phone number: ");
        String phone = sc.next();
        Client client = new Client(name, email, bankName, phone);

        System.out.println("Client: ");
        System.out.println("1.Employee. ");
        System.out.println("2.Customer. ");
        System.out.println("3.Exit. ");
        System.out.println("Enter your choice: ");
        int choice = sc.nextInt();

        if (choice != 3) {
            switch (choice) {
                case 1:
                    System.out.println("Enter your position: ");
                    String position = sc.next();
                    Employee employee = new Employee(name, email, bankName, phone, position);
                    break;
                case 2://customer
                    System.out.println("Enter the account number: ");
                    String accountNumber = sc.next();
                    Customer customer = new Customer(name, email, bankName, phone,
                            accountNumber);
                    System.out.println("Customer: ");
                    System.out.println("1.SinglePerson. ");
                    System.out.println("2.Organization. ");
                    System.out.println("3.Exit. ");
                    System.out.println("Enter your choice: ");
                    choice = sc.nextInt();
                    if (choice != 3) {
                        switch (choice) {
                            case 1:
                                // SinglePerson class used to create a singlePerson object
                                SinglePerson singlePerson = new SinglePerson(name, email, bankName, phone,
                                        accountNumber);
                                System.out.println("Enter the BIN: ");
                                String bin = sc.next();
                                singlePerson.setBin(bin);
                                break;
                            case 2:
                                // Organization class used to create a organization object
                                Organization organization = new Organization(name, email, bankName, phone,
                                        accountNumber);
                                System.out.println("Enter the TIN: ");
                                String tin = sc.next();
                                organization.setTin(tin);
                                break;
                        }
                        Account account=null;

                        System.out.println("Account type: ");
                        System.out.println("1.Savings. ");
                        System.out.println("2.Salary. ");
                        System.out.println("3.Exit. ");
                        System.out.println("Enter your choice: ");
                        choice = sc.nextInt();
                        if (choice != 3) {
                            System.out.println("Enter the balance: ");
                            double balance = sc.nextDouble();
                            System.out.println("Enter the investment period: ");
                            double investmentPeriod = sc.nextDouble();
                            switch (choice) {
                                case 1:
                                    Savings savings = new Savings(accountNumber, balance, investmentPeriod);
                                    account = savings;
            
                                    
                                    break;
                                case 2:
                                    Salary salary = new Salary(accountNumber, balance, investmentPeriod);
                                    account = salary;
                                    break;
                            }
                            
                            System.out.println("Operation to perform: ");
                            System.out.println("1.Withdraw. ");
                            System.out.println("2.Deposit. ");
                            System.out.println("3.Exit ");
                            System.out.println("Enter your choice: ");
                            choice = sc.nextInt();
                            if (choice != 3) {
                                switch (choice) {
                                    case 1:
                                        Withdraw withdraw = new Withdraw(account);
                                        withdraw.withdrawOptions();
                                        break;
                                    case 2:
                                        SendMoneyToAccount deposit = new SendMoneyToAccount(account);
                                        deposit.SendMoneyOptions();
                                        break;
                                }
                            }

                            
                        }
                        System.out.println("Account number: " + account.getAccountNumber());
                        System.out.println("Balance: " + account.getBalance());
                    }
                    break;
                }
        }
        System.out.println("Thank you for using the bank system.");
        sc.close();
    }
}

// Customer part complete with subclasses
