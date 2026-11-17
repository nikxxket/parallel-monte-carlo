import math
import random
import time
import multiprocessing as mp


def worker(args):
    worker_id, n_local, seed = args
    random.seed(seed)

    m_local = 0                      # Количество точек, которые попали в область круга в одном локальном процессе
    for _ in range(n_local):
        x = random.uniform(-1.0, 1.0)   # Генерируем случайные координаты
        y = random.uniform(-1.0, 1.0)

        if x * x + y * y <= 1.0:
            m_local += 1

    return n_local, m_local


def main():
    n = 10000000    # Случайных точек
    workers = 4     # Число процессов
    base_seed = 12345   # Базовое «зерно»

    base = n // workers     # Сколько точек достанется каждому процессу
    remainder = n % workers # Остаток раздадим первым процессам по одной точке

    tasks = []  # Список тасков для процесса
    for i in range(workers):
        n_local = base + (1 if i < remainder else 0)
        seed = base_seed + i * 1000003  # Каждому процессу даем случайный сид
        tasks.append((i, n_local, seed))

    start = time.perf_counter() # Засекаем время старта (до запуска процессов)

    with mp.Pool(processes=workers) as pool:
        results = pool.map(worker, tasks)   # Параллельно запускаем процесс на каждом задании и собираем результаты в список

    elapsed = time.perf_counter() - start   # Время работы параллельной программы

    total_n = sum(r[0] for r in results)    # Складываем все локальные точки
    total_m = sum(r[1] for r in results)

    area = 4.0 * total_m / total_n  # Высчитываем интеграл

    error = abs(area - math.pi) # Высчитываем ошибку

    result = (
        f"Количество процессов = {workers}\n"
        f"Количество случайных точек = {total_n}\n"
        f"Количество случайных точек, которые попали в область D = {total_m}\n"
        f"Время выполнения программы = {elapsed:.4f} s\n"
        f"Приближенное число Пи = {area:.10f}\n"
        f"Истина = {math.pi:.10f}\n"
        f"Погрешность = {error:.10f}\n"
    )

    print(result)


if __name__ == "__main__":
    main()