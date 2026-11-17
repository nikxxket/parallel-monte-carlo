import java.util.Random;

public class Linear {

    static class Result {
        final long n;
        final long m;
        final double area;

        Result(long n, long m, double area) {
            this.n = n;
            this.m = m;
            this.area = area;
        }
    }

    static Result circleArea(long n, long seed) {
        Random random = new Random(seed);

        long m = 0; // Количество точек, попавших в область круга

        for (long i = 0; i < n; i++) {
            double x = -1.0 + 2.0 * random.nextDouble();
            double y = -1.0 + 2.0 * random.nextDouble();

            if (x * x + y * y <= 1.0) {
                m++;
            }
        }

        double area = 4.0 * m / n;
        return new Result(n, m, area);
    }

    public static void main(String[] args) {
        long n = 10000000L;

        long start = System.nanoTime();
        Result result = circleArea(n, 12345L);
        double elapsed = (System.nanoTime() - start) / 1000000000.0;

        double error = Math.abs(result.area - Math.PI);

        String output = String.format(
                "%nКоличество случайных точек = %d%n" +
                        "Количество случайных точек, которые попали в область D = %d%n" +
                        "Время выполнения программы = %.4f сек.%n" +
                        "Приближенное число Пи = %.10f%n" +
                        "Истина = %.10f%n" +
                        "Погрешность = %.10f%n",
                result.n,
                result.m,
                elapsed,
                result.area,
                Math.PI,
                error
        );

        System.out.print(output);
    }
}