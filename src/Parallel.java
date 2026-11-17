import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Parallel {

    static class TaskResult {
        final long n;
        final long m;

        TaskResult(long n, long m) {
            this.n = n;
            this.m = m;
        }
    }

    static TaskResult worker(long nLocal, long seed) {
        Random random = new Random(seed);

        long mLocal = 0; // Количество точек, попавших в круг в одном потоке

        for (long i = 0; i < nLocal; i++) {
            double x = -1.0 + 2.0 * random.nextDouble();
            double y = -1.0 + 2.0 * random.nextDouble();

            if (x * x + y * y <= 1.0) {
                mLocal++;
            }
        }

        return new TaskResult(nLocal, mLocal);
    }

    public static void main(String[] args) throws Exception {
        long n = 10000000L;   // Случайных точек
        int workers = 4;        // Число потоков
        long baseSeed = 12345L; // Базовое зерно

        long base = n / workers;
        long remainder = n % workers;

        List<Callable<TaskResult>> tasks = new ArrayList<>();

        for (int i = 0; i < workers; i++) {
            long nLocal = base + (i < remainder ? 1 : 0);
            long seed = baseSeed + i * 1000003L;

            final long nLocalFinal = nLocal;
            final long seedFinal = seed;

            tasks.add(() -> worker(nLocalFinal, seedFinal));
        }

        long start = System.nanoTime();

        ExecutorService pool = Executors.newFixedThreadPool(workers);
        List<Future<TaskResult>> futures = pool.invokeAll(tasks);

        long totalN = 0;
        long totalM = 0;

        for (Future<TaskResult> future : futures) {
            TaskResult result = future.get();
            totalN += result.n;
            totalM += result.m;
        }

        pool.shutdown();

        double elapsed = (System.nanoTime() - start) / 1_000_000_000.0;

        double area = 4.0 * totalM / totalN;
        double error = Math.abs(area - Math.PI);

        String output = String.format(
                "Количество процессов = %d%n" +
                        "Количество случайных точек = %d%n" +
                        "Количество случайных точек, которые попали в область D = %d%n" +
                        "Время выполнения программы = %.4f s%n" +
                        "Приближенное число Пи = %.10f%n" +
                        "Истина = %.10f%n" +
                        "Погрешность = %.10f%n",
                workers,
                totalN,
                totalM,
                elapsed,
                area,
                Math.PI,
                error
        );

        System.out.print(output);
    }
}