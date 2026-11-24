package com.weekendexplorer.repository;

import com.weekendexplorer.model.Activity;
import com.weekendexplorer.model.ActivityCategory;
import com.weekendexplorer.model.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query("""
        SELECT a
        FROM Activity a
        WHERE (a.category IN :categories
        OR :categories IS NULL)
        AND (a.price <= :price
        OR :price IS NULL) 
        AND a.durationHours <= :hours
        AND (a.environment = :env
        OR :env IS NULL)
        ORDER BY a.id ASC
    """)
    List<Activity> filterActivities(
            @Param("categories") Set<ActivityCategory> categories,
            @Param("price") Integer price,
            @Param("hours") int hours,
            @Param("env") Environment env
    );

}


