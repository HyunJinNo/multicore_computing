import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Car1 extends Thread {
    private final BlockingQueue<String> queue;
    public Car1(String name, BlockingQueue<String> queue) {
        super(name);
        this.queue = queue;
        start();
    }

    private void tryingEnter() {
        System.out.println(getName() + ": trying to enter");
    }

    private void justEntered() {
        System.out.println(getName() + ": just entered. [Remaining places: " + (7 - queue.size()) + "]");
    }

    private void aboutToLeave() {
        System.out.println(getName() + ":                                     about to leave");
    }

    private void Left() {
        System.out.println(getName() + ":                                     have been left. [Remaining places: " + (7 - queue.size()) + "]");
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking

                tryingEnter();
                queue.put(getName());
                justEntered();

                sleep((int)(Math.random() * 20000)); // stay within the parking garage

                aboutToLeave();
                queue.take();
                Left();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

public class ParkingBlockingQueue {
    public static void main(String[] args){
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(7);

        for (int i = 1; i <= 10; i++) {
            new Car1("Car " + i, queue);
        }
    }
}
