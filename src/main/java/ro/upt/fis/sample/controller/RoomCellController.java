package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.model.Room;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomCellController extends JFXListCell<Room> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private ImageView roo;

    @FXML
    private Label roomCellType = new Label();

    @FXML
    private Label roomCellPrice = new Label();

    @FXML
    private Label roomCellCapacity = new Label();

    @FXML
    private Label roomCellReserved = new Label();

    private FXMLLoader fxmlLoader;

    @FXML
    void initialize() {

    }

    @Override
    public void updateItem(Room room, boolean empty) {
        super.updateItem(room, empty);

        if (empty || room == null) {
            setText(null);
            setGraphic(null);
        } else {
            // make this controller to be the cell controller
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource(PathViewConstants.ROOM_CELL));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // set up all the labels inside the cell
            roomCellType.setText("TYPE: " + room.getRoomType());
            roomCellPrice.setText("PRICE: " + room.getPrice());
            roomCellCapacity.setText("CAPACITY: " + room.getCapacity());
            if (room.isReserved()) {
                roomCellReserved.setText("RESERVED");
            } else {
                roomCellReserved.setText("NOT RESERVED");
            }

            setText(null);
            setGraphic(rootAnchor);
        }

    }
}
