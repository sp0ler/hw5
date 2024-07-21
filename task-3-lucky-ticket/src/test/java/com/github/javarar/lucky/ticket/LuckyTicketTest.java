package com.github.javarar.lucky.ticket;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
class LuckyTicketTest {

    private static final LuckyTicket luckyTicket = new LuckyTicket();
    private static final Map<LuckyType, Integer> map = new ConcurrentHashMap<>(LuckyType.values().length, 1.0f);
    private static final AtomicLong benchmark = new AtomicLong(0);

    private static final int startRange = 100_000;
    private static final int endRange = 200_000;

    @BeforeAll
    static void before() {
        for (LuckyType value : LuckyType.values()) {
            map.put(value, 0);
        }
    }

    @AfterAll
    static void after() {
        log.info("Результат:");
        log.info("Время выполнения: {} секунд", benchmark.get() / 1_000_000_000.0);
        log.info("Вероятность, что счастливого билета нет вообще: {}%", calculatePercentage(LuckyType.NOT_LUCKY));
        log.info("Вероятность московского счастливого билета: {}%", calculatePercentage(LuckyType.MOSCOW));
        log.info("Вероятность ленинградского счастливого билета: {}%", calculatePercentage(LuckyType.LENINGRAD));
        log.info("Вероятность московского и ленинградского счастливого билета: {}%", calculatePercentage(LuckyType.ALL_LUCKY));
    }

    @DisplayName("Задание 7. Счастливый билет")
    @ParameterizedTest
    @MethodSource("cases")
    void luckyTicketProbabilityTest(Integer serialNumberLength) {
        long time = System.nanoTime();
        LuckyType type = luckyTicket.luckyTicketProbability(serialNumberLength);
        time = System.nanoTime() - time;

        long allTime = benchmark.get();
        benchmark.set(allTime + time);

        map.merge(type, 1, Integer::sum);
    }

    static Stream<Integer> cases() {
        return IntStream.range(startRange, endRange).boxed();
    }

    private static double calculatePercentage(LuckyType value) {
        return (double) map.get(value) / (endRange - startRange) * 100;
    }
}