package controller;

import Util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.IOException;

public class SignUpController {

    @FXML
    private  TextField nameTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField PhoneNumTF;

    @FXML
    private PasswordField passTF;

    @FXML
    private PasswordField ConPassTF;

    @FXML
    private Button btSubmit;

    @FXML
    private Hyperlink linkLogin;

    private ButtonController bc = new ButtonController();

    @FXML
    public void openLogInView(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/example/vietnamtravel/LogIn.fxml")));
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleSubmit(ActionEvent e) throws IOException {
      String phoneNumber = PhoneNumTF.getText();
      String passWord = passTF.getText();
      String name = nameTF.getText();
      String email = emailTF.getText();
      String confirmPassword = ConPassTF.getText();
        if (phoneNumber.isEmpty() || passWord.isEmpty() || confirmPassword.isEmpty()) {
            bc.showErrorAlert("Error", "Phone number and password cannot be empty.");
            return;
        }

        if (!passWord.equals(confirmPassword)) {
            bc.showErrorAlert("Error", "Password and confirm password do not match.");
            return;
        }

        if (isPhoneNumberExists(phoneNumber)) {
            bc.showErrorAlert("Error", "Phone number already exists.");
        } else {
            registerUser(email, name, phoneNumber, passWord);
            bc.showInformationAlert("Success", "Registration successful!");
            openLogInView(e);
        }
    }

    private boolean isPhoneNumberExists(String phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u WHERE u.soDienThoai = :phoneNumber", User.class);
            query.setParameter("phoneNumber", phoneNumber);
            return !query.getResultList().isEmpty();
        }
    }

    public void registerUser(String email, String name, String phoneNumber, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = new User();
            user.setEmail(email);
            user.setHoTen(name);
            user.setSoDienThoai(phoneNumber);
            user.setMatKhau(password);
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
