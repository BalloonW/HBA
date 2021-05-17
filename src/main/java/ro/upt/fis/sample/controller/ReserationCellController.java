package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.model.Reservation;
import ro.upt.fis.sample.services.CustomerServices;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReserationCellController extends JFXListCell<Reservation> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label START = new Label();

    @FXML
    private Label END = new Label();

    @FXML
    private Label HOTEL_NAME = new Label();

    @FXML
    private Label PRICE = new Label();

    @FXML
    private JFXButton CANCEL;

    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private Label cancelLabel;

    private FXMLLoader fxmlLoader;

    private CustomerServices customerServices = new CustomerServices();
    private HotelServices hotelServices = new HotelServices();
    private Reservation reservation = new Reservation();

    @FXML
    void initialize() {
        CANCEL.setOnAction(event -> {
            hotelServices.cancelReservation(reservation.getHotelManagerEmail(), reservation);
            customerServices.cancelReservation(reservation.getClientEmail(), reservation);
            cancelLabel.setText("Cancelled");
        });

    }

    @Override
    public void updateItem(Reservation reservation, boolean empty) {
        super.updateItem(reservation, empty);

        if (empty || reservation == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.reservation = reservation;

            // make this controller to be the cell controller
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource(PathViewConstants.RESERVATION_CELL));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // set up all the labels inside the cell
            HOTEL_NAME.setText("Hotel name: " + hotelServices.getHotelManager(reservation.getHotelManagerEmail()).getHotelName());
            hotelServices.closeDatabase();
            START.setText("Start date: " + reservation.getStartDate());
            END.setText("End date: " + reservation.getEndDate());
            PRICE.setText("Price: " + reservation.getTotalCost() + " $");

            setText(null);
            setGraphic(rootAnchor);
        }
    }
}
