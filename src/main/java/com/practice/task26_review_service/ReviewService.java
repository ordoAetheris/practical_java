package com.practice.task26_review_service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * СЕРВИС ОТЗЫВОВ
 *
 * <p>Реализуйте сервис управления отзывами на товары. Пользователи оставляют отзывы
 * с рейтингом, отзывы проходят модерацию, на основе рейтингов формируется топ продуктов.</p>
 *
 * <p>Сущности:</p>
 * <ul>
 *   <li>Review — отзыв (id, productId, userId, rating 1-5, text, createdAt, status ACTIVE/MODERATED/DELETED)</li>
 * </ul>
 *
 * <p>Операции:</p>
 * <ul>
 *   <li>Оставить отзыв на продукт</li>
 *   <li>Удалить отзыв (мягкое удаление — перевод в статус DELETED)</li>
 *   <li>Отправить на модерацию</li>
 *   <li>Средний рейтинг продукта (только ACTIVE отзывы)</li>
 *   <li>Отзывы по продукту с сортировкой (по дате или рейтингу)</li>
 *   <li>Отзывы пользователя</li>
 *   <li>Топ-N продуктов по среднему рейтингу</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Один пользователь может оставить только один отзыв на продукт</li>
 *   <li>Рейтинг строго от 1 до 5 (целое число)</li>
 *   <li>Текст отзыва не может быть пустым</li>
 *   <li>В расчёт среднего рейтинга идут только ACTIVE отзывы</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task26_review_service/ReviewService.java
 * java -cp src/main/java com.practice.task26_review_service.ReviewService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ One-review-per-user под конкуррентой</b> (два отзыва при гонке — putIfAbsent на (product,user)); <b>гонка агрегата рейтинга</b> (concurrent reviews → lost-update среднего) — инкрементальный consistent-подсчёт под защитой.</li>
 *   <li>Эффективный Top-N по среднему (heap, не полная сортировка); пересчёт среднего при смене статуса ACTIVE↔DELETED.</li>
 * </ul>
 */
public class ReviewService {

    public enum ReviewStatus {
        ACTIVE, MODERATED, DELETED
    }

    public enum SortBy {
        DATE_ASC, DATE_DESC, RATING_ASC, RATING_DESC
    }

    public record Review(
            String id,
            String productId,
            String userId,
            int rating,
            String text,
            LocalDateTime createdAt,
            ReviewStatus status
    ) {}

    public record ProductRating(
            String productId,
            double averageRating,
            int reviewCount
    ) {}

    /**
     * Оставляет отзыв на продукт.
     *
     * @param productId идентификатор продукта
     * @param userId    идентификатор пользователя
     * @param rating    рейтинг (1-5)
     * @param text      текст отзыва
     * @return созданный отзыв
     * @throws IllegalArgumentException если rating не в диапазоне 1-5 или text пустой
     * @throws IllegalStateException    если пользователь уже оставил отзыв на этот продукт
     */
    public Review addReview(String productId, String userId, int rating, String text) {
        // TODO: implement
        return null;
    }

    /**
     * Мягкое удаление отзыва (перевод в статус DELETED).
     *
     * @param reviewId идентификатор отзыва
     * @throws IllegalArgumentException если отзыв не найден
     */
    public void deleteReview(String reviewId) {
        // TODO: implement
    }

    /**
     * Отправляет отзыв на модерацию.
     *
     * @param reviewId идентификатор отзыва
     * @throws IllegalArgumentException если отзыв не найден
     */
    public void moderate(String reviewId) {
        // TODO: implement
    }

    /**
     * Возвращает средний рейтинг продукта (только ACTIVE отзывы).
     *
     * @param productId идентификатор продукта
     * @return средний рейтинг или 0.0 если отзывов нет
     */
    public double getAverageRating(String productId) {
        // TODO: implement
        return 0.0;
    }

    /**
     * Возвращает отзывы по продукту с указанной сортировкой (только ACTIVE).
     *
     * @param productId идентификатор продукта
     * @param sortBy    критерий сортировки
     * @return отсортированный список отзывов
     */
    public List<Review> getProductReviews(String productId, SortBy sortBy) {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает все отзывы пользователя (все статусы).
     *
     * @param userId идентификатор пользователя
     * @return список отзывов пользователя
     */
    public List<Review> getUserReviews(String userId) {
        // TODO: implement
        return List.of();
    }

    /**
     * Возвращает топ-N продуктов по среднему рейтингу (только ACTIVE отзывы).
     *
     * @param n количество продуктов в топе
     * @return список ProductRating, отсортированный по убыванию рейтинга
     */
    public List<ProductRating> getTopProducts(int n) {
        // TODO: implement
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println("=== ReviewService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ ОДИН-ОТЗЫВ-НА-ЮЗЕРА: один userId из двух потоков → «не оставлял → добавить» создаёт два. Резерв факта
 *    атомарно (putIfAbsent). Тест: N потоков одним userId → ровно один отзыв.
 * 2. СОГЛАСОВАННОЕ СРЕДНЕЕ (running-average): getAverageRating под конкурентными add/delete не должен «рвать» sum и
 *    count; поддерживать sum+count атомарно/консистентно. Тест: K add с известными оценками → среднее точное; delete согласован.
 * 3. ИДЕМПОТЕНТНЫЙ ОТЗЫВ: повтор (userId,productId) — обновление существующего, не второй отзыв; среднее пересчитать (старую оценку на новую).
 * 4. ГРАНУЛЯРНЫЙ ЛОК/АГРЕГАТ НА ТОВАР: отзывы на разные товары идут параллельно; средние независимы.
 */
