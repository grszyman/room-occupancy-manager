package pl.szymsoft.hotel.occupancy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymsoft.hotel.occupancy.domain.ports.RoomRequestsProvider;

import java.util.Collections;

@Configuration
class RoomOccupancyManagerConfiguration {

    @Bean
    RoomRequestsProvider roomRequestsProvider() {
        return Collections::emptyList;
    }

}
