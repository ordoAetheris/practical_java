package com.practice.task17_subscription_manager;

import java.util.*;
import java.time.*;

/**
 * Менеджер подписок.
 *
 * <p>Реализовать in-memory менеджер подписок с поддержкой тарифных планов,
 * пробного периода, продления и отмены.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task17_subscription_manager/SubscriptionManagerService.java
 * java -cp src/main/java com.practice.task17_subscription_manager.SubscriptionManagerService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Гонка состояния:</b> renew + cancel одновременно → невалидное состояние/двойное списание. Атомарный переход под защитой; идемпотентность billing (ключ, против double-charge).</li>
 *   <li>Trial→paid переход; edge (продление отменённой, отмена уже отменённой); проверка истечения (при обращении vs таймер).</li>
 * </ul>
 */
public class SubscriptionManagerService {

    public enum SubscriptionStatus { TRIAL, ACTIVE, EXPIRED, CANCELLED }

    public record Plan(String id, String name, double price, int durationDays) {}
    public record Subscription(String id, String userId, String planId,
                               LocalDate startDate, LocalDate endDate,
                               SubscriptionStatus status) {}

    /** Добавить тарифный план */
    public void addPlan(Plan plan) {
        //TODO implement
    }

    /**
     * Оформить подписку
     * @param trial true если пробный период
     * @throws IllegalStateException если trial уже использован этим пользователем
     */
    public Subscription subscribe(String userId, String planId, boolean trial) {
        //TODO implement
        return null;
    }

    /** Продлить подписку. @throws IllegalStateException если отменена */
    public void renew(String subscriptionId) {
        //TODO implement
    }

    /** Отменить подписку */
    public void cancel(String subscriptionId) {
        //TODO implement
    }

    /** Активна ли подписка на дату */
    public boolean isActive(String subscriptionId, LocalDate date) {
        //TODO implement
        return false;
    }

    /** Подписки, истекающие в ближайшие N дней */
    public List<Subscription> getExpiringWithin(int days) {
        //TODO implement
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("=== SubscriptionManagerService: Smoke Test ===");
        // TODO: создать план, подписку, продлить, проверить статус
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНОЕ ПРОДЛЕНИЕ (против double renewal): два потока renew одной подписки → продление/списание дважды.
 *    Продление = атомарный сдвиг даты окончания на длительность плана (compute/лок на подписку), без гонки read-write.
 *    Тест: N потоков renew → период +1 (по бизнес-правилу), не +N; последовательные renew суммируются корректно.
 * 2. ИДЕМПОТЕНТНОЕ ПРОДЛЕНИЕ (requestId = id платежа): ретрай renew с тем же requestId не продлевает второй раз.
 * 3. ГРАНУЛЯРНЫЙ ЛОК НА ПОДПИСКУ: операции над РАЗНЫМИ подписками параллельны (не глобальный лок).
 */
