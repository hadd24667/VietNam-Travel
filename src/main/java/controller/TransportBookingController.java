package controller;

import Util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.Time;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransportBookingController {
    @FXML
    private Pane EnterAttractionPane;

    @FXML
    private TextField goDateTF;

    @FXML
    private TextField TransportationTF;

    @FXML
    private TextField startTF;

    @FXML
    private TextField endTF;

    @FXML
    private Pane PayPane;

    @FXML
    private Button continueButt;

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
    private TextField PersonNumber;

    @FXML
    private Label FromToLabel;

    @FXML
    private TextField mailTF;

    @FXML
    private Button plusButt;

    @FXML
    private Button minusButt;

    @FXML
    private Label totalMoney;

    @FXML
    private VBox passengerContainer;

    @FXML
    private Pane OrderPane;

    @FXML
    private TextField brandTF;

    private Session session;

    private double totalAmount = 0.0;

    private ObservableList<Pane> passengerPanes;

    private String schedule_ID;

    private double ticketPrice;

    private ButtonController bc = new ButtonController();

    private TransportBooking transportBooking;

    private User User;

    public TransportBookingController() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @FXML
    private void initialize() {
        // Get userID from UserSession
        Integer userID = LogInController.UserSession.getUserID();
        String hoten = LogInController.UserSession.getHoTen();
        String sodenthoai = LogInController.UserSession.getPhoneNumber();

        //Set user
        User = new User();
        User.setId(userID);
        User.setHoTen(hoten);
        User.setSoDienThoai(sodenthoai);
        PersonNumber.setText("1");
        transportBooking = new TransportBooking();
        passengerPanes = FXCollections.observableArrayList();
        addPassengerPane();
        setupButtonActions();
    }

    private Pane createPassengerPane(int passengerNumber) {
        Pane pane = new Pane();
        pane.setPrefSize(200, 150);
        pane.setStyle("-fx-border-color: black;");

        Label nameLabel = new Label("Name");
        nameLabel.relocate(8, 32); // Sử dụng phương thức relocate để đặt vị trí
        nameLabel.setFont(new Font("Berlin Sans FB", 16));

        TextField nameField = new TextField();
        nameField.relocate(8, 57); // Sử dụng phương thức relocate để đặt vị trí
        nameField.setPrefSize(185, 25);

        Label birthLabel = new Label("Birth");
        birthLabel.relocate(8, 92); // Sử dụng phương thức relocate để đặt vị trí
        birthLabel.setFont(new Font("Berlin Sans FB", 16));

        TextField birthField = new TextField();
        birthField.relocate(8, 117); // Sử dụng phương thức relocate để đặt vị trí
        birthField.setPrefSize(185, 25);

        Label passengerNumberLabel = new Label("Passenger " + passengerNumber);
        passengerNumberLabel.relocate(63, 5); // Sử dụng phương thức relocate để đặt vị trí
        passengerNumberLabel.setFont(new Font("Berlin Sans FB", 16));
        passengerNumberLabel.setTextFill(Color.web("#797272"));

        pane.getChildren().addAll(nameLabel, nameField, birthLabel, birthField, passengerNumberLabel);

        return pane;
    }


    private void setupButtonActions() {
        plusButt.setOnMouseClicked(event -> addPassengerPane());
        minusButt.setOnMouseClicked(event -> removePassengerPane());
        continueButt.setOnMouseClicked(event -> {
            EnterAttractionPane.setVisible(false);
            PayPane.setVisible(true);
            handleContinueButton();
        });
        backButton.setOnMouseClicked(event -> {
            EnterAttractionPane.setVisible(true);
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

    public void setBookingDetails(String from, String to, LocalDate goDate, Time startTime, Time endTime, String transportation, String brand, String schedule_ID) {
        FromToLabel.setText(from + " to " + to);
        this.goDateTF.setText(String.valueOf(goDate));
        this.startTF.setText(String.valueOf(startTime));
        this.endTF.setText(String.valueOf(endTime));
        this.TransportationTF.setText(transportation);
        this.brandTF.setText(brand);
        this.schedule_ID = schedule_ID;
    }

    @FXML
    private void PlusButton() {
        int currentValue = Integer.parseInt(PersonNumber.getText());
        PersonNumber.setText(String.valueOf(currentValue + 1));
        updateTotalAmount();
    }

    @FXML
    private void MinusButton() {
        try {
            int currentValue = Integer.parseInt(PersonNumber.getText());
            if (currentValue > 1) {
                PersonNumber.setText(String.valueOf(currentValue - 1));
                updateTotalAmount();
            } else {
                bc.showErrorAlert("ERROR", "Person number cannot be less than 1");
            }
        } catch (NumberFormatException e) {
            bc.showErrorAlert("ERROR", "Invalid number format");
        }
    }

    private void addPassengerPane() {
        Pane passengerPane = createPassengerPane(passengerPanes.size() + 1);
        passengerPanes.add(passengerPane);
        passengerContainer.getChildren().add(passengerPane);
    }

    private void removePassengerPane() {
        if (!passengerPanes.isEmpty()) {
            Pane removedPane = passengerPanes.remove(passengerPanes.size() - 1);
            passengerContainer.getChildren().remove(removedPane);
        }
    }

    public void setScheduleID(String scheduleID) {
        this.schedule_ID = scheduleID;
        ticketPrice = getTicketPrice(scheduleID);
        updateTotalAmount();
    }

    private double getTicketPrice(String maChuyenDi) {
        try {
            String hql = "SELECT S.giaVe FROM Schedule S WHERE S.id = :scheduleId";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("scheduleId", Integer.parseInt(maChuyenDi));
            return query.uniqueResultOptional().orElse(0.0); // Sử dụng Optional để xử lý null
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private void updateTotalAmount() {
        int numberPassenger = Integer.parseInt(PersonNumber.getText());
        totalAmount = numberPassenger * ticketPrice;
        totalMoney.setText(String.format("%.0f VND", totalAmount)); // Định dạng tiền tệ
    }
    private void handleContinueButton() {
        String loaiPhuongTien = TransportationTF.getText();
        String hangXe = brandTF.getText();
        Transportation transportation = getTransportationByLoaiPhuongTien(loaiPhuongTien, hangXe);

        if (transportation == null) {
            bc.showErrorAlert("Error", "Invalid transportation type");
            return;
        }

        updateTotalAmount();
        String transportTypeLabel = transportation.getLoaiPhuongTien();

        setYourOrder(OrderPane,
                FromToLabel.getText(),
                LocalDate.parse(goDateTF.getText()),
                LocalTime.parse(startTF.getText()),
                PersonNumber.getText(),
                mailTF.getText(),
                transportTypeLabel,
                totalAmount);
    }

    public void handlePurchasetButton() {
        String loaiPhuongTien = TransportationTF.getText();
        String hangXe = brandTF.getText();
        Transportation transportation = getTransportationByLoaiPhuongTien(loaiPhuongTien, hangXe);

        if (transportation == null) {
            bc.showErrorAlert("Error", "Invalid transportation type");
            return;
        }
        // Lưu thông tin đặt vé vào CSDL
        try {
            Transaction transaction = session.beginTransaction();

            transportBooking.setUsers(User);
            transportBooking.setTransportation(transportation);

            Schedule schedule = session.get(Schedule.class, Integer.parseInt(schedule_ID));
            if (schedule == null) {
                bc.showErrorAlert("Error", "Invalid schedule ID.");
                return;
            }
            // Liên kết transportBooking với schedule
            transportBooking.setSchedules(schedule);
            // Lưu thông tin vé phương tiện
            session.save(transportBooking);
            // Lưu thông tin hành khách
            List<PassengerInformation> passengers = getPassengerInformation();
            savePassengerInf(passengers, transportBooking);

            try {
                String totalMoneyText = totalMoney.getText();
                // Loại bỏ tất cả các ký tự không phải số hoặc dấu chấm
                totalMoneyText = totalMoneyText.replaceAll("[^\\d.]", "");
                transportBooking.setTotalMoney(Float.parseFloat(totalMoneyText));
            } catch (NumberFormatException e) {
                bc.showErrorAlert("Error", "Invalid total money format.");
                e.printStackTrace();
                return;
            }

            // Cập nhật số lượng chỗ ngồi còn lại
            String updateHql = "UPDATE Schedule SET soChoConLai = soChoConLai - :soLuongVe WHERE id = :scheduleId";
            Query updateQuery = session.createQuery(updateHql);
            updateQuery.setParameter("soLuongVe", passengers.size());
            updateQuery.setParameter("scheduleId", Integer.parseInt(schedule_ID));
            updateQuery.executeUpdate();

            transaction.commit(); // Kết thúc giao dịch
            bc.showInformationAlert("Success", "Booking successful!");

        } catch (NumberFormatException e) {
            bc.showErrorAlert("Error", "Invalid schedule ID format.");
            e.printStackTrace();
        } catch (PersistenceException e) {
            bc.showErrorAlert("Error", "Cannot booking");
            e.printStackTrace();
        } catch (Exception e) {
            bc.showErrorAlert("Error", "An error occurred while processing your booking. Please try again.");
            e.printStackTrace();
        } finally {
            if (session.getTransaction().isActive()) { // Đảm bảo rằng giao dịch đã được đóng hoặc rollback
                session.getTransaction().rollback(); // Rollback giao dịch nếu vẫn còn hoạt động
            }
        }
    }


    // Phương thức để lấy đối tượng Transportation từ cơ sở dữ liệu dựa trên loại phương tiện
    private Transportation getTransportationByLoaiPhuongTien(String loaiPhuongTien, String tenHang) {
        try{
            String hql = "FROM Transportation T WHERE T.loaiPhuongTien = :loaiPhuongTien AND T.tenHang = :tenHang ";
            Query<Transportation> query = session.createQuery(hql, Transportation.class);
            query.setParameter("loaiPhuongTien", loaiPhuongTien);
            query.setParameter("tenHang", tenHang);
            return query.uniqueResultOptional().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<PassengerInformation> getPassengerInformation() {
        List<PassengerInformation> passengers = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Pane passengerPane : passengerPanes) {
            TextField nameField = (TextField) passengerPane.getChildren().get(1);
            TextField birthField = (TextField) passengerPane.getChildren().get(3);

            PassengerInformation passenger = new PassengerInformation();
            passenger.setHoTen(nameField.getText());

            // Kiểm tra xem trường ngày sinh có giá trị không
            if (birthField.getText() != null && !birthField.getText().isEmpty()) {
                try {
                    LocalDate birthDate = LocalDate.parse(birthField.getText(), formatter);
                    passenger.setNgaySinh(birthDate);
                } catch (DateTimeParseException e) {
                    bc.showErrorAlert("ERROR", "Invalid birth date format or out of range. Please use dd/MM/yyyy.");
                    return null;
                }
            } else {
                // Xử lý trường hợp không nhập ngày sinh
                bc.showErrorAlert("ERROR", "Please enter birth date for all passengers.");
                return null;
            }

            passengers.add(passenger);
        }
        return passengers;
    }


    private void savePassengerInf(List<PassengerInformation> passengers, TransportBooking transportBooking) {

        try {
            for (PassengerInformation passenger : passengers) {
                passenger.setBookings(transportBooking);
                session.save(passenger);
                System.out.println("Saving passenger: " + passenger.getHoTen() + ", Birth date: " + passenger.getNgaySinh());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setYourOrder(Pane OrderPane, String fromToLabel, LocalDate goLabel, LocalTime timeLabel, String numberOfSeatLabel, String emailLabel, String transportTypeLabel, double totalMoneyOrderLabel) {
        try {
            // Find label by fx:id
            Label FromAndTo = (Label) OrderPane.lookup("#whereToLabel");
            Label dayGo = (Label) OrderPane.lookup("#goLabel");
            Label timeGo = (Label) OrderPane.lookup("#timeLabel");
            Label numOfSeat = (Label) OrderPane.lookup("#numberOfSeatLabel");
            Label mail = (Label) OrderPane.lookup("#emailLabel");
            Label type = (Label) OrderPane.lookup("#transportTypeLabel");
            Label total = (Label) OrderPane.lookup("#totalMoneyOrderLabel");

            //setText
            if (FromAndTo != null) FromAndTo.setText(fromToLabel);
            if (dayGo != null) dayGo.setText("Date: " + goLabel.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (timeGo != null) timeGo.setText("Time: " + timeLabel.toString());
            if (numOfSeat != null) numOfSeat.setText("Number of seats: " + numberOfSeatLabel);
            if (mail != null) mail.setText("Email: " + emailLabel);
            if (type != null) type.setText("Transportation type:" + transportTypeLabel);
            if (total != null) {
                NumberFormat fommatter = NumberFormat.getNumberInstance(Locale.US);
                total.setText("Total: " + fommatter.format(totalMoneyOrderLabel) + " VND");
            }
        } catch (NullPointerException e)
        {
            System.err.println("Error: One or more labels not found in OrderPane.");
            e.printStackTrace();
        }
    }
}