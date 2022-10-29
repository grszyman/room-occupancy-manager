package pl.szymsoft.hotel.occupancy.domain;

import javax.money.MonetaryAmount;

import static pl.szymsoft.utils.Objects.require;

// in real life, we would need more information about a request (at least an id)
public record RoomRequest(MonetaryAmount maxPrice) {

    public RoomRequest {
        require(maxPrice, MonetaryAmount::isPositive, "maxPrice has to be positive");
    }
}
