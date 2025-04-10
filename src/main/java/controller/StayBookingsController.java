package controller;

import Util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import Util.HibernateUtil;
import model.StayBookings;


import controller.LogInController.UserSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StayBookingsController {

    @FXML
    private Button btSub;

    @FXML
    private TextField cccdTF;

    @FXML
    private DatePicker ngayDiDate;

    @FXML
    private DatePicker ngayVeDate;

    @FXML
    private TextField soLuongPhongTF;

    @FXML
    private Label textDC;

    @FXML
    private Label textDoRongPhong;

    @FXML
    private Label textTenKhachSan;

    @FXML
    private Label textTongTien;

    @FXML
    private AnchorPane rootPane;

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
    private void initialize()
    {
        setupButtonActions();

        textTongTien.setText("0.0");

        // Thêm ChangeListener cho soLuongPhongTF
        soLuongPhongTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isInputValid()) { // Kiểm tra đầu vào hợp lệ
                calAmountTotal();
            }
        });

        // Thêm ChangeListener cho ngayDiDate
        ngayDiDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isInputValid()) {
                calAmountTotal();
            }
        });

        // Thêm ChangeListener cho ngayVeDate
        ngayVeDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isInputValid()) {
                calAmountTotal();
            }
        });
    }

    public void setBackgroundImage(Image image) {
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        rootPane.setBackground(new Background(backgroundImage));
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

    public void setTextTenKhachSan(String tenKhachSan) {
        textTenKhachSan.setText(tenKhachSan);
    }

    public void setTextDC(String diaChi) {
        textDC.setText(diaChi);
    }

    public void setTextDoRongPhong(String doRongPhong) {
        textDoRongPhong.setText(doRongPhong);
    }

    @FXML
    public void handlePurchasetButtonAction(ActionEvent event) {

        ButtonController buttonController = new ButtonController();
        if (cccdTF.getText().isEmpty() || soLuongPhongTF.getText().isEmpty() || ngayDiDate.getValue() == null || ngayVeDate.getValue() == null) {

            buttonController.showErrorAlert("Null","Please fill in all fields.");
            return;
        }


        if (!cccdTF.getText().matches("\\d+")) {
            buttonController.showErrorAlert("Invalid Input","CCCD must be a number.");
            return;
        }

        if (!soLuongPhongTF.getText().matches("\\d+")) {
            buttonController.showErrorAlert("Invalid Input","Please enter a valid number for the number of rooms.");
            return;
        }

        // Get LocalDate from DatePicker
        LocalDate ngayDi = ngayDiDate.getValue();
        LocalDate ngayVe = ngayVeDate.getValue();

        // Define formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convert LocalDate to String with defined format
        String formattedNgayDi = ngayDi.format(formatter);
        String formattedNgayVe = ngayVe.format(formatter);

        if (ngayDi.isAfter(ngayVe)) {
            buttonController.showErrorAlert("Invalid Date","Check-in date must be before check-out date.");
            return;
        }

        try{
        int soLuongPhong = Integer.parseInt(soLuongPhongTF.getText());
        BigDecimal giaPhong = StayController.BookingData.getGiaPhong();
        long soNgay = ChronoUnit.DAYS.between(ngayDi, ngayVe);

        BigDecimal tongTien = giaPhong.multiply(BigDecimal.valueOf(soLuongPhong * soNgay));
        calAmountTotal();

        // Save
        StayBookings stayBookings = new StayBookings();

        // Get userID from UserSession
        Integer userID = UserSession.getUserID();

        // Get User object from database using userID
        Session session = HibernateUtil.getSessionFactory().openSession();
        model.User user = session.get(model.User.class, userID);

        // Set User object to StayBookings object
        stayBookings.setUserID(user);

        // Get accommodationsId from BookingData
        Integer accommodationsId = StayController.BookingData.getAccommodationsId();

        // Get Accommodation object from database using accommodationsId
        model.Accommodation accommodation = session.get(model.Accommodation.class, accommodationsId);

        // Set Accommodation object to StayBookings object
        stayBookings.setAccommodationID(accommodation);
        stayBookings.setCccd(cccdTF.getText());
        stayBookings.setSoLuongPhong(Integer.parseInt(soLuongPhongTF.getText()));
        stayBookings.setTongGiaPhong(tongTien.doubleValue());

        // Convert String to java.sql.Date
        Date sqlNgayDi = Date.valueOf(formattedNgayDi);
        Date sqlNgayVe = Date.valueOf(formattedNgayVe);

        stayBookings.setNgayDi(sqlNgayDi);
        stayBookings.setNgayVe(sqlNgayVe);

        //SAVE DATA
            try {
                // Start a new transaction
                Transaction transaction = session.beginTransaction();

                // Save the StayBookings object
                session.save(stayBookings);

                // Commit the transaction
                transaction.commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
                e.printStackTrace();
            }
        //END SAVE METHOD
        buttonController.showInformationAlert("Success","Booking successful!");
        }
        catch (Exception e){
            e.printStackTrace();
            buttonController.showErrorAlert("Error","Something Wrong:(");
        }
    }
    private boolean isInputValid() {
        return !soLuongPhongTF.getText().isEmpty() && soLuongPhongTF.getText().matches("\\d+") &&
                ngayDiDate.getValue() != null && ngayVeDate.getValue() != null &&
                !ngayDiDate.getValue().isAfter(ngayVeDate.getValue());
    }
    private void calAmountTotal() {
        // Kiểm tra xem các trường đầu vào có hợp lệ không
        if (!soLuongPhongTF.getText().isEmpty() && soLuongPhongTF.getText().matches("\\d+") &&
                ngayDiDate.getValue() != null && ngayVeDate.getValue() != null &&
                !ngayDiDate.getValue().isAfter(ngayVeDate.getValue())) {

            int soLuongPhong = Integer.parseInt(soLuongPhongTF.getText());
            BigDecimal giaPhong = StayController.BookingData.getGiaPhong();
            long soNgay = ChronoUnit.DAYS.between(ngayDiDate.getValue(), ngayVeDate.getValue());

            BigDecimal tongTien = giaPhong.multiply(BigDecimal.valueOf(soLuongPhong * soNgay));
            // Định dạng tổng tiền
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedTongTien = currencyFormat.format(tongTien);

            // Xóa ký hiệu tiền tệ VND và thêm chuỗi " VND"
            formattedTongTien = formattedTongTien.replace("₫", "").trim() + " VND";

            textTongTien.setText(formattedTongTien);
        } else {
            textTongTien.setText("0 VND"); // Hoặc giá trị mặc định khác nếu đầu vào không hợp lệ
        }
    }
}
