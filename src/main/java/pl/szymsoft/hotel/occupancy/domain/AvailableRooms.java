package pl.szymsoft.hotel.occupancy.domain;

import lombok.Builder;
import lombok.Value;

import static pl.szymsoft.utils.Ints.requirePositiveOrZero;

@Value
@Builder
public class AvailableRooms {

    int numberOfPremium;
    int numberOfEconomy;

    private AvailableRooms(int numberOfPremium, int numberOfEconomy) {
        this.numberOfPremium = requirePositiveOrZero(numberOfPremium, "numberOfPremium");
        this.numberOfEconomy = requirePositiveOrZero(numberOfEconomy, "numberOfEconomy");
    }
}
