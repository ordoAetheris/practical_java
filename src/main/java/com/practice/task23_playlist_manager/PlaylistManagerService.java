package com.practice.task23_playlist_manager;

import java.util.*;

/**
 * МЕНЕДЖЕР ПЛЕЙЛИСТОВ
 *
 * <p>Реализуйте сервис управления музыкальными плейлистами. Каждый плейлист содержит
 * упорядоченный список треков. Треки можно добавлять, удалять, перемещать и перемешивать.</p>
 *
 * <p>Сущности:</p>
 * <ul>
 *   <li>Track — трек (id, title, artist, durationSeconds)</li>
 *   <li>Playlist — плейлист (id, name, ownerId, упорядоченный список треков)</li>
 * </ul>
 *
 * <p>Операции:</p>
 * <ul>
 *   <li>Создать плейлист</li>
 *   <li>Добавить трек в плейлист (в конец)</li>
 *   <li>Удалить трек из плейлиста</li>
 *   <li>Переместить трек на другую позицию</li>
 *   <li>Перемешать треки (shuffle)</li>
 *   <li>Получить общую длительность плейлиста</li>
 *   <li>Поиск треков по исполнителю (во всех плейлистах)</li>
 * </ul>
 *
 * <p>Бизнес-правила:</p>
 * <ul>
 *   <li>Трек не может дублироваться в одном плейлисте (проверка по id)</li>
 *   <li>Имя плейлиста не может быть пустым</li>
 *   <li>Длительность трека должна быть положительной</li>
 * </ul>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task23_playlist_manager/PlaylistManagerService.java
 * java -cp src/main/java com.practice.task23_playlist_manager.PlaylistManagerService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — полные условия в блоке внизу файла):</b></p>
 * <ul>
 *   <li><b>1. Атомарная модификация списка</b> — конкурентные add/remove/move не рвут порядок и не теряют треки.</li>
 *   <li><b>2. Идемпотентные add/remove</b> — повтор добавления не дублирует, удаление отсутствующего — no-op.</li>
 *   <li><b>3. Безопасное удаление под обходом</b> — remove во время чтения без ConcurrentModificationException.</li>
 * </ul>
 */
public class PlaylistManagerService {

    public record Track(
            String id,
            String title,
            String artist,
            int durationSeconds
    ) {}

    public record Playlist(
            String id,
            String name,
            String ownerId,
            List<Track> tracks
    ) {}

    /**
     * Создаёт новый пустой плейлист.
     *
     * @param name    название плейлиста
     * @param ownerId идентификатор владельца
     * @return созданный плейлист
     * @throws IllegalArgumentException если name пустое
     */
    public Playlist createPlaylist(String name, String ownerId) {
        // TODO: implement
        return null;
    }

    /**
     * Добавляет трек в конец плейлиста.
     *
     * @param playlistId идентификатор плейлиста
     * @param track      трек для добавления
     * @throws IllegalArgumentException если плейлист не найден
     * @throws IllegalStateException    если трек уже есть в плейлисте
     */
    public void addTrack(String playlistId, Track track) {
        // TODO: implement
    }

    /**
     * Удаляет трек из плейлиста.
     *
     * @param playlistId идентификатор плейлиста
     * @param trackId    идентификатор трека
     * @throws IllegalArgumentException если плейлист или трек не найден
     */
    public void removeTrack(String playlistId, String trackId) {
        // TODO: implement
    }

    /**
     * Перемещает трек на указанную позицию в плейлисте.
     *
     * @param playlistId идентификатор плейлиста
     * @param trackId    идентификатор трека
     * @param newPosition новая позиция (0-based)
     * @throws IllegalArgumentException если позиция вне допустимого диапазона
     */
    public void moveTrack(String playlistId, String trackId, int newPosition) {
        // TODO: implement
    }

    /**
     * Перемешивает треки в плейлисте в случайном порядке.
     *
     * @param playlistId идентификатор плейлиста
     */
    public void shuffle(String playlistId) {
        // TODO: implement
    }

    /**
     * Возвращает общую длительность плейлиста в секундах.
     *
     * @param playlistId идентификатор плейлиста
     * @return общая длительность в секундах
     */
    public int getTotalDuration(String playlistId) {
        // TODO: implement
        return 0;
    }

    /**
     * Ищет треки по исполнителю во всех плейлистах (регистронезависимый поиск).
     *
     * @param artist имя исполнителя
     * @return список уникальных треков данного исполнителя
     */
    public List<Track> findByArtist(String artist) {
        // TODO: implement
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println("=== PlaylistManagerService: Smoke Test ===");
        // TODO: создать тестовые данные и проверить основные сценарии
    }
}

/* ═══════════════════════ УСЛОЖНЕНИЯ (реализовать ИЗНУТРИ этого класса) ═══════════════════════
 *
 * Три НЕЗАВИСИМЫХ усложнения. Здесь — ЗАДАЧА, КОНТРАКТ и в чём ловушка. Способ решения и тесты — сам.
 *
 * ─── 1. АТОМАРНАЯ МОДИФИКАЦИЯ УПОРЯДОЧЕННОГО СПИСКА ───
 *   Бизнес: плейлист — упорядоченный список треков. Параллельные добавление, удаление и перемещение
 *     не должны рвать порядок или терять треки.
 *   Контракт: add / remove / moveTrack над одним плейлистом выполняются как цельные операции; moveTrack
 *     переставляет трек без промежуточного состояния, где он задублирован или пропал. Инвариант:
 *     позиции треков всегда образуют непрерывный ряд 0..n-1 без дыр и дублей.
 *   Ловушка: конкурентные операции над списком сдвигают индексы — трек встаёт не на ту позицию, или
 *     параллельное удаление и вставка теряют элемент.
 *   Проверить: после массовых параллельных операций позиции — это перестановка 0..n-1 без дыр и дублей.
 *
 * ─── 2. ИДЕМПОТЕНТНЫЕ ДОБАВЛЕНИЕ И УДАЛЕНИЕ ───
 *   Бизнес: трек не может дублироваться в плейлисте; повтор запроса не должен это ломать.
 *   Контракт: повторный addTrack того же трека не создаёт дубль; removeTrack отсутствующего трека —
 *     no-op (не ошибка). Инвариант: один трек присутствует в плейлисте не более одного раза.
 *   Ловушка: два запроса на добавление одного трека приходят одновременно — наивная «проверил, что
 *     трека нет → добавил» создаст дубль.
 *   Проверить: addTrack одного трека дважды → в плейлисте он один; removeTrack отсутствующего — без ошибки.
 *
 * ─── 3. БЕЗОПАСНОЕ УДАЛЕНИЕ ВО ВРЕМЯ ОБХОДА ───
 *   Бизнес: удаление трека не должно ломать одновременное чтение плейлиста (подсчёт длительности, поиск).
 *   Контракт: removeTrack во время обхода (getTotalDuration / findByArtist) не приводит к
 *     ConcurrentModificationException и не обращается к уже удалённому треку.
 *   Ловушка: модификация коллекции во время итерации по ней.
 *   Проверить: удаление параллельно с обходом не падает и не читает удалённый трек.
 */
