public class lesson5e {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        float [] Arr = new float[size];
        for (int i = 0; i < size; i++){
            Arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++){
            Arr[i] = (float) (Arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.currentTimeMillis();
        System.out.println("Время выполения в одном потоке " + (System.currentTimeMillis() - a) + "мс");
        for (int i = 0; i < size; i++){
            Arr[i] = 1;
        }
        float [] a1 = new float[size];
        float [] a2 = new float[size];
        long b = System.currentTimeMillis();
        System.arraycopy(Arr, 0, a1, 0, h);
        System.arraycopy(Arr, h, a2, 0, h);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < a1.length; i++){
                    a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < a2.length; i++){
                    a2[i] = (float) (a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(a1, 0, Arr, 0, h);
        System.arraycopy(a2, 0, Arr, h, h);
        System.currentTimeMillis();
        System.out.println("Время выполения в 2ух потоках " + (System.currentTimeMillis() - b) + "мс");
    }
}
