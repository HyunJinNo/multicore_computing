import java.util.concurrent.Semaphore;

class Car2 extends Thread {
    private final Semaphore semaphore;
    public Car2(String name, Semaphore semaphore) {
        super(name);
        this.semaphore = semaphore;
        start();
    }

    private void tryingEnter() {
        System.out.println(getName() + ": trying to enter");
    }

    private void justEntered() {
        System.out.println(getName() + ": just entered. [Remaining places: " + semaphore.availablePermits() + "]");
    }

    private void aboutToLeave() {
        System.out.println(getName() + ":                                     about to leave");
    }

    private void Left() {
        System.out.println(getName() + ":                                     have been left. [Remaining places: " + semaphore.availablePermits() + "]");
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking

                tryingEnter();
                semaphore.acquire();
                justEntered();

                sleep((int)(Math.random() * 20000)); // stay within the parking garage

                aboutToLeave();
                semaphore.release();
                Left();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

public class ParkingSemaphore {
    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(7);
        for (int i = 1; i <= 10; i++) {
            new Car2("Car " + i, semaphore);
        }
    }
}
