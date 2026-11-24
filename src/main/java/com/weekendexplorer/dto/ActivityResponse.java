package com.weekendexplorer.dto;

import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;

import java.util.Set;

public record ActivityResponse(
        Integer id,
        String name,
        ActivityCategory category,
        int price,
        int durationHours,
        Environment environment,
        Set<String> tags,
        String description
) {
    public ActivityResponse {
        tags = Set.copyOf(tags);
    }
}