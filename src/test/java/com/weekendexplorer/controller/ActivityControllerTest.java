package com.weekendexplorer.controller;

import com.weekendexplorer.data.ServiceTestActivityData;
import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.exception.ActivityNotFoundException;
import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;
import com.weekendexplorer.service.ActivityService;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import com.weekendexplorer.dto.QuizRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ActivityService activityService;

    @Test
    void getActivities_shouldReturnAllActivities() throws Exception {
        List<Activity> activities = ServiceTestActivityData.getActivities();

        when(activityService.getAllActivities()).thenReturn(activities);

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].name").value("Pottery Workshop"));

        verify(activityService).getAllActivities();
    }

    @Test
    void filterActivities_shouldReturnFilteredActivities() throws Exception {
        List<Activity> activities = List.of(ServiceTestActivityData.getActivities().getFirst());

        when(activityService.filterActivities(
                Set.of(ActivityCategory.HANDMADE),
                800,
                3,
                Environment.INDOOR
        )).thenReturn(activities);

        mockMvc.perform(get("/api/activities/filter")
                        .param("categories", "HANDMADE")
                        .param("price", "800")
                        .param("hours", "3")
                        .param("env", "INDOOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pottery Workshop"));

        verify(activityService).filterActivities(
                Set.of(ActivityCategory.HANDMADE),
                800,
                3,
                Environment.INDOOR
        );
    }

    @Test
    void filterActivities_shouldPassMultipleCategoriesToService() throws Exception {
        when(activityService.filterActivities(
                Set.of(ActivityCategory.HANDMADE, ActivityCategory.EXHIBITION),
                800,
                4,
                Environment.INDOOR
        )).thenReturn(List.of(ServiceTestActivityData.getActivities().getFirst()));

        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "800")
                        .param("hours", "4")
                        .param("categories", "HANDMADE", "EXHIBITION")
                        .param("env", "INDOOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pottery Workshop"));

        verify(activityService).filterActivities(
                Set.of(ActivityCategory.HANDMADE, ActivityCategory.EXHIBITION),
                800,
                4,
                Environment.INDOOR
        );
    }

    @Test
    void filterActivities_shouldAcceptZeroPrice() throws Exception {
        when(activityService.filterActivities(
                Set.of(ActivityCategory.EXERCISE),
                0,
                4,
                Environment.OUTDOOR
        )).thenReturn(List.of());

        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "0")
                        .param("hours", "4")
                        .param("categories", "EXERCISE")
                        .param("env", "OUTDOOR"))
                .andExpect(status().isOk());

        verify(activityService).filterActivities(
                Set.of(ActivityCategory.EXERCISE),
                0,
                4,
                Environment.OUTDOOR
        );
    }

    @Test
    void recommendActivities_shouldPassParametersToService() throws Exception {
        when(activityService.findRecommendedActivities(
                Set.of(ActivityCategory.HANDMADE, ActivityCategory.EXHIBITION),
                700,
                3,
                Environment.INDOOR,
                Set.of("creativity", "learning", "relaxing")
        )).thenReturn(List.of());

        mockMvc.perform(get("/api/activities/recommend")
                        .param("price", "700")
                        .param("hours", "3")
                        .param("categories", "HANDMADE", "EXHIBITION")
                        .param("env", "INDOOR")
                        .param("interests", "creativity", "learning", "relaxing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(activityService).findRecommendedActivities(
                Set.of(ActivityCategory.HANDMADE, ActivityCategory.EXHIBITION),
                700,
                3,
                Environment.INDOOR,
                Set.of("creativity", "learning", "relaxing")
        );
    }

    @Test
    void getActivity_shouldReturn404WhenActivityNotFound() throws Exception {
        when(activityService.getActivityById(99))
                .thenThrow(new ActivityNotFoundException("Activity not found with ID: 99"));

        mockMvc.perform(get("/api/activities/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Activity not found with ID: 99"));

        verify(activityService).getActivityById(99);
    }

    @Test
    void getActivity_shouldReturn400WhenInputInvalidArgument() throws Exception{
        mockMvc.perform(get("/api/activities/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenPriceIsNegative() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "-50")
                        .param("hours", "1")
                        .param("categories", "HANDMADE")
                        .param("env", "INDOOR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenHoursIsNegative() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "50")
                        .param("hours", "-1")
                        .param("categories", "HANDMADE")
                        .param("env", "INDOOR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenHoursIsZero() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "50")
                        .param("hours", "0")
                        .param("categories", "HANDMADE")
                        .param("env", "INDOOR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenCategoryNotExist() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "500")
                        .param("hours", "1")
                        .param("categories", "UNKNOWN")
                        .param("env", "INDOOR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenEnvNotExist() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "500")
                        .param("hours", "2")
                        .param("categories", "HANDMADE")
                        .param("env", "UNKNOWN"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    @Test
    void filterActivities_shouldReturn400WhenHoursIsMissing() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("price", "500")
                        .param("categories", "HANDMADE")
                        .param("env", "INDOOR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input parameter."));
    }

    QuizRequest createRequest(){
        QuizRequest request = new QuizRequest();
        request.setWeekendDirection("creative");
        request.setEnergyLevel("low_energy");
        request.setEnvironment("indoors");
        request.setInterests(Set.of("making", "art_culture"));
        request.setLearningPreference("definitely");
        request.setAdventureLevel("comfortable");
        request.setMaxDuration("half_day");
        request.setMaxPrice("flexible");
        request.setCompanion("myself");
        request.setAtmosphere(Set.of("peaceful", "inspiring"));

        return request;
    }

    @Test
    void recommendActivitiesByQuiz_shouldPassRequestToService() throws Exception {
        QuizRequest request = createRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        when(activityService.recommendActivitiesBasedOnQuiz(
                any(QuizCriteria.class)
        )).thenReturn(List.of());

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(activityService).recommendActivitiesBasedOnQuiz(
                any(QuizCriteria.class)
        );
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenRequiredFieldIsMissing() throws Exception {
        QuizRequest request = createRequest();
        request.setWeekendDirection("");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenAnswerIdIsInvalid() throws Exception {
        QuizRequest request = createRequest();
        request.setWeekendDirection("unknown");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenInterestsIsEmpty() throws Exception {
        QuizRequest request = createRequest();
        request.setInterests(Set.of());

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenAtmosphereIsEmpty() throws Exception {
        QuizRequest request = createRequest();
        request.setAtmosphere(Set.of());

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recommendActivitiesByQuiz_shouldReturn400WhenEnvironmentIsInvalid() throws Exception {
        QuizRequest request = createRequest();
        request.setEnvironment("invalid");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

}
