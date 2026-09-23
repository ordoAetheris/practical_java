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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Один голос на пользователя</b> — защита от двойного голоса при конкуренции.</li>
 *   <li><b>2. Идемпотентный голос</b> — повтор голоса тем же пользователем не учитывается дважды.</li>
 *   <li><b>3. Потокобезопасный подсчёт</b> — голоса за разные варианты считаются параллельно, результат согласован.</li>
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

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. ОДИН ГОЛОС НА ПОЛЬЗОВАТЕЛЯ ───
 *   Бизнес: каждый пользователь может проголосовать в опросе только один раз.
 *   Контракт: vote(userId, pollId, option) засчитывает голос, только если этот userId ещё не голосовал
 *     в этом опросе; повторный голос отклоняется. Инвариант: на пару (опрос, пользователь) — не более
 *     одного учтённого голоса.
 *   Ловушка: один userId голосует из двух потоков одновременно — наивная «проверил, что не голосовал →
 *     учёл» засчитает оба голоса (TOCTOU).
 *   Проверить: несколько потоков голосуют одним userId → засчитан ровно один голос, остальные отклонены.
 *
 * ─── 2. ИДЕМПОТЕНТНЫЙ ГОЛОС ───
 *   Бизнес: повтор запроса на голос (ретрай, двойной клик) тем же пользователем не должен добавить
 *     второй голос.
 *   Контракт: повторный vote тем же userId — no-op (результат тот же, счётчик не растёт второй раз).
 *   Ловушка: два запроса с одним userId приходят одновременно.
 *   Проверить: vote тем же userId дважды → в результатах один голос этого пользователя.
 *
 * ─── 3. ПОТОКОБЕЗОПАСНЫЙ ПОДСЧЁТ ГОЛОСОВ ───
 *   Бизнес: при массовом голосовании итоговые числа по вариантам должны быть точными, без потерь.
 *   Контракт: голоса за РАЗНЫЕ варианты учитываются параллельно; getResults возвращает согласованный
 *     снимок. Инвариант: сумма голосов по вариантам равна числу принятых голосов.
 *   Ловушка: наивный инкремент счётчика варианта под конкуренцией теряет голоса (lost update на
 *     счётчике — классическая counter-race).
 *   Проверить: массовое голосование → сумма счётчиков по вариантам равна числу принятых голосов.
 */
