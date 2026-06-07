package com.minddream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 统计数据 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {

    private Long totalDreams; // 总梦境数
    private Long totalDreamers; // 总梦境者数
    private Map<String, Long> genderDistribution; // 性别分布
    private Map<String, Long> ageDistribution; // 年龄分布
    private Map<String, Long> emotionDistribution; // 情感分布
    private Map<String, Long> characterDistribution; // 角色分布
    private List<TimelineData> timeline; // 时间线数据

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineData {
        private String period;
        private Long count;
    }
}
