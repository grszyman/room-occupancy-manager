package pl.szymsoft.hotel.occupancy.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Comparator.comparing;
import static lombok.AccessLevel.PACKAGE;
import static pl.szymsoft.utils.Ints.requirePositiveOrZero;
import static pl.szymsoft.utils.Objects.require;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressFBWarnings("EI_EXPOSE_REP")
public class OccupancyPlan {

    List<RoomRequest> premiumRequests;
    List<RoomRequest> economyRequests;
    MonetaryAmount premiumAmount;
    MonetaryAmount economyAmount;

    private OccupancyPlan(
            Stream<RoomRequest> premiumRequests,
            Stream<RoomRequest> economyRequests
    ) {
        this.premiumRequests = premiumRequests.toList();
        this.economyRequests = economyRequests.toList();
        this.premiumAmount = totalPriceOf(premiumRequests);
        this.economyAmount = totalPriceOf(economyRequests);
    }

    private static MonetaryAmount totalPriceOf(Stream<RoomRequest> roomRequests) {
        return roomRequests
                .map(RoomRequest::maxPrice)
                .reduceOption(MonetaryAmount::add)
                .getOrElse(() -> Money.of(0, "EUR"));
    }

    @Builder(access = PACKAGE)
    private static OccupancyPlan create(
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
                        -> createBookingPlan(premiumRequests, economyRequests, premiumRoomsCount, economyRoomsCount));
    }

    private static OccupancyPlan createBookingPlan(
            Stream<RoomRequest> premiumRequests,
            Stream<RoomRequest> economyRequests,
            int premiumRoomsCount,
            int economyRoomsCount
    ) {
        final var extraPremiumRooms = premiumRoomsCount - min(premiumRoomsCount, premiumRequests.size());
        final var extraEconomyRequests = max(economyRoomsCount, economyRequests.size()) - economyRoomsCount;
        final var requestsToUpgrade = min(extraPremiumRooms, extraEconomyRequests);

        return new OccupancyPlan(
                premiumRequests.take(premiumRoomsCount)
                        .appendAll(economyRequests.take(requestsToUpgrade)),
                economyRequests.drop(requestsToUpgrade)
                        .take(economyRoomsCount));
    }
}
