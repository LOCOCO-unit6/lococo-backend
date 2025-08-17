package com.springboot.lococo.content.repository;

import com.springboot.lococo.content.model.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

}
