package com.h2organise.quality.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.quality.dto.InspectionRequest;
import com.h2organise.quality.dto.InspectionResponse;
import com.h2organise.quality.entity.Inspection;
import com.h2organise.quality.entity.InspectionResult;
import com.h2organise.quality.exception.ResourceNotFoundException;
import com.h2organise.quality.repository.InspectionRepository;

@Service
public class InspectionService {

    private static final int DUE_WITHIN_DAYS = 30;

    private final InspectionRepository inspectionRepository;

    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Transactional(readOnly = true)
    public List<InspectionResponse> getInspections(Long zoneId, InspectionResult result) {
        Inspection probe = new Inspection();
        probe.setZoneId(zoneId);
        probe.setResult(result);
        return toResponses(inspectionRepository.findAll(Example.of(probe), Sort.by("id")));
    }

    // Inspections whose next due date falls within the next 30 days (today included)
    @Transactional(readOnly = true)
    public List<InspectionResponse> getDueInspections() {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(DUE_WITHIN_DAYS);
        return toResponses(inspectionRepository.findByNextDueDateBetweenOrderByNextDueDateAsc(today, limit));
    }

    @Transactional(readOnly = true)
    public InspectionResponse getInspection(Long id) {
        return toResponse(findInspection(id));
    }

    @Transactional
    public InspectionResponse createInspection(InspectionRequest request) {
        Inspection inspection = new Inspection();
        copyDetails(request, inspection);
        return toResponse(inspectionRepository.save(inspection));
    }

    @Transactional
    public InspectionResponse updateInspection(Long id, InspectionRequest request) {
        Inspection inspection = findInspection(id);
        copyDetails(request, inspection);
        return toResponse(inspectionRepository.save(inspection));
    }

    @Transactional
    public void deleteInspection(Long id) {
        inspectionRepository.delete(findInspection(id));
    }

    // ---------- helpers ----------

    private Inspection findInspection(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with id " + id));
    }

    private void copyDetails(InspectionRequest request, Inspection inspection) {
        inspection.setSourceId(request.getSourceId());
        inspection.setZoneId(request.getZoneId());
        inspection.setInspectionDate(request.getInspectionDate());
        inspection.setInspectorName(request.getInspectorName());
        inspection.setResult(request.getResult());
        inspection.setRemarks(request.getRemarks());
        inspection.setNextDueDate(request.getNextDueDate());
    }

    private List<InspectionResponse> toResponses(List<Inspection> inspections) {
        List<InspectionResponse> responses = new ArrayList<>();
        for (Inspection inspection : inspections) {
            responses.add(toResponse(inspection));
        }
        return responses;
    }

    private InspectionResponse toResponse(Inspection inspection) {
        InspectionResponse response = new InspectionResponse();
        response.setId(inspection.getId());
        response.setSourceId(inspection.getSourceId());
        response.setZoneId(inspection.getZoneId());
        response.setInspectionDate(inspection.getInspectionDate());
        response.setInspectorName(inspection.getInspectorName());
        response.setResult(inspection.getResult());
        response.setRemarks(inspection.getRemarks());
        response.setNextDueDate(inspection.getNextDueDate());
        return response;
    }
}
