package pl.szymsoft.hotel.occupancy.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlanner;
import pl.szymsoft.hotel.occupancy.domain.ports.RoomRequestsProvider;

import javax.money.MonetaryAmount;

@Configuration
class OccupancyPlannerConfiguration {

    @Bean
    OccupancyPlanner occupancyPlanner(
            @Value("${occupancy-planner.default-premium-price}")
            MonetaryAmount defaultPremiumPrice,
            RoomRequestsProvider roomRequestsProvider
    ) {
        return new OccupancyPlannerService(defaultPremiumPrice, roomRequestsProvider);
    }
}
