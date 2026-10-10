package com.h2organise.complaint.dto;

// used by /complaints/summary for the dashboard cards
public record ComplaintSummary(
        long total,
        long registered,
        long assigned,
        long inProgress,
        long resolved,
        long rejected) {
}
