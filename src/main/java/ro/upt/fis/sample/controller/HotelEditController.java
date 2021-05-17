package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HotelEditController {

    private Hotel hotel = new Hotel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField editLocation;

    @FXML
    private TextField editPhoneNumber;

    @FXML
    private TextArea editDescription;

    @FXML
    private TextField editLastName;

    @FXML
    private TextField editFirstName;

    @FXML
    private TextField hotelEditHotelName;

    @FXML
    private JFXButton editSaveButton;

    private HotelServices hotelServices = new HotelServices();

    @FXML
    void initialize() {
        editSaveButton.setOnAction(event -> {
            // set all the changes
            if (!editFirstName.getText().isEmpty()) {
                hotel.setFirstName(editFirstName.getText());
            }

            if (!editLastName.getText().isEmpty()) {
                hotel.setLastName(editLastName.getText());
            }

            if (!editLocation.getText().isEmpty()) {
                hotel.setLocation(editLocation.getText());
            }

            if (!editDescription.getText().isEmpty()) {
                hotel.setDescription(editDescription.getText());
            }

            if (!editPhoneNumber.getText().isEmpty()) {
                hotel.setPhoneNumber(editPhoneNumber.getText());
            }

            if (!hotelEditHotelName.getText().isEmpty()) {
                hotel.setHotelName(hotelEditHotelName.getText());
            }

            //update database
            hotelServices.updateDatabase(getHotel());

            // go back to view screen
            Stage stage1 = (Stage) editSaveButton.getScene().getWindow();
            stage1.close();

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(PathViewConstants.MANAGER_VIEW));
            try {
                loader.setRoot(loader.getRoot());
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            HotelManagerViewController hotelManagerViewController = loader.getController();
            hotelManagerViewController.setHotel(getHotel());
            hotelManagerViewController.setFromEdit(true);

            stage.show();
        });
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Hotel getHotel() {
        return hotel;
    }
}
