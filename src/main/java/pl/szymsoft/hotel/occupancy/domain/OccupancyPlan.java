package pl.szymsoft.hotel.occupancy.domain;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.money.MonetaryAmount;

import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PACKAGE;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OccupancyPlan {

    List<RoomRequest> fulfilledPremiumRequests;
    List<RoomRequest> fulfilledEconomyRequests;
    MonetaryAmount totalPremiumPrice;
    MonetaryAmount totalEconomyPrice;

    private OccupancyPlan(
            Iterable<RoomRequest> fulfilledPremiumRequests,
            Iterable<RoomRequest> fulfilledEconomyRequests
    ) {
        this.fulfilledPremiumRequests = List.ofAll(fulfilledPremiumRequests);
        this.fulfilledEconomyRequests = List.ofAll(fulfilledEconomyRequests);
        this.totalPremiumPrice = null;
        this.totalEconomyPrice = null;
    }

    @Builder(access = PACKAGE)
    private static OccupancyPlan create(
            Iterable<RoomRequest> requests,
            MonetaryAmount premiumPrice,
            int freePremiumRooms,
            int freeEconomyRooms
    ) {
        return new OccupancyPlan(emptyList(), emptyList());
    }
}
