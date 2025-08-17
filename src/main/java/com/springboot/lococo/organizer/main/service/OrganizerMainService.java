package com.springboot.lococo.organizer.main.service;


import com.springboot.lococo.organizer.main.domain.*;
import com.springboot.lococo.organizer.main.dto.*;
import com.springboot.lococo.organizer.main.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerMainService {

    private final OrganizerMainRepository mainRepo;
    private final OrganizerMainReviewRepository reviewRepo;

    @Transactional
    public Long upsertMain(MainPageUpdateRequest req) {
        OrganizerMain main = mainRepo.findById(1L).orElseGet(OrganizerMain::new);

        main.setHeroTitle(req.getHeroTitle());
        main.setHeroSubtitle(req.getHeroSubtitle());
        main.setServiceGuide(req.getServiceGuide());
        main.setTrustMetrics(req.getTrustMetrics());
        main.setFooterText(req.getFooterText());

        List<FeatureCard> cards = req.getFeatureCards() == null ? List.of()
                : req.getFeatureCards().stream().map(d -> {
            FeatureCard c = new FeatureCard();
            c.setIcon(d.getIcon());
            c.setTitle(d.getTitle());
            c.setDescription(d.getDescription());
            c.setDisplayOrder(d.getDisplayOrder());
            return c;
        }).toList();

        List<CurationItem> items = req.getCurationItems() == null ? List.of()
                : req.getCurationItems().stream().map(d -> {
            CurationItem i = new CurationItem();
            i.setName(d.getName());
            i.setImageUrl(d.getImageUrl());
            i.setDescription(d.getDescription());
            i.setDisplayOrder(d.getDisplayOrder());
            return i;
        }).toList();

        main.setFeatureCards(cards);
        main.setCurationItems(items);

        return mainRepo.save(main).getId();
    }

    public Page<OrganizerMainReview> getReviews(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewRepo.findAll(pageable);
    }
}