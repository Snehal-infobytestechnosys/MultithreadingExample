public class MultiThreadingExample {

    public static void main(String[] args) throws InterruptedException {
        // Creating threads using the Thread class
        Thread thread1 = new Thread(new MyRunnable("Thread-1"));
        thread1.start();

        // Creating threads using the Runnable interface
        Thread thread2 = new Thread(new MyRunnable("Thread-2"));
        thread2.start();

        // Create and start a thread with synchronized block to control access to a shared resource
        Counter counter = new Counter();
        Thread thread3 = new Thread(new MyCounterRunnable(counter));
        Thread thread4 = new Thread(new MyCounterRunnable(counter));
        thread3.start();
        thread4.start();

        // Wait for threads to finish
        thread3.join();
        thread4.join();

        // Thread communication using wait() and notify()
        final Object lock = new Object();
        Thread thread5 = new Thread(new MyWaitNotifyRunnable(lock));
        thread5.start();

        // Main thread waits and communicates with thread5 using wait() and notify()
        synchronized (lock) {
            lock.wait();  // Main thread will wait for thread5 to notify
            System.out.println("Main thread received notification from thread5.");
        }

        thread1.join();
        thread2.join();
        thread5.join();

        System.out.println("Main thread execution finished.");
    }
}

// Example of creating a thread by implementing Runnable interface
class MyRunnable implements Runnable {
    private String threadName;

    MyRunnable(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println(threadName + " started.");
        try {
            Thread.sleep(1000);  // Simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadName + " finished.");
    }
}

// Example of using synchronization to control access to a shared resource
class Counter {
    private int count = 0;

    // Synchronized method ensures only one thread at a time can access this method
    public synchronized void increment() {
        count++;
        System.out.println("Counter: " + count);
    }
}

class MyCounterRunnable implements Runnable {
    private Counter counter;

    MyCounterRunnable(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            counter.increment();
            try {
                Thread.sleep(500);  // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Example of thread communication using wait() and notify()
class MyWaitNotifyRunnable implements Runnable {
    private final Object lock;

    MyWaitNotifyRunnable(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                System.out.println("Thread5 started, will notify main thread in 2 seconds.");
                Thread.sleep(2000);  // Simulate work
                lock.notify();  // Notify the waiting thread (main thread)
                System.out.println("Thread5 finished, notified main thread.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
