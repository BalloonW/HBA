package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.enums.RoomType;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Room;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;

public class AddRoomController {

    @FXML
    private JFXButton addRoomCancel;

    @FXML
    private TextField addRoomPrice;

    @FXML
    private ChoiceBox<RoomType> addRoomType = new ChoiceBox<>();

    @FXML
    private JFXButton addRoom;

    @FXML
    private ChoiceBox<Integer> addRoomCapacity = new ChoiceBox<>();

    private HotelServices hotelServices = new HotelServices();

    private Hotel hotel = new Hotel();
    private Room room;

    @FXML
    void initialize() {
        addRoomType.getItems().add(RoomType.USUAL);
        addRoomType.getItems().add(RoomType.LUX);

        addRoomCapacity.getItems().add(1);
        addRoomCapacity.getItems().add(2);
        addRoomCapacity.getItems().add(3);
        addRoomCapacity.getItems().add(4);

        addRoomType.setOnAction(event -> {

        });

        addRoomCapacity.setOnAction(event -> {

        });

        addRoom.setOnAction(event -> {
            // get info for creating new room
            RoomType type = addRoomType.getSelectionModel().getSelectedItem();
            int capacity = addRoomCapacity.getSelectionModel().getSelectedItem();
            int price = Integer.parseInt(addRoomPrice.getText());

            room = new Room(capacity, price, type);

            // update database info
            hotel.addRoom(room);

            hotelServices.updateDatabase(hotel);

            // go back to main screen
            openManagerViewScreen();
        });

        addRoomCancel.setOnAction(event -> {
            openManagerViewScreen();
        });
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    private void openManagerViewScreen() {
        Stage stage1 = (Stage) addRoom.getScene().getWindow();
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
    }
}