package pl.szymsoft.hotel.occupancy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class RoomOccupancyManagerApplicationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void should_return_expected_response_for_a_given_request() {

        // when
        final var response = webTestClient
                .post()
                .uri("/occupancy-plans")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
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
                            "premiumUsage": 3,
                            "premiumAmount": {
                                "amount": 738.0,
                                "currency": "EUR"
                            },
                            "economyUsage": 3,
                            "economyAmount": {
                                "amount": 167.99,
                                "currency": "EUR"
                            }
                        }
                        """);
    }
}
