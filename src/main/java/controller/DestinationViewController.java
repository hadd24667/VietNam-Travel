package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Flight;
import model.Schedule;
import model.Transportation;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinationViewController extends MenuController implements AccountTextController {
    @FXML
    private  TextField FromTF;
    @FXML
    private  TextField ToTF;
    @FXML
    private  DatePicker GoTF;
    @FXML
    private  DatePicker ReturnTF;
    @FXML
    private  ListView<Pane> listRoutes;
    @FXML
    private  HBox BusBox;
    @FXML
    private  HBox TrainBox;
    @FXML
    private  HBox PlaneBox;

    @FXML
    private Button btAccount;

    private HBox lastSelectedBox;

    private String scheduleID;

    private String flightID;

    private final Session session;

    private TransportBookingController tbc = new TransportBookingController();

    private final ButtonController bc = new ButtonController();


    public DestinationViewController(){
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

    @FXML
    private void initialize() {
        // Bắt sự kiện click cho các HBox
        System.out.println("Initialize called");
        System.out.println("FromTF: " + FromTF); // Debugging line
        System.out.println("ToTF: " + ToTF);     // Debugging line
        BusBox.setOnMouseClicked(event -> handleBoxClick(BusBox));
        TrainBox.setOnMouseClicked(event -> handleBoxClick(TrainBox));
        PlaneBox.setOnMouseClicked(event -> handleBoxClick(PlaneBox));
    }

    @FXML
    private void handleSearchButtonClick() {
        System.out.println("Search button clicked");
        System.out.println("FromTF: " + FromTF.getText()); // Debugging line
        System.out.println("ToTF: " + ToTF.getText());     // Debugging line// Xử lý sự kiện click của nút Search
        String from = FromTF.getText();
        String to = ToTF.getText();
        LocalDate goDate = GoTF.getValue();
        LocalDate returnDate = ReturnTF.getValue();

        // Kiểm tra xem đã chọn loại phương tiện chưa
        if (lastSelectedBox == null) {
            // Hiển thị thông báo lỗi hoặc thực hiện hành động phù hợp
            bc.showErrorAlert("ERROR", "Please choose transportaotion");
            return; // Thoát khỏi phương thức nếu chưa chọn
        }

        String loaiPhuongTien = "";
        if (lastSelectedBox == BusBox) {
            loaiPhuongTien = "Bus";
        } else if (lastSelectedBox == TrainBox) {
            loaiPhuongTien = "Train";
        } else if (lastSelectedBox == PlaneBox) {
            loaiPhuongTien = "Plane";
        }

        List<Object> allRoutes = new ArrayList<>();

        if (loaiPhuongTien.equals("Bus")) {
            List<Schedule> routes = queryBusRoutes(from, to, goDate);
            allRoutes.addAll(routes);
            if (returnDate != null) {
                allRoutes.addAll(queryBusRoutes(to, from, returnDate));
            }
        } else if (loaiPhuongTien.equals("Train")) {
            List<Schedule> routes = queryTrainRoutes(from, to, goDate);
            allRoutes.addAll(routes);
            if (returnDate != null) {
                allRoutes.addAll(queryTrainRoutes(to, from, returnDate));
            }
        } else {
            List<Flight> routesFlight = queryFlightRoutes(from, to, goDate);
            allRoutes.addAll(routesFlight);
            if (returnDate != null) {
                allRoutes.addAll(queryFlightRoutes(to, from, returnDate));
            }
        }

        System.out.println("All routes: " + allRoutes);
        displayRoutes(listRoutes, allRoutes, loaiPhuongTien);
    }

    private void handleBoxClick(HBox box) {
        if (lastSelectedBox != null) {
            // Reset the style of the last selected box
            lastSelectedBox.setStyle("");
        }
        // Set the new style for the clicked box
        box.setStyle("-fx-background-color: white;");
        lastSelectedBox = box;
    }

    private List<Schedule> queryBusRoutes(String from, String to, LocalDate goDate ) {
        String hql = "FROM Schedule S JOIN FETCH S.routes R JOIN FETCH S.transportations T " +
                "WHERE T.loaiPhuongTien = 'Bus' AND "+
                "((R.diemKhoiHanh LIKE :from AND R.diemDen LIKE :to AND S.ngayKhoiHanh = :goDate)) ";
//        hql.setParameter("from", "%" + from + "%");

        Query<Schedule> query = session.createQuery(hql, Schedule.class);
        query.setParameter("from", "%" + from + "%");
        query.setParameter("to", "%"+to+"%");
        query.setParameter("goDate", goDate);
        return query.list();
    }

    private List<Schedule> queryTrainRoutes(String from, String to, LocalDate goDate) {
        String hql = "FROM Schedule S JOIN FETCH S.routes R JOIN FETCH S.transportations T " +
                "WHERE T.loaiPhuongTien = 'Train' AND " +
                "((R.diemKhoiHanh LIKE :from AND R.diemDen LIKE :to AND S.ngayKhoiHanh = :goDate)) ";

        Query<Schedule> query = session.createQuery(hql, Schedule.class);
        query.setParameter("from", "%" + from + "%");
        query.setParameter("to", "%"+to+"%");
        query.setParameter("goDate", goDate);

        String sqlQuery = query.getQueryString();
        System.out.println("SQL Query: " + sqlQuery);
        return query.list();
    }

    private List<Flight> queryFlightRoutes(String from, String to, LocalDate goDate) {
        String hql = "FROM Flight F WHERE " +
                "F.diemKhoiHanh LIKE :from AND F.diemDen LIKE :to AND F.ngayKhoiHanh = :goDate ";
        Query<Flight> query = session.createQuery(hql, Flight.class);
        query.setParameter("from", "%" + from + "%");
        query.setParameter("to", "%"+to+"%");
        query.setParameter("goDate", goDate);

        return query.list();
    }

    private void displayRoutes(ListView<Pane> listRoutes, List<Object> allRoutes, String type) {
        listRoutes.getItems().clear();

        if (allRoutes.isEmpty()) {
            Pane pane = new Pane();
            Label noRouteLabel = new Label("Không có chuyến đi nào trong khoảng thời gian này");
            // Đặt vị trí cho Label ở chính giữa của Pane
            noRouteLabel.layoutXProperty().bind(pane.widthProperty().subtract(noRouteLabel.widthProperty()).divide(2));
            noRouteLabel.layoutYProperty().bind(pane.heightProperty().subtract(noRouteLabel.heightProperty()).divide(2));

            pane.setPrefSize(300, 70);
            pane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
            Font font = Font.font("Calibri", 30);
            noRouteLabel.setFont(font);
            pane.getChildren().add(noRouteLabel);
            listRoutes.getItems().add(pane);
        } else {
            for (Object route : allRoutes) {
                if (route instanceof Schedule) {
                    Schedule schedule = (Schedule) route;
                    Pane pane = createRoutePane(schedule, type);
                    listRoutes.getItems().add(pane);
                    pane.setOnMouseClicked(event -> {
                        scheduleID = ((Schedule) route).getId().toString();
                        String hangXe = (String) pane.getUserData();
                        openTransportBooking(route, hangXe);
                        System.out.println("Schedule route selected: " + scheduleID); // Debugging line
                    });
                } else if (route instanceof Flight) {
                    Flight flight = (Flight) route;
                    Pane pane = createRoutePane(flight, type);
                    listRoutes.getItems().add(pane);
                    pane.setOnMouseClicked(event -> {
                        flightID = ((Flight) route).getId().toString();
                        openFlightBooking(flight);
                        System.out.println("Flight route selected: " + flightID); // Debugging line
                    });
                }
            }
        }
    }

    private Pane createRoutePane(Object entity, String type) {
        Pane pane = new Pane();
        pane.setPrefSize(300, 70);
        pane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        Font font = Font.font("Calibri", 20);

        Label labelTop = new Label();
        Label labelBottom = new Label();
        labelTop.setFont(font);
        labelBottom.setFont(font);

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);

        if (entity instanceof Flight) {
            pane.setUserData(((Flight) entity).getId().toString());
            Flight flight = (Flight) entity;
            labelTop.setText(flight.getSanBayDi() + " -> " + flight.getSanBayDen() + " | " + flight.getHangHangKhong());
            labelBottom.setText(flight.getDiemKhoiHanh() + " -> " + flight.getDiemDen() + " | " + flight.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " | "
                    + flight.getGioKhoiHanh() + " | " +flight.getGioDen() + " | "
                    + formatter.format(flight.getGiaVe()) + " VND | " + flight.getSoGheConLai() + " chỗ");
        } else if (entity instanceof Schedule) {
            pane.setUserData(((Schedule) entity).getId().toString());
            Schedule schedule = (Schedule) entity;
            Transportation transportation = schedule.getTransportations();

            if (transportation != null) {
                labelTop.setText(schedule.getRoutes().getDiemKhoiHanh() + " -> " + schedule.getRoutes().getDiemDen() + " | " + transportation.getTenHang());
                labelBottom.setText(schedule.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " | "
                        + schedule.getGioKhoiHanh() + " - " + schedule.getGioDen() + " | "
                        + formatter.format(schedule.getGiaVe()) + " VND | " + schedule.getSoChoConLai() + " chỗ");
            }
        }

        // Căn chỉnh và định dạng cho các label
        labelTop.setLayoutX(10);
        labelTop.setLayoutY(10);
        labelBottom.setLayoutX(10);
        labelBottom.setLayoutY(35);

        pane.getChildren().addAll(labelTop, labelBottom);
        return pane;
    }

    private void openTransportBooking(Object route, String hangXe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TransportationBookingView.fxml"));
            Pane bookingPane = loader.load();
            TransportBookingController controller = loader.getController();
            controller.setScheduleID(scheduleID);

            if (route instanceof Schedule) {
                Schedule schedule = (Schedule) route;
                hangXe = schedule.getTransportations().getTenHang();
                controller.setBookingDetails(
                        schedule.getRoutes().getDiemKhoiHanh(),
                        schedule.getRoutes().getDiemDen(),
                        LocalDate.parse(schedule.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                        schedule.getGioKhoiHanh(),
                        schedule.getGioDen(),
                        getTransportation(),
                        hangXe,
                        scheduleID
                );
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(bookingPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openFlightBooking(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/FlightBookingView.fxml"));
            Pane flightBookingPane = loader.load();
            FlightBookingsController controller = loader.getController();
            System.out.println("FlightBookingsController loaded");

            controller.setFlightID(flight.getId().toString());
            controller.setBookingDetails(flight);

            Stage stage = new Stage();
            stage.setScene(new Scene(flightBookingPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTransportation()
    {
        if (lastSelectedBox==null)
        {
            bc.showErrorAlert("ERROR", "Please choose transportation!");
        }
        String loaiPhuongTien = "";
        if (lastSelectedBox == BusBox) {
            loaiPhuongTien = "Bus";
        } else if (lastSelectedBox == TrainBox) {
            loaiPhuongTien = "Train";
        } else if (lastSelectedBox == PlaneBox) {
            loaiPhuongTien = "Plane";
        }
        return loaiPhuongTien;
    }
}
