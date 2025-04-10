package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.AdminController;
import controller.AbstractController.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Flight;
import model.Route;
import model.Schedule;
import model.Transportation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import javafx.scene.input.MouseEvent;

public class AdminDestinations extends AdminController implements AccountTextController  {

    @FXML
    private HBox BusBox;

    @FXML
    private HBox PlaneBox;

    @FXML
    private HBox TrainBox;

    @FXML
    private Button btAccount;

    @FXML
    private Button btDelete;

    @FXML
    private Button btSave;

    @FXML
    private TextField fromTF;

    @FXML
    private TextField gioDenTF;

    @FXML
    private TextField gioKhoiHanhTF;

    @FXML
    private DatePicker goTF;

    @FXML
    private TextField maChuyenBayTF;

    @FXML
    private Button menuDes;

    @FXML
    private Button menuHome;

    @FXML
    private Button menuStay;

    @FXML
    private Button menuTicket;

    @FXML
    private TextField priceTF;

    @FXML
    private DatePicker returnTF;

    @FXML
    private TextField sanBayDenTF;

    @FXML
    private TextField sanBayDiTF;

    @FXML
    private TextField soChoTF;

    @FXML
    private TextField tenHangTF;

    @FXML
    private Label textDate;

    @FXML
    private Label textFrom;

    @FXML
    private Label textGioDen;

    @FXML
    private Label textGioKhoiHanh;

    @FXML
    private Label textPrice;

    @FXML
    private Label textSoChoConLai;

    @FXML
    private Label textTenHang;

    @FXML
    private Label textTo;

    @FXML
    private AnchorPane ticketPane;

    @FXML
    private TextField toTF;

    @FXML
    private VBox vBox;

    @FXML
    private Button btManage;

    @FXML
    private GridPane gridPane;

    private String loaiPT;

    private HBox lastSelectedBox;

    private Session session;

    public AdminDestinations(){
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

    }

    @Override
    public void handleBtAccountAction(ActionEvent event) throws IOException {
        super.handleBtAccountAction(event);
    }

    @Override
    public void setBtAccountText(String hoTen) {
        btAccount.setText(hoTen);
        btAccount.setMinWidth(Button.USE_PREF_SIZE);
        btAccount.setMaxWidth(Double.MAX_VALUE);
    }
//END Controller MenuBar
    //Controller HBox TrainBox, PlaneBox, BusBox

    private void handleBoxClick(HBox box) {
        if (lastSelectedBox != null) {
            // Reset the style of the last selected box
            lastSelectedBox.setStyle("");
        }
        // Set the new style for the clicked box
        box.setStyle("-fx-background-color: white;");
        lastSelectedBox = box;
    }
    @FXML
    private void initialize() {
        // Bắt sự kiện click cho các HBox
        BusBox.setOnMouseClicked(event -> handleBoxClick(BusBox));
        TrainBox.setOnMouseClicked(event -> handleBoxClick(TrainBox));
        PlaneBox.setOnMouseClicked(event -> handleBoxClick(PlaneBox));

        menuDes.setStyle("-fx-background-color: #327da8;");

        btAccount.setText("Admin");
    }
//BUTTON SAVE
//    private boolean isValidDate(String dateString) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//        try {
//            LocalDate.parse(dateString, formatter);
//            return true;
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }

    private boolean isValidTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(timeString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidBigDecimal(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    public void handleBtSaveAction(ActionEvent event) {
        // Kiểm tra xem đã chọn loại phương tiện chưa
//        String loaiPT = "";
        if (lastSelectedBox == BusBox) {
            loaiPT = "Bus";
        } else if (lastSelectedBox == TrainBox) {
            loaiPT = "Train";
        } else if (lastSelectedBox == PlaneBox) {
            loaiPT = "Plane";
        }

        if (fromTF.getText().isEmpty() || toTF.getText().isEmpty() || priceTF.getText().isEmpty() ||
                tenHangTF.getText().isEmpty() || goTF.getValue() == null || gioKhoiHanhTF.getText().isEmpty() ||
                returnTF.getValue() == null || gioDenTF.getText().isEmpty()) {
            showAlert("Cần ghi đủ thông tin!");
//        } else if (!isValidDate(goTF.getValue().toString()) || !isValidDate(returnTF.getValue().toString())) {
//            showAlert("Go và Return nhập sai!");
        } else if (!isValidTime(gioKhoiHanhTF.getText()) || !isValidTime(gioDenTF.getText())) {
            showAlert("GioKhoiHanh và GioDen nhập sai!");
            showAlert("GioKhoiHanh và GioDen nhập sai!");
        } else if (!isValidBigDecimal(priceTF.getText())) {
            showAlert("Price nhập sai!");
        } else {
//            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                if (loaiPT.equals("Bus") || loaiPT.equals("Train")) {


//                LocalDate goDate = goTF.getValue();
//                LocalDate returnDate = returnTF.getValue();

                    transaction = session.beginTransaction();

                    // Step 1: Create or update Route
                    Route route = new Route();
                    route.setDiemKhoiHanh(fromTF.getText());
                    route.setDiemDen(toTF.getText());
                    route.setLoaiPhuongTien(loaiPT);
                    session.saveOrUpdate(route);

                    // Step 2: Query or create Transportation
                    Query<Transportation> query = session.createQuery("FROM Transportation T WHERE T.tenHang = :tenHang", Transportation.class);
                    query.setParameter("tenHang", tenHangTF.getText());
                    Transportation transportation = query.uniqueResult();

                    if (transportation == null) {
                        transportation = new Transportation();
                        transportation.setLoaiPhuongTien(loaiPT);
                        transportation.setTenHang(tenHangTF.getText());
                        session.save(transportation);
                    }

                    // Step 3: Create or update Schedule
                    Schedule schedule = new Schedule();
                    schedule.setRoutes(route);
//              schedule.setNgayKhoiHanh(LocalDate.parse(goTF.getValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    schedule.setNgayKhoiHanh(goTF.getValue());

                    // Parse the time strings to LocalTime
                    LocalTime localGioKhoiHanh = LocalTime.parse(gioKhoiHanhTF.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime localGioDen = LocalTime.parse(gioDenTF.getText(), DateTimeFormatter.ofPattern("HH:mm"));

                    // Convert LocalTime to java.sql.Time
                    java.sql.Time sqlGioKhoiHanh = java.sql.Time.valueOf(localGioKhoiHanh);
                    java.sql.Time sqlGioDen = java.sql.Time.valueOf(localGioDen);


                    schedule.setGioKhoiHanh(sqlGioKhoiHanh);
                    schedule.setGioDen(sqlGioDen);
                    schedule.setGiaVe(Double.parseDouble(priceTF.getText()));
                    schedule.setSoChoConLai(Integer.parseInt(soChoTF.getText()));
                    schedule.setTransportations(transportation);
                    session.saveOrUpdate(schedule);

                    transaction.commit();
                    showAlert("Thêm thành công!");
                } else if (loaiPT.equals("Plane")) {
                    try {
                        if (sanBayDiTF.getText().isEmpty() || sanBayDenTF.getText().isEmpty() || maChuyenBayTF.getText().isEmpty()) {
                            showAlert("Ghi đủ thông tin chưa thằng đần?!");
                        } else {
                            transaction = session.beginTransaction();

                            // Create or update Flight
                            Flight flight = new Flight();
                            flight.setDiemKhoiHanh(fromTF.getText());
                            flight.setDiemDen(toTF.getText());
                            flight.setNgayKhoiHanh(goTF.getValue());
                            flight.setNgayDen(returnTF.getValue());
                            flight.setSoGheConLai(Integer.parseInt(soChoTF.getText()));
                            flight.setHangHangKhong(tenHangTF.getText());
                            flight.setGioKhoiHanh(LocalTime.parse(gioKhoiHanhTF.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                            flight.setGioDen(LocalTime.parse(gioDenTF.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                            flight.setSanBayDi(sanBayDiTF.getText());
                            flight.setSanBayDen(sanBayDenTF.getText());
                            flight.setMaChuyenBay(maChuyenBayTF.getText());

                            double giaVe = Double.parseDouble(priceTF.getText());
                            flight.setGiaVe(giaVe);

                            session.saveOrUpdate(flight);

                            transaction.commit();
                            showAlert("Thêm thành công!");
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//BUTTON MANAGE
@FXML
public void handleBtManageAction(ActionEvent event) {
        if(lastSelectedBox == BusBox){
            loaiPT = "Bus";
        } else if (lastSelectedBox == TrainBox){
            loaiPT = "Train";
        } else if (lastSelectedBox == PlaneBox){
            loaiPT = "Plane";
        }
    if (lastSelectedBox == BusBox|| lastSelectedBox == TrainBox){
        // Query the Schedules table
        Query<Schedule> query = session.createQuery("FROM Schedule S JOIN FETCH S.routes R JOIN FETCH S.transportations T WHERE T.loaiPhuongTien =: loaiPT", Schedule.class);

        // Bạn cần gán giá trị cho tham số tên :loaiPT
        query.setParameter("loaiPT", loaiPT); //

        List<Schedule> schedules = query.list();



        // Clear the vBox
        vBox.getChildren().clear();

        // For each Schedule record
        for (Schedule schedule : schedules) {
            try {
                // Load a new transPane from the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TransPane.fxml"));
                AnchorPane transPane = loader.load();

                // Get the Labels and the Delete button
                Label textFrom = (Label) transPane.lookup("#textFrom");
                Label textTo = (Label) transPane.lookup("#textTo");
                Label textTenHang = (Label) transPane.lookup("#textTenHang");
                Label textDate = (Label) transPane.lookup("#textDate");
                Label textGioKhoiHanh = (Label) transPane.lookup("#textGioKhoiHanh");
                Label textGioDen = (Label) transPane.lookup("#textGioDen");
                Label textSoChoConLai = (Label) transPane.lookup("#textSoChoConLai");
                Label textPrice = (Label) transPane.lookup("#textPrice");
                Button btDelete = (Button) transPane.lookup("#btDelete");


                // Set the Labels
                textFrom.setText(schedule.getRoutes().getDiemKhoiHanh());
                textTo.setText(schedule.getRoutes().getDiemDen());
                textTenHang.setText("|| "+ schedule.getTransportations().getTenHang());
                textDate.setText(schedule.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                textGioKhoiHanh.setText("|| "+String.valueOf(schedule.getGioKhoiHanh()) + "  ->");
                textGioDen.setText(String.valueOf(schedule.getGioDen()));
                textSoChoConLai.setText("|| "+ String.valueOf(schedule.getSoChoConLai()));
                textPrice.setText(schedule.getGiaVe().toString() + " VND");

                // Set the Delete button action
                btDelete.setOnAction(e -> {
                    Transaction transaction = null;
                    try {
                        // Start a new transaction
                        transaction = session.beginTransaction();

                        // Delete the Schedule record
                        session.delete(schedule);

                        // Delete the Route record
                        session.delete(schedule.getRoutes());

                        // Commit the transaction
                        transaction.commit();

                        // Remove the transPane from the vBox
                        vBox.getChildren().remove(transPane);
                    } catch (Exception ex) {
                        // If there are any exceptions, roll back the changes
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        // And print the error message
                        System.out.println(ex.getMessage());
                    }
                });

                // Add the transPane to the vBox
                vBox.getChildren().add(transPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    else if (lastSelectedBox == PlaneBox) {
        // Query the Flights table
        Query<Flight> query = session.createQuery("FROM Flight F", Flight.class);
        List<Flight> flights = query.list();

        // Clear the vBox
        vBox.getChildren().clear();

        // For each Flight record
        for (Flight flight : flights) {
            try {
                // Load a new transPane from the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TransPane.fxml"));
                AnchorPane transPane = loader.load();

                // Get the Labels and the Delete button
                Label textFrom = (Label) transPane.lookup("#textFrom");
                Label textTo = (Label) transPane.lookup("#textTo");
                Label textTenHang = (Label) transPane.lookup("#textTenHang");
                Label textDate = (Label) transPane.lookup("#textDate");
                Label textGioKhoiHanh = (Label) transPane.lookup("#textGioKhoiHanh");
                Label textGioDen = (Label) transPane.lookup("#textGioDen");
                Label textSoChoConLai = (Label) transPane.lookup("#textSoChoConLai");
                Label textPrice = (Label) transPane.lookup("#textPrice");
                Button btDelete = (Button) transPane.lookup("#btDelete");

                // Set the Labels
                textFrom.setText(flight.getSanBayDi());
                textTo.setText(flight.getSanBayDen());
                textTenHang.setText("|| "+ flight.getHangHangKhong());
                textDate.setText(flight.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                textGioKhoiHanh.setText("|| "+flight.getGioKhoiHanh().format(DateTimeFormatter.ofPattern("HH:mm")) + "  ->");
                textGioDen.setText(flight.getGioDen().format(DateTimeFormatter.ofPattern("HH:mm")));
                textSoChoConLai.setText("|| "+ String.valueOf(flight.getSoGheConLai()));
                textPrice.setText(flight.getGiaVe().toString() + " VND");

                // Set the Delete button action
                btDelete.setOnAction(e -> {
                    Transaction transaction = null;
                    try {
                        // Start a new transaction
                        transaction = session.beginTransaction();

                        // Delete the Flight record
                        session.delete(flight);

                        // Commit the transaction
                        transaction.commit();

                        // Remove the transPane from the vBox
                        vBox.getChildren().remove(transPane);
                    } catch (Exception ex) {
                        // If there are any exceptions, roll back the changes
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        // And print the error message
                        System.out.println(ex.getMessage());
                    }
                });

                // Add the transPane to the vBox
                vBox.getChildren().add(transPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    else {
        showAlert("Chọn loại phương tiện trước!");
    }
}
}
