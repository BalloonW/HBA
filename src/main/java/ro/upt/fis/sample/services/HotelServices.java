package ro.upt.fis.sample.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import ro.upt.fis.sample.exceptions.EmailAlreadyRegisteredException;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.model.Room;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HotelServices {

    private ObjectRepository<Hotel> hotelRepository;
    private Nitrite database;

    public void initDatabase() {
        database = Nitrite.builder()
                .filePath(FileSystemServer.getPathToFile("hotels.bd").toFile())
                .openOrCreate("hotel", "hotel");

        hotelRepository = database.getRepository(Hotel.class);
    }

    public void closeDatabase() {
        database.close();
    }

    public void addHotel(String fistName, String lastName, String phoneNumber,
                         String email, String password) {
        checkHotelDoesNotExist(email);
        hotelRepository.insert(new Hotel(fistName, lastName, email, phoneNumber, encodePassword(email, password)));
    }

    public Hotel getHotelManager(String email, String password) {
        for (Hotel hotel : hotelRepository.find()) {
            if (Objects.equals(email, hotel.getEmail()) &&
                    Objects.equals(encodePassword(email, password), hotel.getPassword())) {
                return hotel;
            }
        }

        return null;
    }

    public void updateDatabase(Hotel changedHotel) {
        initDatabase();
        try {
            for (Hotel hotel : hotelRepository.find()) {
                if (Objects.equals(changedHotel.getEmail(), hotel.getEmail())) {
                    hotel.setFirstName(changedHotel.getFirstName());
                    hotel.setLastName(changedHotel.getLastName());
                    hotel.setDescription(changedHotel.getDescription());
                    hotel.setLocation(changedHotel.getLocation());
                    hotel.setRoomList(changedHotel.getRoomList());
                    hotel.setHotelName(changedHotel.getHotelName());
                    hotelRepository.update(hotel);
                }
            }
            closeDatabase();
        } catch (Exception e) {
            closeDatabase();
            e.printStackTrace();
        }

    }

    public void addReservation(String email, Reservation reservation) {
        initDatabase();
        for (Hotel hotel : hotelRepository.find()) {
            if (Objects.equals(email, hotel.getEmail())) {
                hotel.addReservation(reservation);

                for (Room room : hotel.getRoomList()) {
                    if (reservation.getRoom().equals(room)) {
                        room.setReserved(true);
                    }
                }
                hotelRepository.update(hotel);
            }
        }
        closeDatabase();
    }

    public void cancelReservation(String email, Reservation reservation) {
        initDatabase();
        for (Hotel hotel : hotelRepository.find()) {
            if (Objects.equals(email, hotel.getEmail())) {
                hotel.deleteReservation(reservation);
                hotelRepository.update(hotel);
            }
        }
        closeDatabase();
    }

    public Hotel getHotelManager(String email) {
        initDatabase();
        try {
            for (Hotel hotel : hotelRepository.find()) {
                if (Objects.equals(email, hotel.getEmail())) {
                    return hotel;
                }
            }
        } catch (Exception e) {
            //closeDatabase();
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return null;
    }

    public List<Hotel> getAllHotels() {
        initDatabase();
        List<Hotel> hotels = new ArrayList<>();
        for (Hotel hotel : hotelRepository.find()) {
            hotels.add(hotel);
        }
        return hotels;
    }

    private void checkHotelDoesNotExist(String email) {
        for (Hotel hotel : hotelRepository.find()) {
            if (hotel.getEmail().equals(email)) {
                throw new EmailAlreadyRegisteredException(email);
            }
        }
    }

    private static String encodePassword(String salt, String password) {
        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", "");
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
}
