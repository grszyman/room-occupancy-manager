package pl.szymsoft.hotel.room.domain.api;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.szymsoft.hotel.occupancy.domain.RoomRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class RoomRequestUnitTest {

    @Test
    void should_not_accept_nullable_maxPrice() {

        // when
        //noinspection ConstantConditions
        final var throwable = catchThrowable(() -> new RoomRequest(null));

        // then
        assertThat(throwable).hasMessageContaining("maxPrice");
    }

    @ParameterizedTest(name = "e.g. should not accept {0}")
    @ValueSource(ints = {-1, 0})
    void should_not_accept_negative_or_zero_maxPrice(int givenValue) {

        // given
        final var givenPrice = Money.of(givenValue, "EUR");

        // when
        final var throwable = catchThrowable(() -> new RoomRequest(givenPrice));

        // then
        assertThat(throwable).hasMessageContaining("maxPrice");
    }

    @Test
    void should_accept_positive_maxPrice() {

        // given
        final var givenPrice = Money.of(1.00, "EUR");

        // when
        final var roomRequest = new RoomRequest(givenPrice);

        // then
        assertThat(roomRequest)
                .isNotNull()
                .extracting(RoomRequest::maxPrice)
                .isEqualTo(givenPrice);
    }
}