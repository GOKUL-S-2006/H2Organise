package com.h2organise.complaint.service;

import com.h2organise.complaint.dto.SupplyRequestRequest;
import com.h2organise.complaint.dto.SupplyRequestResponse;
import com.h2organise.complaint.entity.SupplyRequest;
import com.h2organise.complaint.entity.SupplyRequestStatus;
import com.h2organise.complaint.exception.ResourceNotFoundException;
import com.h2organise.complaint.repository.SupplyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplyRequestService {

    private final SupplyRequestRepository repository;

    // all three filters are optional
    public List<SupplyRequestResponse> findAll(Long zoneId, SupplyRequestStatus status, Long citizenId) {
        Specification<SupplyRequest> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (citizenId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("citizenId"), citizenId));
        }
        // newest first
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt", "id")).stream()
                .map(SupplyRequestResponse::from).toList();
    }

    public SupplyRequestResponse findById(Long id) {
        return SupplyRequestResponse.from(getEntity(id));
    }

    private SupplyRequest getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supply request not found with id " + id));
    }

    @Transactional
    public SupplyRequestResponse create(SupplyRequestRequest request) {
        SupplyRequest supplyRequest = new SupplyRequest();
        apply(supplyRequest, request);
        supplyRequest.setCreatedAt(LocalDateTime.now());
        return SupplyRequestResponse.from(repository.save(supplyRequest));
    }

    @Transactional
    public SupplyRequestResponse update(Long id, SupplyRequestRequest request) {
        SupplyRequest supplyRequest = getEntity(id);
        apply(supplyRequest, request);
        return SupplyRequestResponse.from(repository.save(supplyRequest));
    }

    // the engineer uses this to mark a request SCHEDULED or FULFILLED
    @Transactional
    public SupplyRequestResponse changeStatus(Long id, SupplyRequestStatus status) {
        SupplyRequest supplyRequest = getEntity(id);
        supplyRequest.setStatus(status);
        return SupplyRequestResponse.from(repository.save(supplyRequest));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        repository.flush();
    }

    private void apply(SupplyRequest s, SupplyRequestRequest r) {
        s.setCitizenId(r.citizenId());
        s.setZoneId(r.zoneId());
        s.setRequestType(r.requestType());
        s.setQuantityLitres(r.quantityLitres());
        s.setRequiredDate(r.requiredDate());
        s.setReason(r.reason());
        // if status is not sent, keep what it already has (PENDING for a new request)
        if (r.status() != null) {
            s.setStatus(r.status());
        }
    }
}
