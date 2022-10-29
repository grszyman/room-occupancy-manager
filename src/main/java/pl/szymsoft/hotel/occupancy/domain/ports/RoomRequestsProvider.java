package pl.szymsoft.hotel.occupancy.domain.ports;

import pl.szymsoft.hotel.occupancy.domain.RoomRequest;

public interface RoomRequestsProvider {

    Iterable<RoomRequest> getAll();

}
