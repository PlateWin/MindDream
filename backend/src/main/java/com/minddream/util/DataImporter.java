package com.minddream.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.minddream.entity.Dream;
import com.minddream.repository.DreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入工具
 * 从 JSONL 文件批量导入梦境数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataImporter implements CommandLineRunner {

    private final DreamRepository dreamRepository;
    private final Gson gson = new Gson();

    // 设置为 true 以启用自动导入
    private static final boolean AUTO_IMPORT_ENABLED = true;

    @Override
    public void run(String... args) throws Exception {
        if (!AUTO_IMPORT_ENABLED) {
            log.info("数据自动导入已禁用，如需导入请设置 AUTO_IMPORT_ENABLED = true");
            return;
        }

        // 检查数据库是否已有数据
        long count = dreamRepository.count();
        if (count > 0) {
            log.info("数据库已有 {} 条数据，跳过导入", count);
            return;
        }

        log.info("开始导入梦境数据...");
        importData();
        log.info("数据导入完成！");
    }

    /**
     * 手动触发数据导入（可通过 API 调用）
     */
    public void importData() {
        String[] dataFiles = {
                "D:/project/心理/data/train_parsed_part1.jsonl",
                "D:/project/心理/data/train_parsed_part2.jsonl",
                "D:/project/心理/data/train_parsed_part3.jsonl"
        };

        int totalImported = 0;

        for (String filePath : dataFiles) {
            try {
                int imported = importFromFile(filePath);
                totalImported += imported;
                log.info("从 {} 导入了 {} 条数据", filePath, imported);
            } catch (Exception e) {
                log.error("导入文件失败: {}", filePath, e);
            }
        }

        log.info("总共导入 {} 条梦境数据", totalImported);
    }

    /**
     * 从单个 JSONL 文件导入
     */
    private int importFromFile(String filePath) throws Exception {
        List<Dream> dreams = new ArrayList<>();
        int count = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JsonObject json = gson.fromJson(line, JsonObject.class);
                    Dream dream = parseDream(json);
                    dreams.add(dream);
                    count++;

                    // 批量保存（每1000条）
                    if (dreams.size() >= 1000) {
                        try {
                            dreamRepository.saveAll(dreams);
                            log.info("已导入 {} 条数据...", count);
                        } catch (Exception saveEx) {
                            log.error("批量保存失败: {}", saveEx.getMessage());
                        }
                        dreams.clear();
                    }
                } catch (Exception e) {
                    log.warn("解析行数据失败，跳过该行");
                }
            }

            // 保存剩余数据
            if (!dreams.isEmpty()) {
                try {
                    dreamRepository.saveAll(dreams);
                    log.info("最后批次导入完成");
                } catch (Exception saveEx) {
                    log.error("最后批次保存失败: {}", saveEx.getMessage());
                }
            }
        }

        return count;
    }

    /**
     * 解析 JSON 为 Dream 实体
     */
    private Dream parseDream(JsonObject json) {
        Dream dream = new Dream();

        dream.setId(getStringValue(json, "id"));
        dream.setName(getStringValue(json, "name"));
        dream.setDreamNumber(getIntValue(json, "number"));
        dream.setTimePeriod(getStringValue(json, "time"));
        dream.setBirthYear(getStringValue(json, "date"));
        dream.setGender(getStringValue(json, "gender"));
        dream.setAgeGroup(getStringValue(json, "age"));
        dream.setReport(getStringValue(json, "report"));
        dream.setCharactersRaw(getStringValue(json, "characters_raw"));
        dream.setCharactersCn(getArrayAsString(json, "characters_cn"));
        dream.setEmotionsRaw(getStringValue(json, "emotions_raw"));
        dream.setEmotionsCn(getArrayAsString(json, "emotions_cn"));

        return dream;
    }

    private String getStringValue(JsonObject json, String key) {
        if (json.has(key) && !json.get(key).isJsonNull()) {
            return json.get(key).getAsString();
        }
        return null;
    }

    private Integer getIntValue(JsonObject json, String key) {
        if (json.has(key) && !json.get(key).isJsonNull()) {
            try {
                return json.get(key).getAsInt();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private String getArrayAsString(JsonObject json, String key) {
        if (json.has(key) && !json.get(key).isJsonNull()) {
            return json.get(key).toString();
        }
        return null;
    }
}
