package ro.upt.fis.sample.model;

import lombok.Data;
import ro.upt.fis.sample.enums.RoomType;

@Data
public class Room {

    private static int id = 0;
    private int roomId = Room.id;
    private int capacity;
    private int price;
    private RoomType roomType;
    private boolean reserved;

    public Room(int capacity, int price, RoomType roomType) {
        this.capacity = capacity;
        this.price = price;
        this.roomType = roomType;
        reserved = false;
    }

    public Room() {
    }

    static {
        id++;
    }
}
