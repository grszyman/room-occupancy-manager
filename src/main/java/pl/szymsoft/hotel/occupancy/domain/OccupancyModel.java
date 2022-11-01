package pl.szymsoft.hotel.occupancy.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.collection.Stream;
import lombok.Builder;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlan;
import pl.szymsoft.hotel.occupancy.domain.api.RoomRequest;

import javax.money.MonetaryAmount;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Comparator.comparing;
import static lombok.AccessLevel.PACKAGE;
import static pl.szymsoft.utils.Ints.requirePositiveOrZero;
import static pl.szymsoft.utils.Objects.require;

@SuppressFBWarnings("EI_EXPOSE_REP")
final class OccupancyModel {

    private final OccupancyPlan occupancyPlan;

    private OccupancyModel(
            Stream<RoomRequest> premiumRequests,
            Stream<RoomRequest> economyRequests
    ) {
        occupancyPlan = OccupancyPlan.builder()
                .premiumRequests(premiumRequests.toList())
                .economyRequests(economyRequests.toList())
                .build();
    }

    OccupancyPlan toPlan() {
        return occupancyPlan;
    }

    @Builder(access = PACKAGE)
    private static OccupancyModel create(
            Iterable<RoomRequest> requests,
            MonetaryAmount premiumPrice,
            int premiumRoomsCount,
            int economyRoomsCount
    ) {
        require(premiumPrice, MonetaryAmount::isPositiveOrZero, "premiumPrice has to be positive or zero");
        requirePositiveOrZero(premiumRoomsCount, "premiumRoomsCount");
        requirePositiveOrZero(economyRoomsCount, "economyRoomsCount");

        return Stream.ofAll(requests)
                .sorted(comparing(RoomRequest::maxPrice).reversed())
                .partition(request -> request.maxPrice().isGreaterThanOrEqualTo(premiumPrice))
                .apply((premiumRequests, economyRequests)
                        -> createOccupancyModel(premiumRequests, economyRequests, premiumRoomsCount, economyRoomsCount));
    }

    private static OccupancyModel createOccupancyModel(
            Stream<RoomRequest> premiumRequests,
            Stream<RoomRequest> economyRequests,
            int premiumRoomsCount,
            int economyRoomsCount
    ) {
        final var extraPremiumRooms = premiumRoomsCount - min(premiumRoomsCount, premiumRequests.size());
        final var extraEconomyRequests = max(economyRoomsCount, economyRequests.size()) - economyRoomsCount;
        final var requestsToUpgrade = min(extraPremiumRooms, extraEconomyRequests);

        return new OccupancyModel(
                premiumRequests.take(premiumRoomsCount)
                        .appendAll(economyRequests.take(requestsToUpgrade)),
                economyRequests.drop(requestsToUpgrade)
                        .take(economyRoomsCount));
    }
}
