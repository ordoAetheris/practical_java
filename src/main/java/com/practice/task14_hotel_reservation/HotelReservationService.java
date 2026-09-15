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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Double-booking на пересекающиеся даты:</b> база «нельзя пересечение», но под 2 потоками разом → оба забронировали. Атомарность проверки-доступности+брони (TOCTOU) под локом на комнату.</li>
 *   <li>Корректное пересечение диапазонов дат (interval overlap); идемпотентность.</li>
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

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНАЯ ПРОВЕРКА-И-ВСТАВКА ИНТЕРВАЛА (против double-booking): два потока на ОДИН номер на пересекающиеся
 *    даты → наивная «проверка занятости → вставка» создаёт двойную бронь. Проверку «нет пересечения» + вставку
 *    делать под ОДНИМ локом на КОМНАТУ (единая критическая секция). + корректный overlap диапазонов дат.
 *    Тест: N потоков на пересекающиеся даты одной комнаты → ровно 1 успех; непересекающиеся проходят.
 * 2. ИДЕМПОТЕНТНАЯ БРОНЬ (requestId): повтор reserve с тем же requestId → та же бронь.
 * 3. ГРАНУЛЯРНЫЙ ЛОК НА КОМНАТУ: разные комнаты бронируются параллельно; конкуренты на одну комнату — сериализуются.
 */
