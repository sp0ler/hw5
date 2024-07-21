package com.github.javarar.lucky.ticket;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

@Log4j2
public class LuckyTicket {

    /**
     * Максимальное кол-во символов. Должно быть четное число!
     * <p>Например, если максимальное число у билета 9999 - то countLength = 4</p>
     */
    @Setter
    private int countLength = 6;

    private final Predicate<Integer> lessThanHalfPredicate = i -> i < countLength / 2; // до половины
    private final Predicate<Integer> moreThanHalfPredicate = i -> i > countLength / 2 - 1; // после половины
    private final Predicate<Integer> evenPredicate = i -> (i & 1) == 0; // четное
    private final Predicate<Integer> oddPredicate = i -> (i & 1) != 0; // нечетное

    /**
     * Основной executor.
     * <p>В теории можно было не объявлять переменную, но сделал для вида</p>
     */
    private static final Executor executor = ForkJoinPool.commonPool();

    public LuckyType luckyTicketProbability(int serialNumberLength) {
        int[] array = asArray(serialNumberLength);

        // Ленинградски
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(() -> split(array, lessThanHalfPredicate), executor)
                // вообще executor можно не передавать, под капотом все равно будет ForkJoinPool
                .thenApplyAsync(LuckyTicket::sum, executor);
        CompletableFuture<Integer> future2 = CompletableFuture
                .supplyAsync(() -> split(array, moreThanHalfPredicate), executor)
                .thenApplyAsync(LuckyTicket::sum, executor);

        // Московский
        CompletableFuture<Integer> future3 = CompletableFuture
                .supplyAsync(() -> split(array, evenPredicate), executor)
                .thenApplyAsync(LuckyTicket::sum, executor);
        CompletableFuture<Integer> future4 = CompletableFuture
                .supplyAsync(() -> split(array, oddPredicate), executor)
                .thenApplyAsync(LuckyTicket::sum, executor);

        Integer leningrad1 = future1.join();
        Integer leningrad2 = future2.join();
        boolean isLeningrad = leningrad1.equals(leningrad2);
        log.info("Результат по-ленинградски: [{}] [{}], счастливый = {}", leningrad1, leningrad2, isLeningrad);

        Integer moscow1 = future3.join();
        Integer moscow2 = future4.join();
        boolean isMoscow = moscow1.equals(moscow2);
        log.info("Результат по-московски: [{}] [{}], счастливый = {}", moscow1, moscow2, isMoscow);

        if (isLeningrad && isMoscow) {
            return LuckyType.ALL_LUCKY;
        } else if (isLeningrad) {
            return LuckyType.LENINGRAD;
        } else if (isMoscow){
            return LuckyType.MOSCOW;
        } else {
            return LuckyType.NOT_LUCKY;
        }
    }

    /**
     * Метод преобразует число в массив чисел
     * <p>Работает от 0 до 999_999</p>
     * @param value Число
     * @return массив чисел
     */
    private int[] asArray(int value) {
        String temp = Integer.toString(value);
        StringBuilder sb = new StringBuilder(temp);
        while (sb.length() != countLength) {
            sb.insert(0, "0");
        }
        temp = sb.toString();

        int[] result = new int[temp.length()];
        for (int i = 0; i < temp.length(); i++)
        {
            result[i] = temp.charAt(i) - '0';
        }
        return result;
    }

    /**
     * Метод разделяет массив
     * @param array Массив
     * @param predicate Предикат, которому нужно передать условие
     * @return разделенный массив на основе прелдиката
     */
    private int[] split(int[] array, Predicate<Integer> predicate) {
        int length = array.length;
        int[] result = new int[length / 2];
        int splitedIndex = 0;
        for (int index = 0; index < length; index++) {
            if (predicate.test(index)) {
                result[splitedIndex] = array[index];
                splitedIndex++;

                if (splitedIndex == length / 2) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Метод суммирем все числа в массиве
     * @param array Массив
     * @return сумма чисел
     */
    private static Integer sum(int[] array) {
        int sum = 0;
        for(int i : array) {
            sum += i;
        }
        return sum;
    }
}