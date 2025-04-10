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
import model.Accommodation;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import model.Images;

import javafx.scene.shape.Circle;

import controller.StayBookingsController;


public class StayController extends MenuController implements AccountTextController {


    @FXML
    private Button btAccount;

    @FXML
    private Button btSearch;

    @FXML
    private TextField hotelPlaceTF;

    @FXML
    private VBox listVbox;

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


    private Session session;



    public StayController() {
        // Assuming you have a SessionFactory instance somewhere
        this.session = HibernateUtil.getSessionFactory().openSession();
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

    //Controller Stay
    @FXML
    public void handleBtSearchAction(ActionEvent event) {
        // Get the search term from the TextField
        String searchTerm = hotelPlaceTF.getText();

        // Start a transaction
        session.beginTransaction();

        // Query the Accommodations table
        Query<Accommodation> query = session.createQuery("FROM Accommodation WHERE diaChi LIKE :searchTerm", Accommodation.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        List<Accommodation> accommodations = query.getResultList();

        // Commit the transaction  the session
        session.getTransaction().commit();

        // Clear the ListView
        listVbox.getChildren().clear();

        // For each Accommodation record
        for (Accommodation accommodation : accommodations) {
            try {
                // Load a new HotelItem from the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/HotelItem.fxml"));
                AnchorPane hotelItem = loader.load();

                // Get the Labels and the ImageView
                Label textNameHotel = (Label) hotelItem.lookup("#textNameHotel");
                Label textDiaChi = (Label) hotelItem.lookup("#textDiaChi");
                Label textMoTa = (Label) hotelItem.lookup("#textMoTa");
                Label textGiaPhong = (Label) hotelItem.lookup("#textGiaPhong");
                Pane imgViewHotel = (Pane) hotelItem.lookup("#imgViewHotel");
                Label textDoRongPhong1 = (Label) hotelItem.lookup("#textDoRongPhong1");

                // Parse giaVe from BigDecimal to int
                    int giaVe = accommodation.getGiaPhong().intValueExact();

                     // Create a NumberFormat instance for the default locale
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                    // Set the group size to 3
                    numberFormat.setGroupingUsed(true);
                    numberFormat.setMaximumFractionDigits(0);
                    // Format the number
                    String formattedGiaVe = numberFormat.format(giaVe);

                // Set the Labels
                textNameHotel.setText(accommodation.getTen());
                textDiaChi.setText(accommodation.getDiaChi());
                textMoTa.setText(accommodation.getMoTa());
                textGiaPhong.setText(formattedGiaVe + " VND");
                textDoRongPhong1.setText(accommodation.getDoRongPhong()+" ");


                // Decode the image data
                Image image2 = new Image(new ByteArrayInputStream(accommodation.getImages().getDuLieuAnh()));

                // Set the background of the root
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
                Background background = new Background(backgroundImage);
                imgViewHotel.setBackground(background);

                //Action for btBookNow
                // Get the Book Now button
                Button btBookNow = (Button) hotelItem.lookup("#btBookNow");

                // Set the button action
                btBookNow.setOnAction(e -> {
                    System.out.println("Booking information:");
                    System.out.println("Hotel Name: " + textNameHotel.getText());
                    System.out.println("Address: " + textDiaChi.getText());
                    System.out.println("Description: " + textMoTa.getText());
                    System.out.println("Room Price: " + textGiaPhong.getText());
                    System.out.println("Room Size: " + textDoRongPhong1.getText());


                    // Save the Accommodations_ID and giaPhong
                    BookingData.setAccommodationsId(accommodation.getId());
                    BookingData.setGiaPhong(accommodation.getGiaPhong());

                    //action
                    FXMLLoader loaderSB = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/StayBookingsView.fxml"));
                    try {
                        Scene scene = new Scene(loaderSB.load());
                        Stage stage = new Stage();
                        stage.setTitle("Vietnam Travel");
                        stage.setScene(scene);
                        stage.show();

                        // Get the StayBookingController and set the fields
                        StayBookingsController controller = loaderSB.getController();
                        controller.setTextTenKhachSan(textNameHotel.getText());
                        controller.setTextDC(textDiaChi.getText());
                        controller.setTextDoRongPhong(textDoRongPhong1.getText());
                        controller.setBackgroundImage(image2);
                    } catch (IOException eSB) {
                        eSB.printStackTrace();
                    }
                });


                // Add the HotelItem to the ListView
                listVbox.getChildren().add(hotelItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Hàm lưu trữ getAccommodationsId và giaPhong bảng Accommodations
    public class BookingData {
        private static Integer accommodationsId;
        private static BigDecimal giaPhong;

        public static Integer getAccommodationsId() {
            return accommodationsId;
        }

        public static void setAccommodationsId(Integer accommodationsId) {
            BookingData.accommodationsId = accommodationsId;
        }

        public static BigDecimal getGiaPhong() {
            return giaPhong;
        }

        public static void setGiaPhong(BigDecimal giaPhong) {
            BookingData.giaPhong = giaPhong;
        }
    }

}
