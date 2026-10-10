package com.h2organise.complaint.controller;

import com.h2organise.complaint.dto.ComplaintRequest;
import com.h2organise.complaint.dto.ComplaintResponse;
import com.h2organise.complaint.dto.ComplaintStatusRequest;
import com.h2organise.complaint.dto.ComplaintSummary;
import com.h2organise.complaint.dto.ComplaintUpdateResponse;
import com.h2organise.complaint.entity.ComplaintStatus;
import com.h2organise.complaint.entity.ComplaintType;
import com.h2organise.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for /complaints
@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService service;

    // example: GET /complaints?zoneId=1&status=REGISTERED
    // or GET /complaints?citizenId=2 for "My complaints"
    @GetMapping
    public List<ComplaintResponse> list(
            @RequestParam(name = "zoneId", required = false) Long zoneId,
            @RequestParam(name = "status", required = false) ComplaintStatus status,
            @RequestParam(name = "citizenId", required = false) Long citizenId,
            @RequestParam(name = "type", required = false) ComplaintType type) {
        return service.findAll(zoneId, status, citizenId, type);
    }

    // counts per status for the dashboard
    // (keep this above /{id} so "summary" isn't treated as an id)
    @GetMapping("/summary")
    public ComplaintSummary summary(@RequestParam(name = "zoneId", required = false) Long zoneId) {
        return service.summary(zoneId);
    }

    @GetMapping("/{id}")
    public ComplaintResponse get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // status history, used to track a complaint
    @GetMapping("/{id}/updates")
    public List<ComplaintUpdateResponse> updates(@PathVariable("id") Long id) {
        return service.getUpdates(id);
    }

    // returns 201 Created, status starts as REGISTERED
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComplaintResponse create(@Valid @RequestBody ComplaintRequest request) {
        return service.create(request);
    }

    // edits the details (not the status)
    @PutMapping("/{id}")
    public ComplaintResponse update(@PathVariable("id") Long id, @Valid @RequestBody ComplaintRequest request) {
        return service.update(id, request);
    }

    // assign, start work, resolve or reject, also adds a history row
    @PutMapping("/{id}/status")
    public ComplaintResponse changeStatus(@PathVariable("id") Long id,
                                          @Valid @RequestBody ComplaintStatusRequest request) {
        return service.changeStatus(id, request);
    }

    // returns 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
