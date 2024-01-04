import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Writer[] writers = new Writer[3];

        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Writer(arrayList, readWriteLock, i * 100);
        }

        for (Writer writer : writers) {
            try {
                writer.join();
            } catch (Exception ignored) {}
        }

        for (int i = 0; i < 3; i++) {
            new Reader(arrayList, readWriteLock, ("Thread-" + i));
        }
    }
}

final class Writer extends Thread {
    private final ArrayList<Integer> arrayList;
    private final ReadWriteLock readWriteLock;
    private final int num;


    public Writer(ArrayList<Integer> arrayList, ReadWriteLock readWriteLock, int num) {
        this.arrayList = arrayList;
        this.readWriteLock = readWriteLock;
        this.num = num;
        start();
    }

    @Override
    public void run() {
        readWriteLock.writeLock().lock();

        for (int i = num; i < num + 100; i++) {
            arrayList.add(i);
        }

        readWriteLock.writeLock().unlock();
    }
}

final class Reader extends Thread {
    ArrayList<Integer> arrayList;
    ReadWriteLock readWriteLock;

    public Reader(ArrayList<Integer> arrayList, ReadWriteLock readWriteLock, String name) {
        this.arrayList = arrayList;
        this.readWriteLock = readWriteLock;
        setName(name);
        start();
    }

    @Override
    public void run() {
        readWriteLock.readLock().lock();

        for (Integer integer : arrayList) {
            System.out.println(getName() + ": " + integer);
        }

        readWriteLock.readLock().unlock();
    }
}