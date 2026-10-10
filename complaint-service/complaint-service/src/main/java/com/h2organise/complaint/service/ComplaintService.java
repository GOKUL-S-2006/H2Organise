package com.h2organise.complaint.service;

import com.h2organise.complaint.dto.ComplaintRequest;
import com.h2organise.complaint.dto.ComplaintResponse;
import com.h2organise.complaint.dto.ComplaintStatusRequest;
import com.h2organise.complaint.dto.ComplaintSummary;
import com.h2organise.complaint.dto.ComplaintUpdateResponse;
import com.h2organise.complaint.entity.Complaint;
import com.h2organise.complaint.entity.ComplaintPriority;
import com.h2organise.complaint.entity.ComplaintStatus;
import com.h2organise.complaint.entity.ComplaintType;
import com.h2organise.complaint.entity.ComplaintUpdate;
import com.h2organise.complaint.exception.ResourceNotFoundException;
import com.h2organise.complaint.repository.ComplaintRepository;
import com.h2organise.complaint.repository.ComplaintUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// business logic for complaints
// readOnly by default, methods that change data have their own @Transactional
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComplaintService {

    private final ComplaintRepository repository;
    private final ComplaintUpdateRepository updateRepository;

    // all four filters are optional
    public List<ComplaintResponse> findAll(Long zoneId, ComplaintStatus status, Long citizenId, ComplaintType type) {
        Specification<Complaint> spec = zoneSpec(zoneId);
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (citizenId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("citizenId"), citizenId));
        }
        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        // newest first
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt", "id")).stream()
                .map(ComplaintResponse::from).toList();
    }

    public ComplaintResponse findById(Long id) {
        return ComplaintResponse.from(getEntity(id));
    }

    private Complaint getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id " + id));
    }

    // history of a complaint, this is what the citizen sees when tracking
    public List<ComplaintUpdateResponse> getUpdates(Long id) {
        getEntity(id);   // 404 if the complaint doesn't exist
        return updateRepository.findByComplaint_IdOrderByUpdatedAtAscIdAsc(id).stream()
                .map(ComplaintUpdateResponse::from).toList();
    }

    // counts per status, optionally for one zone (used on the dashboard)
    public ComplaintSummary summary(Long zoneId) {
        List<Complaint> list = repository.findAll(zoneSpec(zoneId));
        return new ComplaintSummary(
                list.size(),
                count(list, ComplaintStatus.REGISTERED),
                count(list, ComplaintStatus.ASSIGNED),
                count(list, ComplaintStatus.IN_PROGRESS),
                count(list, ComplaintStatus.RESOLVED),
                count(list, ComplaintStatus.REJECTED));
    }

    @Transactional
    public ComplaintResponse create(ComplaintRequest request) {
        Complaint complaint = new Complaint();
        apply(complaint, request);
        // every new complaint starts as REGISTERED
        complaint.setStatus(ComplaintStatus.REGISTERED);
        complaint.setCreatedAt(LocalDateTime.now());
        // if the citizen didn't choose a priority, pick one from the type
        if (request.priority() == null) {
            complaint.setPriority(priorityFor(request.type()));
        }
        Complaint saved = repository.save(complaint);
        addHistory(saved, ComplaintStatus.REGISTERED, "Complaint received", "Citizen " + saved.getCitizenId());
        return ComplaintResponse.from(saved);
    }

    // edits the details only, the status is changed with changeStatus()
    @Transactional
    public ComplaintResponse update(Long id, ComplaintRequest request) {
        Complaint complaint = getEntity(id);
        apply(complaint, request);
        return ComplaintResponse.from(repository.save(complaint));
    }

    // REGISTERED -> ASSIGNED -> IN_PROGRESS -> RESOLVED (or REJECTED)
    // every change also adds a row to the history
    @Transactional
    public ComplaintResponse changeStatus(Long id, ComplaintStatusRequest request) {
        Complaint complaint = getEntity(id);

        // a closed complaint can't be changed again
        if (complaint.getStatus() == ComplaintStatus.RESOLVED || complaint.getStatus() == ComplaintStatus.REJECTED) {
            throw new IllegalArgumentException("Complaint is already closed (" + complaint.getStatus() + ")");
        }
        if (complaint.getStatus() == request.status()) {
            throw new IllegalArgumentException("Complaint is already " + request.status());
        }

        // assigning needs a name, the others can keep the current one
        boolean nameSent = request.assignedTo() != null && !request.assignedTo().isBlank();
        if (nameSent) {
            complaint.setAssignedTo(request.assignedTo());
        }
        if (request.status() == ComplaintStatus.ASSIGNED && complaint.getAssignedTo() == null) {
            throw new IllegalArgumentException("assignedTo is required when assigning a complaint");
        }

        complaint.setStatus(request.status());
        if (request.status() == ComplaintStatus.RESOLVED || request.status() == ComplaintStatus.REJECTED) {
            complaint.setResolvedAt(LocalDateTime.now());
            complaint.setResolutionNote(request.comment());
        }

        Complaint saved = repository.save(complaint);
        String by = request.updatedBy() != null ? request.updatedBy()
                : (saved.getAssignedTo() != null ? saved.getAssignedTo() : "Staff");
        addHistory(saved, request.status(), request.comment(), by);
        return ComplaintResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        Complaint complaint = getEntity(id);
        // the history rows point to the complaint, so they go first
        updateRepository.deleteByComplaint_Id(id);
        repository.delete(complaint);
        repository.flush();
    }

    // ---------- helpers ----------

    private void apply(Complaint c, ComplaintRequest r) {
        c.setCitizenId(r.citizenId());
        c.setZoneId(r.zoneId());
        c.setType(r.type());
        c.setTitle(r.title());
        c.setDescription(r.description());
        c.setLocation(r.location());
        if (r.priority() != null) {
            c.setPriority(r.priority());
        }
    }

    // no water and bad water hurt the most, so they come first
    private ComplaintPriority priorityFor(ComplaintType type) {
        if (type == ComplaintType.NO_SUPPLY || type == ComplaintType.QUALITY) {
            return ComplaintPriority.HIGH;
        }
        if (type == ComplaintType.LEAKAGE) {
            return ComplaintPriority.MEDIUM;
        }
        return ComplaintPriority.LOW;
    }

    private void addHistory(Complaint complaint, ComplaintStatus status, String comment, String updatedBy) {
        ComplaintUpdate update = new ComplaintUpdate();
        update.setComplaint(complaint);
        update.setStatus(status);
        update.setComment(comment);
        update.setUpdatedBy(updatedBy);
        update.setUpdatedAt(LocalDateTime.now());
        updateRepository.save(update);
    }

    // "match everything", or only one zone if zoneId is given
    private Specification<Complaint> zoneSpec(Long zoneId) {
        Specification<Complaint> spec = (root, query, cb) -> cb.conjunction();
        if (zoneId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("zoneId"), zoneId));
        }
        return spec;
    }

    private long count(List<Complaint> list, ComplaintStatus status) {
        return list.stream().filter(c -> c.getStatus() == status).count();
    }
}
