package pl.szymsoft.hotel.occupancy.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OccupancyPlanUnitTest {

    @Test
    void should_not_accept_negative_premium_price() {

        // when
        final var throwable = catchThrowable(() -> OccupancyPlan.builder()
                .requests(emptyList())
                .premiumPrice(Money.of(-1, "EUR"))
                .freePremiumRooms(0)
                .freeEconomyRooms(0)
                .build());

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}