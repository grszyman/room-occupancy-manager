package pl.szymsoft.hotel.occupancy.domain.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.collection.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNullElseGet;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class OccupancyPlanDto implements OccupancyPlan {

    private static final OccupancyPlanDto EMPTY = OccupancyPlanDto.builder().build();

    List<RoomRequest> premiumRequests;
    List<RoomRequest> economyRequests;
    MonetaryAmount premiumAmount;
    MonetaryAmount economyAmount;

    public static OccupancyPlanDto empty() {
        return EMPTY;
    }

    @SuppressWarnings("unused")
    public static class OccupancyPlanDtoBuilder {

        public OccupancyPlanDto build() {
            return new OccupancyPlanDto(
                    requireNonNullElseGet(premiumRequests, List::empty),
                    requireNonNullElseGet(economyRequests, List::empty),
                    requireNonNullElseGet(premiumAmount, () -> Money.of(0, "EUR")),
                    requireNonNullElseGet(economyAmount, () -> Money.of(0, "EUR"))
            );
        }
    }
}
