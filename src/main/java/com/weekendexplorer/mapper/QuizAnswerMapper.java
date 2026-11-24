package com.weekendexplorer.mapper;

import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.dto.QuizRequest;
import com.weekendexplorer.model.Environment;

import java.util.HashSet;
import java.util.Set;

public class QuizAnswerMapper {

    private static Set<String> mapWeekendDirection(String answer){
        return switch (answer) {
            case "creative" -> Set.of("creativity");
            case "adventurous" -> Set.of("adventure");
            case "cultural" -> Set.of("culture");
            case "relaxing" -> Set.of("relaxing");
            default -> throw new IllegalArgumentException("Invalid weekend direction: " + answer);
        };
    }

    private static Set<String> mapEnergyLevel(String answer){
        return switch (answer) {
            case "low_energy" -> Set.of("relaxing");
            case "moderate_energy" -> Set.of("exercise");
            case "high_energy" -> Set.of("challenge", "adventure");
            default -> throw new IllegalArgumentException("Invalid energy level: " + answer);
        };
    }

    private static Environment mapEnvironment(String answer) {
        return switch (answer) {
            case "indoors" -> Environment.INDOOR;
            case "outdoors" -> Environment.OUTDOOR;
            case "any" -> null;
            default -> throw new IllegalArgumentException("Invalid environment: " + answer);
        };
    }

    private static Set<String> mapInterests(Set<String> answers){
        Set<String> interests = new HashSet<>();
        for (String answer : answers){
            switch (answer) {
                case "making" -> interests.add("creativity");
                case "art_culture" -> interests.addAll(Set.of("art", "culture"));
                case "nature" -> interests.add("nature");
                case "active" -> interests.add("exercise");
                case "exploration" -> interests.add("exploration");
                default -> throw new IllegalArgumentException("Invalid interest: " + answer);
            }

        }
        return interests;
    }

    private static String mapLearningPreference(String answer) {
        return switch (answer) {
            case "definitely" -> "definitely";
            case "maybe" -> "maybe";
            case "just_enjoy" -> "just_enjoy";
            default -> throw new IllegalArgumentException("Invalid learning preference: " + answer);
        };
    }

    private static Set<String> mapAdventureLevel(String answer) {
        return switch (answer) {
            case "comfortable" -> Set.of("relaxing");
            case "curious" -> Set.of("exploration");
            case "adventurous" -> Set.of("challenge", "adventure");
            default -> throw new IllegalArgumentException("Invalid adventure level: " + answer);
        };
    }

    private static Integer mapMaxDuration(String answer) {
        return switch (answer) {
            case "under_2_hours" -> 2;
            case "2_to_3_hours" -> 3;
            case "3_to_5_hours" -> 5;
            case "half_day" -> 8;
            default -> throw new IllegalArgumentException("Invalid max duration: " + answer);
        };
    }

    private static Integer mapMaxPrice(String answer) {
        return switch (answer) {
            case "free" -> 0;
            case "under_500" -> 500;
            case "500_to_1000" -> 1000;
            case "1000_to_1500" -> 1500;
            case "flexible" -> null;
            default -> throw new IllegalArgumentException("Invalid max price: " + answer);
        };
    }

    private static String mapCompanion(String answer) {
        return switch (answer) {
            case "myself" -> "myself";
            case "partner" -> "partner";
            case "friends" -> "friends";
            case "family" -> "family";
            case "anyone" -> "anyone";
            default -> throw new IllegalArgumentException("Invalid companion: " + answer);
        };
    }

    private static Set<String> mapAtmosphere(Set<String> answers){

        Set<String> atmosphere = new HashSet<>();
        for (String answer : answers){
            switch (answer) {
                case "peaceful" -> atmosphere.addAll(Set.of("relaxing", "healing"));
                case "inspiring" -> atmosphere.addAll(Set.of("art", "creativity"));
                case "natural" -> atmosphere.add("nature");
                case "exciting" -> atmosphere.addAll(Set.of("adventure", "exercise"));
                case "educational" -> atmosphere.addAll(Set.of("learning", "exploration"));
                default -> throw new IllegalArgumentException("Invalid atmosphere: " + answer);
            }

        }
        return atmosphere;
    }

    public static QuizCriteria mapAnswers(QuizRequest request){
        Set<String> weekendDirection = mapWeekendDirection(request.getWeekendDirection());
        Set<String> energyLevel = mapEnergyLevel(request.getEnergyLevel());
        Environment environment = mapEnvironment(request.getEnvironment());
        Set<String> interests = mapInterests(request.getInterests());
        String learningPreference = mapLearningPreference(request.getLearningPreference());
        Set<String> adventureLevel = mapAdventureLevel(request.getAdventureLevel());
        Integer maxDuration = mapMaxDuration(request.getMaxDuration());
        Integer maxPrice = mapMaxPrice(request.getMaxPrice());
        String companion = mapCompanion(request.getCompanion());
        Set<String> atmosphere = mapAtmosphere(request.getAtmosphere());

        return new QuizCriteria(weekendDirection, energyLevel, environment, interests,
                learningPreference, adventureLevel, maxDuration, maxPrice, companion, atmosphere);
    }

}
