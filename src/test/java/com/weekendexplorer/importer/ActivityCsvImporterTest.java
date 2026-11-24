package com.weekendexplorer.importer;

import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityCsvImporterTest {

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityCsvImporter importer;

    @Captor
    private ArgumentCaptor<List<Activity>> activityCaptor;

    @Test
    void shouldSkipImportWhenActivitiesAlreadyExist() throws IOException {
        when(activityService.hasActivities()).thenReturn(true);

        importer.importActivities();

        verify(activityService).hasActivities();
        verify(activityService, never()).createActivities(anyList());
    }

    @Test
    void shouldImportActivitiesWhenDatabaseIsEmpty() throws IOException {
        when(activityService.hasActivities()).thenReturn(false);

        importer.importActivities();

        verify(activityService).createActivities(activityCaptor.capture());

        List<Activity> importedActivities = activityCaptor.getValue();

        Activity firstActivity = importedActivities.get(0);
        assertEquals(50, importedActivities.size());
        assertNull(firstActivity.getId());

        assertEquals("Pottery Workshop", firstActivity.getName());
        assertEquals(ActivityCategory.HANDMADE, firstActivity.getCategory());
        assertEquals(800, firstActivity.getPrice());

    }
}
