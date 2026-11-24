package com.weekendexplorer.repository;

import com.weekendexplorer.data.RepositoryTestActivityData;
import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ActivityRepositoryTest {
    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
        activityRepository.saveAll(
                RepositoryTestActivityData.getActivities()
        );
    }

    @Test
    void save_shouldPersistActivity() {

        Activity activity = new Activity(
                null,
                "Pottery Workshop",
                ActivityCategory.HANDMADE,
                800,
                2,
                Environment.INDOOR,
                Set.of("creativity", "relaxing"),
                "Explore the art of pottery and create your own unique ceramic piece. " +
                        "Enjoy a relaxing hands-on experience while working with clay."
        );

        Activity savedActivity = activityRepository.save(activity);
        int id = savedActivity.getId();
        Optional<Activity> result = activityRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals("Pottery Workshop", result.get().getName());
        assertEquals(Set.of("creativity", "relaxing"), result.get().getTags());
    }

    @Test
    void findById_shouldReturnEmptyWhenActivityDoesNotExist() {
        Optional<Activity> result = activityRepository.findById(99);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        assertEquals(10, activities.size());
    }

    @Test
    void filterActivities_shouldReturnOutdoorActivities(){
        List<Activity> filteredActivities = activityRepository.filterActivities(Set.of(ActivityCategory.LEISURE),
                500, 3, Environment.OUTDOOR);
        assertEquals(3, filteredActivities.size());
        assertEquals(List.of("Riverside Cycling", "Sunset Beach Walk", "Botanical Garden Walk"),
                filteredActivities.stream().map(Activity::getName).toList());
    }

    @Test
    void filterActivities_shouldReturnAllActivitiesWhenCategoryAndEnvironmentAreNull(){
        List<Activity> filteredActivities = activityRepository.filterActivities(null, 900, 5, null);
        assertEquals(10, filteredActivities.size());
    }

    @Test
    void filterActivities_shouldReturnEmptyListWhenNoActivityMatches(){
        List<Activity> filteredActivities = activityRepository.filterActivities(Set.of(ActivityCategory.EXHIBITION),
                50, 1, Environment.INDOOR);
        assertEquals(0, filteredActivities.size());
    }

    @Test
    void filterActivities_shouldIncludeActivitiesAtBoundaryValues(){
        List<Activity> filteredActivities = activityRepository.filterActivities(null, 150,
                3, Environment.OUTDOOR);
        assertEquals(3, filteredActivities.size());
        assertEquals(List.of("Riverside Cycling", "Sunset Beach Walk", "Botanical Garden Walk"),
                filteredActivities.stream().map(Activity::getName).toList());
    }

    @Test
    void filterActivities_shouldSupportMultipleCategories() {
        List<Activity> filteredActivities = activityRepository.filterActivities(
                Set.of(ActivityCategory.HANDMADE, ActivityCategory.EXHIBITION),
                800, 4, Environment.INDOOR);

        assertEquals(5, filteredActivities.size());
        assertEquals(List.of("Pottery Workshop", "Contemporary Art Exhibition", "Hand-brewed Coffee Workshop", "Watercolor Painting Class", "Permanent Museum Exhibition"),
                filteredActivities.stream().map(Activity::getName).toList());
    }

    @Test
    void filterActivities_shouldNotFilterByPriceWhenPriceIsNull() {
        List<Activity> filteredActivities = activityRepository.filterActivities(
                null, null, 4, Environment.INDOOR);

        assertEquals(6, filteredActivities.size());
    }


}
