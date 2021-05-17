package ro.upt.fis.sample.model;

import lombok.Data;
import org.dizitart.no2.objects.Id;
import ro.upt.fis.sample.enums.Role;

import java.util.ArrayList;
import java.util.List;


@Data
public class Customer {

    private String lastName;
    private String firstName;
    private String phoneNumber;
    @Id
    private String email;
    private String password;
    private Role role = Role.CUSTOMER;
    private List<Reservation> reservationList = new ArrayList<>();

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public Customer() {
    }

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
    }

    public void deleteReservation(Reservation reservation) {
        reservationList.remove(reservation);
    }
}
