package com.successScoreCalculationHydration.SuccessScoreCalculationHydration.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.successScoreCalculationHydration.SuccessScoreCalculationHydration.service.SuccessScoreCalculationHydration;
import com.successScoreCalculationHydration.SuccessScoreCalculationHydration.dto.HydratedPlayer;

@RestController
@RequestMapping("/api/calculate")
public class SuccessScoreCalculationHydrationController {
    private final SuccessScoreCalculationHydration successScoreCalculationHydration;

    public SuccessScoreCalculationHydrationController(
            SuccessScoreCalculationHydration successScoreCalculationHydration) {
        this.successScoreCalculationHydration = successScoreCalculationHydration;
    }

    @PostMapping("/score")
    public double calculate(@RequestBody HydratedPlayer player, @RequestParam int targetLevel) {
        List<Map<String, Object>> recentRuns = player.getRecentRuns();
        double currentIO = player.getCurrentSeasonScore();
        double previousIO = player.getPreviousSeasonScore();
        boolean isMainRole = player.getIsMainRole();
        return successScoreCalculationHydration.calculatePlayerScore(recentRuns, targetLevel, currentIO, previousIO,
                isMainRole);
    }
}
