package controller;

import Util.HibernateUtil;
//import controller.OpenSessionHB.Global;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class LogInController {

    @FXML
    private PasswordField passTF;

    @FXML
    private AnchorPane paneLogin;

    @FXML
    private AnchorPane paneAccount;

    @FXML
    private TextField PhoneNumTF;

    @FXML
    private Button btSubmit;

    @FXML
    private Hyperlink linkSignUp;


    private ButtonController bc = new ButtonController();

    public void openSignUpView(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/example/vietnamtravel/SignUpView.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleSubmit(ActionEvent event) throws IOException {
        String phoneNumber = PhoneNumTF.getText();
        String password = passTF.getText();

        if (phoneNumber.isEmpty() || password.isEmpty()) {
            bc.showErrorAlert("Error", "Phone number and password cannot be empty.");
            return;
        }

        if (isLoginValid(phoneNumber, password)) {
            bc.showInformationAlert("Success", "Login successful!");

            // Save phoneNumber to UserSession
            UserSession.setPhoneNumber(phoneNumber);

            // Save userID to UserSession
            Integer userId = getUserId(phoneNumber);
            UserSession.setUserID(userId);

            // Save hoTen to UserSession
            UserSession.setHoTen(getHoTen(phoneNumber));

            // Get the user's ad value
            String ad = getAd(phoneNumber);

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader;
            String fxmlPath;

            // If the user is an admin, open the AdminAttractions view
            if ("admin".equals(ad)) {
                fxmlPath = "/com/example/vietnamtravel/AdminAttractions.fxml";
            } else {
                // Otherwise, open the MainView
                fxmlPath = "/com/example/vietnamtravel/MainView.fxml";
            }

            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

            if (!"admin".equals(ad)) {
                MainViewController mainViewController = loader.getController();

                // Get HoTen from SoDienThoai and set it to btAccount
                String hoTen = getHoTen(PhoneNumTF.getText());
                mainViewController.setBtAccountText(hoTen);
            }
        } else {
            bc.showErrorAlert("Error", "Invalid phone number or password.");
        }
    }

    // Method to get the ad value of a user
    public String getAd(String phoneNumber) {
        Session session = factory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE soDienThoai = :phoneNumber", User.class);
        query.setParameter("phoneNumber", phoneNumber);
        User user = query.uniqueResult();
        return user.getAd();
    }

    private static SessionFactory factory; // Tạo SessionFactory tĩnh

    public boolean isLoginValid(String phoneNumber, String password) {
        if (factory == null) {
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }

        try (Session session = factory.openSession()) { // Sử dụng try-with-resources để tự động đóng Session
            session.beginTransaction();

            Query<User> query = session.createQuery("FROM User u WHERE u.soDienThoai = :phoneNumber AND u.matKhau = :password", User.class);
            query.setParameter("phoneNumber", phoneNumber);
            query.setParameter("password", password);

            List<User> users = query.getResultList();

            session.getTransaction().commit();
            return !users.isEmpty();
        }
    }

//    lấy HoTen của controller set text cho btAccount
    public String getHoTen(String phoneNumber) {
        Session session = factory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE soDienThoai = :phoneNumber", User.class);
        query.setParameter("phoneNumber", phoneNumber);
        User user = query.uniqueResult();
//        session.close();
        return user.getHoTen();
    }

    //lưu trữ phoneNumber vào UserSession  để thực hiện các tác vụ sau
    public class UserSession {
        private static Integer userID;

        private static String phoneNumber;

        private static String hoTen;


        public static Integer getUserID() {
            return userID;
        }

        public static void setUserID(Integer userID) {
            UserSession.userID = userID;
        }

        public static String getPhoneNumber() {
            return phoneNumber;
        }

        public static void setPhoneNumber(String phoneNumber) {
            UserSession.phoneNumber = phoneNumber;
        }

        public static String getHoTen() {
            return hoTen;
        }

        public static void setHoTen(String hoTen) {
            UserSession.hoTen = hoTen;
        }
    }
    //
    public Integer getUserId(String phoneNumber) {
        Session session = factory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE soDienThoai = :phoneNumber", User.class);
        query.setParameter("phoneNumber", phoneNumber);
        User user = query.uniqueResult();
        return user.getId();
    }
}
