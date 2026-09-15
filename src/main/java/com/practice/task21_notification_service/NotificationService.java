package com.practice.task21_notification_service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * СЕРВИС УВЕДОМЛЕНИЙ
 *
 * <p>Реализуйте сервис управления уведомлениями пользователей.
 * Уведомление (Notification) содержит: id, userId, title, message, type (EMAIL/SMS/PUSH),
 * status (PENDING/SENT/FAILED/READ), createdAt.</p>
 *
 * <p>Необходимо реализовать следующие операции:</p>
 * <ul>
 *   <li>Отправить уведомление (создать со статусом PENDING, затем перевести в SENT)</li>
 *   <li>Пометить уведомление как прочитанное (READ)</li>
 *   <li>Получить список непрочитанных уведомлений пользователя</li>
 *   <li>Получить все уведомления пользователя с пагинацией (page, pageSize)</li>
 *   <li>Удалить старые уведомления (старше N дней)</li>
 *   <li>Подсчитать количество уведомлений по типу для пользователя</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Нельзя пометить как прочитанное уведомление, которое ещё не отправлено (статус не SENT)</li>
 *   <li>userId и title не могут быть пустыми</li>
 *   <li>При повторной отправке FAILED-уведомления статус возвращается в PENDING</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task21_notification_service/NotificationService.java
 * java -cp src/main/java com.practice.task21_notification_service.NotificationService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>Гонка перехода статуса</b> (PENDING→SENT + mark-READ разом); атомарность; idempotent send (не отправить дважды).</li>
 *   <li><b>Дедуп</b> (не слать одинаковое N раз/24ч — реальная Яндекс-тема); <b>async отправка через worker-pool + graceful shutdown</b>; пагинация консистентна под конкурентной вставкой.</li>
 * </ul>
 */
public class NotificationService {

    public enum NotificationType {
        EMAIL, SMS, PUSH
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED, READ
    }

    public record Notification(
            String id,
            String userId,
            String title,
            String message,
            NotificationType type,
            NotificationStatus status,
            LocalDateTime createdAt
    ) {}

    /**
     * Отправляет уведомление пользователю. Создаёт уведомление со статусом PENDING
     * и немедленно переводит в SENT. Возвращает созданное уведомление.
     *
     * @param userId  идентификатор пользователя
     * @param title   заголовок уведомления
     * @param message текст уведомления
     * @param type    тип уведомления (EMAIL, SMS, PUSH)
     * @return созданное уведомление со статусом SENT
     * @throws IllegalArgumentException если userId или title пустые
     */
    public Notification send(String userId, String title, String message, NotificationType type) {
        // TODO: implement
        return null;
    }

    /**
     * Помечает уведомление как прочитанное.
     *
     * @param notificationId идентификатор уведомления
     * @return обновлённое уведомление со статусом READ
     * @throws IllegalArgumentException если уведомление не найдено
     * @throws IllegalStateException    если уведомление ещё не отправлено (статус не SENT)
     */
    public Notification markAsRead(String notificationId) {
        // TODO: implement
        return null;
    }

    /**
     * Возвращает список непрочитанных уведомлений пользователя (статус SENT).
     * Отсортированы по дате создания (новые первыми).
     *
     * @param userId идентификатор пользователя
     * @return список непрочитанных уведомлений
     */
    public List<Notification> getUnread(String userId) {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает все уведомления пользователя с пагинацией.
     *
     * @param userId   идентификатор пользователя
     * @param page     номер страницы (начиная с 0)
     * @param pageSize размер страницы
     * @return список уведомлений на указанной странице
     */
    public List<Notification> getAllPaginated(String userId, int page, int pageSize) {
        // TODO: implement
        return List.of();
    }

    /**
     * Удаляет уведомления старше указанного количества дней.
     *
     * @param days количество дней
     * @return количество удалённых уведомлений
     */
    public int deleteOlderThan(int days) {
        // TODO: implement
        return 0;
    }

    /**
     * Подсчитывает количество уведомлений указанного типа для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param type   тип уведомления
     * @return количество уведомлений данного типа
     */
    public long countByType(String userId, NotificationType type) {
        // TODO: implement
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== NotificationService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. DEDUP В ОКНЕ (идемпотентность): одинаковое уведомление (userId+title+message) в пределах 24ч не слать
 *    повторно; ключ дедупа + отметка времени, протухшие вычищать (окно не растёт). Тест: дубль дважды → одно доставленное; после окна — снова можно.
 * 2. ИНТЕРФЕЙС РЕПОЗИТОРИЯ + IN-MEM СТАБ: вынести хранение за интерфейс NotificationRepository; сервис не зависит
 *    от стораджа; in-mem реализация — стаб (без внешних систем, «БД» подменяется в тестах). Тест: работает с любой реализацией.
 * 3. THREAD-SAFE ОТПРАВКА/ЧТЕНИЕ: конкурентные send/getUnread/markAsRead не теряют и не дают рваных чтений;
 *    генерация id + запись атомарны. Тест: K потоков send → ровно K уведомлений.
 */
