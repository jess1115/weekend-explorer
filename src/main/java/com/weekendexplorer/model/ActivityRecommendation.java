package com.weekendexplorer.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents an activity with recommendation score")
public class ActivityRecommendation {
    @Schema(
            description = "The activity recommended to the user",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final Activity activity;

    @Schema(
            description = "Recommendation score of the activity",
            example = "14",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final int score;

    public ActivityRecommendation(Activity activity, int score) {
        this.activity = activity;
        this.score = score;
    }

    public Activity getActivity(){return this.activity;}

    public int getScore(){return this.score;}
}
