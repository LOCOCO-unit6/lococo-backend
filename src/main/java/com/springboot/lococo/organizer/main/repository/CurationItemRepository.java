package com.springboot.lococo.organizer.main.repository;



import com.springboot.lococo.organizer.main.domain.CurationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurationItemRepository extends JpaRepository<CurationItem, Long> {}