package com.h2organise.complaint.entity;

// what the citizen is complaining about (leak reports are also complaints with type LEAKAGE)
public enum ComplaintType {
    LEAKAGE, LOW_PRESSURE, QUALITY, NO_SUPPLY, OTHER
}
