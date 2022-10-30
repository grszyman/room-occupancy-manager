package pl.szymsoft.hotel.occupancy.domain.api;

import io.vavr.collection.List;

import javax.money.MonetaryAmount;

public interface OccupancyPlan {

    List<RoomRequest> getPremiumRequests();

    List<RoomRequest> getEconomyRequests();

    MonetaryAmount getPremiumAmount();

    MonetaryAmount getEconomyAmount();
}
