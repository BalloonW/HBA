package ro.upt.fis.sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.upt.fis.sample.constants.PathViewConstants;
import ro.upt.fis.sample.enums.Role;
import ro.upt.fis.sample.exceptions.EmailAlreadyRegisteredException;
import ro.upt.fis.sample.exceptions.EmptyInputException;
import ro.upt.fis.sample.exceptions.InvalidEmailException;
import ro.upt.fis.sample.exceptions.InvalidPasswordException;
import ro.upt.fis.sample.services.CustomerServices;
import ro.upt.fis.sample.services.HotelServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpEmail;

    @FXML
    private TextField signUpPhone;

    @FXML
    private ChoiceBox<Role> signUpChoiceBox = new ChoiceBox<>();

    @FXML
    private JFXButton signUpButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label invalidEmailLabel;

    @FXML
    private Label invalidPasswordLabel;

    private HotelServices hotelServices = new HotelServices();
    private CustomerServices customerServices = new CustomerServices();

    @FXML
    void initialize() {

        signUpChoiceBox.getItems().add(Role.CUSTOMER);
        signUpChoiceBox.getItems().add(Role.HOTEL);

        final Role[] role = new Role[1];

        signUpChoiceBox.setOnAction(actionEvent -> {
            role[0] = signUpChoiceBox.getSelectionModel().getSelectedItem();
        });

        signUpButton.setOnAction(actionEvent -> {

            try {
                validateFields(signUpFirstName.getText().trim(), signUpLastName.getText().trim(),
                        signUpPhone.getText().trim(), signUpEmail.getText().trim(),
                        signUpPassword.getText().trim(), role[0]);

                if (role[0].equals(Role.CUSTOMER)) {
                    try {
                        customerServices.initDataBase();

                        customerServices.addCustomer(signUpFirstName.getText(), signUpLastName.getText(), signUpPhone.getText(),
                                signUpEmail.getText(), signUpPassword.getText());

                        customerServices.closeDatabase();
                    } catch (EmailAlreadyRegisteredException e) {
                        customerServices.closeDatabase();
                        e.printStackTrace();
                        errorLabel.setText("EMAIL ALREADY REGISTERED");
                    }
                } else if (role[0].equals(Role.HOTEL)) {
                    try {
                        hotelServices.initDatabase();

                        hotelServices.addHotel(signUpFirstName.getText(), signUpLastName.getText(), signUpPhone.getText(),
                                signUpEmail.getText(), signUpPassword.getText());

                        hotelServices.closeDatabase();
                    } catch (EmailAlreadyRegisteredException e) {
                        e.printStackTrace();
                        hotelServices.closeDatabase();
                        errorLabel.setText("EMAIL ALREADY REGISTERED");
                    }
                }
                openNewScreen(signUpButton, PathViewConstants.LOGIN);
            } catch (EmptyInputException e) {
                e.printStackTrace();
                errorLabel.setText("COMPLETE ALL FIELDS");
            } catch (InvalidEmailException e) {
                invalidEmailLabel.setText("email is not valid");
                e.printStackTrace();
            } catch (InvalidPasswordException e) {
                invalidPasswordLabel.setText("password is not valid");
                e.printStackTrace();
            }
        });
    }

    private void validateFields(String firstName, String lastName,
                                String phoneNumber, String email, String password, Role role) {
        if (verifyEmpty(firstName)) {
            throw new EmptyInputException("first name is empty");
        }
        if (verifyEmpty(lastName)) {
            throw new EmptyInputException("last name is empty");
        }
        if (verifyEmpty(phoneNumber)) {
            throw new EmptyInputException("phone number is empty");
        }
        if (verifyEmpty(email)) {
            throw new EmptyInputException("email is empty");
        }
        validateEmail(email);
        if (verifyEmpty(password)) {
            throw new EmptyInputException("password is empty");
        }
        validatePassword(password);
        if (role == null) {
            throw new EmptyInputException("role is empty");
        }
    }

    private boolean verifyEmpty(String inputToVerify) {
        return inputToVerify.isEmpty();
    }

    private void openNewScreen(JFXButton button, String viewPath) {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource(viewPath));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void validateEmail(String email) {
        String regexEmail = "([^@]+@[^@]+\\.)(\\w{2,3})$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException(email + "must be of this form: user.name@exemple.com");
        }
    }

    private void validatePassword(String password) {
        String regexPassword = "((?=.*[A-Z])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@^_`{|}~])).{10,}$";
        Pattern pattern = Pattern.compile(regexPassword);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new InvalidPasswordException("password must be of this form: user.name@exemple.com");
        }
    }
}
