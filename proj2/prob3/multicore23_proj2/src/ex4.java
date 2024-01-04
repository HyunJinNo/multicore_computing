import java.util.concurrent.CyclicBarrier;

public class ex4 {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        MyThread[] myThreads = new MyThread[5];

        for (int i = 0; i < myThreads.length; i++) {
            myThreads[i] = new MyThread(cyclicBarrier);
        }
    }
}

final class MyThread extends Thread {
    CyclicBarrier cyclicBarrier;

    public MyThread(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        start();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("[" + getName() + "]: waiting at barrier " + i);
            try {
                this.cyclicBarrier.await();
            } catch (Exception ignored) {}
        }
    }
}
