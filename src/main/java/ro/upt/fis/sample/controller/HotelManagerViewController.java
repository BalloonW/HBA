package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
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
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.model.Room;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HotelManagerViewController {

    private String userEmail;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextArea hotelViewDescriptionArea;

    @FXML
    private JFXListView<Room> hotelViewRoomsList = new JFXListView<>();

    @FXML
    private Label hotelViewEmailLabel;

    @FXML
    private Label hotelViewPhoneLabel;

    @FXML
    private Label hotelViewLocationLabel;

    @FXML
    private JFXButton hotelViewAddRoomButton;

    @FXML
    private Label hotelViewHelloLabel;

    @FXML
    private JFXButton hotelViewLogoutButton;

    @FXML
    private JFXButton hotelViewEditButton;


    private HotelServices hotelServices = new HotelServices();
    private Hotel hotel = new Hotel();
    private boolean fromEdit = false;
    // list of rooms to be showed
    private ObservableList<Room> rooms;

    @FXML
    void initialize() {
        anchorPane.setOnMouseClicked(event -> {
            if (!isFromEdit()) {
                setHotel();
                hotelServices.closeDatabase();
            }

            hotelViewHelloLabel.setText("Hello" + " " + hotel.getFirstName());
            hotelViewEmailLabel.setText(hotel.getEmail());
            hotelViewPhoneLabel.setText(hotel.getPhoneNumber());
            hotelViewLocationLabel.setText(hotel.getLocation());
            hotelViewDescriptionArea.setText(hotel.getDescription());

            // update rooms list view area
            rooms = FXCollections.observableArrayList();
            rooms.addAll(hotel.getRoomList());
            hotelViewRoomsList.setItems(rooms);
            hotelViewRoomsList.setCellFactory(RoomCellController -> new RoomCellController());
        });

        hotelViewEditButton.setOnAction(event -> {
            Stage stage1 = (Stage) hotelViewEditButton.getScene().getWindow();
            stage1.close();

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(PathViewConstants.HOTEL_EDIT));
            try {
                loader.setRoot(loader.getRoot());
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            HotelEditController hotelEditController = loader.getController();
            hotelEditController.setHotel(this.hotel);

            stage.show();
        });

        hotelViewLogoutButton.setOnAction(event -> {
            Stage stage1 = (Stage) hotelViewLogoutButton.getScene().getWindow();
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

        hotelViewAddRoomButton.setOnAction(event -> {
            Stage stage1 = (Stage) hotelViewAddRoomButton.getScene().getWindow();
            stage1.close();

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(PathViewConstants.ADD_ROOM));
            try {
                loader.setRoot(loader.getRoot());
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            AddRoomController addRoomController = loader.getController();
            addRoomController.setHotel(this.hotel);

            stage.show();
        });
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    private void setHotel() {
        hotel = hotelServices.getHotelManager(getUserEmail());
    }

    public boolean isFromEdit() {
        return fromEdit;
    }

    public void setFromEdit(boolean fromEdit) {
        this.fromEdit = fromEdit;
    }
}
