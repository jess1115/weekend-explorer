package com.weekendexplorer.data;

import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServiceTestActivityData {

    public static List<Activity> getActivities(){
        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1, "Pottery Workshop", ActivityCategory.HANDMADE,
                800, 2, Environment.INDOOR,
                Set.of("creativity", "relaxing"),
                "Explore the art of pottery and create your own unique ceramic piece. Enjoy a relaxing hands-on experience while working with clay."));
        activities.add(new Activity(2, "Hiking Trail Adventure", ActivityCategory.EXERCISE,
                0, 4, Environment.OUTDOOR,
                Set.of("nature", "exercise", "adventure"),
                "Discover scenic hiking trails and enjoy the beauty of nature. Get some exercise while breathing in fresh air and exploring the outdoors."));
        activities.add(new Activity(3, "Contemporary Art Exhibition", ActivityCategory.EXHIBITION,
                250, 2, Environment.INDOOR,
                Set.of("art", "exploration", "relaxing"),
                "Explore contemporary artworks across different mediums and styles. Discover new perspectives on creativity, culture, and everyday life."));
        activities.add(new Activity(4, "Hand-brewed Coffee Workshop", ActivityCategory.HANDMADE,
                600, 2, Environment.INDOOR,
                Set.of("creativity", "learning", "relaxing"),
                "Learn about coffee beans, grinding techniques, and manual brewing methods. Create your own perfect cup of coffee through this hands-on experience."));
        activities.add(new Activity(5, "Riverside Cycling", ActivityCategory.LEISURE,
                150, 3, Environment.OUTDOOR,
                Set.of("nature", "exercise", "exploration"),
                "Enjoy a relaxing cycling trip along riverside paths. Take in scenic waterfront views and spend some quality time outdoors."));
        activities.add(new Activity(6, "Watercolor Painting Class", ActivityCategory.HANDMADE,
                700, 3, Environment.INDOOR,
                Set.of("art", "creativity", "relaxing"),
                "Learn basic watercolor techniques and express your creativity through colors. Paint your favorite scenes while enjoying a relaxing artistic experience."));
        activities.add(new Activity(7, "Sunset Beach Walk", ActivityCategory.LEISURE,
                0, 2, Environment.OUTDOOR,
                Set.of("nature", "relaxing", "healing"),
                "Take a peaceful walk along the coastline and enjoy the beautiful sunset. Relax to the sound of ocean waves and the gentle sea breeze."));
        activities.add(new Activity(8, "Permanent Museum Exhibition", ActivityCategory.EXHIBITION,
                150, 2, Environment.INDOOR,
                Set.of("history", "learning", "exploration"),
                "Explore historical artifacts, cultural exhibits, and fascinating stories from the past. Discover new knowledge and gain a deeper understanding of history."));
        activities.add(new Activity(9, "Indoor Bouldering Experience", ActivityCategory.EXERCISE,
                900, 2, Environment.INDOOR,
                Set.of("exercise", "challenge", "adventure"),
                "Challenge yourself on indoor climbing routes of different difficulty levels. Improve your coordination, strength, and problem-solving skills along the way."));
        activities.add(new Activity(10, "Botanical Garden Walk", ActivityCategory.LEISURE,
                100, 2, Environment.OUTDOOR,
                Set.of("nature", "relaxing", "healing"),
                "Take a leisurely walk through a botanical garden and discover a variety of plants and ecosystems. Slow down and enjoy the peaceful atmosphere surrounded by greenery."));

        return List.copyOf(activities);
    }
}
