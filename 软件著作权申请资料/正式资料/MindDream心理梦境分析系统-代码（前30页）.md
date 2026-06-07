MindDream心理梦境分析系统 V1.0 源代码文档（前30页）

================================================================================
软件名称：MindDream心理梦境分析系统
版本号：V1.0
代码行数：前30页（共1500行）
================================================================================

文件1：MindDreamApplication.java（后端主应用入口）
--------------------------------------------------------------------------------
package com.minddream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MindDream 心理梦境分析平台 - 主应用入口
 */
@SpringBootApplication
public class MindDreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindDreamApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("🌙 MindDream Backend Started!");
        System.out.println("📖 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("📡 API Docs: http://localhost:8080/api/api-docs");
        System.out.println("=================================\n");
    }
}

文件2：Dream.java（梦境实体类）
--------------------------------------------------------------------------------
package com.minddream.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 梦境实体类
 */
@Data
@Entity
@Table(name = "dreams")
public class Dream {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 200)
    private String name;

    @Column(name = "dream_number")
    private Integer dreamNumber;

    @Column(name = "time_period", length = 50)
    private String timePeriod;

    @Column(name = "birth_year", length = 10)
    private String birthYear;

    @Column(length = 1)
    private String gender;

    @Column(name = "age_group", length = 1)
    private String ageGroup;

    @Column(columnDefinition = "TEXT")
    private String report;

    @Column(name = "characters_raw", length = 500)
    private String charactersRaw;

    @Column(name = "characters_cn", columnDefinition = "TEXT")
    private String charactersCn;

    @Column(name = "emotions_raw", length = 500)
    private String emotionsRaw;

    @Column(name = "emotions_cn", columnDefinition = "TEXT")
    private String emotionsCn;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

文件3：Statistics.java（统计数据实体类）
--------------------------------------------------------------------------------
package com.minddream.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统计数据实体类
 */
@Data
@Entity
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_type", length = 50, nullable = false)
    private String statType;

    @Column(name = "stat_key", length = 100, nullable = false)
    private String statKey;

    @Column(name = "stat_value", length = 500)
    private String statValue;

    @Column(name = "count")
    private Integer count;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

文件4：DreamController.java（梦境控制器）
--------------------------------------------------------------------------------
package com.minddream.controller;

import com.minddream.entity.Dream;
import com.minddream.dto.SearchRequest;
import com.minddream.service.DreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 梦境管理控制器
 */
@RestController
@RequestMapping("/dreams")
@RequiredArgsConstructor
@Tag(name = "梦境管理", description = "梦境数据的增删改查接口")
public class DreamController {

    private final DreamService dreamService;

    @GetMapping
    @Operation(summary = "获取梦境列表", description = "支持分页和排序")
    public ResponseEntity<Page<Dream>> getDreams(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortOrder) {

        Page<Dream> dreams = dreamService.getAllDreams(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(dreams);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取梦境详情", description = "根据ID获取单个梦境的完整信息")
    public ResponseEntity<Dream> getDreamById(
            @Parameter(description = "梦境ID") @PathVariable String id) {

        return dreamService.getDreamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    @Operation(summary = "高级搜索", description = "支持多条件组合搜索")
    public ResponseEntity<Page<Dream>> searchDreams(@RequestBody SearchRequest request) {
        Page<Dream> results = dreamService.searchDreams(request);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/random")
    @Operation(summary = "随机获取梦境", description = "随机返回指定数量的梦境")
    public ResponseEntity<List<Dream>> getRandomDreams(
            @Parameter(description = "数量") @RequestParam(defaultValue = "1") int limit) {

        List<Dream> dreams = dreamService.getRandomDreams(limit);
        return ResponseEntity.ok(dreams);
    }

    @GetMapping("/count")
    @Operation(summary = "获取总数", description = "获取梦境总数量")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(dreamService.getTotalCount());
    }

    @PostMapping
    @Operation(summary = "创建梦境", description = "创建新的梦境记录")
    public ResponseEntity<Dream> createDream(@RequestBody Dream dream) {
        Dream savedDream = dreamService.saveDream(dream);
        return ResponseEntity.ok(savedDream);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除梦境", description = "根据ID删除梦境（管理功能）")
    public ResponseEntity<Void> deleteDream(@PathVariable String id) {
        dreamService.deleteDream(id);
        return ResponseEntity.ok().build();
    }
}

文件5：AIAnalysisController.java（AI分析控制器）
--------------------------------------------------------------------------------
package com.minddream.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI 梦境分析控制器
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI 分析", description = "智能梦境分析接口")
public class AIAnalysisController {

    @PostMapping("/analyze")
    @Operation(summary = "分析梦境", description = "使用 AI 分析梦境内容，提供心理学解读")
    public ResponseEntity<AnalysisResult> analyzeDream(@RequestBody AnalysisRequest request) {

        AnalysisResult result = new AnalysisResult();
        result.setDreamId(request.getDreamId());
        result.setReportExcerpt(truncate(request.getReport(), 100));

        // Mock AI 分析结果
        result.setOverallSummary(generateSummary(request.getReport()));
        result.setEmotionalAnalysis(analyzeEmotion(request.getReport()));
        result.setSymbolInterpretation(interpretSymbols(request.getReport()));
        result.setPersonalInsights(generateInsights(request.getCharacters(), request.getEmotions()));
        result.setSuggestions(generateSuggestions());
        result.setConfidence(calculateConfidence(request.getReport()));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/chat")
    @Operation(summary = "梦境对话", description = "与 AI 进行梦境相关的对话")
    public ResponseEntity<ChatResponse> chat(@RequestParam String question) {
        ChatResponse response = new ChatResponse();
        response.setQuestion(question);
        response.setAnswer(generateChatResponse(question));
        response.setTimestamp(new Date());
        return ResponseEntity.ok(response);
    }

    // === Mock AI 分析逻辑 ===

    private String generateSummary(String report) {
        if (report == null || report.isEmpty()) {
            return "梦境内容较少，难以进行深入分析。";
        }

        List<String> summaries = Arrays.asList(
                "这个梦境反映了您潜意识中对于人际关系的思考和情感体验。梦中的场景和人物可能代表着您生活中的某些重要关系或未解决的情绪。",
                "您的梦境展现了丰富的想象力和创造性。梦中出现的场景变化可能暗示着您对生活变化的适应过程。",
                "这个梦境包含了多个层次的象征意义。从心理学角度看，它可能反映了您对安全感、归属感或个人成长的深层需求。",
                "梦境中的叙事结构显示出您的思维正在整合日常经历和情感记忆，这是一个健康的心理过程。");

        return summaries.get(new Random().nextInt(summaries.size()));
    }

    private EmotionalAnalysis analyzeEmotion(String report) {
        EmotionalAnalysis analysis = new EmotionalAnalysis();

        String reportLower = report.toLowerCase();

        // 简单的情感关键词匹配
        if (reportLower.contains("happy") || reportLower.contains("joy")) {
            analysis.setPrimary("积极 - 快乐");
            analysis.setIntensity(75);
        } else if (reportLower.contains("sad") || reportLower.contains("cry")) {
            analysis.setPrimary("消极 - 悲伤");
            analysis.setIntensity(65);
        } else if (reportLower.contains("scary") || reportLower.contains("afraid")) {
            analysis.setPrimary("消极 - 恐惧");
            analysis.setIntensity(70);
        } else {
            analysis.setPrimary("中性 - 平静");
            analysis.setIntensity(50);
        }

        analysis.setSecondary(Arrays.asList("好奇", "期待", "困惑"));
        analysis.setExplanation("梦境中的情感状态反映了您当前的心理状态和潜意识中的情感需求。");

        return analysis;
    }

    private List<SymbolInfo> interpretSymbols(String report) {
        List<SymbolInfo> symbols = new ArrayList<>();

        // 检测常见梦境符号
        Map<String, String> symbolMap = new HashMap<>();
        symbolMap.put("house", "家 - 代表安全感、归属感和自我认同");
        symbolMap.put("water", "水 - 象征情感、潜意识和生命力");
        symbolMap.put("car", "车 - 代表人生方向、控制力和前进动力");
        symbolMap.put("door", "门 - 象征机会、转变和新的可能性");
        symbolMap.put("animal", "动物 - 代表本能、直觉和自然天性");
        symbolMap.put("people", "人物 - 可能代表自我的不同面向或重要关系");

        String reportLower = report.toLowerCase();
        for (Map.Entry<String, String> entry : symbolMap.entrySet()) {
            if (reportLower.contains(entry.getKey())) {
                SymbolInfo symbol = new SymbolInfo();
                symbol.setSymbol(entry.getKey());
                symbol.setMeaning(entry.getValue());
                symbol.setContext("在您的梦境中出现");
                symbols.add(symbol);
            }
        }

        if (symbols.isEmpty()) {
            SymbolInfo defaultSymbol = new SymbolInfo();
            defaultSymbol.setSymbol("梦境场景");
            defaultSymbol.setMeaning("您的梦境场景独特且富有个人意义");
            defaultSymbol.setContext("整体梦境氛围");
            symbols.add(defaultSymbol);
        }

        return symbols;
    }

    private List<String> generateInsights(String characters, String emotions) {
        return Arrays.asList(
                "💡 您的梦境显示出丰富的人际互动，这可能反映您对社交关系的重视",
                "💡 梦中的情感体验可以帮助您了解自己的真实感受",
                "💡 定期记录和分析梦境有助于自我认知和心理健康",
                "💡 梦境是潜意识与意识沟通的桥梁，值得细心倾听");
    }

    private List<String> generateSuggestions() {
        return Arrays.asList(
                "📝 建议保持梦境日记，记录更多细节以便深入分析",
                "🧘 尝试在睡前冥想或放松，提高梦境回忆质量",
                "💭 思考梦境中的元素与现实生活的联系",
                "📚 了解更多心理学知识，加深对自我的理解");
    }

    private int calculateConfidence(String report) {
        // 基于梦境长度计算分析置信度
        int length = report != null ? report.length() : 0;
        if (length < 100)
            return 60;
        if (length < 300)
            return 75;
        if (length < 600)
            return 85;
        return 90;
    }

    private String generateChatResponse(String question) {
        String q = question.toLowerCase();

        if (q.contains("梦") && q.contains("为什么")) {
            return "梦境是大脑在睡眠期间整理信息、处理情绪和巩固记忆的自然过程。每个人每晚都会做梦，只是不一定都能记住。梦境内容往往反映了我们的潜意识思维、日常经历和情感状态。";
        } else if (q.contains("噩梦")) {
            return "噩梦通常是压力、焦虑或未解决情绪的表现。它们实际上是心理的一种自我保护机制，帮助我们在安全的环境中处理负面情绪。如果经常做噩梦，建议关注生活中的压力源，并考虑通过放松技巧改善睡眠质量。";
        } else if (q.contains("预示") || q.contains("预言")) {
            return "从科学角度看，梦境不能预测未来，但它们确实可以反映我们对未来的期望、担忧或潜意识的感知。有时梦境中的'预示'实际上是我们大脑对当前信息的整合和推演。";
        } else if (q.contains("记住")) {
            return "要更好地记住梦境，可以尝试：1) 在床边放置笔记本，醒来后立即记录；2) 保持规律的睡眠时间；3) 睡前提醒自己想要记住梦境；4) 避免闹钟突然惊醒，尝试自然醒来。";
        } else if (q.contains("颜色")) {
            return "研究表明大多数人的梦境是有颜色的，但我们往往不会特别注意到。彩色梦境被认为与更强烈的情感体验和创造性思维相关。如果您能记住梦中的颜色，这往往意味着该梦境对您有特殊意义。";
        } else {
            return "这是一个很有意思的问题！梦境分析是一个复杂而个性化的过程。每个人的梦境都带有独特的个人意义，建议结合自己的生活经历和情感状态来理解梦境。如果您想深入探索，可以咨询专业的心理咨询师。";
        }
    }

    private String truncate(String text, int length) {
        if (text == null || text.length() <= length)
            return text;
        return text.substring(0, length) + "...";
    }

    // === DTO 类 ===

    @Data
    public static class AnalysisRequest {
        private String dreamId;
        private String report;
        private String characters;
        private String emotions;
    }

    @Data
    public static class AnalysisResult {
        private String dreamId;
        private String reportExcerpt;
        private String overallSummary;
        private EmotionalAnalysis emotionalAnalysis;
        private List<SymbolInfo> symbolInterpretation;
        private List<String> personalInsights;
        private List<String> suggestions;
        private int confidence;
    }

    @Data
    public static class EmotionalAnalysis {
        private String primary;
        private int intensity;
        private List<String> secondary;
        private String explanation;
    }

    @Data
    public static class SymbolInfo {
        private String symbol;
        private String meaning;
        private String context;
    }

    @Data
    public static class ChatResponse {
        private String question;
        private String answer;
        private Date timestamp;
    }
}

文件6：DreamService.java（梦境服务层）
--------------------------------------------------------------------------------
package com.minddream.service;

import com.minddream.entity.Dream;
import com.minddream.dto.SearchRequest;
import com.minddream.repository.DreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 梦境业务逻辑层
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DreamService {

    private final DreamRepository dreamRepository;

    /**
     * 分页获取所有梦境
     */
    public Page<Dream> getAllDreams(int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return dreamRepository.findAll(pageable);
    }

    /**
     * 根据ID获取梦境
     */
    public Optional<Dream> getDreamById(String id) {
        return dreamRepository.findById(id);
    }

    /**
     * 高级搜索
     */
    public Page<Dream> searchDreams(SearchRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        return dreamRepository.searchDreams(
                request.getGender(),
                request.getAgeGroup(),
                request.getTimePeriod(),
                request.getKeyword(),
                pageable);
    }

    /**
     * 获取随机梦境
     */
    public List<Dream> getRandomDreams(int limit) {
        return dreamRepository.findRandomDreams(limit);
    }

    /**
     * 删除梦境
     */
    @Transactional
    public void deleteDream(String id) {
        dreamRepository.deleteById(id);
    }

    /**
     * 保存梦境
     */
    @Transactional
    public Dream saveDream(Dream dream) {
        return dreamRepository.save(dream);
    }

    /**
     * 获取总数
     */
    public long getTotalCount() {
        return dreamRepository.count();
    }
}

文件7：DreamRepository.java（梦境数据访问层）
--------------------------------------------------------------------------------
package com.minddream.repository;

import com.minddream.entity.Dream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 梦境数据访问层
 */
@Repository
public interface DreamRepository extends JpaRepository<Dream, String> {

    /**
     * 根据性别分页查询
     */
    Page<Dream> findByGender(String gender, Pageable pageable);

    /**
     * 根据年龄组分页查询
     */
    Page<Dream> findByAgeGroup(String ageGroup, Pageable pageable);

    /**
     * 多条件搜索
     */
    @Query("SELECT d FROM Dream d WHERE " +
            "(:gender IS NULL OR d.gender = :gender) AND " +
            "(:ageGroup IS NULL OR d.ageGroup = :ageGroup) AND " +
            "(:timePeriod IS NULL OR d.timePeriod LIKE %:timePeriod%) AND " +
            "(:keyword IS NULL OR d.report LIKE %:keyword%)")
    Page<Dream> searchDreams(@Param("gender") String gender,
            @Param("ageGroup") String ageGroup,
            @Param("timePeriod") String timePeriod,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * 统计性别分布
     */
    @Query("SELECT d.gender, COUNT(d) FROM Dream d GROUP BY d.gender")
    List<Object[]> countByGender();

    /**
     * 统计年龄分布
     */
    @Query("SELECT d.ageGroup, COUNT(d) FROM Dream d GROUP BY d.ageGroup")
    List<Object[]> countByAgeGroup();

    /**
     * 获取随机梦境
     */
    @Query(value = "SELECT * FROM dreams ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Dream> findRandomDreams(@Param("limit") int limit);
}

文件8：StatisticsRepository.java（统计数据访问层）
--------------------------------------------------------------------------------
package com.minddream.repository;

import com.minddream.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 统计数据访问层
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    /**
     * 根据统计类型查询
     */
    List<Statistics> findByStatType(String statType);

    /**
     * 根据类型和键查询
     */
    Statistics findByStatTypeAndStatKey(String statType, String statKey);
}

文件9：SearchRequest.java（搜索请求DTO）
--------------------------------------------------------------------------------
package com.minddream.dto;

import lombok.Data;

/**
 * 搜索请求 DTO
 */
@Data
public class SearchRequest {

    private String gender; // 性别: M/F
    private String ageGroup; // 年龄组: A/C/O
    private String timePeriod; // 时间段
    private String keyword; // 关键词
    private Integer page = 0; // 页码（从0开始）
    private Integer size = 10; // 每页数量
    private String sortBy = "createdAt"; // 排序字段
    private String sortOrder = "desc"; // 排序方向: asc/desc
}

文件10：StatisticsDTO.java（统计数据DTO）
--------------------------------------------------------------------------------
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

文件11：CorsConfig.java（跨域配置）
--------------------------------------------------------------------------------
package com.minddream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有域名跨域
        config.addAllowedOriginPattern("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有HTTP方法
        config.addAllowedMethod("*");

        // 允许携带凭证
        config.setAllowCredentials(true);

        // 配置路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

文件12：SwaggerConfig.java（Swagger配置）
--------------------------------------------------------------------------------
package com.minddream.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MindDream API")
                        .description("心理梦境分析平台 RESTful API 文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MindDream Team")
                                .email("support@minddream.com")));
    }
}

文件13：DataImporter.java（数据导入工具）
--------------------------------------------------------------------------------
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

文件14：application.yml（后端配置文件）
--------------------------------------------------------------------------------
spring:
  application:
    name: minddream-backend

  datasource:
    url: jdbc:mysql://localhost:3306/minddream?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: wsxhr666
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false

server:
  port: 8080
  servlet:
    context-path: /api

# MyBatis Plus 配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto

# Swagger/OpenAPI 配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  api-docs:
    path: /api-docs

# 日志配置
logging:
  level:
    com.minddream: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
  charset:
    console: UTF-8

文件15：schema.sql（数据库建表脚本）
--------------------------------------------------------------------------------
-- 创建数据库
CREATE DATABASE IF NOT EXISTS minddream DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE minddream;

-- 梦境数据表
CREATE TABLE IF NOT EXISTS dreams (
    id VARCHAR(50) PRIMARY KEY COMMENT '梦境唯一标识',
    name VARCHAR(200) COMMENT '梦境者名称',
    dream_number INT COMMENT '梦境编号',
    time_period VARCHAR(100) COMMENT '时间段',
    birth_year VARCHAR(50) COMMENT '出生年份',
    gender CHAR(1) COMMENT '性别: M-男性, F-女性',
    age_group CHAR(1) COMMENT '年龄组: A-成人, C-儿童, O-老年',
    report TEXT COMMENT '梦境报告内容',
    characters_raw VARCHAR(500) COMMENT '角色原始编码',
    characters_cn TEXT COMMENT '角色中文描述',
    emotions_raw VARCHAR(500) COMMENT '情感原始编码',
    emotions_cn TEXT COMMENT '情感中文描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_gender (gender),
    INDEX idx_age (age_group),
    INDEX idx_time (time_period),
    INDEX idx_birth_year (birth_year),
    FULLTEXT idx_report (report)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='梦境数据表';

-- 统计数据表
CREATE TABLE IF NOT EXISTS statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    stat_type VARCHAR(50) NOT NULL COMMENT '统计类型: gender, age, emotion, character, timeline',
    stat_key VARCHAR(100) NOT NULL COMMENT '统计键',
    stat_value VARCHAR(500) COMMENT '统计值',
    count INT DEFAULT 0 COMMENT '计数',
    percentage DECIMAL(5,2) COMMENT '百分比',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (stat_type),
    INDEX idx_key (stat_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据表';

文件16：main.js（前端主入口）
--------------------------------------------------------------------------------
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/styles/typography.css'
import './assets/styles/static-gradients.css'
import './assets/styles/apple-glass.css'
import './assets/styles/spline-integration.css'
import './assets/styles/main.css'
import './assets/styles/ghibli.css'
import './assets/styles/dream-deep.css'
import './assets/styles/performance.css'
import './assets/styles/apple-unified.css'

// Import Dream Flow Components
import GlassCard from './components/GlassCard.vue'
import DreamButton from './components/DreamButton.vue'
import DreamInput from './components/DreamInput.vue'
import DreamLoader from './components/DreamLoader.vue'
import DreamTag from './components/DreamTag.vue'

const app = createApp(App)

// Register Dream Flow Components Globally
app.component('GlassCard', GlassCard)
app.component('DreamButton', DreamButton)
app.component('DreamInput', DreamInput)
app.component('DreamLoader', DreamLoader)
app.component('DreamTag', DreamTag)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(router)
app.mount('#app')

文件17：router/index.js（前端路由配置）
--------------------------------------------------------------------------------
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
    },
    {
        path: '/dreams',
        name: 'DreamList',
        component: () => import('../views/DreamList.vue')
    },
    {
        path: '/dreams/:id',
        name: 'DreamDetail',
        component: () => import('../views/DreamDetail.vue')
    },
    {
        path: '/dreams/:id/analyze',
        name: 'AIAnalysis',
        component: () => import('../views/AIAnalysis.vue')
    },
    {
        path: '/search',
        name: 'Search',
        component: () => import('../views/Search.vue')
    },
    {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        }
        return { top: 0, behavior: 'smooth' }
    }
})

export default router

文件18：dream.js（前端API封装）
--------------------------------------------------------------------------------
import axios from 'axios'

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
api.interceptors.request.use(
    config => {
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
api.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        console.error('API Error:', error)
        return Promise.reject(error)
    }
)

// 梦境相关 API
export const dreamAPI = {
    // 获取梦境列表
    getDreams(page = 0, size = 10, sortBy = 'createdAt', sortOrder = 'desc') {
        return api.get('/dreams', { params: { page, size, sortBy, sortOrder } })
    },

    // 获取梦境详情
    getDreamById(id) {
        return api.get(`/dreams/${id}`)
    },

    // 高级搜索
    searchDreams(searchRequest) {
        return api.post('/dreams/search', searchRequest)
    },

    // 随机获取梦境
    getRandomDreams(limit = 1) {
        return api.get('/dreams/random', { params: { limit } })
    },

    // 获取总数
    getTotalCount() {
        return api.get('/dreams/count')
    },

    // 创建梦境
    createDream(dream) {
        return api.post('/dreams', dream)
    },

    // 删除梦境
    deleteDream(id) {
        return api.delete(`/dreams/${id}`)
    }
}

// 统计相关 API
export const statisticsAPI = {
    // 获取总体统计
    getOverview() {
        return api.get('/statistics/overview')
    },

    // 获取性别分布
    getGenderDistribution() {
        return api.get('/statistics/gender')
    },

    // 获取年龄分布
    getAgeDistribution() {
        return api.get('/statistics/age')
    },

    // 获取情感分布
    getEmotionDistribution() {
        return api.get('/statistics/emotions')
    },

    // 获取情感光谱（全部情感 + 维度分类）
    getEmotionSpectrum() {
        return api.get('/statistics/spectrum')
    },

    // 获取性别×情感交叉分析
    getGenderEmotionCross() {
        return api.get('/statistics/gender-emotions')
    },

    // 获取角色分布
    getCharacterDistribution() {
        return api.get('/statistics/characters')
    }
}

export default api

================================================================================
前30页代码结束
================================================================================
