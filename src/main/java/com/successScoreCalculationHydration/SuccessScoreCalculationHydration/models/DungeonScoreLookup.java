package com.successScoreCalculationHydration.SuccessScoreCalculationHydration.models;

import java.util.Map;
import static java.util.Map.entry;

public class DungeonScoreLookup {
    public static final Map<Integer, Integer> ESTIMATED_SCORE_BY_LEVEL = Map.ofEntries(
            entry(2, 75),
            entry(3, 120),
            entry(4, 155),
            entry(5, 190),
            entry(6, 225),
            entry(7, 265),
            entry(8, 280),
            entry(9, 300),
            entry(10, 320),
            entry(11, 335),
            entry(12, 360),
            entry(13, 380),
            entry(14, 395),
            entry(15, 410),
            entry(16, 425),
            entry(17, 440),
            entry(18, 455),
            entry(19, 470),
            entry(20, 485));
}