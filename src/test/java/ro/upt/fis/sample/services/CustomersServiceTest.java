package ro.upt.fis.sample.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import ro.upt.fis.sample.exceptions.EmailAlreadyRegisteredException;
import ro.upt.fis.sample.model.Customer;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.model.Room;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomersServiceTest {

    CustomerServices customerServices = new CustomerServices();

    @Test
    public void whenGetValidClientWithCredentials_thenCustomerObjectReturned() {
        customerServices.initDataBase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";
        Customer expectedCustomer = new Customer(firstName, lastName, phoneNumber, email, encodePassword(email, password));
        customerServices.addCustomer(firstName, lastName, phoneNumber, email, password);

        Customer actualCustomer = customerServices.getClient(email, password);
        customerServices.closeDatabase();

        actualCustomer.equals(expectedCustomer);
    }

    @Test
    public void whenEmailAlreadyRegistered_whenThrowException() {
        customerServices.initDataBase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";

        customerServices.addCustomer(firstName, lastName, phoneNumber, email, password);

        assertThrows(EmailAlreadyRegisteredException.class, () -> customerServices.addCustomer(firstName, lastName, phoneNumber, email, password));
        customerServices.closeDatabase();
    }

    @Test
    public void whenGetValidClientWithEmail_thenCustomerObjectReturned() {
        customerServices.initDataBase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";
        Customer expectedCustomer = new Customer(firstName, lastName, phoneNumber, email, encodePassword(email, password));
        customerServices.addCustomer(firstName, lastName, phoneNumber, email, password);

        Customer actualCustomer = customerServices.getClient(email);
        customerServices.closeDatabase();

        actualCustomer.equals(expectedCustomer);
    }

    @Test
    public void whenAddReservation_thenReservationIsAssignedToAHotel(){
        customerServices.initDataBase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";

        customerServices.addCustomer(firstName, lastName, phoneNumber, email, password);
        customerServices.closeDatabase();

        Reservation reservation = new Reservation();
        reservation.setRoom(new Room());
        reservation.setTotalCost(100);
        reservation.setHotelManagerEmail(email);

        customerServices.addReservation(email, reservation);
        List<Customer> customers = getCustomerList();

        boolean flag = false;

        for(Customer customer: customers){
            if(email.equals(customer.getEmail())){
                for(Reservation reservation1: customer.getReservationList()){
                    if(reservation.equals(reservation1)){
                        flag = true;
                    }
                }
            }
        }
        assertTrue(flag);
    }

    @Test
    public void whenCancelReservation_thenItDoesNotExistAnymore(){
        customerServices.initDataBase();
        String firstName = "Alina";
        String lastName = "Ivanov";
        String phoneNumber = "0734562980";
        String email = RandomStringUtils.random(10, true, true);
        String password = "alina";

        customerServices.addCustomer(firstName, lastName, phoneNumber, email, password);
        customerServices.closeDatabase();

        Reservation reservation = new Reservation();
        reservation.setRoom(new Room());
        reservation.setTotalCost(100);
        reservation.setHotelManagerEmail(email);

        customerServices.addReservation(email, reservation);
        customerServices.cancelReservation(email, reservation);
        List<Customer> customers = getCustomerList();

        boolean flag = true;

        for(Customer customer: customers){
            if(email.equals(customer.getEmail())){
                for(Reservation reservation1: customer.getReservationList()){
                    if(reservation.equals(reservation1)){
                        flag = false;
                    }
                }
            }
        }
        assertTrue(flag);
    }

    private List<Customer> getCustomerList() {
        customerServices.initDataBase();
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : customerServices.getCustomers().find()) {
            customers.add(customer);
        }
        customerServices.closeDatabase();
        return customers;
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
