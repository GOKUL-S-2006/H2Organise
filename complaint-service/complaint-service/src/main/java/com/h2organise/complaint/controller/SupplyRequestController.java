package com.h2organise.complaint.controller;

import com.h2organise.complaint.dto.SupplyRequestRequest;
import com.h2organise.complaint.dto.SupplyRequestResponse;
import com.h2organise.complaint.dto.SupplyStatusRequest;
import com.h2organise.complaint.entity.SupplyRequestStatus;
import com.h2organise.complaint.service.SupplyRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for /supply-requests
@RestController
@RequestMapping("/supply-requests")
@RequiredArgsConstructor
public class SupplyRequestController {

    private final SupplyRequestService service;

    // example: GET /supply-requests?zoneId=1&status=PENDING
    @GetMapping
    public List<SupplyRequestResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) SupplyRequestStatus status,
            @RequestParam(name = "citizenId", required = false) Long citizenId) {
        return service.findAll(zoneId, status, citizenId);
    }

    @GetMapping("/{id}")
    public SupplyRequestResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplyRequestResponse create(@Valid @RequestBody SupplyRequestRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public SupplyRequestResponse update(@PathVariable("id") Long id,
                                        @Valid @RequestBody SupplyRequestRequest request) {
        return service.update(id, request);
    }

    // change only the status, e.g. PENDING -> SCHEDULED
    @PutMapping("/{id}/status")
    public SupplyRequestResponse changeStatus(@PathVariable("id") Long id,
                                              @Valid @RequestBody SupplyStatusRequest request) {
        return service.changeStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
