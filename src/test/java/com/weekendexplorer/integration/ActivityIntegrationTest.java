package com.weekendexplorer.integration;

import com.weekendexplorer.data.RepositoryTestActivityData;
import com.weekendexplorer.dto.QuizRequest;
import com.weekendexplorer.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    void shouldReturnAllActivities() throws Exception {
        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void shouldFilterActivities() throws Exception {
        mockMvc.perform(get("/api/activities/filter")
                        .param("categories", "HANDMADE")
                        .param("price", "900")
                        .param("hours", "4")
                        .param("env", "INDOOR"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Pottery Workshop"))
                .andExpect(jsonPath("$[1].name").value("Hand-brewed Coffee Workshop"))
                .andExpect(jsonPath("$[2].name").value("Watercolor Painting Class"));

    }

    @Test
    void shouldRecommendActivities() throws Exception {
        mockMvc.perform(get("/api/activities/recommend")
                        .param("categories", "HANDMADE", "EXHIBITION")
                        .param("price", "900")
                        .param("hours", "4")
                        .param("env", "INDOOR")
                        .param("interests", "art", "relaxing"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].activity.name").value("Contemporary Art Exhibition"))
                .andExpect(jsonPath("$[0].score").value(2))
                .andExpect(jsonPath("$[1].activity.name").value("Watercolor Painting Class"))
                .andExpect(jsonPath("$[2].activity.name").value("Pottery Workshop"))
                .andExpect(jsonPath("$[2].score").value(1))
                .andExpect(jsonPath("$[3].activity.name").value("Hand-brewed Coffee Workshop"))
                .andExpect(jsonPath("$[4].activity.name").value("Permanent Museum Exhibition"))
                .andExpect(jsonPath("$[4].score").value(0));

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
    void shouldRecommendActivitiesByQuiz() throws Exception {
        QuizRequest request = createRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/activities/recommend/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].activity.name").value("Hand-brewed Coffee Workshop"))
                .andExpect(jsonPath("$[0].score").value(14))
                .andExpect(jsonPath("$[1].activity.name").value("Pottery Workshop"))
                .andExpect(jsonPath("$[1].score").value(12))
                .andExpect(jsonPath("$[2].activity.name").value("Watercolor Painting Class"))
                .andExpect(jsonPath("$[2].score").value(12))
                .andExpect(jsonPath("$[3].activity.name").value("Contemporary Art Exhibition"))
                .andExpect(jsonPath("$[3].score").value(9))
                .andExpect(jsonPath("$[4].activity.name").value("Permanent Museum Exhibition"))
                .andExpect(jsonPath("$[4].score").value(2))
                .andExpect(jsonPath("$[5].activity.name").value("Indoor Bouldering Experience"))
                .andExpect(jsonPath("$[5].score").value(0));

    }
}
