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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Double-redeem:</b> лимитированный купон применяют 2 потока разом → оба использовали (over-redeem). Атомарно: CAS/лок на счётчик использований, проверка-лимита-и-декремент под защитой.</li>
 *   <li>Идемпотентность применения (тот же юзер дважды); expiry; edge (применение деактивированного/просроченного).</li>
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

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ ИНКРЕМЕНТ В ПРЕДЕЛАХ ЛИМИТА (против overissue): остался 1 use, N потоков applyCoupon → наивная
 *    «usedCount<maxUses → ++» выдаёт больше maxUses. usedCount++ через CAS-петлю ТОЛЬКО если usedCount<maxUses, иначе отказ.
 *    Тест: maxUses=M, N>M потоков → ровно M успешных применений, usedCount==M.
 * 2. ИДЕМПОТЕНТНОЕ ПРИМЕНЕНИЕ (на пользователя): applyCoupon с userId — один юзер применяет один раз, ретрай = тот же
 *    результат, usedCount не растёт повторно. Тест: applyCoupon(user) дважды → скидка та же, usedCount +1 не +2.
 * 3. ГРАНУЛЯРНЫЙ ЛОК НА КУПОН: применения РАЗНЫХ купонов параллельны (не глобальный лок).
 */
