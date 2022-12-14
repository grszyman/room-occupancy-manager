package pl.szymsoft.hotel.occupancy.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlanner;
import pl.szymsoft.hotel.occupancy.domain.api.RoomRequest;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OccupancyPlannerUnitTest {

    private static final String EUR = "EUR";
    private static final Money PREMIUM_PRICE = Money.of(100, EUR);

    private final OccupancyPlanner occupancyPlanner = new OccupancyPlannerConfiguration()
            .occupancyPlanner(PREMIUM_PRICE,
                    () -> Stream.of(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209)
                            .map(maxPrice -> new RoomRequest(Money.of(maxPrice, EUR)))
                            .toList());


    @ParameterizedTest(name = "e.g. for {0} free premium rooms and {1} free economy rooms it should use " +
            "{2} premium rooms (EUR {3}) and {4} economy rooms (EUR {5})")
    @CsvSource(delimiter = '|', textBlock = """
            # Basic cases -------------------------------------------------------------------------------------------
            # Free Premium rooms | Free Economy rooms | Usage Premium | Price Premium | Usage Economy | Price Economy
                               3 |                  3 |             3 |           738 |             3 |        167.99
                               7 |                  5 |             6 |          1054 |             4 |        189.99
                               2 |                  7 |             2 |           583 |             4 |        189.99
                               7 |                  1 |             7 |       1153.99 |             1 |         45.00
            # Extra cases -------------------------------------------------------------------------------------------
                               0 |                  0 |             0 |             0 |             0 |             0
                               1 |                  0 |             1 |           374 |             0 |             0
                               0 |                  1 |             0 |             0 |             1 |         99.99
                              10 |                  0 |            10 |       1243.99 |             0 |             0
                              10 |                  1 |             9 |       1221.99 |             1 |            22
            """)
    void should_create_expected_occupancy_plan(
            int premiumRoomsCount,
            int economyRoomsCount,
            int expectedUsagePremium,
            BigDecimal expectedTotalPremiumPrice,
            int expectedUsageEconomy,
            BigDecimal expectedTotalEconomyPrice
    ) {
        // when
        final var occupancyPlan = occupancyPlanner.createPlanFor(premiumRoomsCount, economyRoomsCount);

        // then
        assertThat(occupancyPlan.getPremiumRequests().size())
                .isEqualTo(expectedUsagePremium);
        assertThat(occupancyPlan.getPremiumAmount())
                .isEqualTo(Money.of(expectedTotalPremiumPrice, EUR));

        assertThat(occupancyPlan.getEconomyRequests().size())
                .isEqualTo(expectedUsageEconomy);
        assertThat(occupancyPlan.getEconomyAmount())
                .isEqualTo(Money.of(expectedTotalEconomyPrice, EUR));
    }

    @ParameterizedTest(name = "e.g. it should not accept {0} free premium rooms and {1} free economy rooms")
    @CsvSource(textBlock = """
            -1,  0
             0, -1
            -1, -1
            """)
    void should_not_accept_negative_numbers_of_free_rooms(int premiumRoomsCount, int economyRoomsCount) {

        // when
        final var throwable = catchThrowable(() -> occupancyPlanner.createPlanFor(premiumRoomsCount, economyRoomsCount));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}