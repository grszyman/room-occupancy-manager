package pl.szymsoft.hotel.occupancy.restapi;

import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlan;

import javax.money.MonetaryAmount;

record OccupancyPlanResponse(
        int premiumUsage,
        MonetaryAmount premiumAmount,
        int economyUsage,
        MonetaryAmount economyAmount
) {
    static OccupancyPlanResponse from(OccupancyPlan occupancyPlan) {
        return new OccupancyPlanResponse(
                occupancyPlan.getPremiumRequests().size(),
                occupancyPlan.getPremiumAmount(),
                occupancyPlan.getEconomyRequests().size(),
                occupancyPlan.getEconomyAmount());
    }
}
