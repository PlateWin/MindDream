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
