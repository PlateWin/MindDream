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
