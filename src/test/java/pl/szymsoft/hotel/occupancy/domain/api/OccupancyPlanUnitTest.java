package pl.szymsoft.hotel.occupancy.domain.api;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OccupancyPlanUnitTest {

    private static final Money ZERO_EUR = Money.of(0, "EUR");

    private static Stream<Arguments> nullableAndEmptyRequests() {
        return Stream.of(
                arguments(null, null),
                arguments(null, List.empty()),
                arguments(List.empty(), null),
                arguments(List.empty(), List.empty())
        );
    }

    @ParameterizedTest(name = "e.g. for given premium requests: {0} and economy requests: {1} should return empty values")
    @MethodSource("nullableAndEmptyRequests")
    void should_accept_lack_of_requests_and_return_empty_values(
            List<RoomRequest> givenPremiumRequests,
            List<RoomRequest> givenEconomyRequests
    ) {
        // when
        final var occupancyPlan = OccupancyPlan.builder()
                .premiumRequests(givenPremiumRequests)
                .economyRequests(givenEconomyRequests)
                .build();

        // then
        assertThat(occupancyPlan.getPremiumRequests())
                .isEmpty();

        assertThat(occupancyPlan.getPremiumAmount())
                .isEqualTo(ZERO_EUR);

        assertThat(occupancyPlan.getEconomyRequests())
                .isEmpty();

        assertThat(occupancyPlan.getEconomyAmount())
                .isEqualTo(ZERO_EUR);
    }

    @Test
    void should_return_total_amounts_and_given_requests() {

        // given
        final var premiumRequests = roomRequestsWithMaxPrices(155, 374, 100, 101, 116, 209);
        final var economyRequests = roomRequestsWithMaxPrices(23, 45, 22, 99.99);

        // when
        final var occupancyPlan = OccupancyPlan.builder()
                .premiumRequests(premiumRequests)
                .economyRequests(economyRequests)
                .build();

        // then
        assertThat(occupancyPlan.getPremiumRequests())
                .containsExactlyInAnyOrderElementsOf(premiumRequests);

        assertThat(occupancyPlan.getPremiumAmount())
                .isEqualTo(Money.of(1055, "EUR"));

        assertThat(occupancyPlan.getEconomyRequests())
                .containsExactlyInAnyOrderElementsOf(economyRequests);

        assertThat(occupancyPlan.getEconomyAmount())
                .isEqualTo(Money.of(189.99, "EUR"));
    }

    private static List<RoomRequest> roomRequestsWithMaxPrices(Number... maxPrices) {
        return Stream.of(maxPrices)
                .map(value -> Money.of(value, "EUR"))
                .map(RoomRequest::new)
                .toList();
    }
}