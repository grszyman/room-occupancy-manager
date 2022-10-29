package pl.szymsoft.hotel.occupancy.domain;

import javax.money.MonetaryAmount;

// in real life, we would need more information about a request (at least an id)
public record RoomRequest(MonetaryAmount maxPrice) {

}