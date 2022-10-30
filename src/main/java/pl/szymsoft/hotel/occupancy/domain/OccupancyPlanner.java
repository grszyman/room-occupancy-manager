package pl.szymsoft.hotel.occupancy.domain;

import lombok.Builder;
import lombok.NonNull;
import pl.szymsoft.hotel.occupancy.domain.ports.RoomRequestsProvider;

import javax.money.MonetaryAmount;

import static lombok.AccessLevel.PROTECTED;

@Builder(access = PROTECTED)
public class OccupancyPlanner {

    @NonNull
    private final MonetaryAmount premiumPrice;

    @NonNull
    private final RoomRequestsProvider roomRequestsProvider;

    public OccupancyPlan createPlanFor(int premiumRoomsCount, int economyRoomsCount) {

        return OccupancyPlan.builder()
                .requests(roomRequestsProvider.getAll())
                .premiumPrice(premiumPrice)
                .premiumRoomsCount(premiumRoomsCount)
                .economyRoomsCount(economyRoomsCount)
                .build();
    }

}
