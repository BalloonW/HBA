package ro.upt.fis.sample.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import ro.upt.fis.sample.exceptions.EmailAlreadyRegisteredException;
import ro.upt.fis.sample.model.Customer;
import ro.upt.fis.sample.model.Reservation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class CustomerServices {

    private static ObjectRepository<Customer> customerRepository;
    Nitrite database;

    public void initDataBase() {
        database = Nitrite.builder()
                .filePath(FileSystemServer.getPathToFile("client.bd").toFile())
                .openOrCreate("client", "client");

        customerRepository = database.getRepository(Customer.class);
    }

    public void closeDatabase() {
        database.close();
    }

    public void addCustomer(String fistName, String lastName, String phoneNumber,
                            String email, String password) {
        checkCustomerDoesNotExist(email);
        customerRepository.insert(new Customer(fistName, lastName, phoneNumber,
                email, encodePassword(email, password)));
    }

    public Customer getClient(String email, String password) {
        for (Customer customer : customerRepository.find()) {
            if (Objects.equals(email, customer.getEmail()) &&
                    Objects.equals(encodePassword(email, password), customer.getPassword())) {
                return customer;
            }
        }

        return null;
    }

    public Customer getClient(String email) {
        for (Customer customer : customerRepository.find()) {
            if (Objects.equals(email, customer.getEmail())) {
                return customer;
            }
        }

        return null;
    }

    public void addReservation(String email, Reservation reservation) {
        initDataBase();
        for (Customer customer : customerRepository.find()) {
            if (Objects.equals(email, customer.getEmail())) {
                customer.addReservation(reservation);
                customerRepository.update(customer);
            }
        }
        closeDatabase();
    }

    public void cancelReservation(String email, Reservation reservation) {
        initDataBase();
        for (Customer customer : customerRepository.find()) {
            if (Objects.equals(email, customer.getEmail())) {
                customer.deleteReservation(reservation);
                customerRepository.update(customer);
            }
        }
        closeDatabase();
    }

    private void checkCustomerDoesNotExist(String email) {
        for (Customer customer : customerRepository.find()) {
            if (Objects.equals(email, customer.getEmail())) {
                throw new EmailAlreadyRegisteredException(email);
            }
        }
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

    public ObjectRepository<Customer> getCustomers() {
        return customerRepository;
    }
}
