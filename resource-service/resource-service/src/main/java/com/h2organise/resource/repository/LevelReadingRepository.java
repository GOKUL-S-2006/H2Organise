package com.h2organise.resource.repository;

import com.h2organise.resource.entity.LevelReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LevelReadingRepository extends JpaRepository<LevelReading, Long>, JpaSpecificationExecutor<LevelReading> {

    // spring writes the query from the method name:
    // newest reading of one source (the "_" means source.id)
    Optional<LevelReading> findFirstBySource_IdOrderByReadingTimeDesc(Long sourceId);
}
