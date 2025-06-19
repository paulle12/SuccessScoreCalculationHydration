package com.successScoreCalculationHydration.SuccessScoreCalculationHydration.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import com.successScoreCalculationHydration.SuccessScoreCalculationHydration.models.DungeonScoreLookup;
import org.springframework.stereotype.Service;

@Service
public class SuccessScoreCalculationHydration {

    public double getTimeWeight(String completedAtStr) {
        // Parse ISO-8601 string like "2025-04-02T04:04:28.000Z"
        ZonedDateTime completedAt = Instant.parse(completedAtStr).atZone(ZoneId.of("UTC"));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        long daysAgo = Duration.between(completedAt, now).toDays();

        double timeWeight = 0.4;
        if (daysAgo <= 7) {
            timeWeight = 1.0;
        } else if (daysAgo <= 14) {
            timeWeight = 0.7;
        }

        return timeWeight;
    }

    public double calculatePlayerScore(
            List<Map<String, Object>> recentRuns,
            int targetLevel,
            double currentIO,
            double previousIO,
            boolean isMainRole) {
        // // === 1. Last 10 runs scoring (up to 2.5 points) ===
        double runscore = 0.0;
        for (Map<String, Object> run : recentRuns) {
            int level = (int) run.get("mythic_level");
            boolean withinRange = level >= targetLevel - 2;
            double timeWeight = getTimeWeight((String) run.get("completed_at"));

            // we set the weight to a cap of +15 since its pretty unrealstic to go higher
            // atm we would need to adjust as patches come out
            double levelWeight = Math.min(1.0, level / 15.0);
            if (withinRange) {
                double baseScore = 0;
                int chest = (int) run.get("num_keystone_upgrades");
                switch (chest) {
                    case 3:
                        baseScore = 1.3;
                        break;
                    case 2:
                        baseScore = 1.2;
                        break;
                    case 1:
                        baseScore = 1.0;
                        break;
                    default:
                        baseScore = 0.3;
                }
                runscore += baseScore * timeWeight * levelWeight;
            }
        }
        // Normalize to 2.5 max (10 perfect runs = 10.0 raw score → 2.5 normalized)
        double cappedRunScore = Math.min(2.5, (runscore / 10.0) * 2.5);

        // // === 2. Current IO (5.0 max) ===
        int perDungeonScore = DungeonScoreLookup.ESTIMATED_SCORE_BY_LEVEL.get(targetLevel);
        double expectedIO = perDungeonScore * 8;
        double ioScore = Math.min(currentIO / expectedIO, 1) * 5.0;

        // // === 3. Role match (1.5 max) ===
        double roleBonus = isMainRole ? 1.5 : 0.5;

        // // === 4. Previous Season IO (1.0 max) ===
        double previousScore = Math.min(previousIO / expectedIO, 1.0) * 1.0;

        double total = cappedRunScore + ioScore + roleBonus + previousScore;

        double cappedScore = Math.min(total, 10.0);
        double roundedScore = Math.round(cappedScore * 100.0) / 100.0;
        System.out.println(roundedScore);
        return roundedScore;
    }

}
