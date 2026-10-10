package com.h2organise.distribution.dto;

import com.h2organise.distribution.entity.SupplyZone;
import com.h2organise.distribution.entity.ZoneStatus;

// data we send back for a zone
public record ZoneResponse(
        Long id,
        String name,
        String description,
        Integer population,
        Integer households,
        Long primarySourceId,
        ZoneStatus status) {

    // converts the entity into a response object
    public static ZoneResponse from(SupplyZone z) {
        return new ZoneResponse(z.getId(), z.getName(), z.getDescription(), z.getPopulation(),
                z.getHouseholds(), z.getPrimarySourceId(), z.getStatus());
    }
}
