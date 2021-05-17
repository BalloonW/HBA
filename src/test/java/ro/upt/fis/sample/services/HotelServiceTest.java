package ro.upt.fis.sample.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import ro.upt.fis.sample.exceptions.EmailAlreadyRegisteredException;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.model.Room;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HotelServiceTest {

    HotelServices hotelServices = new HotelServices();
    
    @Test
    public void whenAddReservation_thenReservationIsAssignedToAHotel() {
        hotelServices.initDatabase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";

        hotelServices.addHotel(firstName, lastName, phoneNumber, email, password);
        hotelServices.closeDatabase();

        Reservation reservation = new Reservation();
        reservation.setRoom(new Room());
        reservation.setTotalCost(100);
        reservation.setHotelManagerEmail(email);

        hotelServices.addReservation(email, reservation);
        List<Hotel> hotels = hotelServices.getAllHotels();

        boolean flag = false;

        for (Hotel hotel : hotels) {
            if (email.equals(hotel.getEmail())) {
                for (Reservation reservation1 : hotel.getReservations()) {
                    if (reservation.equals(reservation1)) {
                        flag = true;
                    }
                }
            }
        }
        assertTrue(flag);
    }

    @Test
    public void whenGetValidClientWithCredentials_thenCustomerObjectReturned() {
        hotelServices.initDatabase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";
        Hotel expectedHotel = new Hotel(firstName, lastName, phoneNumber, email, encodePassword(email, password));
        hotelServices.addHotel(firstName, lastName, phoneNumber, email, password);

        Hotel actualCustomer = hotelServices.getHotelManager(email, password);
        hotelServices.closeDatabase();

        actualCustomer.equals(expectedHotel);
    }

    @Test
    public void whenEmailAlreadyRegistered_whenThrowException() {
        hotelServices.initDatabase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";

        hotelServices.addHotel(firstName, lastName, phoneNumber, email, password);

        assertThrows(EmailAlreadyRegisteredException.class, () -> hotelServices.addHotel(firstName, lastName, phoneNumber, email, password));
        hotelServices.closeDatabase();
    }

    private String encodePassword(String salt, String password) {
        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", "");
    }

    private MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
}
