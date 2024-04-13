package com.mirea.bykonyaia.mireaproject.labs.lab_8;

import org.osmdroid.util.GeoPoint;

public class PointsOfInterestDto {
    public final String title;
    public final String description;
    public final GeoPoint location;

    public PointsOfInterestDto(String title, String description, GeoPoint location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }
}
