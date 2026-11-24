package com.weekendexplorer.service;

import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.exception.ActivityNotFoundException;
import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.ActivityRecommendation;
import com.weekendexplorer.model.Environment;
import com.weekendexplorer.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAllActivities() { return activityRepository.findAll(); }

    public List<Activity> filterActivities(Set<ActivityCategory> categories, Integer price, int hours, Environment env) {
        if (categories != null && categories.isEmpty()) categories = null;
        if ((price != null && price < 0) || hours < 0) throw new IllegalArgumentException();

        return activityRepository.filterActivities(categories, price, hours, env);
    }

    private int calculateMatchScore(Set<String> interests, Set<String> tags) {
        int score = 0;

        for (String interest : interests){
            if (tags.contains(interest)){
                score++;
            }
        }
        return score;
    }

    public List<ActivityRecommendation> recommendActivities(
            List<Activity> filteredActivities, Set<String> interests) {

        List<ActivityRecommendation> activityRecommendations = new ArrayList<>();
        int score;

        for (Activity activity : filteredActivities){
            score = calculateMatchScore(interests, activity.getTags());
            ActivityRecommendation activityRecommendation = new ActivityRecommendation(activity, score);
            activityRecommendations.add(activityRecommendation);
        }
        activityRecommendations.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
        return activityRecommendations;
    }

    public List<ActivityRecommendation> findRecommendedActivities(Set<ActivityCategory> categories, Integer price, int hours,
                                                                       Environment env, Set<String> interests) {

        return recommendActivities(filterActivities(categories, price, hours, env), interests);
    }

    public Activity getActivityById(int id) {

        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with ID: " + id));
    }

    private int calculateQuizMatchScore(Activity activity, QuizCriteria quizCriteria) {
        int score = 0;
        Set<String> activityTags = activity.getTags();

        if (activityTags.stream().anyMatch(quizCriteria.getWeekendDirection()::contains)) score += 3;
        if (activityTags.stream().anyMatch(quizCriteria.getEnergyLevel()::contains)) score += 2;
        if (activityTags.stream().anyMatch(quizCriteria.getInterests()::contains)) score += 3;

        score +=  switch (quizCriteria.getLearningPreference()) {
            case "definitely" -> activityTags.contains("learning") ? 2 : 0;
            case "maybe" -> activityTags.contains("learning") ? 1 : 0;
            case "just_enjoy" -> activityTags.contains("relaxing") ? 2 : 0;
            default -> 0;
        };

        if (activityTags.stream().anyMatch(quizCriteria.getAdventureLevel()::contains)) score += 2;
        if (activityTags.stream().anyMatch(quizCriteria.getAtmosphere()::contains)) score += 2;

        return score;
    }

    public List<ActivityRecommendation> recommendActivitiesBasedOnQuiz(QuizCriteria quizCriteria) {
        List<Activity> filteredActivities = filterActivities(null, quizCriteria.getMaxPrice(),
                quizCriteria.getMaxDuration(), quizCriteria.getEnvironment());

        List<ActivityRecommendation> activityRecommendations = new ArrayList<>();
        int score;

        for (Activity activity : filteredActivities){
            score = calculateQuizMatchScore(activity, quizCriteria);
            ActivityRecommendation activityRecommendation = new ActivityRecommendation(activity, score);
            activityRecommendations.add(activityRecommendation);
        }
        activityRecommendations.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
        return activityRecommendations;
    }

    @Transactional
    public void createActivities(List<Activity> activities){
        activityRepository.saveAll(activities);
    }

    public boolean hasActivities() {
        return activityRepository.count() > 0;
    }

}
