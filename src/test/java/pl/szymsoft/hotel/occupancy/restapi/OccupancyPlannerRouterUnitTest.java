package pl.szymsoft.hotel.occupancy.restapi;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlan;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlanner;
import pl.szymsoft.hotel.occupancy.domain.api.RoomRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static pl.szymsoft.hotel.occupancy.restapi.OccupancyPlannerRouterUnitTest.LocalConfiguration;

// This class illustrates how to test the REST adapter as a unit test (in isolation from potentially 'heavy' other modules)
// we still need Spring for that, but we don't need DB and any other dependencies.
@WebFluxTest
@ContextConfiguration(classes = {OccupancyPlannerRouter.class, LocalConfiguration.class})
class OccupancyPlannerRouterUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    // This test may be considered redundant, as we test a very similar case in IT,
    // but on the other case it is lighter than IT, so it can be run often.
    @Test
    void should_return_valid_response_for_a_valid_request() {

        // when
        final var response = postToOccupancyPlansEndpoint()
                .bodyValue("""
                        {
                        	"premiumRooms": 3,
                        	"economyRooms": 3
                        }
                        """)
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody().json("""
                        {
                            "premiumUsage": 2,
                            "premiumAmount": {
                                "amount": 201,
                                "currency": "EUR"
                            },
                            "economyUsage": 1,
                            "economyAmount": {
                                "amount": 99.99,
                                "currency": "EUR"
                            }
                        }
                        """);
    }

    @Test
    void should_return_status_400_for_an_invalid_request_schema() {

        // when
        final var response = postToOccupancyPlansEndpoint()
                .bodyValue("""
                        {
                            "INVALID_FIELD": 3
                        }
                        """)
                .exchange();

        // then
        response.expectStatus().isBadRequest();
    }

    @Test
    void should_return_status_400_for_an_invalid_request_values() {

        // when
        final var response = postToOccupancyPlansEndpoint()
                .bodyValue("""
                        {
                        	"premiumRooms": -3,
                        	"economyRooms": 3
                        }
                        """)
                .exchange();

        // then
        response.expectStatus().isBadRequest();
    }

    private WebTestClient.RequestBodySpec postToOccupancyPlansEndpoint() {
        return webTestClient
                .post()
                .uri("/occupancy-plans")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
    }

    @TestConfiguration
    static class LocalConfiguration {

        @Bean
        OccupancyPlanner occupancyPlanner() {
            return (premiumRoomsCount, economyRoomsCount) -> OccupancyPlan.builder()
                    .premiumRequests(
                            new RoomRequest(Money.of(100, "EUR")),
                            new RoomRequest(Money.of(101, "EUR")))
                    .economyRequests(
                            new RoomRequest(Money.of(99.99, "EUR")))
                    .build();
        }
    }
}
