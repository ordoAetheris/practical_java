package com.practice.task16_order_management;

import java.util.*;

/**
 * Управление заказами.
 *
 * <p>Реализовать in-memory систему управления заказами с жизненным циклом:
 * CREATED -> PAID -> SHIPPED -> DELIVERED. Отмена возможна до доставки.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task16_order_management/OrderManagementService.java
 * java -cp src/main/java com.practice.task16_order_management.OrderManagementService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — интервьюер додавливает, дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Гонка перехода статуса (thread-safety):</b> pay() и cancel() одновременно → невалидное/двойное состояние.
 *       Атомарный переход под защитой + проверка допустимости (FSM) ВНУТРИ критической секции. Оптимистичная блокировка (version).</li>
 *   <li><b>Валидность перехода:</b> запрет CREATED→DELIVERED и пр.; идемпотентность повторного перехода (double-pay/double-cancel).</li>
 * </ul>
 */
public class OrderManagementService {

    public enum OrderStatus { CREATED, PAID, SHIPPED, DELIVERED, CANCELLED }

    public record Customer(String id, String name) {}
    public record OrderItem(String productName, int quantity, double price) {}
    public record Order(String id, String customerId, List<OrderItem> items,
                        OrderStatus status, long createdAt) {}

    /** Зарегистрировать клиента */
    public void registerCustomer(Customer customer) {
        //TODO implement
    }

    /** Создать заказ */
    public String createOrder(String customerId, List<OrderItem> items) {
        //TODO implement
        return null;
    }

    /** Оплатить заказ. @throws IllegalStateException если статус не CREATED */
    public void payOrder(String orderId) {
        //TODO implement
    }

    /** Отправить заказ. @throws IllegalStateException если статус не PAID */
    public void shipOrder(String orderId) {
        //TODO implement
    }

    /** Доставить заказ. @throws IllegalStateException если статус не SHIPPED */
    public void deliverOrder(String orderId) {
        //TODO implement
    }

    /** Отменить заказ. @throws IllegalStateException если заказ уже доставлен */
    public void cancelOrder(String orderId) {
        //TODO implement
    }

    /** Заказы клиента */
    public List<Order> getOrdersByCustomer(String customerId) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Заказы по статусу */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Общая сумма заказа */
    public double getOrderTotal(String orderId) {
        //TODO implement
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== OrderManagementService: Smoke Test ===");
        // TODO: создать клиента, заказ, провести по статусам
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ ВАЛИДНЫЙ ПЕРЕХОД FSM: конкурентные pay/ship/cancel читают-проверяют-пишут статус → несогласованный
 *    переход. Переход разрешён только из ожидаемого статуса: compareAndSet(expected→next) атомарно, иначе
 *    IllegalStateException. Матрица: CREATED→PAID→SHIPPED→DELIVERED; CANCEL из не-терминальных. Тест: ship из CREATED нельзя, из PAID можно.
 * 2. ИДЕМПОТЕНТНАЯ ОПЛАТА: повторный payOrder (двойной клик/ретрай) — no-op если уже оплачен, деньги не списываются дважды.
 *    Тест: payOrder дважды → статус PAID один раз, эффект один.
 * 3. ГОНКА PAY vs CANCEL: из CREATED одновременно payOrder и cancelOrder → победить должен РОВНО ОДИН (PAID xor
 *    CANCELLED), проигравший — IllegalStateException. Никаких «оплачен И отменён». Тест: 1000 итераций гонки → всегда один терминал.
 */
