import java.util.*;

//30 500 500
public class BankingThread {
    static ArrayList<Account> accounts = new ArrayList<>();
    static ArrayList<DepositTransaction> depositQueue = new ArrayList<>();
    static ArrayList<WithdrawTransaction> withdrawQueue = new ArrayList<>();
    static Object lock = new Object();
    static Object lockDeposit = new Object();
    static Object lockWithdraw = new Object();
    static int limit = 500;
    static int acno = 30;
    static int processedDeposits = 0;
    static int processedWithdraws = 0;

    class Account {
        String accountHolderName;
        String accountNumber;
        int maximumTransactionLimit;
        double balance;

        Account(String accountHolderName, String accountNumber, int maximumTransactionLimit) {
            this.accountHolderName = accountHolderName;
            this.accountNumber = accountNumber;
            this.maximumTransactionLimit = maximumTransactionLimit;
            this.balance = 0;
        }
    }

    class DepositTransaction {
        String accountNumber;
        double depositAmount;

        DepositTransaction(String accountNumber, double depositAmount) {
            this.accountNumber = accountNumber;
            this.depositAmount = depositAmount;
        }
    }

    class WithdrawTransaction {
        String accountNumber;
        double withdrawAmount;

        WithdrawTransaction(String accountNumber, double withdrawAmount) {
            this.accountNumber = accountNumber;
            this.withdrawAmount = withdrawAmount;
        }
    }

    class TransactionLimit extends Exception {
        private String detail;

        public TransactionLimit(boolean flag) {
            if (flag)
                this.detail = "Maximum DepositTransaction Limit Violated.";
            else
                this.detail = "Maximum WithdrawTransaction Limit Violated.";
        }

        public String toString() {
            return this.detail;
        }
    }

    private boolean ex(double a, double b, boolean flag) {
        try {
            if (a > b)
                throw new TransactionLimit(flag);
        } catch (TransactionLimit e) {
            System.out.println(e + " Failed Transaction:" + a + " Transaction Limit:" + b);
            return true;
        }
        return false;
    }

    private Account findAccountByNumber(String accountNumber) {
        synchronized (accounts) {
            for (Account account : accounts) {
                if (account.accountNumber.equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    private void generateDeposit() {
        Random random = new Random();
        if (accounts.size() == 0)
            return;
        int accountIndex = random.nextInt(accounts.size());
        Account account = accounts.get(accountIndex);
        double amount = 1 + random.nextInt(50000);
        DepositTransaction transaction = new DepositTransaction(account.accountNumber, amount);
        depositQueue.add(transaction);
        System.out.println("Generated deposit transaction of " + amount + " into account " + account.accountNumber);
    }

    private void generateWithdraw() {
        Random random = new Random();
        if (accounts.size() == 0)
            return;
        int accountIndex = random.nextInt(accounts.size());
        Account account = accounts.get(accountIndex);
        double amount = 1 + random.nextInt(100000);
        WithdrawTransaction transaction = new WithdrawTransaction(account.accountNumber, amount);
        withdrawQueue.add(transaction);
        System.out.println("Generated withdrawal " + amount + " from " + account.accountNumber);
    }

    class AccountGenerationThread implements Runnable {
        Thread t;

        String[] firstNames = {
                "John", "Jane", "Michael", "Emily", "David", "Sarah", "Robert", "Laura",
                "James", "Amy", "William", "Megan", "Joseph", "Emma", "Charles", "Chloe",
                "Christopher", "Olivia", "Daniel", "Sophia", "Matthew", "Isabella", "Joshua", "Ava",
                "Andrew", "Mia", "Ethan", "Charlotte", "Alexander", "Amelia"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
                "Taylor", "Moore", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
                "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee",
                "Walker", "Hall", "Allen", "Young", "Hernandez", "King"
        };

        Random random = new Random();

        AccountGenerationThread() {
            t = new Thread(this, "AccountGenerationThread");
        }

        public void run() {
            for (int i = 0; i < acno; i++) {

                try {
                    int firstNameIndex = random.nextInt(firstNames.length);
                    int lastNameIndex = random.nextInt(lastNames.length);
                    String accountHolderName = firstNames[firstNameIndex] + " " + lastNames[lastNameIndex];

                    String accountNumber = generateAccountNumber();
                    int maxTransactionLimit = /* 10000000; */random.nextInt(1000) + 1;// (random num between 0 and
                                                                                      // 999)+1

                    Account newAccount = new Account(accountHolderName, accountNumber, maxTransactionLimit);

                    synchronized (accounts) {
                        accounts.add(newAccount);
                    }
                    System.out.println("Created new account: " + accountHolderName + " " + accountNumber);

                    Thread.sleep(1000);
                    if (accounts.size() == 1) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception in AccountGenerationThread");
                }
            }
        }

        private String generateAccountNumber() {
            char prefix1 = (char) ('a' + random.nextInt(26));
            char prefix2 = (char) ('a' + random.nextInt(26));
            StringBuilder sb = new StringBuilder();
            sb.append(prefix1).append(prefix2);
            for (int i = 0; i < 10; i++) {
                sb.append(random.nextInt(10));// 2 samll letters and 10 digits
            }
            return sb.toString();
        }
    }

    class DepositGenerationThread extends Thread {
        Random random = new Random();

        DepositGenerationThread() {
            super("DepositGenerationThread");
        }

        public void run() {
            for (int i = 0; i < limit; i++) {
                try {
                    synchronized (lock) {
                        while (accounts.size() == 0) {
                            lock.wait();
                        }
                    }
                    generateDeposit();
                    Thread.sleep(1000);
                    if (depositQueue.size() == 1) {
                        synchronized (lockDeposit) {
                            lockDeposit.notifyAll();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception in DepositGenerationThread");
                }
            }
        }
    }

    class WithdrawGenerationThread extends Thread {
        Random random = new Random();

        WithdrawGenerationThread() {
            super("WithdrawGenerationThread");
        }

        public void run() {
            for (int i = 0; i < limit; i++) {
                try {
                    synchronized (lock) {
                        while (accounts.size() == 0) {
                            lock.wait();
                        }
                    }
                    generateWithdraw();
                    Thread.sleep(1000);
                    if (withdrawQueue.size() == 1) {
                        synchronized (lockWithdraw) {
                            lockWithdraw.notifyAll();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception in WithdrawGenerationThread");
                }
            }
        }
    }

    class DepositProcessingThread1 extends Thread {
        DepositProcessingThread1() {
            super("DepositProcessingThread1");
        }

        public void run() {
            while (processedDeposits < limit) {
                try {
                    synchronized (lockDeposit) {
                        while (depositQueue.size() == 0) {
                            lockDeposit.wait();
                        }
                    }
                    processDeposit();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in DepositProcessingThread1 " + e.getMessage());
                }
            }
        }
    }

    synchronized void processDeposit() {
        if (processedDeposits < depositQueue.size()) {
            DepositTransaction transaction = depositQueue.get(processedDeposits);
            Account account = findAccountByNumber(transaction.accountNumber);
            processedDeposits++;
            if (ex(transaction.depositAmount, account.maximumTransactionLimit, true)) {
                System.out.println("Account Number: " + account.accountNumber + " Balance: " + account.balance);
                return;
            }
            if (transaction.depositAmount <= account.maximumTransactionLimit) {
                account.balance += transaction.depositAmount;
                System.out.println("Deposited " + transaction.depositAmount + " to account "
                        + account.accountNumber + " Balance: " + account.balance);
            }
        }
    }

    class DepositProcessingThread2 extends Thread {
        DepositProcessingThread2() {
            super("DepositProcessingThread2");
        }

        public void run() {
            while (processedDeposits < limit) {
                try {
                    synchronized (lockDeposit) {
                        while (depositQueue.size() == 0) {
                            lockDeposit.wait();
                        }
                    }
                    processDeposit();
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in DepositProcessingThread2 " + e.getMessage());
                }
            }
        }
    }

    class WithdrawProcessingThread1 extends Thread {
        WithdrawProcessingThread1() {
            super("WithdrawProcessingThread1");
        }

        public void run() {
            while (processedWithdraws < limit) {
                try {
                    synchronized (lockWithdraw) {
                        while (withdrawQueue.size() == 0) {
                            lockWithdraw.wait();
                        }
                    }
                    processWithdraw();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in WithdrawProcessingThread1 " + e.getMessage());
                }
            }
        }
    }

    synchronized void processWithdraw() {
        if (processedWithdraws < withdrawQueue.size()) {
            WithdrawTransaction withdraw = withdrawQueue.get(processedWithdraws);
            Account account = findAccountByNumber(withdraw.accountNumber);
            processedWithdraws++;
            if (ex(withdraw.withdrawAmount, account.maximumTransactionLimit, false)) {
                System.out.println("Account Number: " + account.accountNumber + " Balance: " + account.balance);
                return;
            }
            if (account.balance < withdraw.withdrawAmount) {
                System.out.println("Insufficient balance in account " + account.accountNumber);
                return;
            }
            account.balance -= withdraw.withdrawAmount;
            System.out.println("Withdrew " + withdraw.withdrawAmount + " from account "
                    + account.accountNumber + " Balance: " + account.balance);
        }
    }

    class WithdrawProcessingThread2 extends Thread {
        WithdrawProcessingThread2() {
            super("WithdrawProcessingThread2");
        }

        public void run() {
            while (processedWithdraws < limit) {
                try {
                    synchronized (lockWithdraw) {
                        while (withdrawQueue.size() == 0) {
                            lockWithdraw.wait();
                        }
                    }
                    processWithdraw();

                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in WithdrawProcessingThread2 " + e.getMessage());
                }
            }
        }
    }

    private void execute() {
        AccountGenerationThread accountThread = new AccountGenerationThread();

        DepositGenerationThread depositThread = new DepositGenerationThread();
        WithdrawGenerationThread withdrawThread = new WithdrawGenerationThread();

        DepositProcessingThread1 depositProcessingThread1 = new DepositProcessingThread1();
        DepositProcessingThread2 depositProcessingThread2 = new DepositProcessingThread2();
        WithdrawProcessingThread1 withdrawProcessingThread1 = new WithdrawProcessingThread1();
        WithdrawProcessingThread2 withdrawProcessingThread2 = new WithdrawProcessingThread2();

        accountThread.t.start();
        depositThread.start();
        withdrawThread.start();

        // join dile age acno(30) account,limit(500) deposit, withdraw generation er
        // thread shesh hole 4ta working thread er kaj shuru hobe ja shesh hole main
        // thread exit korbe
        // ekhon sobgulu thread eksathe cholbe at least ekta dposit withdraw hole hobe.

        /*
         * try {
         * accountThread.t.join();
         * depositThread.join();
         * withdrawThread.join();
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         * if (!accountThread.t.isAlive() && !depositThread.isAlive() &&
         * !withdrawThread.isAlive()) {
         */

        // System.out.println(withdrawQueue.size());
        // System.out.println(depositQueue.size());
        // System.out.println(accounts.size());
        depositProcessingThread1.start();
        depositProcessingThread2.start();
        withdrawProcessingThread1.start();
        withdrawProcessingThread2.start();
        /*
         * try {
         * depositProcessingThread1.join();
         * depositProcessingThread2.join();
         * withdrawProcessingThread1.join();
         * withdrawProcessingThread2.join();
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         * if (!depositProcessingThread1.isAlive() &&
         * !depositProcessingThread2.isAlive()
         * && !withdrawProcessingThread1.isAlive() &&
         * !withdrawProcessingThread2.isAlive())
         * System.out.println("Main thread is done");
         * }
         */

    }

    public static void main(String[] args) {

        BankingThread roll_08 = new BankingThread();
        roll_08.execute();

    }
}
