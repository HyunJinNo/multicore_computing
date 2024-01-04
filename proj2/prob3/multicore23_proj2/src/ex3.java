import java.util.concurrent.atomic.AtomicInteger;

public class ex3 {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(1000);

        NumProducer1 numProducer1 = new NumProducer1(atomicInteger);
        NumProducer2 numProducer2 = new NumProducer2(atomicInteger);

        try {
            numProducer1.join();
            numProducer2.join();
        } catch (Exception ignored) {}

        System.out.println("Result: " + atomicInteger.get());
    }
}

final class NumProducer1 extends Thread {
    private final AtomicInteger atomicInteger;

    public NumProducer1(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
                System.out.println("[" + getName() + "] ==> current value before addition: " + atomicInteger.getAndAdd(1));
            } catch (Exception ignored) {}
        }
    }
}

final class NumProducer2 extends Thread {
    private final AtomicInteger atomicInteger;

    public NumProducer2(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
                System.out.println("[" + getName() + "] ==> current value after addition: " + atomicInteger.addAndGet(1));
            } catch (Exception ignored) {}
        }
    }
}

