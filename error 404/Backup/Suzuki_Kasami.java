class CriticalSection implements Runnable {
    private static int counter = 0;
    private final int id;

    public CriticalSection(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (counter < 10) {
            synchronized (CriticalSection.class) {
                if (counter % 5 == id) {
                    System.out.println("Node " + id + " is in critical section");
                    counter++;
                }
            }
        }
    }
}

public class Suzuki_Kasami {
    public static void main(String[] args) {
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(new CriticalSection(i));
            threads[i].start();
        }
    }
}