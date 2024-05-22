import java.util.*;
import java.util.concurrent.*;

class Task {
    protected int id;
    protected int executionTime;
    protected boolean isCompleted = false;

    Task(int id, int executionTime) {
        this.id = id;
        this.executionTime = executionTime;
    }
    public synchronized void execute() throws InterruptedException {
        if(!isCompleted)
        {
            System.out.println("Task "+id+" is being executed");
            wait(executionTime);
            isCompleted=true;
            System.out.println("Task "+id+" is completed");
        }
    }
}

class Server implements Runnable {
    Thread t;
    protected String serverName;
    private int runtime;
    protected Queue<Task> taskQueue = new ConcurrentLinkedQueue<>();
    private Task currentTask;
    private Task lastCompletedTask;
    private final Object lock = new Object();

    Server(String serverName, int runtime) {
        this.serverName = serverName;
        this.runtime = runtime;
        t=new Thread(this);
    }

    public void addTask(Task task) {
        taskQueue.add(task);
        synchronized (lock) {
            lock.notify();
        }
    }

    public Task getLastCompletedTask() {
        return lastCompletedTask;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public Queue<Task> getTaskQueue() {
        return taskQueue;
    }

    public boolean canHandleTask(Task task) {
        return task.executionTime <= runtime;
    }

    @Override
    public void run() {
        while (taskQueue.size()!=0) {
            synchronized (lock) {
                if (taskQueue.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentTask = taskQueue.poll();
                System.out.println("----"+serverName + "'s---- \nLast Completed Task: " + (lastCompletedTask != null ? lastCompletedTask.id : "None")+"\nCurrent Task: " + (currentTask != null ? currentTask.id : "None\n\n"));
                if (currentTask != null) {
                    try {
                        currentTask.execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lastCompletedTask = currentTask;
                    currentTask = null;
                }
            }
        }
    }
}


public class MultiThreadPractice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int K = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Server> servers = new ArrayList<>();

        for (int i = 0; i < K; i++) {
            String name = scanner.next();
            int maxTime = scanner.nextInt();
            servers.add(new Server(name, maxTime));
        }

        int X = scanner.nextInt();
        scanner.nextLine(); // consume newline

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < X; i++) {
            int id = scanner.nextInt();
            int time = scanner.nextInt();
            tasks.add(new Task(id, time));
        }

        Iterator<Task> taskIterator = tasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            boolean assigned = false;
            for (Server server : servers) {
                if (server.canHandleTask(task)) {
                    server.addTask(task);
                    taskIterator.remove();
                    assigned = true;
                    break;
                }
            }
            if (!assigned) {
                System.out.println("Task " + task.id + " could not be assigned to any server.");
            }
        }
        for (Server server : servers) {
            server.t.start();
            
        }
        for(Server server: servers)
        {
            try{
                server.t.join();
            }
            catch(Exception e)
            {
                System.out.println(server.serverName+" "+e);
            }
        }
        
        System.out.println("All tasks completed");
        
    }
}
