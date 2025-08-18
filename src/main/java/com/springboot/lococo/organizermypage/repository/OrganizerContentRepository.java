package com.springboot.lococo.organizermypage.repository;


import com.springboot.lococo.organizermypage.model.Content;
import com.springboot.lococo.organizermypage.model.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByAffiliationOrderByCreatedAtDesc(String affiliation);
    List<Content> findByAffiliationAndTypeOrderByCreatedAtDesc(String affiliation, ContentType type);
}