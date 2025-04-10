package controller;

import Util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FlightBookingsController {
    @FXML
    private Pane enterFlightBookingPane;

    @FXML
    private Pane PayPane;

    @FXML
    private TextField PersonNumber;

    @FXML
    private Button plusButt;

    @FXML
    private Button minusButt;

    @FXML
    private Button continueButt;

    @FXML
    private Button backButton;

    @FXML
    private Pane QRBankingPaneButton;

    @FXML
    private Pane CreditCardPaneButton2;

    @FXML
    private Pane CreditCardPane;

    @FXML
    private Pane QRBankingPane;

    @FXML
    private VBox passengerContainer;

    @FXML
    private Label FromToLabel;

    @FXML
    private TextField brandTF;

    @FXML
    private TextField goTimeTF;

    @FXML
    private TextField goDateTF;

    @FXML
    private TextField airPortTF;

    @FXML
    private TextField emailTF;

    @FXML
    private ChoiceBox<String> TicketType;

    @FXML
    private Label totalMoney;

    @FXML
    private Pane OrderPane;

    private int transportationID;

    private User HieuNgu;

    private double giaVe;

    private String flightID;

    private double totalAmount;

    private ObservableList<Pane> passengerPanes;

    private ButtonController bc = new ButtonController();

    private TransportBooking transportBooking;

    private Session session;

    public FlightBookingsController()
    {
     this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @FXML
    private void initialize()
    {
        // Get userID from UserSession
        Integer userID = LogInController.UserSession.getUserID();
        String hoten = LogInController.UserSession.getHoTen();
        String sodenthoai = LogInController.UserSession.getPhoneNumber();

        //model.User user = session.get(model.User.class, userID);

        HieuNgu = new User();
        HieuNgu.setId(userID);
        HieuNgu.setHoTen(hoten);
//        HieuNgu.setEmail("bosuahieu@booo.com");
        HieuNgu.setSoDienThoai(sodenthoai);

        PersonNumber.setText("1");
        transportBooking = new TransportBooking();
        TicketType.getItems().addAll("Economy class","Business class");
        passengerPanes = FXCollections.observableArrayList();
        addPassengerPane();
        setupButtonActions();
        TicketType.setOnAction(event -> updateTotalAmount());
    }

    private void setupButtonActions() {
        plusButt.setOnMouseClicked(event -> addPassengerPane());
        minusButt.setOnMouseClicked(event -> removePassengerPane());
        continueButt.setOnMouseClicked(event -> {
            enterFlightBookingPane.setVisible(false);
            PayPane.setVisible(true);
            handleContinueButton();
        });
        backButton.setOnMouseClicked(event -> {
            enterFlightBookingPane.setVisible(true);
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

    private double getBaseTicketPrice(String flightID) {
        try {
            session.beginTransaction();
            String hql = "SELECT f.giaVe FROM Flight f WHERE f.id = :flightID";
            Double price = (Double) session.createQuery(hql)
                    .setParameter("flightID", Integer.parseInt(flightID))
                    .uniqueResult();
            session.getTransaction().commit();
            System.out.println("Base ticket price for flightID " + flightID + " is " + price);
            return price != null ? price : 0.0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return 0.0;
        }
    }

    private void updateTotalAmount() {
        try {
            String selectedTicketType = TicketType.getValue();
            double basePrice = getBaseTicketPrice(flightID);
            System.out.println("Selected ticket type: " + selectedTicketType);
            if ("Business class".equals(selectedTicketType)) {
                giaVe = basePrice * 1.6;  // Giá vé thương gia đắt hơn 60%
            } else {
                giaVe = basePrice;  // Giá vé thường
            }
            int personCount = Integer.parseInt(PersonNumber.getText());
            totalAmount = giaVe * personCount;
            totalMoney.setText(String.format("%.2f", totalAmount));
        } catch (NumberFormatException e) {
            bc.showErrorAlert("ERROR", "Invalid number format for person count");
        }
    }


    public void setFlightID(String flightID) {
        this.flightID = flightID;
        System.out.println("Flight ID set to: " + flightID);
        updateTotalAmount();
    }

    public void setBookingDetails(Flight flight) {
        FromToLabel.setText(flight.getDiemKhoiHanh() + " to " + flight.getDiemDen());
        brandTF.setText(flight.getHangHangKhong());
        goDateTF.setText(flight.getNgayKhoiHanh().toString());
        goTimeTF.setText(flight.getGioKhoiHanh().toString());
        airPortTF.setText(flight.getSanBayDi());
        updateTotalAmount();
    }
    private void handleContinueButton() {

        updateTotalAmount();

        setYourOrder(OrderPane,
                FromToLabel.getText(),
                LocalDate.parse(goDateTF.getText()),
                LocalTime.parse(goTimeTF.getText()),
                airPortTF.getText(),
                PersonNumber.getText(),
                emailTF.getText(),
                brandTF.getText(),
                totalAmount);
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

    private void setYourOrder(Pane OrderPane, String fromToLabel, LocalDate goLabel, LocalTime timeLabel,String airportLabel, String numberOfSeatLabel, String emailLabel, String brandLabel, double totalMoneyOrderLabel) {
        try {
            // Find label by fx:id
            Label FromAndTo = (Label) OrderPane.lookup("#from_To");
            Label dayGo = (Label) OrderPane.lookup("#goDate");
            Label timeGo = (Label) OrderPane.lookup("#goTime");
            Label airport = (Label) OrderPane.lookup("#airport");
            Label numOfSeat = (Label) OrderPane.lookup("#numSeat");
            Label mail = (Label) OrderPane.lookup("#email");
            Label brand = (Label) OrderPane.lookup("#brand");
            Label total = (Label) OrderPane.lookup("#totalMoneyOrder");

            //setText
            if (FromAndTo != null) FromAndTo.setText(fromToLabel);
            if (dayGo != null) dayGo.setText("Date: " + goLabel.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (timeGo != null) timeGo.setText("Time: " + timeLabel.toString());
            if (timeGo != null) airport.setText("Airport: " + airportLabel);
            if (numOfSeat != null) numOfSeat.setText("Number of seats: " + numberOfSeatLabel);
            if (mail != null) mail.setText("Email: " + emailLabel);
            if (brand != null) brand.setText("Brand:" + brandLabel);
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
    // Phương thức để lấy đối tượng Transportation từ cơ sở dữ liệu dựa trên loại phương tiện
    private Transportation getTransportationByLoaiPhuongTien(String loaiPhuongTien) {
        try{
            String hql = "FROM Transportation T WHERE T.loaiPhuongTien = :loaiPhuongTien";
            Query<Transportation> query = session.createQuery(hql, Transportation.class);
            query.setParameter("loaiPhuongTien", loaiPhuongTien);
            return query.uniqueResultOptional().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handlePurchasetButton() {
        // Lưu thông tin đặt vé vào CSDL
        try {
            Transaction transaction = session.beginTransaction();

            transportBooking.setUsers(HieuNgu);
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
            Flight flight = session.get(Flight.class, Integer.parseInt(flightID));
            if (flight == null) {
                bc.showErrorAlert("Error", "Invalid schedule ID.");
                return;
            }

            // Liên kết transportBooking với flight
            transportBooking.setPlane(flight);
            // Lưu thông tin vé phương tiện
            session.save(transportBooking);
            // Lưu thông tin hành khách
            List<PassengerInformation> passengers = getPassengerInformation();
            savePassengerInf(passengers, transportBooking);

            // Cập nhật số lượng chỗ ngồi còn lại
            String updateHql = "UPDATE Flight SET soGheConLai = soGheConLai - :soLuongVe WHERE id = :flightID";
            Query updateQuery = session.createQuery(updateHql);
            updateQuery.setParameter("soLuongVe", passengers.size());
            updateQuery.setParameter("flightID", Integer.parseInt(flightID));
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
}
