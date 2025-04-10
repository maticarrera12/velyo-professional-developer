package com.backend.velyo_backend.Util;

public record DashboardResponse (
    int totalUsers,
    int totalAccommodation,
    int totalFeatures,
    int totalCategories,
    int totalBookings
){}
