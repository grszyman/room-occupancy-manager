package pl.szymsoft.hotel.occupancy.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OccupancyModelUnitTest {

    @Test
    void should_not_accept_negative_premium_price() {

        // when
        final var throwable = catchThrowable(() -> OccupancyModel.builder()
                .requests(emptyList())
                .premiumPrice(Money.of(-1, "EUR"))
                .premiumRoomsCount(0)
                .economyRoomsCount(0)
                .build());

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}