package com.practice.task14_hotel_reservation;

import java.util.*;
import java.time.*;

/**
 * Бронирование отелей.
 *
 * <p>Реализовать in-memory сервис бронирования отелей.
 * Отели имеют комнаты разных типов. Нельзя бронировать на пересекающиеся даты.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task14_hotel_reservation/HotelReservationService.java
 * java -cp src/main/java com.practice.task14_hotel_reservation.HotelReservationService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные бизнес-требования в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Атомарная проверка-и-бронь интервала</b> — защита от double-booking (TOCTOU на пересекающихся датах).</li>
 *   <li><b>2. Идемпотентная бронь</b> — дедупликация повторов запроса по requestId.</li>
 *   <li><b>3. Параллельность разных комнат</b> — разные комнаты не мешают друг другу, одна комната сериализуется.</li>
 * </ul>
 */
public class HotelReservationService {

    public enum RoomType { SINGLE, DOUBLE, SUITE }
    public enum ReservationStatus { CONFIRMED, CANCELLED }

    public record Room(String id, String hotelId, RoomType type, double pricePerNight) {}
    public record Reservation(String id, String roomId, String guestName,
                              LocalDate checkIn, LocalDate checkOut,
                              ReservationStatus status) {}

    /** Добавить отель */
    public String addHotel(String name) {
        //TODO implement
        return null;
    }

    /** Добавить комнату к отелю */
    public void addRoom(Room room) {
        //TODO implement
    }

    /**
     * Забронировать комнату
     * @throws IllegalStateException если даты пересекаются с существующей бронью
     */
    public Reservation reserve(String roomId, String guestName, LocalDate checkIn, LocalDate checkOut) {
        //TODO implement
        return null;
    }

    /** Отменить бронирование */
    public void cancelReservation(String reservationId) {
        //TODO implement
    }

    /** Свободные комнаты на даты */
    public List<Room> findAvailableRooms(String hotelId, LocalDate checkIn, LocalDate checkOut) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Доход отеля за период */
    public double getRevenue(String hotelId, LocalDate from, LocalDate to) {
        //TODO implement
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== HotelReservationService: Smoke Test ===");
        // TODO: создать отель, комнаты, забронировать, проверить пересечения
    }
}

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. АТОМАРНАЯ ПРОВЕРКА-И-БРОНЬ ИНТЕРВАЛА — защита от double-booking ───
 *   Бизнес: одну комнату нельзя сдать двум гостям на пересекающиеся даты.
 *   Контракт: reserve(room, checkIn, checkOut) создаёт бронь, только если интервал НЕ пересекается с
 *     существующими бронями этой комнаты; иначе отказ. Инвариант: у комнаты нет двух броней с
 *     пересекающимися интервалами. Касание границ (выезд одного = заезд другого в тот же день) — НЕ
 *     пересечение.
 *   Ловушка: наивная «проверил, что нет пересечения → вставил» под двумя потоками на одну комнату с
 *     пересекающимися датами создаёт двойную бронь (TOCTOU). ⚠️ В отличие от task13 занятость тут —
 *     не счётчик, а скан интервалов; подумай, чем это ограничивает выбор механизма атомарности.
 *   Проверить: N потоков на ОДНУ комнату с пересекающимися датами → ровно 1 успех; на непересекающиеся — все.
 *
 * ─── 2. ИДЕМПОТЕНТНАЯ БРОНЬ — защита от дублей при повторе запроса ───
 *   Бизнес: гость нажал «забронировать» дважды / повтор из-за сети — одна бронь, не две.
 *   Контракт: reserve принимает requestId (стабильный на намерение); повтор с тем же requestId
 *     возвращает уже созданную бронь.
 *   Ловушка: два запроса с одним requestId приходят одновременно.
 *   Проверить: один requestId дважды (последовательно и параллельно) → ровно один Reservation.
 *
 * ─── 3. ПАРАЛЛЕЛЬНОСТЬ РАЗНЫХ КОМНАТ ───
 *   Бизнес: бронирования РАЗНЫХ комнат не должны мешать друг другу — конкурировать должны только
 *     запросы на ОДНУ комнату.
 *   Контракт: критическая секция из усложнения 1 не должна сериализовать брони разных комнат.
 *   Ловушка: слишком грубая синхронизация (один общий замок на весь сервис) корректна, но душит
 *     пропускную способность — все комнаты в одной очереди.
 *   Проверить: брони разных комнат идут параллельно (не блокируют друг друга); одной комнаты на
 *     пересечение — сериализуются.
 */
