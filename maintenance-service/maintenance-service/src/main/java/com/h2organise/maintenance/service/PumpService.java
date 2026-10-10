package com.h2organise.maintenance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h2organise.maintenance.dto.PumpRequest;
import com.h2organise.maintenance.dto.PumpResponse;
import com.h2organise.maintenance.entity.Pump;
import com.h2organise.maintenance.entity.PumpStatus;
import com.h2organise.maintenance.exception.ResourceNotFoundException;
import com.h2organise.maintenance.repository.PumpRepository;

@Service
public class PumpService {

    private final PumpRepository pumpRepository;

    public PumpService(PumpRepository pumpRepository) {
        this.pumpRepository = pumpRepository;
    }

    // Both filters are optional and are combined with AND
    @Transactional(readOnly = true)
    public List<PumpResponse> getPumps(Long zoneId, PumpStatus status) {
        Pump probe = new Pump();
        probe.setZoneId(zoneId);
        probe.setStatus(status);

        List<PumpResponse> responses = new ArrayList<>();
        for (Pump pump : pumpRepository.findAll(Example.of(probe), Sort.by("id"))) {
            responses.add(toResponse(pump));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public PumpResponse getPump(Long id) {
        return toResponse(findPump(id));
    }

    @Transactional
    public PumpResponse createPump(PumpRequest request) {
        Pump pump = new Pump();
        copyDetails(request, pump);
        pump.setStatus(request.getStatus() != null ? request.getStatus() : PumpStatus.WORKING);
        return toResponse(pumpRepository.save(pump));
    }

    @Transactional
    public PumpResponse updatePump(Long id, PumpRequest request) {
        Pump pump = findPump(id);
        copyDetails(request, pump);
        if (request.getStatus() != null) {
            pump.setStatus(request.getStatus());
        }
        return toResponse(pumpRepository.save(pump));
    }

    @Transactional
    public void deletePump(Long id) {
        pumpRepository.delete(findPump(id));
    }

    private Pump findPump(Long id) {
        return pumpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pump not found with id " + id));
    }

    private void copyDetails(PumpRequest request, Pump pump) {
        pump.setName(request.getName().trim());
        pump.setZoneId(request.getZoneId());
        pump.setSourceId(request.getSourceId());
        pump.setCapacityLph(request.getCapacityLph());
        pump.setInstalledDate(request.getInstalledDate());
    }

    private PumpResponse toResponse(Pump pump) {
        PumpResponse response = new PumpResponse();
        response.setId(pump.getId());
        response.setName(pump.getName());
        response.setZoneId(pump.getZoneId());
        response.setSourceId(pump.getSourceId());
        response.setCapacityLph(pump.getCapacityLph());
        response.setInstalledDate(pump.getInstalledDate());
        response.setStatus(pump.getStatus());
        return response;
    }
}
