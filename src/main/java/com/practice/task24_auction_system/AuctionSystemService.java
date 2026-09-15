package com.practice.task24_auction_system;

import java.time.LocalDateTime;
import java.util.*;

/**
 * СИСТЕМА АУКЦИОНОВ
 *
 * <p>Реализуйте систему онлайн-аукциона. Продавцы выставляют лоты,
 * покупатели делают ставки, аукцион завершается и определяется победитель.</p>
 *
 * <p>Сущности:</p>
 * <ul>
 *   <li>Lot — лот (id, title, description, startPrice, currentPrice, sellerId, status ACTIVE/SOLD/EXPIRED)</li>
 *   <li>Bid — ставка (id, lotId, bidderId, amount, timestamp)</li>
 * </ul>
 *
 * <p>Операции:</p>
 * <ul>
 *   <li>Создать лот с начальной ценой</li>
 *   <li>Сделать ставку на лот</li>
 *   <li>Завершить аукцион по лоту (определить победителя)</li>
 *   <li>Получить список активных лотов</li>
 *   <li>Получить историю ставок по лоту</li>
 *   <li>Получить лоты пользователя (как продавца)</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Ставка должна быть строго больше текущей цены лота</li>
 *   <li>Продавец не может делать ставки на свой собственный лот</li>
 *   <li>Нельзя делать ставку на завершённый или истёкший лот</li>
 *   <li>Начальная цена должна быть положительной</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task24_auction_system/AuctionSystemService.java
 * java -cp src/main/java com.practice.task24_auction_system.AuctionSystemService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — интервьюер додавливает, дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Конкурентные ставки (thread-safety):</b> база требует «ставка &gt; текущей», но под N потоками это LOST-UPDATE:
 *       двое читают currentPrice=100, оба ставят 110, один затирается. Атомарно: CAS/лок на лот, проверка-и-запись под защитой. ← класс биллер-гонки, твоя зона.</li>
 *   <li><b>Закрытие под гонкой:</b> ставка в момент finish() — принять или отклонить (граница).</li>
 *   <li><b>Идемпотентность</b> повторной ставки (double-submit); <b>anti-sniping</b> (продлить при ставке в последние секунды).</li>
 * </ul>
 */
public class AuctionSystemService {

    public enum LotStatus {
        ACTIVE, SOLD, EXPIRED
    }

    public record Lot(
            String id,
            String title,
            String description,
            double startPrice,
            double currentPrice,
            String sellerId,
            LotStatus status
    ) {}

    public record Bid(
            String id,
            String lotId,
            String bidderId,
            double amount,
            LocalDateTime timestamp
    ) {}

    /**
     * Создаёт новый лот на аукционе.
     *
     * @param title       название лота
     * @param description описание лота
     * @param startPrice  начальная цена
     * @param sellerId    идентификатор продавца
     * @return созданный лот со статусом ACTIVE
     * @throws IllegalArgumentException если startPrice <= 0 или title пустой
     */
    public Lot createLot(String title, String description, double startPrice, String sellerId) {
        // TODO: implement
        return null;
    }

    /**
     * Делает ставку на лот.
     *
     * @param lotId    идентификатор лота
     * @param bidderId идентификатор участника
     * @param amount   сумма ставки
     * @return сделанная ставка
     * @throws IllegalArgumentException если лот не найден или ставка <= текущей цены
     * @throws IllegalStateException    если лот не активен или bidderId == sellerId
     */
    public Bid placeBid(String lotId, String bidderId, double amount) {
        // TODO: implement
        return null;
    }

    /**
     * Завершает аукцион по лоту. Если были ставки — статус SOLD, иначе EXPIRED.
     *
     * @param lotId идентификатор лота
     * @return обновлённый лот
     * @throws IllegalStateException если лот уже завершён
     */
    public Lot finishAuction(String lotId) {
        // TODO: implement
        return null;
    }

    /**
     * Возвращает список всех активных лотов.
     *
     * @return список активных лотов
     */
    public List<Lot> getActiveLots() {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает историю ставок по лоту, отсортированную по времени (новые первыми).
     *
     * @param lotId идентификатор лота
     * @return список ставок
     */
    public List<Bid> getBidHistory(String lotId) {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает все лоты указанного продавца.
     *
     * @param sellerId идентификатор продавца
     * @return список лотов продавца
     */
    public List<Lot> getLotsBySeller(String sellerId) {
        // TODO: implement
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println("=== AuctionSystemService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНОЕ ОБНОВЛЕНИЕ МАКСИМАЛЬНОЙ СТАВКИ: параллельные placeBid «read max→compare→write» теряют ставки.
 *    «ставка>max → принять и стать новым max» единой атомарной операцией (CAS/лок на лот). Тест: N растущих ставок → highest == реальному максимуму; меньшие/равные отклонены.
 * 2. ГОНКА НА ЗАКРЫТИИ (bid vs finish): ставка ПОСЛЕ закрытия не учитывается, ДО — учитывается; согласованная граница.
 *    Тест: победитель определён по ставкам строго до закрытия, поздние отклонены.
 * 3. ANTI-SNIPE: ставка в последние T сек продлевает дедлайн на T; продление и приём ставки согласованы под конкурентой.
 *    Тест: серия поздних ставок отодвигает дедлайн; закрытие только после «тихого» окна.
 */
