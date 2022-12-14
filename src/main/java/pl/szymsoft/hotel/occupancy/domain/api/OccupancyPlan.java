package pl.szymsoft.hotel.occupancy.domain.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.javamoney.moneta.Money;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNullElseGet;
import static lombok.EqualsAndHashCode.CacheStrategy.LAZY;

@Value
@EqualsAndHashCode(cacheStrategy = LAZY)
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"})
public class OccupancyPlan {

    List<RoomRequest> premiumRequests;
    List<RoomRequest> economyRequests;
    MonetaryAmount premiumAmount;
    MonetaryAmount economyAmount;

    @Builder
    private OccupancyPlan(
            @Nullable Iterable<RoomRequest> premiumRequests,
            @Nullable Iterable<RoomRequest> economyRequests
    ) {
        this.premiumRequests = List.ofAll(requireNonNullElseGet(premiumRequests, List::empty));
        this.economyRequests = List.ofAll(requireNonNullElseGet(economyRequests, List::empty));
        this.premiumAmount = totalPriceOf(this.premiumRequests);
        this.economyAmount = totalPriceOf(this.economyRequests);
    }

    @SuppressWarnings("unused")
    public static class OccupancyPlanBuilder {

        public OccupancyPlanBuilder premiumRequests(Iterable<RoomRequest> roomRequests) {
            this.premiumRequests = roomRequests;
            return this;
        }

        public OccupancyPlanBuilder premiumRequests(RoomRequest... roomRequests) {
            this.premiumRequests = List.of(roomRequests);
            return this;
        }

        public OccupancyPlanBuilder economyRequests(Iterable<RoomRequest> economyRequests) {
            this.economyRequests = economyRequests;
            return this;
        }

        public OccupancyPlanBuilder economyRequests(RoomRequest... economyRequests) {
            this.economyRequests = List.of(economyRequests);
            return this;
        }
    }

    private static MonetaryAmount totalPriceOf(Iterable<RoomRequest> roomRequests) {
        return Stream.ofAll(roomRequests)
                .map(RoomRequest::maxPrice)
                .reduceOption(MonetaryAmount::add)
                .getOrElse(() -> Money.of(0, "EUR"));
    }
}
