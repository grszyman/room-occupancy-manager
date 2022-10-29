package pl.szymsoft.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ObjectsUnitTest {

    @Nested
    class Objects$require_should {

        @Test
        void return_a_given_value_if_it_matches_a_given_predicate() {

            // given
            final var givenValue = "given string";

            // when
            var value = Objects.require(givenValue, StringUtils::isNoneBlank, "givenString has to be non black");

            // then
            assertThat(value).isEqualTo(givenValue);
        }

        @Test
        void throw_NullPointerException_if_a_given_value_is_null() {

            // given
            final String givenValue = null;

            // when
            //noinspection ConstantConditions
            final var throwable = catchThrowable(() ->
                    Objects.require(givenValue, StringUtils::isNoneBlank, "givenString has to be non black"));

            // then
            assertThat(throwable)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void throw_IllegalArgumentException_if_a_given_value_does_not_match_a_given_predicate() {

            // given
            final var givenValue = " ";

            // when
            final var throwable = catchThrowable(() ->
                    Objects.require(givenValue, StringUtils::isNotBlank, "givenString has to be non black"));

            // then
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}