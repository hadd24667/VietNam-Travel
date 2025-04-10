package controller;

import Util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.TourBooking;
import model.Transportation;
import org.hibernate.Session;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TourBookingController {

    @FXML
    private Button btMinus;

    @FXML
    private Button btMinusChild;

    @FXML
    private Button btPlus;

    @FXML
    private Button btPlusChild;

    @FXML
    private Button btSub;

    @FXML
    private TextField cccdTF;

    @FXML
    private TextField emailTF;

    @FXML
    private DatePicker goDateTF;

    @FXML
    private Label returnDateText;

    @FXML
    private Label textSoLuongVe;

    @FXML
    private Label textSoLuongVeChild;

    @FXML
    private Label textTotalPayment;

    @FXML
    private Label textYourPhoneNum;

    @FXML
    private Button backButton;

    @FXML
    private Pane QRBankingPaneButton;

    @FXML
    private Pane QRBankingPane;

    @FXML
    private Pane CreditCardPane;

    @FXML
    private Pane CreditCardPaneButton2;

    @FXML
    private Pane PayPane;


    @FXML
    private Pane EnterPane;

    @FXML
    private Button PurchasetButton;



    private Session session;

    private int userID;

    private String phoneNum;

    private int giaVe = MainViewController.TourData.getGiaVe();

    private double TotalPayment = giaVe;

    private int soLuongVe = 1;
    private int soLuongVeChild = 0;

    private NumberFormat numberFormat;
    private String formattedGiaVe;

    private ButtonController buttonController = new ButtonController();

    public TourBookingController() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }


    public void initialize() {
        setupButtonActions();
        //Goi ham UserSession de lay ID va PhoneNum
        userID = LogInController.UserSession.getUserID();
        phoneNum = LogInController.UserSession.getPhoneNumber();

        //Tao dau phay cho don vi tien
        // Create a NumberFormat instance for the default locale
        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormat.setGroupingUsed(true);
        numberFormat.setMaximumFractionDigits(0);
        formattedGiaVe = numberFormat.format(giaVe);

        //setText
        textYourPhoneNum.setText(phoneNum);
        textTotalPayment.setText(formattedGiaVe + " VND");

    }
    private void setupButtonActions() {
        btSub.setOnMouseClicked(event -> {
            EnterPane.setVisible(false);
            PayPane.setVisible(true);
        });
        backButton.setOnMouseClicked(event -> {
            EnterPane.setVisible(true);
            PayPane.setVisible(false);
        });
        QRBankingPaneButton.setOnMouseClicked(event -> {
            CreditCardPane.setVisible(false);
            QRBankingPane.setVisible(true);
        });
        CreditCardPaneButton2.setOnMouseClicked(event -> {
            QRBankingPane.setVisible(false);
            CreditCardPane.setVisible(true);
        });
    }

    //BUTTON DATEPICKER
    @FXML
    public void handleGoDateTFAction(ActionEvent event) {
    }

    //Button + -
    @FXML
    public void handleBtMinusAction(ActionEvent event) {
        if (soLuongVe > 1) {

            TotalPayment = TotalPayment - giaVe;
            soLuongVe--;
            textSoLuongVe.setText(String.valueOf(soLuongVe));

            NumberFormat numberFormatNew = NumberFormat.getNumberInstance(Locale.getDefault());
            numberFormatNew.setGroupingUsed(true);
            numberFormatNew.setMaximumFractionDigits(0);
            String formattedGiaVeNew = numberFormatNew.format(TotalPayment);

            textTotalPayment.setText(formattedGiaVeNew + " VND");
        }else{
            textTotalPayment.setText(formattedGiaVe + " VND");
        }
    }

    @FXML
    public void handleBtPlusAction(ActionEvent event) {
        soLuongVe++;
        textSoLuongVe.setText(String.valueOf(soLuongVe));

        TotalPayment = TotalPayment + giaVe;
        NumberFormat numberFormatNew = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormatNew.setGroupingUsed(true);
        numberFormatNew.setMaximumFractionDigits(0);
        String formattedGiaVeNew = numberFormatNew.format(TotalPayment);

        textTotalPayment.setText(formattedGiaVeNew + " VND");
    }

    @FXML
    public void handleBtMinusChildAction(ActionEvent event) {
        if (soLuongVeChild > 0) {

            TotalPayment = TotalPayment - giaVe*0.7;
            soLuongVeChild--;
            textSoLuongVeChild.setText(String.valueOf(soLuongVeChild));

            NumberFormat numberFormatNew = NumberFormat.getNumberInstance(Locale.getDefault());
            numberFormatNew.setGroupingUsed(true);
            numberFormatNew.setMaximumFractionDigits(0);
            String formattedGiaVeNew = numberFormatNew.format(TotalPayment);

            textTotalPayment.setText(formattedGiaVeNew + " VND");
        }
    }

    @FXML
    public void handleBtPlusChildAction(ActionEvent event) {
        soLuongVeChild++;
        textSoLuongVeChild.setText(String.valueOf(soLuongVeChild));


        TotalPayment = TotalPayment + giaVe*0.7;
        NumberFormat numberFormatNew = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormatNew.setGroupingUsed(true);
        numberFormatNew.setMaximumFractionDigits(0);
        String formattedGiaVeNew = numberFormatNew.format(TotalPayment);

        textTotalPayment.setText(formattedGiaVeNew + " VND");
    }

    @FXML
    public void handlePurchasetButtonAction(ActionEvent event) {
        String cccd = cccdTF.getText();
        String email = emailTF.getText();
        LocalDate goDate = goDateTF.getValue();

        // Check if cccdTF, goDateTF, emailTF are empty
        if (cccd.isEmpty() || email.isEmpty() || goDate == null) {
            buttonController.showErrorAlert("Eror","Something is empty");
            return;
        }

        // Check if emailTF does not contain '@'
        else if (!email.contains("@")) {
            buttonController.showErrorAlert("Eror","please enter a valid email");
            return;
        }else if (!cccd.matches("[0-9]+")) {
            buttonController.showErrorAlert("Error", "CCCD must be a number");
            return;
        }else{

                try{
                    // Start a transaction
                    session.beginTransaction();

                    // Create a new TourBooking object
                    TourBooking tourBooking = new TourBooking();

                    tourBooking.setCccd(cccd);

                    tourBooking.setEmailNhanVe(email);
                    tourBooking.setNgayThamQuan(goDate);
                    tourBooking.setSoVe(Integer.parseInt(textSoLuongVe.getText()));
                    tourBooking.setSoVeTreEm(Integer.parseInt(textSoLuongVeChild.getText()));
                    tourBooking.setTotalPayment(TotalPayment);

                    // Get userID from UserSession
                    Integer userID = LogInController.UserSession.getUserID();
                    model.User user = session.get(model.User.class, userID);
                    tourBooking.setUsers(user);

                    Integer TouristAttractions_ID = MainViewController.TourData.getTouristAttractions_ID();
                    model.TouristAttraction touristAttractions = session.get(model.TouristAttraction.class, TouristAttractions_ID);
                    tourBooking.setTouristAttractions(touristAttractions);

                    // Save the TourBooking object to the database
                    session.save(tourBooking);
                    session.getTransaction().commit();

                    buttonController.showInformationAlert("Success","Booking successful");
                }catch (Exception e){
                    e.printStackTrace();
                    // If there is an error, rollback the transaction
                    if (session.getTransaction() != null) {
                        session.getTransaction().rollback();
                    }
                    buttonController.showErrorAlert("Eror","Something went wrong");
                }

            }
    }
}
