import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ex1 {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        new Producer(queue);
        new Consumer(queue);
    }
}

final class Producer extends Thread {
    private final BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
        start();
    }

    private void beforePutting(int number) {
        System.out.println("Producer is trying to put " + number + " into the queue.");
    }

    private void afterPutting(int number) {
        System.out.println("Producer puts " + number + " into the queue.");
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                beforePutting(i + 1);
                queue.put(i + 1);
                afterPutting(i + 1);
            } catch (Exception ignored) {}
        }
    }
}

final class Consumer extends Thread {
    private final BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
        start();
    }

    private void beforeTaking() {
        System.out.println("          Consumer is trying to take a number from the queue.");
    }

    private void afterTaking(int number) {
        System.out.println("          Consumer takes " + number + " from the queue.");
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                beforeTaking();
                int number = queue.take();
                afterTaking(number);
            } catch (Exception ignored) {}
        }
    }
}
