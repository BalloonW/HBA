package ro.upt.fis.sample.model;

import lombok.Data;
import ro.upt.fis.sample.enums.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Reservation {

    private String hotelManagerEmail;
    private String clientEmail;
    private Date startDate;
    private Date endDate;
    private Room room;
    private int totalCost = 0;
    private List<Service> services = new ArrayList<>();
    // add list of services

    public Reservation(String hotelManagerEmail, String clientEmail, Date startDate, Date endDate, Room room) {
        this.hotelManagerEmail = hotelManagerEmail;
        this.clientEmail = clientEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    public Reservation() {

    }

    public void addService(Service service) {
        services.add(service);
    }
}
