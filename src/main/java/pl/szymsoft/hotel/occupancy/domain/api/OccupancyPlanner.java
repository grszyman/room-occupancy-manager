package pl.szymsoft.hotel.occupancy.domain.api;

public interface OccupancyPlanner {

    OccupancyPlan createPlanFor(int premiumRoomsCount, int economyRoomsCount);
}
