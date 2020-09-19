package cn.dustlight.oauth2.uim;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Logger;


@SpringBootTest
public class RandomTest {

    public static class Counter {

        private long[] table;
        private long top;

        public Counter(int width) {
            table = new long[width];
        }

        public int width() {
            return table.length;
        }

        public void add(int index) {
            table[index] += 1;
            if (table[index] > top)
                top = table[index];
        }

        public void fill(long val) {
            Arrays.fill(table, val);
        }

        public double[] normalize() {
            int len = table.length;
            double[] target = new double[len];
            if (top == 0)
                return target;
            for (int i = 0; i < len; i++)
                target[i] = (double) table[i] / top;
            return target;
        }
    }

    public static class Printer {

        private int width, height;

        public Printer(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void print(double[] data, String c) {
            for (int i = 0; i < height; i++) {
                int index = (int) (((float) i / height) * data.length);
                int len = (int) (data[index] * width);
                System.out.printf("[%d] \t|", i + 1);
                for (int j = 0; j < len; j++) {
                    System.out.print(c);
                }
                System.out.printf("| %.1f", data[index]);
                System.out.println();
            }
        }
    }


    public static void main(String[] args) {
        Logger logger = Logger.getLogger(RandomTest.class.getName());
        Counter counter = new Counter(50000);
        Printer printer = new Printer(50, 50);
        SecureRandom secureRandom = new SecureRandom();
        long count = 1000000;
        int repeat = 3;
        long s = System.currentTimeMillis();
        logger.info("Task starting.");
        for (int r = 0; r < repeat; r++) {
            logger.info("Repeat " + (r + 1) + " starting.");
            long start = System.currentTimeMillis();
            for (int c = 0; c < count; c++) {
//                counter.add(new Object().hashCode() % (counter.width()));
                counter.add(secureRandom.nextInt(counter.width()));
            }
            logger.info("Repeat finished in " + (System.currentTimeMillis() - start) + "ms.");
        }
        logger.info("Task finished in " + (System.currentTimeMillis() - s) + "ms.");
        printer.print(counter.normalize(), "——");
    }
}
