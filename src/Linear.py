import math
import random
import time

def circle_area(n, seed=12345):
    random.seed(seed)

    m = 0                                # Количество точек, которые попали в область круга
    for _ in range(n):
        x = random.uniform(-1.0, 1.0)    # Генерируем случайные точки
        y = random.uniform(-1.0, 1.0)

        if x * x + y * y <= 1.0:         # Проверяем, что точка внутри области D
            m += 1                       # Считаем попавшие точки

    area = 4.0 * m / n                   # Вычисляем интеграл

    return n, m, area


def main():
    n = 10000000

    start = time.perf_counter()          # Засекаем время старта
    n, m, area = circle_area(n)
    elapsed = time.perf_counter() - start   # Время завершения

    error = abs(area - math.pi) # Высчитываем ошибку

    result = (
        f"\nКоличество случайных точек = {n}\n"
        f"Количество случайных точек, которые попали в область D = {m}\n"
        f"Время выполнения программы = {elapsed:.4f} сек.\n"
        f"Приближенное число Пи = {area:.10f}\n"
        f"Истина = {math.pi:.10f}\n"
        f"Погрешность = {error:.10f}\n"
    )

    print(result)


if __name__ == "__main__":
    main()