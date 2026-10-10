package com.h2organise.distribution.service;

import com.h2organise.distribution.dto.ScheduleRequest;
import com.h2organise.distribution.dto.ScheduleResponse;
import com.h2organise.distribution.entity.DistributionSchedule;
import com.h2organise.distribution.entity.ScheduleStatus;
import com.h2organise.distribution.exception.ResourceNotFoundException;
import com.h2organise.distribution.repository.DistributionScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistributionScheduleService {

    private final DistributionScheduleRepository repository;
    private final SupplyZoneService zoneService;

    // all three filters are optional
    public List<ScheduleResponse> findAll(Long zoneId, ScheduleStatus status, LocalDate date) {
        Specification<DistributionSchedule> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zone").get("id"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplyDate"), date));
        }
        // earliest first, so the "next supply cycle" is at the top
        return repository.findAll(spec, Sort.by("supplyDate", "startTime")).stream()
                .map(ScheduleResponse::from).toList();
    }

    public ScheduleResponse findById(Long id) {
        return ScheduleResponse.from(getEntity(id));
    }

    private DistributionSchedule getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id " + id));
    }

    @Transactional
    public ScheduleResponse create(ScheduleRequest request) {
        DistributionSchedule schedule = new DistributionSchedule();
        apply(schedule, request);
        return ScheduleResponse.from(repository.save(schedule));
    }

    // the web-app uses this to reduce or reschedule the supply
    @Transactional
    public ScheduleResponse update(Long id, ScheduleRequest request) {
        DistributionSchedule schedule = getEntity(id);
        apply(schedule, request);
        return ScheduleResponse.from(repository.save(schedule));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        repository.flush();
    }

    private void apply(DistributionSchedule s, ScheduleRequest r) {
        // simple sanity check, supply can't end before it starts
        if (!r.endTime().isAfter(r.startTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        s.setZone(zoneService.getEntity(r.zoneId()));
        s.setSupplyDate(r.supplyDate());
        s.setStartTime(r.startTime());
        s.setEndTime(r.endTime());
        s.setPlannedVolume(r.plannedVolume());
        s.setRemarks(r.remarks());
        if (r.status() != null) {
            s.setStatus(r.status());
        }
    }
}
