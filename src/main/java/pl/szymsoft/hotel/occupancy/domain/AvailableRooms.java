package pl.szymsoft.hotel.occupancy.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AvailableRooms {

    int numberOfPremium;
    int numberOfEconomy;

    private AvailableRooms(int numberOfPremium, int numberOfEconomy) {

        if (numberOfPremium < 0) {
            throw new IllegalArgumentException("numberOfPremium has to be positive or zero, but was " + numberOfPremium);
        }

        if (numberOfEconomy < 0) {
            throw new IllegalArgumentException("numberOfEconomy has to be positive or zero, but was " + numberOfEconomy);
        }

        this.numberOfPremium = numberOfPremium;
        this.numberOfEconomy = numberOfEconomy;
    }
}
