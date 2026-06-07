package com.minddream.service;

import com.minddream.dto.StatisticsDTO;
import com.minddream.repository.DreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计分析业务逻辑层
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final DreamRepository dreamRepository;

    private static final Map<String, String> AGE_LABELS = Map.of(
        "A", "成人", "C", "儿童", "T", "青少年", "Y", "青年", "O", "老年"
    );

    private static final Map<String, String> GENDER_LABELS = Map.of(
        "M", "男性", "F", "女性"
    );

    /**
     * 获取总体统计概览
     */
    public StatisticsDTO getOverviewStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        stats.setTotalDreams(dreamRepository.count());

        stats.setTotalDreamers((long) dreamRepository.findAll().stream()
                .map(d -> d.getName())
                .filter(Objects::nonNull)
                .distinct()
                .count());

        // 性别分布
        Map<String, Long> genderDist = new LinkedHashMap<>();
        dreamRepository.countByGender().forEach(obj -> {
            String gender = (String) obj[0];
            Long count = (Long) obj[1];
            genderDist.put(GENDER_LABELS.getOrDefault(gender, gender), count);
        });
        stats.setGenderDistribution(genderDist);

        // 年龄分布
        Map<String, Long> ageDist = new LinkedHashMap<>();
        dreamRepository.countByAgeGroup().forEach(obj -> {
            String age = (String) obj[0];
            Long count = (Long) obj[1];
            ageDist.put(AGE_LABELS.getOrDefault(age, age), count);
        });
        stats.setAgeDistribution(ageDist);

        return stats;
    }

    /**
     * 获取性别分布统计
     */
    public Map<String, Long> getGenderDistribution() {
        Map<String, Long> distribution = new LinkedHashMap<>();
        dreamRepository.countByGender().forEach(obj -> {
            String gender = (String) obj[0];
            Long count = (Long) obj[1];
            distribution.put(GENDER_LABELS.getOrDefault(gender, gender), count);
        });
        return distribution;
    }

    /**
     * 获取年龄分布统计
     */
    public Map<String, Long> getAgeDistribution() {
        Map<String, Long> distribution = new LinkedHashMap<>();
        dreamRepository.countByAgeGroup().forEach(obj -> {
            String age = (String) obj[0];
            Long count = (Long) obj[1];
            distribution.put(AGE_LABELS.getOrDefault(age, age), count);
        });
        return distribution;
    }

    /**
     * 获取情感分布（从梦境数据中解析）
     */
    public Map<String, Long> getEmotionDistribution() {
        Map<String, Long> distribution = new HashMap<>();

        dreamRepository.findAll().forEach(dream -> {
            if (dream.getEmotionsCn() != null && !dream.getEmotionsCn().isEmpty()) {
                String[] emotions = dream.getEmotionsCn()
                        .replaceAll("[\\[\\]'\"]", "")
                        .split(",");
                for (String emotion : emotions) {
                    emotion = emotion.trim();
                    if (!emotion.isEmpty() && !emotion.equals("null")) {
                        distribution.merge(emotion, 1L, Long::sum);
                    }
                }
            }
        });

        return distribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    /**
     * 获取情感光谱数据（全部情感，用于雷达图）
     * 返回所有情感及其频次 + 情感维度分类
     */
    public Map<String, Object> getEmotionSpectrum() {
        Map<String, Long> distribution = new HashMap<>();

        dreamRepository.findAll().forEach(dream -> {
            if (dream.getEmotionsCn() != null && !dream.getEmotionsCn().isEmpty()) {
                String[] emotions = dream.getEmotionsCn()
                        .replaceAll("[\\[\\]'\"]", "")
                        .split(",");
                for (String emotion : emotions) {
                    emotion = emotion.trim();
                    if (!emotion.isEmpty() && !emotion.equals("null")) {
                        distribution.merge(emotion, 1L, Long::sum);
                    }
                }
            }
        });

        // 情感维度分类（用于雷达图维度分组）
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("emotions", distribution);

        // 计算情感维度得分（正面/负面/中性）
        long positive = 0, negative = 0, neutral = 0;
        Set<String> positiveSet = Set.of("快乐", "喜悦", "幸福", "满足", "期待", "惊喜", "轻松", "愉快");
        Set<String> negativeSet = Set.of("悲伤", "愤怒", "恐惧", "焦虑", "痛苦", "厌恶", "沮丧", "后悔", "内疚");

        for (Map.Entry<String, Long> entry : distribution.entrySet()) {
            if (positiveSet.contains(entry.getKey())) {
                positive += entry.getValue();
            } else if (negativeSet.contains(entry.getKey())) {
                negative += entry.getValue();
            } else {
                neutral += entry.getValue();
            }
        }

        Map<String, Long> dimensions = new LinkedHashMap<>();
        dimensions.put("正面情感", positive);
        dimensions.put("负面情感", negative);
        dimensions.put("中性/复杂情感", neutral);
        result.put("dimensions", dimensions);
        result.put("totalEmotions", distribution.values().stream().mapToLong(Long::longValue).sum());

        return result;
    }

    /**
     * 获取性别×情感交叉分析
     */
    public Map<String, Map<String, Long>> getGenderEmotionCross() {
        Map<String, Map<String, Long>> result = new LinkedHashMap<>();
        result.put("男性", new HashMap<>());
        result.put("女性", new HashMap<>());

        dreamRepository.findAll().forEach(dream -> {
            String genderLabel = GENDER_LABELS.getOrDefault(dream.getGender(), dream.getGender());
            Map<String, Long> target = result.get(genderLabel);
            if (target == null) return;

            if (dream.getEmotionsCn() != null && !dream.getEmotionsCn().isEmpty()) {
                String[] emotions = dream.getEmotionsCn()
                        .replaceAll("[\\[\\]'\"]", "")
                        .split(",");
                for (String emotion : emotions) {
                    emotion = emotion.trim();
                    if (!emotion.isEmpty() && !emotion.equals("null")) {
                        target.merge(emotion, 1L, Long::sum);
                    }
                }
            }
        });

        // 只保留 Top 8
        Map<String, Map<String, Long>> topResult = new LinkedHashMap<>();
        result.forEach((gender, emotions) -> {
            Map<String, Long> top = emotions.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(8)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
            topResult.put(gender, top);
        });

        return topResult;
    }

    /**
     * 获取角色类型分布
     */
    public Map<String, Long> getCharacterDistribution() {
        Map<String, Long> distribution = new HashMap<>();

        dreamRepository.findAll().forEach(dream -> {
            if (dream.getCharactersCn() != null && !dream.getCharactersCn().isEmpty()) {
                String[] characters = dream.getCharactersCn()
                        .replaceAll("[\\[\\]'\"]", "")
                        .split(",");
                for (String character : characters) {
                    character = character.trim();
                    if (!character.isEmpty() && !character.equals("null")) {
                        distribution.merge(character, 1L, Long::sum);
                    }
                }
            }
        });

        return distribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }
}
