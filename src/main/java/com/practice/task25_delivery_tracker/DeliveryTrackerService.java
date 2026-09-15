package com.practice.task25_delivery_tracker;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ТРЕКЕР ДОСТАВКИ
 *
 * <p>Реализуйте систему отслеживания посылок. Каждая посылка проходит через
 * цепочку статусов, и каждое изменение статуса фиксируется как событие отслеживания.</p>
 *
 * <p>Сущности:</p>
 * <ul>
 *   <li>Parcel — посылка (id, senderName, recipientName, weight, текущий статус)</li>
 *   <li>TrackingEvent — событие (parcelId, status, location, timestamp)</li>
 * </ul>
 *
 * <p>Допустимый порядок статусов (строгий):</p>
 * <pre>CREATED -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED</pre>
 * <p>Из IN_TRANSIT также возможен переход в RETURNED.</p>
 *
 * <p>Операции:</p>
 * <ul>
 *   <li>Создать посылку (автоматически получает статус CREATED)</li>
 *   <li>Обновить статус посылки (добавить событие)</li>
 *   <li>Получить текущий статус посылки</li>
 *   <li>Получить полную историю отслеживания</li>
 *   <li>Получить все посылки получателя</li>
 *   <li>Среднее время доставки (от CREATED до DELIVERED)</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Статусы идут в строгом порядке, нельзя пропускать</li>
 *   <li>Вес посылки должен быть положительным</li>
 *   <li>Нельзя обновить статус доставленной или возвращённой посылки</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task25_delivery_tracker/DeliveryTrackerService.java
 * java -cp src/main/java com.practice.task25_delivery_tracker.DeliveryTrackerService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Гонка обновления статуса:</b> два update разом → пропуск статуса / невалидный порядок. Атомарный переход + проверка допустимости (строгий FSM) ВНУТРИ критической секции.</li>
 *   <li>Атомарность «статус + событие в историю» (вместе или никак); идемпотентность повторного статуса.</li>
 * </ul>
 */
public class DeliveryTrackerService {

    public enum ParcelStatus {
        CREATED, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, RETURNED
    }

    public record Parcel(
            String id,
            String senderName,
            String recipientName,
            double weight,
            ParcelStatus status
    ) {}

    public record TrackingEvent(
            String parcelId,
            ParcelStatus status,
            String location,
            LocalDateTime timestamp
    ) {}

    /**
     * Создаёт новую посылку со статусом CREATED.
     *
     * @param senderName    имя отправителя
     * @param recipientName имя получателя
     * @param weight        вес посылки в кг
     * @return созданная посылка
     * @throws IllegalArgumentException если weight <= 0 или имена пустые
     */
    public Parcel createParcel(String senderName, String recipientName, double weight) {
        // TODO: implement
        return null;
    }

    /**
     * Обновляет статус посылки. Проверяет допустимость перехода.
     *
     * @param parcelId  идентификатор посылки
     * @param newStatus новый статус
     * @param location  местоположение
     * @return событие отслеживания
     * @throws IllegalStateException    если переход недопустим
     * @throws IllegalArgumentException если посылка не найдена
     */
    public TrackingEvent updateStatus(String parcelId, ParcelStatus newStatus, String location) {
        // TODO: implement
        return null;
    }

    /**
     * Возвращает текущий статус посылки.
     *
     * @param parcelId идентификатор посылки
     * @return текущий статус
     */
    public ParcelStatus getCurrentStatus(String parcelId) {
        // TODO: implement
        return null;
    }

    /**
     * Возвращает полную историю отслеживания посылки, отсортированную по времени.
     *
     * @param parcelId идентификатор посылки
     * @return список событий отслеживания
     */
    public List<TrackingEvent> getTrackingHistory(String parcelId) {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает все посылки указанного получателя.
     *
     * @param recipientName имя получателя
     * @return список посылок
     */
    public List<Parcel> getParcelsByRecipient(String recipientName) {
        // TODO: implement
        return List.of();
    }

    /**
     * Вычисляет среднее время доставки в часах (от CREATED до DELIVERED)
     * для всех доставленных посылок.
     *
     * @return среднее время доставки в часах
     */
    public double getAverageDeliveryTimeHours() {
        // TODO: implement
        return 0.0;
    }

    public static void main(String[] args) {
        System.out.println("=== DeliveryTrackerService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ ВАЛИДНЫЙ ПЕРЕХОД FSM: конкурентные updateStatus → рассинхрон статуса и истории. Переход по матрице
 *    (CREATED→IN_TRANSIT→DELIVERED), compareAndSet(expected→next), назад/через ступень — запрет; смена статуса + запись события атомарно. Тест: недопустимый переход отклонён; история монотонна.
 * 2. ИДЕМПОТЕНТНОЕ ОБНОВЛЕНИЕ: повтор updateStatus тем же статусом (дубль трекинга) — no-op, второе событие не пишется.
 * 3. ГРАНУЛЯРНЫЙ ЛОК НА ПОСЫЛКУ: обновления разных посылок идут параллельно.
 */
