package com.lucky.bot.part.grade;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum PartGrade {
    STANDARD("ga", "\uD83D\uDD39", Arrays.asList(
            new Upgrade(2, 10),
            new Upgrade(3, 50),
            new Upgrade(4, 100),
            new Upgrade(5, 150),
            new Upgrade(6, 200),
            new Upgrade(7, 300),
            new Upgrade(8, 400),
            new Upgrade(9, 600),
            new Upgrade(14, 800),
            new Upgrade(19, 2000),
            new Upgrade(29, 5000),
            new Upgrade(49, 15000),
            new Upgrade(59, 20000),
            new Upgrade(79, 25000, 10),
            new Upgrade(99, 30000, 12),
            new Upgrade(119, 35000, 14),
            new Upgrade(144, 40000, 16),
            new Upgrade(169, 45000, 18),
            new Upgrade(194, 50000, 20)
    )),
    POLISHED("gb", "\uD83D\uDD39\uD83D\uDD39", Arrays.asList(
            new Upgrade(1, 20),
            new Upgrade(2, 100),
            new Upgrade(3, 200),
            new Upgrade(4, 300),
            new Upgrade(5, 400),
            new Upgrade(5, 600),
            new Upgrade(5, 800),
            new Upgrade(6, 1200),
            new Upgrade(9, 1600),
            new Upgrade(14, 4000),
            new Upgrade(19, 8000),
            new Upgrade(34, 25000),
            new Upgrade(49, 32500),
            new Upgrade(64, 40000, 20),
            new Upgrade(79, 47500, 24),
            new Upgrade(94, 55000, 28),
            new Upgrade(114, 62500, 32),
            new Upgrade(134, 70000, 36),
            new Upgrade(154, 77500, 40)
    )),
    REFINED("gc", "\uD83D\uDD39\uD83D\uDD39\uD83D\uDD39", Arrays.asList(
            new Upgrade(1, 50),
            new Upgrade(1, 250),
            new Upgrade(1, 500),
            new Upgrade(2, 750),
            new Upgrade(2, 1000),
            new Upgrade(2, 1500),
            new Upgrade(2, 2000),
            new Upgrade(3, 3000),
            new Upgrade(4, 4000),
            new Upgrade(5, 7500),
            new Upgrade(7, 12500),
            new Upgrade(14, 50000),
            new Upgrade(24, 75000),
            new Upgrade(34, 100000, 30),
            new Upgrade(44, 125000, 36),
            new Upgrade(54, 150000, 42),
            new Upgrade(69, 175000, 48),
            new Upgrade(84, 200000, 54),
            new Upgrade(99, 225000, 60)
    )),
    SUPERIOR("gd", "\uD83D\uDD38", Arrays.asList(
            new Upgrade(1, 1000),
            new Upgrade(1, 1500),
            new Upgrade(1, 2000),
            new Upgrade(1, 2500),
            new Upgrade(2, 5000),
            new Upgrade(2, 7500),
            new Upgrade(2, 10000),
            new Upgrade(2, 15000),
            new Upgrade(3, 20000),
            new Upgrade(4, 30000),
            new Upgrade(5, 50000),
            new Upgrade(11, 100000),
            new Upgrade(19, 135000),
            new Upgrade(27, 170000, 40),
            new Upgrade(35, 205000, 48),
            new Upgrade(43, 240000, 56),
            new Upgrade(53, 275000, 64),
            new Upgrade(63, 310000, 72),
            new Upgrade(73, 345000, 80)
    )),
    OUTSTANDING("ge", "\uD83D\uDD38\uD83D\uDD38", Arrays.asList(
            new Upgrade(1, 3000),
            new Upgrade(1, 4500),
            new Upgrade(1, 6000),
            new Upgrade(1, 7500),
            new Upgrade(2, 15000),
            new Upgrade(2, 22500),
            new Upgrade(2, 30000),
            new Upgrade(2, 45000),
            new Upgrade(2, 60000),
            new Upgrade(3, 90000),
            new Upgrade(3, 150000),
            new Upgrade(7, 300000),
            new Upgrade(11, 350000),
            new Upgrade(13, 400000, 50),
            new Upgrade(15, 450000, 60),
            new Upgrade(17, 500000, 70),
            new Upgrade(22, 550000, 80),
            new Upgrade(27, 600000, 90),
            new Upgrade(32, 650000, 100)
    )),
    EXTRAORDINARY("gf", "\uD83D\uDD38\uD83D\uDD38\uD83D\uDD38", Arrays.asList(
            new Upgrade(2, 8000),
            new Upgrade(2, 12000),
            new Upgrade(3, 16000),
            new Upgrade(3, 25000, 1),
            new Upgrade(4, 30000, 4),
            new Upgrade(4, 50000, 8),
            new Upgrade(5, 75000, 13),
            new Upgrade(6, 125000, 19),
            new Upgrade(8, 175000, 26),
            new Upgrade(10, 250000, 34),
            new Upgrade(12, 325000, 43),
            new Upgrade(15, 425000, 53),
            new Upgrade(20, 525000, 64),
            new Upgrade(25, 650000, 76),
            new Upgrade(30, 800000, 89),
            new Upgrade(35, 900000, 103),
            new Upgrade(40, 1000000, 118)
    ));

    private final String code;
    private final String stars;
    private final List<Upgrade> upgrades;

    PartGrade(final String code, String stars, List<Upgrade> upgrades) {
        this.code = code;
        this.stars = stars;
        this.upgrades = upgrades;
    }

    public static Optional<PartGrade> from(String code) {
        for (PartGrade partGrade : values()) {
            if (partGrade.code.equalsIgnoreCase(code)) {
                return Optional.of(partGrade);
            }
        }
        return Optional.empty();
    }
}
