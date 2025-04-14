package com.backend.velyo_backend.Util;

public record DashboardResponse (
    int totalUsers,
    int totalAccommodation,
    int totalAmenities,
    int totalCategories,
    int totalBookings
){}
