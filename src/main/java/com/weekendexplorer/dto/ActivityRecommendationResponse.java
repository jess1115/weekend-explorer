package com.weekendexplorer.dto;

import com.weekendexplorer.dto.ActivityResponse;

public record ActivityRecommendationResponse(
        ActivityResponse activity,
        int score
) {}