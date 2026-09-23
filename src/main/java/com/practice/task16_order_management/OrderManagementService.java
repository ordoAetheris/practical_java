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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Корректный переход статуса</b> — конечный автомат заказа, запрет недопустимых переходов под конкуренцией.</li>
 *   <li><b>2. Идемпотентная оплата</b> — повторная оплата не списывает деньги дважды.</li>
 *   <li><b>3. Гонка оплаты и отмены</b> — из CREATED побеждает ровно один: заказ либо PAID, либо CANCELLED.</li>
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

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. КОРРЕКТНЫЙ ПЕРЕХОД СТАТУСА ЗАКАЗА (конечный автомат) ───
 *   Бизнес: заказ проходит стадии CREATED → PAID → SHIPPED → DELIVERED; отмена возможна только из
 *     нетерминальных статусов. Нельзя перепрыгнуть стадию (например, из CREATED сразу в DELIVERED).
 *   Контракт: методы перехода (payOrder, shipOrder, deliverOrder, cancelOrder) разрешают переход
 *     ТОЛЬКО из ожидаемого текущего статуса, иначе отказ (IllegalStateException). Инвариант: заказ
 *     всегда в одном валидном статусе, недопустимых переходов не происходит.
 *   Ловушка: конкурентные вызовы читают статус, проверяют допустимость и пишут новый — между чтением
 *     и записью статус мог измениться другим потоком, и получается несогласованный переход
 *     (read-modify-write гонка).
 *   Проверить: ship из CREATED запрещён, из PAID разрешён; под конкуренцией недопустимый переход не проходит.
 *
 * ─── 2. ИДЕМПОТЕНТНАЯ ОПЛАТА ───
 *   Бизнес: двойной клик или повтор запроса на оплату не должен списать деньги дважды.
 *   Контракт: повторный payOrder уже оплаченного заказа — no-op (статус остаётся PAID один раз,
 *     повторного эффекта нет).
 *   Ловушка: два запроса на оплату одного заказа приходят одновременно.
 *   Проверить: payOrder дважды → заказ переходит в PAID ровно один раз, эффект оплаты один.
 *
 * ─── 3. ГОНКА ОПЛАТЫ И ОТМЕНЫ ───
 *   Бизнес: из статуса CREATED заказ можно либо оплатить, либо отменить — но не то и другое сразу.
 *   Контракт: при одновременных payOrder и cancelOrder из CREATED победить должен РОВНО ОДИН — заказ
 *     окажется либо PAID, либо CANCELLED, но не в противоречивом состоянии; проигравший получает отказ.
 *     Инвариант: не бывает «оплачен И отменён».
 *   Ловушка: обе операции стартуют из одного статуса и пытаются сменить его одновременно.
 *   Проверить: много итераций гонки pay-против-cancel → заказ всегда ровно в одном терминальном статусе.
 */
