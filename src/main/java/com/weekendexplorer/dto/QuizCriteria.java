package com.weekendexplorer.dto;

import com.weekendexplorer.model.Environment;

import java.util.Set;

public class QuizCriteria {
    private final Set<String> weekendDirection;
    private final Set<String> energyLevel;
    private final Environment environment;
    private final Set<String> interests;
    private final String learningPreference;
    private final Set<String> adventureLevel;
    private final Integer maxDuration;
    private final Integer maxPrice;
    private final String companion;
    private final Set<String> atmosphere;

    public QuizCriteria(Set<String> weekendDirection, Set<String> energyLevel, Environment environment,
                        Set<String> interests, String learningPreference, Set<String> adventureLevel,
                        Integer maxDuration, Integer maxPrice, String companion, Set<String> atmosphere) {
        this.weekendDirection = Set.copyOf(weekendDirection);
        this.energyLevel = Set.copyOf(energyLevel);
        this.environment = environment;
        this.interests = Set.copyOf(interests);
        this.learningPreference = learningPreference;
        this.adventureLevel = Set.copyOf(adventureLevel);
        this.maxDuration = maxDuration;
        this.maxPrice = maxPrice;
        this.companion = companion;
        this.atmosphere = Set.copyOf(atmosphere);
    }


    public Set<String> getWeekendDirection() { return this.weekendDirection; }

    public Set<String> getEnergyLevel() { return this.energyLevel; }

    public Environment getEnvironment() { return this.environment; }

    public Set<String> getInterests() { return this.interests; }

    public String getLearningPreference() { return this.learningPreference; }

    public Set<String> getAdventureLevel() { return this.adventureLevel; }

    public Integer getMaxDuration() { return this.maxDuration; }

    public Integer getMaxPrice() { return this.maxPrice; }

    public String getCompanion() { return this.companion; }

    public Set<String> getAtmosphere() { return this.atmosphere; }


}
