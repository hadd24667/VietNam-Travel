package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Images;
import model.TouristAttraction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javafx.event.EventHandler;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.NumberFormat;
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.scene.control.Tooltip;

public class MainViewController  extends MenuController implements AccountTextController {

    @FXML
    private Button btAccount;

    @FXML
    private Button btBooking;

    @FXML
    private Button btLocation;

    @FXML
    private Button btLogin;

    @FXML
    private Button btPrice;

    @FXML
    private Button btSearch;

    @FXML
    private Button btSignUp;

    @FXML
    private Button menuDes;

    @FXML
    private Button menuHome;

    @FXML
    private Button menuStay;

    @FXML
    private Button menuTicket;

    @FXML
    private AnchorPane paneAccount;

    @FXML
    private AnchorPane paneLogin;

    @FXML
    private TextField textSearch;

    @FXML
    private VBox vBoxSearch;

    @FXML
    private Label textLocation;

    @FXML
    private Label textPlace;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label textContent;

    private Session session;

    public MainViewController()
    {
        this.session = HibernateUtil.getSessionFactory().openSession();

    }

    @FXML
    public void initialize() {
        // Tạo một tooltip mới
        Tooltip tooltip = new Tooltip("Discount 30% for children!");

        tooltip.setStyle("-fx-font-size: 25; -fx-background-color: #c76b79; -fx-text-fill: #f9f7e8; -fx-background-radius: 6; -fx-background-insets: 0; -fx-padding: 5;");

        // Đặt tooltip cho nút btPrice
        btPrice.setTooltip(tooltip);
    }



    //Controller MenuBar
    @Override
    public void handleMenuDestiAction(ActionEvent event) throws IOException {

        super.handleMenuDestiAction(event);
    }

    @Override
    public void handleMenuHomeAction(ActionEvent event) throws IOException {

        super.handleMenuHomeAction(event);
    }

    @Override
    public void handleMenuStayAction(ActionEvent event) throws IOException {

        super.handleMenuStayAction(event);
    }

    @Override
    public void handleMenuTicketAction(ActionEvent event) throws IOException {
        super.handleMenuTicketAction(event);
    }

    @Override
    public void handleBtAccountAction(ActionEvent event) throws IOException {
        super.handleBtAccountAction(event);
    }
    //setText ACCOUNT BUTTON
    public void setBtAccountText(String hoTen) {
        btAccount.setText(hoTen);
        btAccount.setMinWidth(Button.USE_PREF_SIZE);
        btAccount.setMaxWidth(Double.MAX_VALUE);
    }

    //SEARCH BUTTON
    @FXML
    public void handleSearchAction(ActionEvent event) {
        try{
            String keyword = textSearch.getText();
            // Begin a transaction
            session.beginTransaction();

            // Create a Hibernate Query
            Query<TouristAttraction> query = session.createQuery("FROM TouristAttraction WHERE ten LIKE :keyword", TouristAttraction.class);
            query.setParameter("keyword", "%" + keyword + "%");

            // Execute the query and get the result list
            List<TouristAttraction> attractions = query.getResultList();

            // Commit the transaction and close the session
            session.getTransaction().commit();

            // Clear the vBoxSearch
            vBoxSearch.getChildren().clear();

            // For each result, create a new button, set its text to the result, add a style class, and add it to the vBoxSearch
            for (TouristAttraction attraction : attractions) {
                Button resultButton = new Button();
                resultButton.setText(attraction.getTen());
                resultButton.getStyleClass().add("resultButton");

                // Add an action to the result button
                resultButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Handle the button click here
                        System.out.println("Button clicked: " + resultButton.getText());

                        // Begin a transaction
                        session.beginTransaction();

                        // Create a Hibernate Query
                        Query<TouristAttraction> query = session.createQuery("FROM TouristAttraction WHERE ten = :ten", TouristAttraction.class);
                        query.setParameter("ten", resultButton.getText());

                        // Execute the query and get the result
                        TouristAttraction attraction = query.getSingleResult();

                        // Commit the transaction and close the session
                        session.getTransaction().commit();


                        //SAVE TouristAttractions_ID to TourData
                        int TouristAttractions_ID = attraction.getId();
                        TourData tourData = new TourData();
                        tourData.setTouristAttractions_ID(TouristAttractions_ID);

                        System.out.println(attraction.getId());

                        // Parse giaVe from BigDecimal to int
                        int giaVe = attraction.getGiaVe().intValueExact();


                        //SAVE GiaVe to TourData
                        tourData.setGiaVe(giaVe);



                        // Create a NumberFormat instance for the default locale
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                        // Set the group size to 3
                        numberFormat.setGroupingUsed(true);
                        numberFormat.setMaximumFractionDigits(0);

                        // Format the number
                        String formattedGiaVe = numberFormat.format(giaVe);

                        // Set the text for btPrice
                        btPrice.setText(formattedGiaVe + " VND");


                        // Set the text fields
                        textPlace.setText(attraction.getTen());
                        textLocation.setText(attraction.getDiaChi());
                        textContent.setText(attraction.getMoTa());

                        try{
                            // Get the image data
                            Images images = attraction.getImages();
                            byte[] imageData = images.getDuLieuAnh();

                            // Decode the image data
                            Image image = new Image(new ByteArrayInputStream(imageData));

                            // Set the background of the root
                            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
                            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
                            Background background = new Background(backgroundImage);
                            mainPane.setBackground(background);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

                vBoxSearch.getChildren().add(resultButton);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void handleBookingAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TourBookingView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Thêm logo cho cửa sổ ứng dụng
            Image logo = new Image(getClass().getResourceAsStream("/img/MainLogo.png"));
            Stage stage = new Stage();
            stage.getIcons().add(logo);

            stage.setTitle("Vietnam Travel");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Class lưu dữ liệu Tour đã đặt
    public class TourData {
        private static Integer TouristAttractions_ID;

        private static Integer GiaVe;

        public static Integer getTouristAttractions_ID() {
            return TouristAttractions_ID;
        }

        public static void setTouristAttractions_ID(Integer TouristAttractions_ID) {
            TourData.TouristAttractions_ID = TouristAttractions_ID;
        }

        public static Integer getGiaVe() {
            return GiaVe;
        }

        public static void setGiaVe(Integer GiaVe) {
            TourData.GiaVe = GiaVe;
        }
    }

}


