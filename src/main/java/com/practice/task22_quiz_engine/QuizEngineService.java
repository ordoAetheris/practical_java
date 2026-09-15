package com.practice.task22_quiz_engine;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ДВИЖОК ТЕСТОВ (QUIZ ENGINE)
 *
 * <p>Реализуйте движок для проведения тестов/квизов. Система состоит из трёх основных сущностей:</p>
 * <ul>
 *   <li>Quiz — тест, содержащий список вопросов</li>
 *   <li>Question — вопрос с вариантами ответов и индексом правильного</li>
 *   <li>QuizAttempt — попытка прохождения теста пользователем</li>
 * </ul>
 *
 * <p>Необходимо реализовать:</p>
 * <ul>
 *   <li>Создать тест с вопросами</li>
 *   <li>Начать попытку прохождения</li>
 *   <li>Ответить на вопрос в рамках попытки</li>
 *   <li>Завершить попытку и подсчитать результат</li>
 *   <li>Получить статистику по тесту: средний балл, процент прохождения (>= 60% правильных)</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Нельзя ответить на вопрос после завершения попытки</li>
 *   <li>Нельзя начать попытку на тест без вопросов</li>
 *   <li>Индекс правильного ответа должен быть в пределах списка вариантов</li>
 *   <li>Один пользователь может проходить тест несколько раз</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task22_quiz_engine/QuizEngineService.java
 * java -cp src/main/java com.practice.task22_quiz_engine.QuizEngineService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>Гонка answer + finish:</b> ответ приходит в момент завершения попытки → атомарность (после finish ответы отклоняются под защитой).</li>
 *   <li>Thread-safety состояния попытки; консистентный расчёт статистики под конкурентными попытками; таймер на попытку (авто-finish по времени).</li>
 * </ul>
 */
public class QuizEngineService {

    public record Question(
            String id,
            String text,
            List<String> options,
            int correctOptionIndex
    ) {}

    public record Quiz(
            String id,
            String title,
            List<Question> questions
    ) {}

    public record QuizAttempt(
            String id,
            String quizId,
            String userId,
            Map<String, Integer> answers,
            int score,
            LocalDateTime startedAt,
            LocalDateTime finishedAt
    ) {}

    public record QuizStats(
            double averageScore,
            double passRate,
            int totalAttempts
    ) {}

    /**
     * Создаёт новый тест с указанными вопросами.
     *
     * @param title     название теста
     * @param questions список вопросов
     * @return созданный тест
     * @throws IllegalArgumentException если список вопросов пуст или title пустой
     */
    public Quiz createQuiz(String title, List<Question> questions) {
        // TODO: implement
        return null;
    }

    /**
     * Начинает попытку прохождения теста.
     *
     * @param quizId идентификатор теста
     * @param userId идентификатор пользователя
     * @return новая попытка прохождения
     * @throws IllegalArgumentException если тест не найден
     * @throws IllegalStateException    если у теста нет вопросов
     */
    public QuizAttempt startAttempt(String quizId, String userId) {
        // TODO: implement
        return null;
    }

    /**
     * Записывает ответ пользователя на вопрос.
     *
     * @param attemptId   идентификатор попытки
     * @param questionId  идентификатор вопроса
     * @param optionIndex индекс выбранного варианта
     * @throws IllegalStateException если попытка уже завершена
     * @throws IllegalArgumentException если вопрос не принадлежит тесту
     */
    public void answer(String attemptId, String questionId, int optionIndex) {
        // TODO: implement
    }

    /**
     * Завершает попытку и подсчитывает результат.
     *
     * @param attemptId идентификатор попытки
     * @return завершённая попытка с подсчитанным баллом
     * @throws IllegalStateException если попытка уже завершена
     */
    public QuizAttempt finishAttempt(String attemptId) {
        // TODO: implement
        return null;
    }

    /**
     * Возвращает статистику по тесту: средний балл, процент прохождения (>= 60%),
     * общее количество попыток.
     *
     * @param quizId идентификатор теста
     * @return статистика по тесту
     */
    public QuizStats getStats(String quizId) {
        // TODO: implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== QuizEngineService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНОЕ ЗАВЕРШЕНИЕ ПОПЫТКИ: конкурентные answer()/finishAttempt() → гонка на состояние. finish переводит
 *    IN_PROGRESS→FINISHED ровно раз (CAS), после finish ответы не принимаются, балл считается однажды. Тест: параллельные answer+finish → согласованный набор; два finish → один балл.
 * 2. ИДЕМПОТЕНТНЫЙ ОТВЕТ/ЗАВЕРШЕНИЕ: повтор answer на тот же вопрос — перезапись, не второй засчёт; повтор finish — тот же результат.
 * 3. ГРАНУЛЯРНЫЙ ЛОК НА ПОПЫТКУ: разные попытки/юзеры проходят квиз параллельно.
 */
