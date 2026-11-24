package com.weekendexplorer.mapper;

import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.dto.QuizRequest;
import com.weekendexplorer.model.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QuizAnswerMapperTest {

    private QuizRequest request;

    @BeforeEach
    void createRequest(){
        request = new QuizRequest();
        request.setWeekendDirection("creative");
        request.setEnergyLevel("low_energy");
        request.setEnvironment("indoors");
        request.setInterests(Set.of("making", "art_culture"));
        request.setLearningPreference("definitely");
        request.setAdventureLevel("comfortable");
        request.setMaxDuration("under_2_hours");
        request.setMaxPrice("free");
        request.setCompanion("myself");
        request.setAtmosphere(Set.of("peaceful", "inspiring"));
    }

    // Q1
    @ParameterizedTest
    @CsvSource({
            "creative, creativity",
            "adventurous, adventure",
            "cultural, culture",
            "relaxing, relaxing"
    })
    void mapAnswers_shouldMapWeekendDirectionCorrectly(String answer, String expectedTag) {
        request.setWeekendDirection(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of(expectedTag), quizCriteria.getWeekendDirection());
    }

    // Q2
    @ParameterizedTest
    @CsvSource({
            "low_energy, relaxing",
            "moderate_energy, exercise",
            "high_energy, challenge|adventure"
    })
    void mapAnswers_shouldMapEnergyLevelCorrectly(String answer, String expectedTag) {
        request.setEnergyLevel(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of(expectedTag.split("\\|")), quizCriteria.getEnergyLevel());
    }

    // Q3
    @ParameterizedTest
    @CsvSource(value = {
            "indoors, INDOOR",
            "outdoors, OUTDOOR",
            "any, NULL"
    }, nullValues = "NULL")
    void mapAnswers_shouldMapEnvironmentCorrectly(String answer, Environment expectedEnvironment) {
        request.setEnvironment(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(expectedEnvironment, quizCriteria.getEnvironment());
    }

    // Q4
    @ParameterizedTest
    @CsvSource({
            "making, creativity",
            "art_culture, art|culture",
            "nature, nature",
            "active, exercise",
            "exploration, exploration"
    })
    void mapAnswers_shouldMapInterestsCorrectly(String answer, String expectedTag) {
        request.setInterests(Set.of(answer));
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of(expectedTag.split("\\|")), quizCriteria.getInterests());
    }

    @Test
    void mapAnswers_shouldMapMultipleInterestsCorrectly() {
        request.setInterests(Set.of("making", "art_culture", "active"));
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of("creativity","art", "culture", "exercise"), quizCriteria.getInterests());
    }

    // Q5
    @ParameterizedTest
    @CsvSource({
            "definitely, definitely",
            "maybe, maybe",
            "just_enjoy, just_enjoy",
    })
    void mapAnswers_shouldMapLearningPreferenceCorrectly(String answer, String expectedTag) {
        request.setLearningPreference(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(expectedTag, quizCriteria.getLearningPreference());
    }

    // Q6
    @ParameterizedTest
    @CsvSource({
            "comfortable, relaxing",
            "curious, exploration",
            "adventurous, challenge|adventure"

    })
    void mapAnswers_shouldMapAdventureLevelCorrectly(String answer, String expectedTag) {
        request.setAdventureLevel(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of(expectedTag.split("\\|")), quizCriteria.getAdventureLevel());
    }

    // Q7
    @ParameterizedTest
    @CsvSource({
            "under_2_hours, 2",
            "2_to_3_hours, 3",
            "3_to_5_hours, 5",
            "half_day, 8"
    })
    void mapAnswers_shouldMapMaxDurationCorrectly(String answer, int expectedDuration) {
        request.setMaxDuration(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(expectedDuration, quizCriteria.getMaxDuration());
    }


    // Q8
    @ParameterizedTest
    @CsvSource(value = {
            "free, 0",
            "under_500, 500",
            "500_to_1000, 1000",
            "1000_to_1500, 1500",
            "flexible, NULL"
    }, nullValues = "NULL")
    void mapAnswers_shouldMapMaxPriceCorrectly(String answer, Integer expectedPrice) {
        request.setMaxPrice(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        if (expectedPrice != null) assertEquals(expectedPrice, quizCriteria.getMaxPrice());
        else assertNull(quizCriteria.getMaxPrice());
    }

    // Q9
    @ParameterizedTest
    @CsvSource({
            "myself, myself",
            "partner, partner",
            "friends, friends",
            "family, family",
            "anyone, anyone"
    })
    void mapAnswers_shouldMapCompanionCorrectly(String answer, String expectedTag) {
        request.setCompanion(answer);
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(expectedTag, quizCriteria.getCompanion());
    }

    // Q10
    @ParameterizedTest
    @CsvSource({
            "peaceful, relaxing|healing",
            "inspiring, art|creativity",
            "natural, nature",
            "exciting, adventure|exercise",
            "educational, learning|exploration"
    })
    void mapAnswers_shouldMapAtmosphereCorrectly(String answer, String expectedTag) {
        request.setAtmosphere(Set.of(answer));
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of(expectedTag.split("\\|")), quizCriteria.getAtmosphere());
    }

    @Test
    void mapAnswers_shouldMapMultipleAtmosphereCorrectly() {
        request.setAtmosphere(Set.of("peaceful", "inspiring", "educational"));
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        assertEquals(Set.of("relaxing", "healing", "art", "creativity", "learning", "exploration"), quizCriteria.getAtmosphere());
    }


    // Exception Test
    @Test
    void mapAnswers_shouldThrowExceptionWhenWeekendDirectionIsInvalid() {
        request.setWeekendDirection("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenEnergyLevelIsInvalid() {
        request.setEnergyLevel("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenEnvironmentIsInvalid() {
        request.setEnvironment("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenInterestsIsInvalid() {
        request.setInterests(Set.of("making", "Invalid"));
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenLearningPreferenceIsInvalid() {
        request.setLearningPreference("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenAdventureLevelIsInvalid() {
        request.setAdventureLevel("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenMaxDurationIsInvalid() {
        request.setMaxDuration("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenMaxPriceIsInvalid() {
        request.setMaxPrice("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenCompanionIsInvalid() {
        request.setCompanion("Invalid");
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

    @Test
    void mapAnswers_shouldThrowExceptionWhenAtmosphereIsInvalid() {
        request.setAtmosphere(Set.of("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> QuizAnswerMapper.mapAnswers(request));
    }

}
