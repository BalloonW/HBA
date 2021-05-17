package ro.upt.fis.sample.exceptions;

public class AllRoomReservedException extends RuntimeException {

    public AllRoomReservedException() {
        super("All rooms of this type are reserved");
    }
}
