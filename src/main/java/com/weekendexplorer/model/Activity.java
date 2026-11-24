package com.weekendexplorer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "activities")
@Schema(description = "Represents an available activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the activity", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Name of the activity", example = "Pottery Workshop", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Category of the activity", example = "HANDMADE", requiredMode = Schema.RequiredMode.REQUIRED)
    private ActivityCategory category;

    @Column(nullable = false)
    @Schema(description = "Price of the activity", example = "500", requiredMode = Schema.RequiredMode.REQUIRED)
    private int price;

    @Column(name = "duration_hours", nullable = false)
    @Schema(description = "Duration of the activity in hours", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private int durationHours;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Environment in which the activity takes place", example = "OUTDOOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private Environment environment;

    @ElementCollection
    @CollectionTable(
            name = "activity_tags",
            joinColumns = @JoinColumn(name = "activity_id")
    )
    @Column(name = "tag", nullable = false)
    @Schema(description = "Tags of the activity", example = "[\"creativity\", \"learning\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> tags;

    @Column(nullable = false, length = 1000)
    @Schema(description = "Description of the activity", example = "Experience the joy of pottery shaping.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    protected Activity() {

    }

    public Activity(Integer id, String name, ActivityCategory category, int price, int durationHours,
                    Environment environment, Set<String> tags, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.durationHours = durationHours;
        this.environment = environment;
        this.tags = Set.copyOf(tags);
        this.description = description;
    }

    public Integer getId() {return this.id;}

    public String getName() {return this.name;}

    public ActivityCategory getCategory() {return this.category;}

    public int getPrice() {return this.price;}

    public int getDurationHours() {return this.durationHours;}

    public Environment getEnvironment() {return this.environment;}

    public Set<String> getTags() {return this.tags;}

    public String getDescription() {return this.description;}

    public String toString() {
        return "Name: " + this.name +"\nPrice: " + this.price +
                "\nHours: " + this.durationHours +
                "\nDescription: " + this.description + "\n";
    }


}