package com.h2organise.resource.service;

import com.h2organise.resource.dto.LevelRequest;
import com.h2organise.resource.dto.LevelResponse;
import com.h2organise.resource.entity.LevelReading;
import com.h2organise.resource.entity.WaterSource;
import com.h2organise.resource.repository.LevelReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelReadingService {

    private final LevelReadingRepository repository;
    private final WaterSourceService sourceService;

    // all filters are optional, from / to are dates and both days are included
    public List<LevelResponse> findAll(Long sourceId, LocalDate from, LocalDate to) {
        Specification<LevelReading> spec = (root, query, cb) -> cb.conjunction();
        if (sourceId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("source").get("id"), sourceId));
        }
        if (from != null) {
            LocalDateTime start = from.atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("readingTime"), start));
        }
        if (to != null) {
            // everything before the start of the next day
            LocalDateTime end = to.plusDays(1).atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("readingTime"), end));
        }
        // newest first
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "readingTime")).stream()
                .map(LevelResponse::from).toList();
    }

    // newest reading for each source
    // pass sourceId for one source, zoneId for all sources of a zone, or nothing for every source
    // always returns a list so the web-app handles it the same way each time
    public List<LevelResponse> latest(Long sourceId, Long zoneId) {
        List<WaterSource> sources;
        if (sourceId != null) {
            // this throws 404 if the source doesn't exist
            sources = List.of(sourceService.getEntity(sourceId));
        } else {
            sources = sourceService.findEntities(zoneId, null, null);
        }
        List<LevelResponse> result = new ArrayList<>();
        for (WaterSource s : sources) {
            repository.findFirstBySource_IdOrderByReadingTimeDesc(s.getId())
                    .ifPresent(r -> result.add(LevelResponse.from(r)));
        }
        return result;
    }

    @Transactional
    public LevelResponse create(LevelRequest r) {
        // this throws 404 if the source id doesn't exist
        WaterSource source = sourceService.getEntity(r.sourceId());

        // a source can't hold more water than its capacity
        if (r.currentVolume() > source.getTotalCapacity()) {
            throw new IllegalArgumentException("currentVolume cannot be more than the total capacity ("
                    + source.getTotalCapacity() + " ML)");
        }

        LevelReading reading = new LevelReading();
        reading.setSource(source);
        // use the time sent by the client, or now if it's missing
        reading.setReadingTime(r.readingTime() != null ? r.readingTime() : LocalDateTime.now());
        reading.setCurrentVolume(r.currentVolume());
        // inflow and outflow are optional, treat missing as 0
        reading.setInflowRate(r.inflowRate() != null ? r.inflowRate() : 0.0);
        reading.setOutflowRate(r.outflowRate() != null ? r.outflowRate() : 0.0);
        reading.setRecordedBy(r.recordedBy());
        // the percent is always calculated here, never trusted from the client
        reading.setLevelPercent(calculatePercent(r.currentVolume(), source.getTotalCapacity()));

        return LevelResponse.from(repository.save(reading));
    }

    // volume / capacity * 100, rounded to 2 decimals (e.g. 110 of 500 -> 22.0)
    private double calculatePercent(double volume, double capacity) {
        return Math.round(volume / capacity * 100.0 * 100.0) / 100.0;
    }
}
