package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
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

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class HotelCellController extends JFXListCell<Map<Hotel, Customer>> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private Label hotelCellName;

    @FXML
    private Label hotelCellLocation;

    @FXML
    private Label hotelCellRating;

    @FXML
    private JFXButton hotelCellViewButton;

    private FXMLLoader fxmlLoader;

    private Hotel localHotel = new Hotel();
    private Customer customer = new Customer();

    @FXML
    void initialize() {
        // set up actions on view button
        hotelCellViewButton.setOnAction(event -> {
            // open view for customer screen
            Stage stage1 = (Stage) hotelCellViewButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(PathViewConstants.HOTEL_VIEW_FOR_CLIENT));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            HotelViewForClientController hotelViewForClientController = loader.getController();
            hotelViewForClientController.setCustomer(this.customer);
            hotelViewForClientController.setHotel(this.localHotel);

            stage.show();
        });
    }

    @Override
    public void updateItem(Map<Hotel, Customer> hotelAndCustomer, boolean empty) {
        super.updateItem(hotelAndCustomer, empty);

        if (empty || hotelAndCustomer == null) {
            setText(null);
            setGraphic(null);
        } else {
            hotelAndCustomer
                    .forEach((key, value) -> {
                                this.localHotel = key;
                                this.customer = value;
                            }
                    );

            // make this controller to be the cell controller
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource(PathViewConstants.HOTEL_CELL));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // set up all the labels inside the cell
            hotelCellName.setText(localHotel.getHotelName());
            hotelCellLocation.setText(localHotel.getLocation());
            hotelCellRating.setText(localHotel.getRating() + "");

            setText(null);
            setGraphic(rootAnchor);
        }
    }
}
