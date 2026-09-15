package com.practice.task18_voting_system;

import java.util.*;
import java.time.*;

/**
 * Система голосования.
 *
 * <p>Реализовать in-memory систему голосования: создание опросов,
 * голосование, подсчёт результатов, управление жизненным циклом.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task18_voting_system/VotingSystemService.java
 * java -cp src/main/java com.practice.task18_voting_system.VotingSystemService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐⭐ Гонка счётчика голосов:</b> конкурентные голоса → lost-update на count (классика counter-race). Атомарно: AtomicLong / CAS / лок на вариант.</li>
 *   <li><b>One-vote-per-user под конкуррентой</b> (двойной голос при гонке — putIfAbsent на (poll,user)); гонка голос+закрытие опроса (граница).</li>
 * </ul>
 */
public class VotingSystemService {

    public record Poll(String id, String question, List<String> options,
                       LocalDateTime startDate, LocalDateTime endDate, boolean closed) {}
    public record Vote(String userId, String pollId, int optionIndex, LocalDateTime timestamp) {}

    /**
     * Создать голосование
     * @param question вопрос
     * @param options варианты ответов (минимум 2)
     * @param endDate дата окончания
     */
    public String createPoll(String question, List<String> options, LocalDateTime endDate) {
        //TODO implement
        return null;
    }

    /**
     * Проголосовать
     * @throws IllegalStateException если уже голосовал, голосование закрыто или просрочено
     */
    public void vote(String userId, String pollId, int optionIndex) {
        //TODO implement
    }

    /** Результаты: индекс варианта -> количество голосов */
    public Map<Integer, Long> getResults(String pollId) {
        //TODO implement
        return Collections.emptyMap();
    }

    /** Закрыть голосование вручную */
    public void closePoll(String pollId) {
        //TODO implement
    }

    /** Активные голосования */
    public List<Poll> getActivePolls() {
        //TODO implement
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("=== VotingSystemService: Smoke Test ===");
        // TODO: создать опрос, проголосовать, проверить результаты
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ ОДИН-ГОЛОС-НА-ЮЗЕРА (против двойного голоса): один userId из двух потоков → наивная «не голосовал →
 *    учесть» засчитывает два. Резерв факта «userId проголосовал» атомарно (Set/Map.putIfAbsent); инкрементит счётчик
 *    ТОЛЬКО победивший. Тест: N потоков одним userId → счётчик +1, повторные отклонены.
 * 2. ИДЕМПОТЕНТНЫЙ ГОЛОС: повтор vote тем же userId — no-op; опц. revote (старая опция −1, новая +1, инвариант «одна запись на юзера»).
 * 3. THREAD-SAFE ПОДСЧЁТ / ГРАНУЛЯРНЫЕ СЧЁТЧИКИ: голоса за РАЗНЫЕ опции инкрементятся параллельно (атомарные счётчики
 *    на опцию, не общий лок); getResults — согласованный снимок. Тест: массовое голосование → сумма счётчиков == принятых голосов.
 */
