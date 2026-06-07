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
