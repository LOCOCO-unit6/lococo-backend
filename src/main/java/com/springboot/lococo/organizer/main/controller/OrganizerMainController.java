package com.springboot.lococo.organizer.main.controller;


import com.springboot.lococo.organizer.main.domain.OrganizerMainReview;
import com.springboot.lococo.organizer.main.dto.*;
import com.springboot.lococo.organizer.main.service.OrganizerMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer/main")
public class OrganizerMainController {

    private final OrganizerMainService service;

    @PutMapping
    public Long upsertMain(@RequestBody MainPageUpdateRequest req) {
        return service.upsertMain(req);
    }

    @GetMapping("/reviews")
    public PageResponse<ReviewResponse> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort
    ) {
        String[] s = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(s.length > 1 ? s[1] : "DESC"), s[0]);

        Page<OrganizerMainReview> result = service.getReviews(page, size, sortObj);

        List<ReviewResponse> content = result.getContent().stream().map(r -> {
            ReviewResponse dto = new ReviewResponse();
            dto.setId(r.getId());
            dto.setUserName(r.getUserName());
            dto.setRating(r.getRating());
            dto.setComment(r.getComment());
            dto.setCreatedAt(r.getCreatedAt());
            return dto;
        }).toList();

        return new PageResponse<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }
}