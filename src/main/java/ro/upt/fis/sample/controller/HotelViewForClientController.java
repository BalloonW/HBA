package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.enums.RoomType;
import ro.upt.fis.sample.exceptions.AllRoomReservedException;
import ro.upt.fis.sample.exceptions.InvalidDateException;
import ro.upt.fis.sample.model.Customer;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.model.Room;
import ro.upt.fis.sample.services.CustomerServices;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class HotelViewForClientController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextArea hotelViewDescriptionArea;

    @FXML
    private JFXListView<Room> roomsList;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label helloLabel;

    @FXML
    private JFXButton backButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker enddatePicker;

    @FXML
    private ChoiceBox<Integer> capacityChioce = new ChoiceBox<>();

    @FXML
    private ChoiceBox<RoomType> roomTypeChoice = new ChoiceBox<>();

    @FXML
    private JFXCheckBox breakfastService;

    @FXML
    private JFXCheckBox fitnessService;

    @FXML
    private JFXButton reservationButton;

    @FXML
    private Label totalPrice;

    @FXML
    private Label ALL_ROOMS_RESRVED;

    @FXML
    private Label sorryLABEL;

    @FXML
    private Label INVALID_DATE;

    @FXML
    private Label reservationSuccess;

    private Hotel hotel = new Hotel();
    private Customer customer = new Customer();
    private ObservableList<Room> rooms;
    private Reservation reservation = new Reservation();
    private HotelServices hotelServices = new HotelServices();
    private CustomerServices customerServices = new CustomerServices();

    @FXML
    void initialize() {

        capacityChioce.getItems().add(1);
        capacityChioce.getItems().add(2);
        capacityChioce.getItems().add(3);
        capacityChioce.getItems().add(4);

        roomTypeChoice.getItems().add(RoomType.LUX);
        roomTypeChoice.getItems().add(RoomType.USUAL);

        anchorPane.setOnMouseClicked(event -> {

            // set up all the labels
            helloLabel.setText("Welcome " + customer.getFirstName() + " " + customer.getLastName());
            locationLabel.setText(hotel.getLocation());
            phoneLabel.setText(hotel.getPhoneNumber());
            emailLabel.setText(hotel.getEmail());
            hotelViewDescriptionArea.setText(hotel.getDescription());

            // populate listView of rooms
            rooms = FXCollections.observableArrayList();
            rooms.addAll(hotel.getRoomList());
            roomsList.setItems(rooms);
            roomsList.setCellFactory(RoomCellController -> new RoomCellController());
        });

        reservationButton.setOnAction(event -> {
            try {
                LocalDate localStartDate = startDatePicker.getValue();
                Instant instant = Instant.from(localStartDate.atStartOfDay(ZoneId.systemDefault()));
                reservation.setStartDate(Date.from(instant));

                validateDate(Date.from(instant));

                LocalDate endDate = enddatePicker.getValue();
                instant = Instant.from(endDate.atStartOfDay(ZoneId.systemDefault()));
                reservation.setEndDate(Date.from(instant));

                int capacity = capacityChioce.getSelectionModel().getSelectedItem();
                RoomType roomType = roomTypeChoice.getSelectionModel().getSelectedItem();

                reservation.setTotalCost(0);

                if (breakfastService.isSelected()) {
                    reservation.setTotalCost(reservation.getTotalCost() + 50);
                }

                if (fitnessService.isSelected()) {
                    reservation.setTotalCost(reservation.getTotalCost() + 100);
                }

                Room room = hotel.getRoom(capacity, roomType);
                reservation.setRoom(room);
                reservation.setHotelManagerEmail(hotel.getEmail());
                reservation.setClientEmail(customer.getEmail());
                hotelServices.addReservation(hotel.getEmail(), reservation);
                customerServices.addReservation(customer.getEmail(), reservation);
                reservation.setTotalCost(reservation.getTotalCost() + room.getPrice());
                totalPrice.setText("Total price: " + reservation.getTotalCost() + " $");

                reservationSuccess.setText("Room reserved!");

            } catch (InvalidDateException e) {
                e.printStackTrace();
                INVALID_DATE.setText("date is invalid");
            } catch (AllRoomReservedException e) {
                e.printStackTrace();
                sorryLABEL.setText("We're sorry, ");
                ALL_ROOMS_RESRVED.setText("All rooms of this type are reserved");
            }
        });

        backButton.setOnAction(event -> {
            // return to client view
            Stage stage1 = (Stage) backButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(PathViewConstants.CLIENT_VIEW));

            try {
                loader.setRoot(loader.getRoot());
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            CustomerViewController customerViewController = loader.getController();
            customerViewController.setUserEmail(this.customer.getEmail());

            stage.show();
        });
    }


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void validateDate(Date date) {
        Date currentDate = new Date(System.currentTimeMillis());
        if (date.before(currentDate)) {
            throw new InvalidDateException("date should be after the day of today");
        }
    }
}