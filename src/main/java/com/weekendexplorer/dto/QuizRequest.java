package com.weekendexplorer.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Schema(description = "User's answers to the weekend activity recommendation quiz")
public class QuizRequest {

    @NotBlank
    @Schema(
            description = "Preferred weekend direction",
            allowableValues = {
                    "creative",
                    "adventurous",
                    "cultural",
                    "relaxing"
            },
            example = "creative"
    )
    private String weekendDirection;

    @NotBlank
    @Schema(
            description = "Preferred energy level",
            allowableValues = {
                    "low_energy",
                    "moderate_energy",
                    "high_energy"
            },
            example = "low_energy"
    )
    private String energyLevel;

    @NotBlank
    @Schema(description = "Preferred environment",
            allowableValues = {
                    "indoors",
                    "outdoors",
                    "any"
            },
            example = "indoors"
    )
    private String environment;

    @NotEmpty
    @ArraySchema(
            arraySchema = @Schema(description = "User's interests"),
            schema = @Schema(
                    allowableValues = {
                            "making",
                            "art_culture",
                            "nature",
                            "active",
                            "exploration"
                    }
            )
    )
    private Set<String> interests;


    @NotBlank
    @Schema(description = "Learning preference",
            allowableValues = {
                    "definitely",
                    "maybe",
                    "just_enjoy"
            },
            example = "definitely"
    )
    private String learningPreference;

    @NotBlank
    @Schema(description = "Preferred adventure level",
            allowableValues = {
                    "comfortable",
                    "curious",
                    "adventurous"
            },
            example = "comfortable"
    )
    private String adventureLevel;


    @NotBlank
    @Schema(description = "Maximum duration hours",
            allowableValues = {
                    "under_2_hours",
                    "2_to_3_hours",
                    "3_to_5_hours",
                    "half_day"
            },
            example = "under_2_hours"
    )
    private String maxDuration;

    @NotBlank
    @Schema(description = "Maximum price",
            allowableValues = {
                    "free",
                    "under_500",
                    "500_to_1000",
                    "1000_to_1500",
                    "flexible"
            },
            example = "free"
    )
    private String maxPrice;

    @NotBlank
    @Schema(description = "Preferred companion",
            allowableValues = {
                    "myself",
                    "partner",
                    "friends",
                    "family",
                    "anyone"
            },
            example = "myself"
    )
    private String companion;


    @NotEmpty
    @ArraySchema(
            arraySchema = @Schema(description = "Preferred atmosphere"),
            schema = @Schema(
                    allowableValues = {
                            "peaceful",
                            "inspiring",
                            "natural",
                            "exciting",
                            "educational"
                    }
            )
    )
    private Set<String> atmosphere;

    public void setWeekendDirection(String weekendDirection) { this.weekendDirection = weekendDirection; }

    public String getWeekendDirection() { return this.weekendDirection; }

    public void setEnergyLevel(String energyLevel) { this.energyLevel = energyLevel; }

    public String getEnergyLevel() { return this.energyLevel; }

    public void setEnvironment(String environment) { this.environment = environment; }

    public String getEnvironment() { return this.environment; }

    public void setInterests(Set<String> interests) { this.interests = interests; }

    public Set<String> getInterests() { return this.interests; }

    public void setLearningPreference(String learningPreference) { this.learningPreference = learningPreference; }

    public String getLearningPreference() {return this.learningPreference; }

    public void setAdventureLevel(String adventureLevel) { this.adventureLevel = adventureLevel; }

    public String getAdventureLevel() {return this.adventureLevel; }

    public void setMaxDuration(String maxDuration) { this.maxDuration = maxDuration; }

    public String getMaxDuration() { return this.maxDuration; }

    public void setMaxPrice(String maxPrice) { this.maxPrice = maxPrice; }

    public String getMaxPrice() { return this.maxPrice; }

    public void setCompanion(String companion) { this.companion = companion; }

    public String getCompanion() { return this.companion; }

    public void setAtmosphere(Set<String> atmosphere) { this.atmosphere = atmosphere; }

    public Set<String> getAtmosphere() { return this.atmosphere; }
}
