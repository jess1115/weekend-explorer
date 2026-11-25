package com.weekendexplorer.service;

import com.weekendexplorer.data.ServiceTestActivityData;
import com.weekendexplorer.dto.ActivityRecommendationResponse;
import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.exception.ActivityNotFoundException;
import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.ActivityRecommendation;
import com.weekendexplorer.model.Environment;
import com.weekendexplorer.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActivityServiceTest {
    @Mock
    private ActivityRepository activityRepository;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(activityRepository.findAllWithTags())
                .thenReturn(ServiceTestActivityData.getActivities());
        activityService = new ActivityService(activityRepository);
    }

    @Test
    void filterActivities_shouldThrowExceptionWhenPriceIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> activityService.filterActivities(null, -150,
                3, Environment.OUTDOOR));
    }

    @Test
    void filterActivities_shouldThrowExceptionWhenHoursIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> activityService.filterActivities(null, 150,
                -3, Environment.OUTDOOR));
    }

    @Test
    void recommendActivities_shouldSortActivitiesByMatchScoreDescending(){
        Set<String> interests = Set.of("nature", "relaxing");
        List<ActivityRecommendation> recommendedActivities =
                activityService.recommendActivities(ServiceTestActivityData.getActivities(),interests);
        assertEquals(List.of(2,2,1,1,1,1,1,1,0,0), recommendedActivities.stream().map(ActivityRecommendation::getScore).toList());
        assertEquals(List.of(7,10,1,2,3,4,5,6,8,9),
                recommendedActivities.stream()
                        .map(activityRecommendation -> activityRecommendation.getActivity().getId()).toList());
    }

    @Test
    void recommendActivities_shouldReturnZeroScoresWhenInterestsAreEmpty(){
        Set<String> interests = Set.of();
        List<ActivityRecommendation> recommendedActivities =
                activityService.recommendActivities(ServiceTestActivityData.getActivities(),interests);

        assertEquals(List.of(0,0,0,0,0,0,0,0,0,0), recommendedActivities.stream().map(ActivityRecommendation::getScore).toList());
    }

    @Test
    void getAllActivities_shouldReturnActivitiesWithUnmodifiableTags(){
        assertThrows(UnsupportedOperationException.class, () ->
                activityService.getAllActivities().getFirst().tags().add("nature"));
    }

    @Test
    void activity_shouldDefensivelyCopyOriginalTags(){
        Set<String> originalTags = new HashSet<>(Set.of("creativity", "relaxing"));

        Activity activity = new Activity(1, "Pottery Workshop",ActivityCategory.HANDMADE,
                800, 2, Environment.INDOOR,
                originalTags, "Explore the art of pottery and create your own unique ceramic piece. Enjoy a relaxing hands-on experience while working with clay.");

        originalTags.add("nature");

        assertFalse(activity.getTags().contains("nature"));
    }

    @Test
    void filterActivities_shouldTreatEmptyCategoriesAsNull() {
        activityService.filterActivities(Set.of(), 800, 4, Environment.INDOOR);

        verify(activityRepository).filterActivitiesWithTags(null, 800, 4, Environment.INDOOR);
    }

    @Test
    void getActivityById_shouldThrowExceptionWhenActivityNotFound() {
        when(activityRepository.findByIdWithTags(99))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class,
                () -> activityService.getActivityById(99));
    }

    @Test
    void recommendActivitiesBasedOnQuiz_shouldSortByScoreDescending() {
        QuizCriteria quizCriteria = new QuizCriteria(
                Set.of("creativity"),
                Set.of("relaxing"),
                null,
                Set.of("art"),
                "definitely",
                Set.of("exploration"),
                8,
                null,
                "myself",
                Set.of("peaceful")
        );

        when(activityRepository.filterActivitiesWithTags(
                any(), any(), anyInt(), any()
        )).thenReturn(ServiceTestActivityData.getActivities());

        List<ActivityRecommendationResponse> recommendedActivities =
                activityService.recommendActivitiesBasedOnQuiz(quizCriteria);

        assertEquals(
                List.of(6, 3, 4, 1, 8, 5, 7, 10, 2, 9),
                recommendedActivities.stream()
                        .map(r -> r.activity().id())
                        .toList()
        );

        assertEquals(
                List.of(8, 7, 7, 5, 4, 2, 2, 2, 0, 0),
                recommendedActivities.stream()
                        .map(ActivityRecommendationResponse::score)
                        .toList()
        );
    }

}
