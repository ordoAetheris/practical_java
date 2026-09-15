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
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить):</b></p>
 * <ul>
 *   <li><b>⭐ Гонка на упорядоченном списке:</b> конкурентные add/remove/move → сдвиг индексов, некорректная позиция. Атомарность операции над плейлистом (лок на плейлист); dup-check (по id) под защитой.</li>
 *   <li>Индекс artist→tracks для поиска во всех плейлистах (не скан); shuffle детерминизм в тесте (seed).</li>
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

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНАЯ МОДИФИКАЦИЯ СПИСКА: параллельные add/remove/moveTrack → рассинхрон позиций/потери треков. Под
 *    защитой; moveTrack — атомарный сдвиг без промежуточного дубля/пропуска. Тест: позиции — перестановка 0..n-1 без дыр/дублей.
 * 2. ИДЕМПОТЕНТНЫЕ ADD/REMOVE: повтор addTrack — не дублирует; removeTrack отсутствующего — no-op.
 * 3. SAFE-DELETE ПОД ОБХОДОМ: removeTrack во время обхода (getTotalDuration/findByArtist) → без ConcurrentModificationException
 *    и без обращения к удалённому (снимок для чтения / конкурентная коллекция).
 */
