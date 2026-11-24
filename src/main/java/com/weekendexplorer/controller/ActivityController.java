package com.weekendexplorer.controller;

import com.weekendexplorer.dto.ActivityRecommendationResponse;
import com.weekendexplorer.dto.ActivityResponse;
import com.weekendexplorer.dto.QuizCriteria;
import com.weekendexplorer.dto.QuizRequest;
import com.weekendexplorer.mapper.QuizAnswerMapper;
import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.ActivityRecommendation;
import com.weekendexplorer.model.Environment;
import com.weekendexplorer.service.ActivityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(
        name = "Activities",
        description = "APIs for browsing, filtering, and recommending activities."
)
@RestController
@Validated
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService){
        this.activityService = activityService;
    }

    @Operation(
            summary = "Get all activities",
            description = "Retrieve a list of all available activities."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved activities",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Activity.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/api/activities")
    public List<ActivityResponse> getActivities() {
        return activityService.getAllActivities();
    }

    @Operation(
            summary = "Filter activities",
            description = "Retrieve activities that match the specified price, duration, category, and environment filters."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved filtered activities",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Activity.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Invalid input parameter."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/api/activities/filter")
    public List<ActivityResponse> filterActivities(
            @Parameter(
                    description = "Maximum activity price",
                    example = "300",
                    required = true
            )
            @Min(0)
            @RequestParam int price,

            @Parameter(
                    description = "Categories to filter activities",
                    example = "HANDMADE",
                    required = false
            )
            @RequestParam(required = false) Set<ActivityCategory> categories,

            @Parameter(
                    description = "Maximum duration of the activity in hours",
                    example = "3",
                    required = true
            )
            @Positive
            @RequestParam int hours,

            @Parameter(
                    description = "Environment in which the activity takes place",
                    example = "INDOOR",
                    required = false
            )
            @RequestParam(required = false) Environment env){
        return activityService.filterActivities(categories, price, hours, env);
    }

    @Operation(
            summary = "Recommend activities",
            description = "Recommend activities based on the user's selected conditions and interests, ordered by match score."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved recommended activities",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = ActivityRecommendation.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Invalid input parameter."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/api/activities/recommend")
    public List<ActivityRecommendationResponse> recommendActivities(
            @Parameter(
                    description = "Maximum activity price",
                    example = "300",
                    required = true
            )
            @Min(0)
            @RequestParam int price,

            @Parameter(
                    description = "Categories to filter activities",
                    example = "HANDMADE",
                    required = false
            )
            @RequestParam(required = false) Set<ActivityCategory> categories,

            @Parameter(
                    description = "Maximum duration of the activity in hours",
                    example = "3",
                    required = true
            )
            @Positive
            @RequestParam int hours,

            @Parameter(
                    description = "Environment in which the activity takes place",
                    example = "INDOOR",
                    required = false
            )
            @RequestParam(required = false) Environment env,

            @Parameter(
                    description = "Interests used to match activities",
                    example = "learning",
                    required = true
            )
            @RequestParam Set<String> interests){
        return activityService.findRecommendedActivities(categories, price, hours, env, interests);
    }

    @Operation(
            summary = "Get activity",
            description = "Retrieve an activity by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved activity",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Activity not found."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Invalid input parameter."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/api/activities/{id}")
    public ActivityResponse getActivity(
            @Parameter(
            description = "ID of the activity",
            example = "1",
            required = true
            )
            @PathVariable int id){
        return activityService.getActivityById(id);
    }

    @Operation(
            summary = "Recommend activities",
            description = "Recommend activities based on ten-question activity recommendation quiz, ordered by match score."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User's answers to the ten-question activity recommendation quiz",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Creative weekend",
                            summary = "A user who enjoys creative and relaxing activities",
                            value = """
                {
                  "weekendDirection": "creative",
                  "energyLevel": "low_energy",
                  "environment": "indoors",
                  "interests": ["making", "art_culture"],
                  "learningPreference": "definitely",
                  "adventureLevel": "comfortable",
                  "maxDuration": "half_day",
                  "maxPrice": "flexible",
                  "companion": "myself",
                  "atmosphere": ["peaceful", "inspiring"]
                }
                """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved recommended activities",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = ActivityRecommendation.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Invalid input parameter."
                            )
                    )

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/api/activities/recommend/quiz")
    public List<ActivityRecommendationResponse> recommendActivitiesBasedOnQuiz(@Valid @RequestBody QuizRequest request){
        QuizCriteria quizCriteria = QuizAnswerMapper.mapAnswers(request);
        return activityService.recommendActivitiesBasedOnQuiz(quizCriteria);
    }

}
