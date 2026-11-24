package com.weekendexplorer.importer;

import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;
import com.weekendexplorer.service.ActivityService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ActivityCsvImporter {
    private final ActivityService activityService;

    public ActivityCsvImporter(ActivityService activityService) { this.activityService = activityService; }

    public void importActivities() throws IOException {
        if (activityService.hasActivities()) return;

        try (InputStream inputStream = getClass().getResourceAsStream("/data/activities.csv")) {

            if (inputStream == null) {
                throw new IllegalStateException("Activities CSV file not found.");
            }

            try (CSVParser parser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                convertToActivities(parser);
            }
        }
    }

    private void convertToActivities(CSVParser parser) {

        List<Activity> activities = new ArrayList<>();

        for (CSVRecord record : parser) {
            String name = record.get("name").trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty at CSV record: " + record.getRecordNumber());
            }

            ActivityCategory category;
            try {
                category = ActivityCategory.valueOf(record.get("category").trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category at CSV record: "
                                + record.getRecordNumber()
                                + ". Value: " + record.get("category"), e);
            }

            int price = Integer.parseInt(record.get("price").trim());
            if (price < 0) {
                throw new IllegalArgumentException("Invalid price at CSV record: " + record.getRecordNumber());
            }

            int durationHours = Integer.parseInt(record.get("durationHours").trim());
            if (durationHours <= 0) {
                throw new IllegalArgumentException("Invalid hours at CSV record: " + record.getRecordNumber());
            }

            Environment environment;
            try {
                environment = Environment.valueOf(record.get("environment").trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid environment at CSV record: "
                        + record.getRecordNumber()
                        + ". Value: " + record.get("environment"), e);
            }

            Set<String> tags = Arrays.stream(record.get("tags").split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toSet());
            if (tags.isEmpty()) {
                throw new IllegalArgumentException("Tags cannot be empty at CSV record: " + record.getRecordNumber());
            }

            String description = record.get("description").trim();
            if (description.isEmpty()) {
                throw new IllegalArgumentException(
                        "Description cannot be empty at CSV record: "
                                + record.getRecordNumber()
                );
            }

            Activity activity = new Activity(
                    null, name, category, price, durationHours, environment, tags, description
            );
            activities.add(activity);
        }
        activityService.createActivities(activities);
    }



}
