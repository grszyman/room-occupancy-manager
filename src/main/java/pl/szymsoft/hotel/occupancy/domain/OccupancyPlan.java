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

    List<RoomRequest> fulfilledPremiumRequests;
    List<RoomRequest> fulfilledEconomyRequests;
    MonetaryAmount totalPremiumPrice;
    MonetaryAmount totalEconomyPrice;

    private OccupancyPlan(
            Stream<RoomRequest> fulfilledPremiumRequests,
            Stream<RoomRequest> fulfilledEconomyRequests
    ) {
        this.fulfilledPremiumRequests = fulfilledPremiumRequests.toList();
        this.fulfilledEconomyRequests = fulfilledEconomyRequests.toList();
        this.totalPremiumPrice = totalPriceOf(fulfilledPremiumRequests);
        this.totalEconomyPrice = totalPriceOf(fulfilledEconomyRequests);
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
            int freePremiumRooms,
            int freeEconomyRooms
    ) {
        require(premiumPrice, MonetaryAmount::isPositiveOrZero, "premiumPrice has to be positive or zero");
        requirePositiveOrZero(freePremiumRooms, "freePremiumRooms");
        requirePositiveOrZero(freeEconomyRooms, "freeEconomyRooms");

        return Stream.ofAll(requests)
                .sorted(comparing(RoomRequest::maxPrice).reversed())
                .partition(request -> request.maxPrice().isGreaterThanOrEqualTo(premiumPrice))
                .apply((premiumRequests, economyRequests)
                        -> createBookingPlan(premiumRequests, economyRequests, freePremiumRooms, freeEconomyRooms));
    }

    private static OccupancyPlan createBookingPlan(
            Stream<RoomRequest> premiumRequests,
            Stream<RoomRequest> economyRequests,
            int freePremiumRooms,
            int freeEconomyRooms
    ) {
        final var extraPremiumRooms = freePremiumRooms - min(freePremiumRooms, premiumRequests.size());
        final var extraEconomyRequests = max(freeEconomyRooms, economyRequests.size()) - freeEconomyRooms;
        final var requestsToUpgrade = min(extraPremiumRooms, extraEconomyRequests);

        return new OccupancyPlan(
                premiumRequests.take(freePremiumRooms)
                        .appendAll(economyRequests.take(requestsToUpgrade)),
                economyRequests.drop(requestsToUpgrade)
                        .take(freeEconomyRooms));
    }
}
