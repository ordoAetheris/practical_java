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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Корректный переход статуса</b> — строгий порядок статусов, запрет пропусков под конкуренцией.</li>
 *   <li><b>2. Идемпотентное обновление</b> — повтор того же статуса не пишет второе событие.</li>
 *   <li><b>3. Параллельность разных посылок</b> — обновления разных посылок не мешают друг другу.</li>
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

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. КОРРЕКТНЫЙ ПЕРЕХОД СТАТУСА (строгий порядок) ───
 *   Бизнес: посылка проходит статусы строго по порядку (CREATED → PICKED_UP → IN_TRANSIT →
 *     OUT_FOR_DELIVERY → DELIVERED; из IN_TRANSIT возможен RETURNED). Пропускать ступени нельзя, назад
 *     ходить нельзя.
 *   Контракт: updateStatus разрешает переход только из ожидаемого текущего статуса; смена статуса и
 *     запись события в историю происходят вместе (либо и то, и другое, либо ничего). Инвариант: статус
 *     посылки и её история согласованы, недопустимых переходов нет.
 *   Ловушка: два updateStatus приходят одновременно — между чтением текущего статуса и записью нового
 *     он мог измениться, и получается пропуск ступени или рассинхрон статуса с историей.
 *   Проверить: недопустимый переход отклонён; история статусов монотонна (без скачков и откатов).
 *
 * ─── 2. ИДЕМПОТЕНТНОЕ ОБНОВЛЕНИЕ ───
 *   Бизнес: повтор того же статуса (дубль трекингового события) не должен добавлять второе событие.
 *   Контракт: повторный updateStatus тем же статусом — no-op (второе событие не пишется).
 *   Ловушка: два запроса на один и тот же статус приходят одновременно.
 *   Проверить: updateStatus одним статусом дважды → в истории одно событие.
 *
 * ─── 3. ПАРАЛЛЕЛЬНОСТЬ РАЗНЫХ ПОСЫЛОК ───
 *   Бизнес: обновления РАЗНЫХ посылок не должны мешать друг другу — конкурировать могут только
 *     обновления ОДНОЙ посылки.
 *   Контракт: критическая секция обновления не должна сериализовать разные посылки.
 *   Ловушка: один общий замок на весь сервис корректен, но все посылки встают в одну очередь.
 *   Проверить: обновления разных посылок идут параллельно; одной посылки — сериализуются.
 */
