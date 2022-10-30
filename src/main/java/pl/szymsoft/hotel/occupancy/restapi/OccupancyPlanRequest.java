package pl.szymsoft.hotel.occupancy.restapi;

import javax.validation.constraints.Min;

record OccupancyPlanRequest(
        @Min(0) int premiumRooms,
        @Min(0) int economyRooms
) {
}
