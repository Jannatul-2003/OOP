import java.util.*;

class Bank {
    private String BankName;
    private List<Employee> ListNameEmployees;
    private List<Customer> ListNameCustomers;

    public Bank() {
    }

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

    public Customer checkUser(String email, String password) {
        for (Customer customer : ListNameCustomers) {
            if (customer.email.equals(email) && customer.password.equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public void Banking() {
        System.out.println("Welcome to the bank system");
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the bank name: ");
        String bankName = sc.next();
        Bank bank = new Bank(bankName);
        String name = new String();
        String email = new String();
        String phone = new String();
        System.out.println("Client:\n1.Employee\n2.Customer\n3.Exit\nEnter your choice: ");
        int choice;
        if (sc.hasNextInt()) {
            choice = sc.nextInt();
            if (choice != 3) {
                switch (choice) {
                    case 1:
                        System.out.println("Enter the employee name: ");
                        name = sc.next();
                        System.out.println("Enter the email: ");
                        email = sc.next();
                        System.out.println("Enter the phone number: ");
                        phone = sc.next();
                        System.out.println("Enter your position: ");
                        String position = sc.next();
                        Bank.Employee employee = bank.new Employee(name, email, bankName, phone, position);
                        bank.addEmployee(employee);
                        break;
                    case 2:
                        Customer customer = null;
                        while (true) {
                            System.out.println("1.Create Account.\n2.Login.\nEnter your choice:");
                            if (sc.hasNextInt()) {
                                int c = sc.nextInt();
                                if (c == 1) {
                                    bank.createAccount();
                                    System.out.print("\033[H\033[2J");
                                    System.out.flush();
                                } else {
                                    customer = bank.login();
                                    if (customer != null)
                                        break;
                                }
                            } else {
                                System.out.println("Invalid input");
                                sc.next();
                            }
                        }
                        System.out.println("Customer: \n1.SinglePerson\n2.Organization\n3.Exit\nEnter your choice: ");
                        if (sc.hasNextInt()) {
                            choice = sc.nextInt();
                            if (choice != 3) {
                                switch (choice) {
                                    case 1:
                                        // SinglePerson class used to create a singlePerson object
                                        Bank.SinglePerson singlePerson = bank.new SinglePerson(name, email, bankName,
                                                phone, customer.getPassword());
                                        System.out.println("Enter the BIN: ");
                                        String bin = sc.next();
                                        singlePerson.setBin(bin);
                                        break;
                                    case 2:
                                        // Organization class used to create a organization object
                                        Bank.Organization organization = bank.new Organization(name, email, bankName,
                                                phone, customer.getPassword());
                                        System.out.println("Enter the TIN: ");
                                        String tin = sc.next();
                                        organization.setTin(tin);
                                        break;
                                }
                                Bank bank1 = bank;
                                Bank.Account account = null;
                                Bank.Account account1 = null;
                                int flag = 1;
                                while (true) {
                                    System.out
                                            .println("Account type:\n1.Savings\n2.Salary\n3.Exit\nEnter your choice: ");
                                    choice = sc.nextInt();
                                    if (choice != 3) {
                                        System.out.println("Enter the balance: ");
                                        double balance = sc.nextDouble();
                                        System.out.println("Enter the investment period: ");
                                        double investmentPeriod = sc.nextDouble();
                                        switch (choice) {
                                            case 1:
                                                Bank.Savings savings = bank1.new Savings(customer, balance,
                                                        investmentPeriod);
                                                if (flag == 1)
                                                    account = savings;
                                                flag = 0;
                                                account1 = savings;
                                                break;
                                            case 2:
                                                Bank.Salary salary = bank1.new Salary(customer, balance,
                                                        investmentPeriod);
                                                if (flag == 1)
                                                    account = salary;
                                                flag = 0;
                                                account1 = salary;
                                                break;
                                        }
                                        account1.BankName = bank1.getBankName();
                                        customer.addAccount(account1);
                                        System.out.println("Any other bank account:\n1.Yes\n2.No\nEnter your choice: ");
                                        int ch = sc.nextInt();
                                        if (ch == 1) {
                                            System.out.println("Enter Bankname:");
                                            String BankName1 = sc.next();
                                            bank1 = new Bank(BankName1);
                                        } else
                                            break;
                                    }
                                }
                                System.out.println(
                                        "Operation to perform:\n1.Withdraw\n2.Deposit\n3.Exit\nEnter your choice: ");
                                if (sc.hasNextInt()) {
                                    choice = sc.nextInt();
                                    if (choice != 3) {
                                        switch (choice) {
                                            case 1:
                                                Bank.Withdraw withdraw = bank.new Withdraw(account);
                                                withdraw.withdrawOptions();
                                                break;
                                            case 2:
                                                Bank.SendMoneyToAccount deposit = bank.new SendMoneyToAccount(account);
                                                deposit.SendMoneyOptions();
                                                break;
                                        }
                                    }
                                    bank.customerInfo(customer);

                                }
                            }
                        } else
                            System.out.println("Invalid input");

                }
            }
            System.out.println("Thank you for using the bank system.");
            sc.close();

        } else
            System.out.println("Invalid input");
    }

    public void createAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the customer name: ");
        String name = sc.next();
        System.out.println("Enter the email: ");
        String email = sc.next();
        System.out.println("Enter the phone number: ");
        String phoneNumber = sc.next();
        System.out.println("Enter the password: ");
        String password = sc.next();
        Customer customer = new Customer(name, email, BankName, phoneNumber, password);
        addCustomer(customer);
    }

    public Customer login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the email: ");
        String email = sc.next();
        System.out.println("Enter the password: ");
        String password = sc.next();

        if (checkUser(email, password) != null) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid email or password.");
        }
        return checkUser(email, password);
    }

    public void customerInfo(Customer customer) {
        System.out.println("Customer Name: " + customer.name);
        System.out.println("Customer Email: " + customer.email);
        System.out.println("Customer Phone Number: " + customer.phoneNumber);
        System.out.println("Customer Account List: ");
        for (Account account : customer.getListNameAccounts()) {
            System.out.println("Customer Bank Name: " + account.BankName);
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.accountType);
            System.out.println("Account Balance: " + account.getBalance());
            System.out.println("Account Investment Period: " + account.getInvestmentPeriod());
        }
    }

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
            super(name, email, bankName, phoneNumber);// super is used to call the constructor of Client
            // class
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
        // private String accountNumber;
        private String password;
        List<Account> ListNameAccounts;// A customer can have multiple accounts

        public Customer(String name, String email, String bankName, String phoneNumber, String password) {
            super(name, email, bankName, phoneNumber);
            this.ListNameAccounts = new ArrayList<>();
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public List<Account> getListNameAccounts() {// customer can have multiple accounts
            return ListNameAccounts;
        }

        public void addAccount(Account account) {
            ListNameAccounts.add(account);
        }
    }

    class SinglePerson extends Customer {

        private String BIN;

        public SinglePerson(String name, String email, String bankName, String phoneNumber,
                String password) {
            super(name, email, bankName, phoneNumber, password);
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

        public Organization(String name, String email, String bankName, String phoneNumber,
                String password) {
            super(name, email, bankName, phoneNumber, password);
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
        // static class commonAttribute{
        protected String BankName;
        private String accountNumber;
        protected String accountType;
        private double balance;
        private double investmentPeriod;
        protected double taxRate = 0.2;

        public Account(Customer customer, String accountType, double balance, double investmentPeriod) {

            this.BankName = customer.bankName;
            // this.accountNumber = customer.accountNumber;
            final long LIMIT = 10000000L;
            long id = System.currentTimeMillis() % LIMIT;
            this.accountNumber = "1" + String.valueOf(id);
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
                System.out.println("Insufficient funds for withdrawal. Your Balance is: " + balance + " Taka.");
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

        public Savings(Customer customer, double balance, double investmentPeriod) {
            super(customer, "Savings", balance, investmentPeriod);
            balance = balance + balance * this.interestRate * investmentPeriod
                    - balance * (super.taxRate - this.taxCut) * investmentPeriod;
            super.setBalance(balance);
        }

    }

    class Salary extends Account {
        private double interestRate = 0.02;
        private double taxCut = 0.1;

        public Salary(Customer customer, double balance, double investmentPeriod) {
            super(customer, "Salary", balance, investmentPeriod);
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
            System.out.println(
                    "Send Money To Account by:\n1.BkashWallet\n2.EFT\n3.BankReceipt\n4.Exit\nEnter your choice: ");
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
            System.out.println("Withdraw by:\n1.DirectCheck\n2.BkashWallet\n3.CreditCard\n4.Exit\nEnter your choice:");
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

}

public class BankSystem {
    public static void main(String[] args) {

        Bank bank = new Bank();
        bank.Banking();
    }
}