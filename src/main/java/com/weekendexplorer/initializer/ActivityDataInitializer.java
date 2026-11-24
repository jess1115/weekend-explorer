package com.weekendexplorer.initializer;

import com.weekendexplorer.importer.ActivityCsvImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.data.import-enabled",
        havingValue = "true"
)
public class ActivityDataInitializer implements CommandLineRunner {

    private final ActivityCsvImporter importer;

    public ActivityDataInitializer(ActivityCsvImporter importer) { this.importer = importer; }

    @Override
    public void run(String... args) throws Exception { importer.importActivities(); }
}