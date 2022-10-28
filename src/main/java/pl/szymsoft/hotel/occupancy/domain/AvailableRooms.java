package pl.szymsoft.hotel.occupancy.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AvailableRooms {

    int numberOfPremium;
    int numberOfEconomy;
}
