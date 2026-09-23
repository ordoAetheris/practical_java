package com.practice.task20_coupon_service;

import java.util.*;
import java.time.*;

/**
 * Сервис купонов и промокодов.
 *
 * <p>Реализовать in-memory сервис управления купонами: создание, применение,
 * проверка валидности, деактивация, статистика использования.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task20_coupon_service/CouponService.java
 * java -cp src/main/java com.practice.task20_coupon_service.CouponService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Применение в пределах лимита</b> — защита от превышения maxUses при конкуренции.</li>
 *   <li><b>2. Идемпотентное применение</b> — повтор применения одним пользователем не считается дважды.</li>
 *   <li><b>3. Параллельность разных купонов</b> — применения разных купонов не мешают друг другу.</li>
 * </ul>
 */
public class CouponService {

    public record Coupon(String code, int discountPercent, int maxUses, int usedCount,
                         LocalDate validFrom, LocalDate validTo,
                         Set<String> applicableCategories, boolean active) {}

    /**
     * Создать купон
     * @param code уникальный код
     * @param discountPercent процент скидки (1-100)
     * @param maxUses максимальное количество использований
     * @param applicableCategories категории (пустой set — все категории)
     */
    public void createCoupon(String code, int discountPercent, int maxUses,
                             LocalDate validFrom, LocalDate validTo,
                             Set<String> applicableCategories) {
        //TODO implement
    }

    /**
     * Применить купон к товару
     * @return цена со скидкой
     * @throws IllegalStateException если купон невалиден
     */
    public double applyCoupon(String code, String category, double originalPrice) {
        //TODO implement
        return 0;
    }

    /** Проверить валидность купона на дату */
    public boolean isValid(String code, LocalDate date) {
        //TODO implement
        return false;
    }

    /** Деактивировать купон */
    public void deactivate(String code) {
        //TODO implement
    }

    /** Получить статистику купона */
    public Coupon getStats(String code) {
        //TODO implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== CouponService: Smoke Test ===");
        // TODO: создать купон, применить, проверить скидку
    }
}

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. ПРИМЕНЕНИЕ В ПРЕДЕЛАХ ЛИМИТА — защита от превышения maxUses ───
 *   Бизнес: у купона ограниченное число использований (maxUses) — нельзя применить больше.
 *   Контракт: applyCoupon засчитывает использование, только если usedCount < maxUses; иначе отказ.
 *     Инвариант: usedCount НИКОГДА не превышает maxUses.
 *   Ловушка: остался один use, N потоков применяют купон одновременно — наивная «usedCount < maxUses →
 *     увеличил» выдаёт больше maxUses (все проверили, что можно, и все применили — TOCTOU).
 *   Проверить: maxUses = M, N>M потоков применяют → успешны ровно M, usedCount == M.
 *
 * ─── 2. ИДЕМПОТЕНТНОЕ ПРИМЕНЕНИЕ ───
 *   Бизнес: повтор запроса на применение (ретрай, двойной клик) одним пользователем не должен
 *     израсходовать второе использование.
 *   Контракт: applyCoupon принимает userId; один пользователь применяет купон один раз, повтор
 *     возвращает тот же результат и не увеличивает usedCount второй раз.
 *   Ловушка: два запроса от одного userId приходят одновременно.
 *   Проверить: applyCoupon одним userId дважды → скидка та же, usedCount +1 (не +2).
 *
 * ─── 3. ПАРАЛЛЕЛЬНОСТЬ РАЗНЫХ КУПОНОВ ───
 *   Бизнес: применения РАЗНЫХ купонов не должны мешать друг другу — конкурировать могут только
 *     применения ОДНОГО купона.
 *   Контракт: критическая секция применения не должна сериализовать разные купоны.
 *   Ловушка: один общий замок на весь сервис корректен, но все купоны встают в одну очередь.
 *   Проверить: применения разных купонов идут параллельно; одного купона — сериализуются.
 */
