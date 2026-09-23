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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Атомарное продление</b> — защита от двойного продления/списания при конкурентном renew.</li>
 *   <li><b>2. Идемпотентное продление</b> — повтор renew по requestId не продлевает второй раз.</li>
 *   <li><b>3. Параллельность разных подписок</b> — операции над разными подписками не мешают друг другу.</li>
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

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. АТОМАРНОЕ ПРОДЛЕНИЕ — защита от двойного продления/списания ───
 *   Бизнес: продление подписки сдвигает дату окончания на длительность плана и списывает оплату один
 *     раз. Два одновременных запроса на продление не должны продлить или списать дважды.
 *   Контракт: renew(subscriptionId) сдвигает endDate на длительность плана атомарно. Инвариант: дата
 *     окончания и число списаний согласованы с числом реальных продлений.
 *   Ловушка: renew читает текущую дату окончания, прибавляет срок и записывает — два потока прочитали
 *     одну и ту же дату, оба записали свою, одно продление потерялось (read-modify-write гонка).
 *   Проверить: несколько потоков renew одной подписки → период сдвинулся согласно бизнес-правилу
 *     (не «плюс по разу за каждый поток», если это один логический запрос); последовательные renew
 *     суммируются корректно.
 *
 * ─── 2. ИДЕМПОТЕНТНОЕ ПРОДЛЕНИЕ — защита от повторного списания ───
 *   Бизнес: повтор запроса на продление (ретрай платежа, двойной клик) не должен продлить и списать
 *     деньги второй раз.
 *   Контракт: renew принимает requestId (стабильный на одно намерение, обычно id платежа); повтор с
 *     тем же requestId не меняет дату окончания второй раз.
 *   Ловушка: два запроса с одним requestId приходят одновременно.
 *   Проверить: renew с одним requestId дважды → период продлён один раз, списание одно.
 *
 * ─── 3. ПАРАЛЛЕЛЬНОСТЬ РАЗНЫХ ПОДПИСОК ───
 *   Бизнес: операции над РАЗНЫМИ подписками не должны мешать друг другу — конкурировать могут только
 *     операции над ОДНОЙ подпиской.
 *   Контракт: критическая секция продления не должна сериализовать операции по разным подпискам.
 *   Ловушка: один общий замок на весь сервис корректен, но все подписки встают в одну очередь и
 *     душат пропускную способность.
 *   Проверить: операции над разными подписками идут параллельно; над одной — сериализуются.
 */
