package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.model.Customer;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.services.CustomerServices;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CustomerViewController {

    private String userEmail;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane HBAPane;

    @FXML
    private JFXButton clientViewLogoutButton;

    @FXML
    private JFXListView<Map<Hotel, Customer>> clientViewHotelsAvailable;

    @FXML
    private JFXListView<Reservation> clientViewListOfReservations;

    @FXML
    private Label clientViewEmailLabel;

    @FXML
    private Label clientViewPhoneLabel;

    @FXML
    private Label welcomeLable;

    private ObservableList<Map<Hotel, Customer>> hotels;
    private HotelServices hotelServices = new HotelServices();
    private List<Map<Hotel, Customer>> hotelMapList = new ArrayList<>();
    private List<Hotel> hotelList;

    private ObservableList<Reservation> reservationObservableList;

    private CustomerServices customerServices = new CustomerServices();
    private Customer customer = new Customer();

    @FXML
    void initialize() {
        HBAPane.setOnMouseClicked(event -> {
            setUser();

            // get all the hotels
            hotelList = hotelServices.getAllHotels();
            hotelServices.closeDatabase();

            // construct a list of maps
            for (Hotel hotel : hotelList) {
                Map<Hotel, Customer> map = new HashMap<>();
                map.put(hotel, this.customer);

                if (hotelMapList != null) {
                    hotelMapList.add(map);
                }
            }

            // populate hotels list view
            hotels = FXCollections.observableArrayList();
            if (hotelMapList != null) {
                hotels.addAll(hotelMapList);
                clientViewHotelsAvailable.setItems(hotels);
                clientViewHotelsAvailable.setCellFactory(HotelCellController -> new HotelCellController());
            }
            hotelMapList = null;

            // populate view of reservations
            if (this.customer.getReservationList().size() != 0) {
                reservationObservableList = FXCollections.observableArrayList();
                reservationObservableList.addAll(this.customer.getReservationList());
                clientViewListOfReservations.setItems(reservationObservableList);
                clientViewListOfReservations.setCellFactory(ReserationCellController -> new ReserationCellController());
            }

            // initialize all the labels
            clientViewEmailLabel.setText(customer.getEmail());
            clientViewPhoneLabel.setText(customer.getPhoneNumber());
            welcomeLable.setText("Welcome " + customer.getFirstName());
        });

        clientViewLogoutButton.setOnAction(event -> {
            Stage stage1 = (Stage) clientViewLogoutButton.getScene().getWindow();
            stage1.close();

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(PathViewConstants.LOGIN));
            try {
                loader.setRoot(loader.getRoot());
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        });


    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUser() {
        customerServices.initDataBase();
        this.customer = customerServices.getClient(getUserEmail());
        customerServices.closeDatabase();
    }

    public String getUserEmail() {
        return userEmail;
    }
}
