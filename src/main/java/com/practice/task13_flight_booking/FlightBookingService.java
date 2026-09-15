package com.practice.task13_flight_booking;

import java.util.*;
import java.time.*;

/**
 * Бронирование авиабилетов.
 *
 * <p>Реализовать in-memory сервис бронирования авиабилетов.
 * Поддержка рейсов, бронирования мест, отмены, поиска рейсов.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task13_flight_booking/FlightBookingService.java
 * java -cp src/main/java com.practice.task13_flight_booking.FlightBookingService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Конкурентная бронь последнего места → overbooking:</b> атомарно занять место (CAS счётчика/статуса); TOCTOU «проверил свободно → занял».</li>
 *   <li>Expiry брони; идемпотентность бронирования; гонка cancel+book.</li>
 * </ul>
 */
public class FlightBookingService {

    public enum BookingStatus { CONFIRMED, CANCELLED }

    public record Flight(String id, String from, String to,
                         LocalDateTime departureTime, LocalDateTime arrivalTime,
                         int totalSeats, double price) {}
    public record Booking(String id, String flightId, String passengerName,
                          int seatNumber, BookingStatus status) {}

    /** Добавить рейс */
    public void addFlight(Flight flight) {
        //TODO implement
    }

    /**
     * Забронировать место на рейс
     * @param flightId идентификатор рейса
     * @param passengerName имя пассажира
     * @return бронь
     * @throws IllegalStateException если нет свободных мест
     */
    public Booking bookFlight(String flightId, String passengerName) {
        //TODO implement
        return null;
    }

    /**
     * Отменить бронирование
     * @throws IllegalStateException если уже отменено
     */
    public void cancelBooking(String bookingId) {
        //TODO implement
    }

    /** Найти рейсы по маршруту */
    public List<Flight> findFlights(String from, String to) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Найти рейсы на дату */
    public List<Flight> findFlightsByDate(LocalDate date) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Количество свободных мест на рейс */
    public int getAvailableSeats(String flightId) {
        //TODO implement
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== FlightBookingService: Smoke Test ===");
        // TODO: создать рейсы, забронировать, проверить
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНОЕ СПИСАНИЕ МЕСТА (против oversell): наивная «проверка мест → бронь» на последнее место продаёт
 *    больше, чем есть. availableSeats уменьшать как ЕДИНУЮ операцию check(>0)-and-decrement (AtomicInteger+CAS /
 *    лок на рейс / compute). Тест: seats=M, N>M потоков → ровно M броней, счётчик не в минус.
 * 2. ИДЕМПОТЕНТНАЯ БРОНЬ (requestId): повтор bookFlight с тем же requestId → та же бронь, второе место не занимает.
 *    Тест: bookFlight(requestId) дважды → один Booking, мест −1 не −2.
 * 3. ГРАНУЛЯРНЫЙ ЛОК + HOLD-EXPIRY: лок на конкретный рейс (брони РАЗНЫХ рейсов параллельны); hold() — удержать
 *    место с TTL, не подтверждено вовремя → авто-release. Тест: брони разных рейсов не сериализуются; протухший hold освобождает место.
 */
