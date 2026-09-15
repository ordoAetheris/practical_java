package com.practice.task12_bank_account;

import java.util.*;

/**
 * Банковские счета и переводы.
 *
 * <p>Реализовать in-memory банковский сервис с поддержкой создания счетов,
 * пополнения, снятия, переводов между счетами и истории транзакций.</p>
 *
 * <p>Уровень: Базовый</p>
 *
 * <h3>Запуск из терминала:</h3>
 * <pre>
 * javac src/main/java/com/practice/task12_bank_account/BankAccountService.java
 * java -cp src/main/java com.practice.task12_bank_account.BankAccountService
 * </pre>
 *
 * <p><b>УСЛОЖНЕНИЯ (сверх базы — дрилить; ЭТО КЛАССИКА ГОНОК, твоя зона):</b></p>
 * <ul>
 *   <li><b>⭐⭐ Конкурентные debit/withdraw:</b> lost-update баланса, овердрафт при гонке (двое сняли по остатку). Атомарно: CAS/лок на счёт, проверка-и-списание под защитой.</li>
 *   <li><b>⭐ Transfer = ДВА счёта под защитой:</b> lock-ordering (по id счёта) против deadlock; атомарность (оба или ни одного); идемпотентность транзакции (ключ, против double-apply).</li>
 * </ul>
 */
public class BankAccountService {

    public enum Currency { RUB, USD, EUR }
    public enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }

    public record Account(String id, String ownerName, double balance, Currency currency) {}
    public record Transaction(String id, String fromAccountId, String toAccountId,
                              double amount, long timestamp, TransactionType type) {}

    /**
     * Создать новый счёт
     * @param ownerName имя владельца
     * @param currency валюта счёта
     * @return идентификатор нового счёта
     */
    public String createAccount(String ownerName, Currency currency) {
        //TODO implement
        return null;
    }

    /**
     * Пополнить счёт
     * @param accountId идентификатор счёта
     * @param amount сумма пополнения (> 0)
     */
    public void deposit(String accountId, double amount) {
        //TODO implement
    }

    /**
     * Снять со счёта
     * @param accountId идентификатор счёта
     * @param amount сумма снятия (> 0)
     * @throws IllegalStateException если недостаточно средств
     */
    public void withdraw(String accountId, double amount) {
        //TODO implement
    }

    /**
     * Перевод между счетами
     * @param fromId счёт отправителя
     * @param toId счёт получателя
     * @param amount сумма перевода
     * @throws IllegalArgumentException если валюты не совпадают
     * @throws IllegalStateException если недостаточно средств
     */
    public void transfer(String fromId, String toId, double amount) {
        //TODO implement
    }

    /**
     * Получить баланс счёта
     */
    public double getBalance(String accountId) {
        //TODO implement
        return 0;
    }

    /**
     * История транзакций по счёту
     * @param accountId идентификатор счёта
     * @return список транзакций отсортированный по времени
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        //TODO implement
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("=== BankAccountService: Smoke Test ===");
        // TODO: создать счета, выполнить переводы, проверить балансы
    }
}

/* ═══════════════ УСЛОЖНЕНИЯ (дрилить ИЗНУТРИ этого класса) ═══════════════
 * 1. АТОМАРНОЕ ИЗМЕНЕНИЕ БАЛАНСА: наивное read→compute→write под конкурентными deposit/withdraw теряет
 *    операции (lost update). Сделать атомарным (лок на счёт / atomic-число). Тест: K×deposit(+1)+K×withdraw(-1) → итог==старт.
 * 2. LOCK-ORDERING ПРИ ПЕРЕВОДЕ (anti-deadlock): transfer(A→B) и transfer(B→A) одновременно при «сначала from,
 *    потом to» дают deadlock. Захватывать локи двух счетов в ЕДИНОМ порядке (по id/hash); перевод атомарен.
 *    Тест: встречные переводы в цикле не зависают; сумма балансов — инвариант.
 * 3. ИДЕМПОТЕНТНАЯ ТРАНЗАКЦИЯ (txnId): ретрай transfer с тем же txnId НЕ применяет перевод дважды.
 *    Тест: transfer(txnId) дважды → баланс изменился один раз; журнал — одна запись txnId.
 * 4. ОПТИМИСТИЧНАЯ БЛОКИРОВКА (version+CAS+retry): у счёта version; read(balance,version)→compute→CAS
 *    применить только если version не менялась, иначе перечитать и повторить. Без локов в счастливом пути.
 *    Тест: K потоков меняют баланс через CAS → итог верен, version += число успешных операций.
 */
