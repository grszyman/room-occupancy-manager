package pl.szymsoft.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class IntsUnitTest {

    @Nested
    class Ints$require_should {

        @Test
        void return_a_given_number_if_it_matches_a_given_predicate() {

            // given
            final var givenNumber = 10;

            // when
            var object = Ints.require(givenNumber, number -> number > 0, "givenNumber has to be positive");

            // then
            assertThat(object).isEqualTo(10);
        }

        @Test
        void throw_IllegalArgumentException_if_a_given_number_does_not_match_a_given_predicate() {

            // given
            final var givenNumber = 10;

            // when
            final var throwable = catchThrowable(() ->
                    Ints.require(givenNumber, number -> number == 0, "givenNumber has to be zero"));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Ints$requirePositiveOrZero_should {

        @ParameterizedTest(name = "e.g. for {0} should return {0}")
        @ValueSource(ints = {0, 1})
        void return_a_given_number_if_it_is_positive_or_zero(int givenNumber) {

            // when
            var object = Ints.requirePositiveOrZero(givenNumber, "givenNumber");

            // then
            assertThat(object)
                    .isEqualTo(givenNumber);
        }

        @Test
        void throw_IllegalArgumentException_if_a_given_number_is_negative() {

            // given
            final var givenNumber = -1;

            // when
            final var throwable = catchThrowable(() ->
                    Ints.requirePositiveOrZero(givenNumber, "givenNumber"));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}