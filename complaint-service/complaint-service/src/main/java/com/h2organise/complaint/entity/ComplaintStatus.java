package com.h2organise.complaint.entity;

// REGISTERED -> ASSIGNED -> IN_PROGRESS -> RESOLVED (or REJECTED)
public enum ComplaintStatus {
    REGISTERED, ASSIGNED, IN_PROGRESS, RESOLVED, REJECTED
}
