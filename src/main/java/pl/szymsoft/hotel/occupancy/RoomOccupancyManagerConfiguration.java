package pl.szymsoft.hotel.occupancy;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymsoft.hotel.occupancy.domain.api.RoomRequest;
import pl.szymsoft.hotel.occupancy.domain.ports.RoomRequestsProvider;

import java.math.BigDecimal;

import static java.util.Arrays.stream;

@Configuration
class RoomOccupancyManagerConfiguration {

    @Bean
    RoomRequestsProvider roomRequestsProvider(
            @Value("${occupancy-planner.room-requests}") String roomRequestsConfig) {

        final var roomRequests = stream(roomRequestsConfig.split(","))
                .map(String::trim)
                .map(BigDecimal::new)
                .map(value -> Money.of(value, "EUR"))
                .map(RoomRequest::new)
                .toList();

        return () -> roomRequests;
    }
}
