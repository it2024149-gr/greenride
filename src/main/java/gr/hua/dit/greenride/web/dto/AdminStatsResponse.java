package gr.hua.dit.greenride.web.dto;



public record AdminStatsResponse(
        long totalRides,
        double averageOccupancyRatio
) {}
