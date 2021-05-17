package ro.upt.fis.sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.upt.fis.sample.animations.Shaker;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.enums.Role;
import ro.upt.fis.sample.exceptions.EmptyInputException;
import ro.upt.fis.sample.model.Customer;
import ro.upt.fis.sample.model.Hotel;
import ro.upt.fis.sample.services.CustomerServices;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    private String userEmail;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private Button loginButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private Label errorIntroduceCredentialsLabel;

    @FXML
    private ChoiceBox<Role> loginChoiceBox = new ChoiceBox<>();

    private HotelServices hotelServices = new HotelServices();
    private CustomerServices customerServices = new CustomerServices();

    @FXML
    void initialize() {
        loginChoiceBox.getItems().add(Role.CUSTOMER);
        loginChoiceBox.getItems().add(Role.HOTEL);

        final Role[] role = new Role[1];

        loginChoiceBox.setOnAction(actionEvent -> {
            role[0] = loginChoiceBox.getSelectionModel().getSelectedItem();
        });

        loginButton.setOnAction(actionEvent -> {
            // get introduced information
            String loginText = loginUsername.getText();
            String loginPwd = loginPassword.getText();

            // login
            loginUser(loginText, loginPwd, role[0], loginButton);
        });

        loginSignUpButton.setOnAction(actionEvent -> {
            openNewScreen(loginSignUpButton, PathViewConstants.SIGN_UP);
        });
    }

    private void openNewScreen(Button button, String pathToView) {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource(pathToView));
        try {
            loader.setRoot(loader.getRoot());
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        if (pathToView.equals(PathViewConstants.CLIENT_VIEW)) {
            CustomerViewController customerViewController = loader.getController();
            customerViewController.setUserEmail(this.userEmail);
        } else if (pathToView.equals(PathViewConstants.MANAGER_VIEW)) {
            HotelManagerViewController hotelManagerViewController = loader.getController();
            hotelManagerViewController.setUserEmail(this.userEmail);
        }

        stage.show();
    }

    private void loginUser(String userName, String password, Role role, Button button) {
        try {
            validateCredentials(userName, password, role);
            
            // set userEmail and role to help next main screen
            this.userEmail = userName;

            // login manager
            if (role.equals(Role.HOTEL)) {
                hotelServices.initDatabase();
                Hotel hotel = hotelServices.getHotelManager(userName, password);
                hotelServices.closeDatabase();

                if (hotel != null) {
                    openNewScreen(button, PathViewConstants.MANAGER_VIEW);
                } else {
                    Shaker usernameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    usernameShaker.shake();
                    passwordShaker.shake();
                }
                // login customer
            } else if (role.equals(Role.CUSTOMER)) {
                customerServices.initDataBase();
                Customer customer = customerServices.getClient(userName, password);
                customerServices.closeDatabase();

                if (customer != null) {
                    openNewScreen(button, PathViewConstants.CLIENT_VIEW);
                } else {
                    Shaker usernameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    usernameShaker.shake();
                    passwordShaker.shake();
                }
            }
        } catch (EmptyInputException e) {
            e.printStackTrace();
            errorIntroduceCredentialsLabel.setText("introduce credentials");
        }
    }

    private void validateCredentials(String userName, String password, Role role) {
        if (role == null || userName == null || password == null) {
            throw new EmptyInputException("credentials fields were not filled");
        }
    }
}
