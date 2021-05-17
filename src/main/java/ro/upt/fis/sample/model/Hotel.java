package ro.upt.fis.sample.model;

import lombok.Data;
import org.dizitart.no2.objects.Id;
import ro.upt.fis.sample.enums.Role;
import ro.upt.fis.sample.enums.RoomType;
import ro.upt.fis.sample.exceptions.AllRoomReservedException;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hotel {

    private String hotelName = "set hotel's name please";
    private String firstName;
    private String lastName;
    @Id
    private String email;
    private String phoneNumber;
    private String location = "set your location please";
    private String password;
    private String description = "set your description please";
    private Role role = Role.HOTEL;
    private List<Room> roomList = new ArrayList<>();
    private int rating = 0;
    private List<Reservation> reservations = new ArrayList<>();

    public Hotel(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Hotel() {
    }

    public void addRoom(Room room) {
        roomList.add(room);
    }

    public Room getRoom(int capacity, RoomType roomType) {
        for (Room room : roomList) {
            if (!room.isReserved() && room.getCapacity() == capacity && roomType.equals(room.getRoomType())) {
                return room;
            }
        }
        throw new AllRoomReservedException();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void deleteReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
}
