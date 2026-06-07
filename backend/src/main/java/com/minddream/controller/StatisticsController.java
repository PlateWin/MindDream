package com.minddream.controller;

import com.minddream.dto.StatisticsDTO;
import com.minddream.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "统计分析", description = "数据统计与分析接口")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    @Operation(summary = "总体统计概览", description = "获取所有维度的统计数据")
    public ResponseEntity<StatisticsDTO> getOverview() {
        StatisticsDTO stats = statisticsService.getOverviewStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/gender")
    @Operation(summary = "性别分布", description = "获取性别分布统计")
    public ResponseEntity<Map<String, Long>> getGenderDistribution() {
        Map<String, Long> distribution = statisticsService.getGenderDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/age")
    @Operation(summary = "年龄分布", description = "获取年龄组分布统计")
    public ResponseEntity<Map<String, Long>> getAgeDistribution() {
        Map<String, Long> distribution = statisticsService.getAgeDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/emotions")
    @Operation(summary = "情感分布", description = "获取情感类型分布统计（Top 10）")
    public ResponseEntity<Map<String, Long>> getEmotionDistribution() {
        Map<String, Long> distribution = statisticsService.getEmotionDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/spectrum")
    @Operation(summary = "情感光谱分析", description = "获取情感光谱数据（全部情感 + 维度分类，用于雷达图）")
    public ResponseEntity<Map<String, Object>> getEmotionSpectrum() {
        Map<String, Object> spectrum = statisticsService.getEmotionSpectrum();
        return ResponseEntity.ok(spectrum);
    }

    @GetMapping("/gender-emotions")
    @Operation(summary = "性别×情感交叉分析", description = "按性别分组的情感分布对比")
    public ResponseEntity<Map<String, Map<String, Long>>> getGenderEmotionCross() {
        Map<String, Map<String, Long>> cross = statisticsService.getGenderEmotionCross();
        return ResponseEntity.ok(cross);
    }

    @GetMapping("/characters")
    @Operation(summary = "角色类型分布", description = "获取角色类型分布统计（Top 10）")
    public ResponseEntity<Map<String, Long>> getCharacterDistribution() {
        Map<String, Long> distribution = statisticsService.getCharacterDistribution();
        return ResponseEntity.ok(distribution);
    }
}
