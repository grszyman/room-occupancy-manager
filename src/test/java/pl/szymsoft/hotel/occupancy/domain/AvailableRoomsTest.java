package pl.szymsoft.hotel.occupancy.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

// @SuppressFBWarnings on lambdas doesn't work - https://github.com/spotbugs/spotbugs/issues/724
@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
class AvailableRoomsTest {

    @ParameterizedTest(name = "e.g. a combination of {0} premium rooms and {1} economy rooms should be rejected.")
    @CsvSource(textBlock = """
            -1, -1
            -1,  0
             0, -1
            """)
    void should_reject_negative_number_of_rooms(int premiumRooms, int economyRooms) {

        // when
        final var throwable = catchThrowable(() -> AvailableRooms.builder()
                .numberOfPremium(premiumRooms)
                .numberOfEconomy(economyRooms)
                .build());

        // then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "e.g. a combination of {0} premium rooms and {1} economy rooms should be accepted.")
    @CsvSource(textBlock = """
             0, 0
             0, 1
             1, 0
            """)
    void should_accept_positive_and_zero_rooms(int premiumRooms, int economyRooms) {

        // when
        final var availableRooms = AvailableRooms.builder()
                .numberOfPremium(premiumRooms)
                .numberOfEconomy(economyRooms)
                .build();

        // then
        assertThat(availableRooms.getNumberOfPremium())
                .isEqualTo(premiumRooms);
        assertThat(availableRooms.getNumberOfEconomy())
                .isEqualTo(economyRooms);
    }
}