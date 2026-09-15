package com.practice.task15_warehouse;

import java.util.*;

/**
 * Складской учёт.
 *
 * <p>Реализовать in-memory систему складского учёта: приход, расход, остатки,
 * история движения товаров.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task15_warehouse/WarehouseService.java
 * java -cp src/main/java com.practice.task15_warehouse.WarehouseService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить; инвентарная гонка, близко к твоей зоне):</b></p>
 * <ul>
 *   <li><b>⭐⭐ Гонка прихода/расхода:</b> конкурентный расход → остаток в минус / oversell (lost-update). Атомарно: CAS/лок на позицию, проверка-остатка-и-списание под защитой.</li>
 *   <li>Идемпотентность движения (double-apply); консистентность истории с остатком (атомарно: остаток + запись в историю вместе).</li>
 * </ul>
 */
public class WarehouseService {

    public enum Operation { INCOMING, OUTGOING }

    public record Product(String id, String name, String category) {}
    public record StockRecord(String productId, int quantity, Operation operation, long timestamp) {}

    /** Добавить товар в каталог */
    public void addProduct(Product product) {
        //TODO implement
    }

    /**
     * Приход товара
     * @param productId идентификатор товара
     * @param quantity количество (> 0)
     */
    public void incoming(String productId, int quantity) {
        //TODO implement
    }

    /**
     * Расход товара
     * @param productId идентификатор товара
     * @param quantity количество (> 0)
     * @throws IllegalStateException если остаток < quantity
     */
    public void outgoing(String productId, int quantity) {
        //TODO implement
    }

    /** Текущий остаток товара */
    public int getStock(String productId) {
        //TODO implement
        return 0;
    }

    /** История движения товара */
    public List<StockRecord> getHistory(String productId) {
        //TODO implement
        return Collections.emptyList();
    }

    /** Товары с остатком ниже минимума */
    public List<Product> getLowStock(int minQuantity) {
        //TODO implement
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("=== WarehouseService: Smoke Test ===");
        // TODO: добавить товары, приход/расход, проверить остатки
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНЫЙ CHECK-AND-DECREMENT (против oversell): наивное outgoing «проверил stock>=qty → списал» уводит
 *    остаток в минус. Списание — единая атомарная операция «если хватает → вычесть» (compute/CAS-петля/лок на товар).
 *    Тест: stock=M, N>M потоков outgoing(1) → ровно M успешных, getStock()==0, без минуса.
 * 2. ИДЕМПОТЕНТНАЯ ОПЕРАЦИЯ (opId): ретрай incoming/outgoing с тем же opId не применяет движение дважды.
 *    Тест: outgoing(opId,5) дважды → остаток −5 не −10; в истории одна запись opId.
 * 3. LOCK-ORDERING ПЕРЕВОД МЕЖДУ СКЛАДАМИ: transfer(A→B) и (B→A) → локи складов в ЕДИНОМ порядке (по id),
 *    перевод атомарен (всё или ничего). Тест: встречные переводы не зависают; суммарный остаток — инвариант.
 */
