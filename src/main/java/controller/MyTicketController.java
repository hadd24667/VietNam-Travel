package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class MyTicketController extends MenuController implements AccountTextController {

    @FXML
    private Button btAccount;

    @FXML
    private ChoiceBox<?> choiceBox;

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
    private VBox vBoxTicket;

    private ButtonController buttonController;

    private Session session;

    private int userID;

    public MyTicketController() {
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

    //Controller MyTicket

    @FXML
    public void handleChoiceBoxAction() {
        try {
            userID = LogInController.UserSession.getUserID();
            String selectedValue = (String) choiceBox.getValue();
            // Query the HotelBookings table
            User user = session.get(User.class, userID);

            if (selectedValue.equals("Tourist attraction tickets")) {
                // Clear the vBoxTicket
                vBoxTicket.getChildren().clear();
                System.out.println("Cleared vBoxTicket");

                Query<TourBooking> query = session.createQuery("from TourBooking where users = :user", TourBooking.class);
                query.setParameter("user", user);
                List<TourBooking> tourBookings = query.getResultList();
                System.out.println("Tour bookings count: " + tourBookings.size());

                for (TourBooking tourBooking : tourBookings) {
                    try {
                        System.out.println("Processing TourBooking ID: " + tourBooking.getTourBookingsId());

                        // Load the TourTicketItem.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TourTicketItem.fxml"));
                        AnchorPane tourItem = loader.load();

                        Label textTourBookID = (Label) tourItem.lookup("#textTourBookID");
                        Label textNameTour = (Label) tourItem.lookup("#textNameTour");
                        Label textVisitDate = (Label) tourItem.lookup("#textVisitDate");
                        Label textAdults = (Label) tourItem.lookup("#textAdults");
                        Label textChild = (Label) tourItem.lookup("#textChild");
                        Label textTotalPayment = (Label) tourItem.lookup("#textTotalPayment");
                        Label textName = (Label) tourItem.lookup("#textName");
                        Label textPhoneNum = (Label) tourItem.lookup("#textPhoneNum");

                        // Set the text of the fields in the TourTicketItem.fxml
                        textTourBookID.setText(String.valueOf(tourBooking.getTourBookingsId()));

                        // Query the TouristAttractions table
                        Query<TouristAttraction> query2 = session.createQuery("from TouristAttraction where id = :touristAttractionsID", TouristAttraction.class);
                        query2.setParameter("touristAttractionsID", tourBooking.getTouristAttractions().getId());
                        TouristAttraction touristAttraction = query2.getSingleResult();

                        textNameTour.setText(touristAttraction.getTen());
                        textVisitDate.setText(tourBooking.getNgayThamQuan().toString());
                        textAdults.setText(String.valueOf(tourBooking.getSoVe()));
                        textChild.setText(String.valueOf(tourBooking.getSoVeTreEm()));
                        textName.setText(LogInController.UserSession.getHoTen());
                        textPhoneNum.setText(LogInController.UserSession.getPhoneNumber());

                        // Parse giaVe from BigDecimal to int
                        double giaVe = tourBooking.getTotalPayment();

                        // Create a NumberFormat instance for the default locale
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                        // Set the group size to 3
                        numberFormat.setGroupingUsed(true);
                        numberFormat.setMaximumFractionDigits(0);

                        // Format the number
                        String formattedGiaVe = numberFormat.format(giaVe);
                        textTotalPayment.setText(formattedGiaVe+ " VND");

                        // Log the data to ensure it is being set correctly
                        System.out.println("TourBooking Data: " +
                                textTourBookID.getText() + ", " +
                                textNameTour.getText() + ", " +
                                textVisitDate.getText() + ", " +
                                textAdults.getText() + ", " +
                                textChild.getText() + ", " +
                                textTotalPayment.getText());

                        tourItem.setStyle("-fx-border-color: black");

                        // Add the TourTicketItem.fxml to vBoxTicket
                        vBoxTicket.getChildren().add(tourItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (selectedValue.equals("Hotel tickets")) {
                // Clear the vBoxTicket
                vBoxTicket.getChildren().clear();
                System.out.println("Cleared vBoxTicket");


                Query<StayBookings> query = session.createQuery("from StayBookings where userID = :UserID", StayBookings.class);
                query.setParameter("UserID", user);
                List<StayBookings> stayBookings = query.getResultList();
                System.out.println("Tour bookings count: " + stayBookings.size());

                for (StayBookings stayBooking : stayBookings) {
                    try {
                        System.out.println("Processing TourBooking ID: " + stayBooking.getId());

                        // Load the HotelTicketItem.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/HotelTicketItem.fxml"));
                        AnchorPane hotelTicketItem = loader.load();

                        Label textID = (Label) hotelTicketItem.lookup("#textID");
                        Label textHotelName = (Label) hotelTicketItem.lookup("#textHotelName");
                        Label textCheckIn = (Label) hotelTicketItem.lookup("#textCheckIn");
                        Label textCheckOut = (Label) hotelTicketItem.lookup("#textCheckOut");
                        Label textPhoneNum = (Label) hotelTicketItem.lookup("#textPhoneNum");
                        Label textName = (Label) hotelTicketItem.lookup("#textName");
                        Label textNumRoom = (Label) hotelTicketItem.lookup("#textNumRoom");
                        Label textCapacity = (Label) hotelTicketItem.lookup("#textCapacity");
                        Label textTotalPayment = (Label) hotelTicketItem.lookup("#textTotalPayment");

                        // Set the text of the fields in the HotelTicketItem.fxml
                        textID.setText(String.valueOf(stayBooking.getId()));

                        // Query the Accommodations table
                        Query<Accommodation> query3 = session.createQuery("from Accommodation where id = :Accommodations_ID", Accommodation.class);
                        query3.setParameter("Accommodations_ID", stayBooking.getAccommodationID().getId());
                        Accommodation accommodation = query3.getSingleResult();

                        textID.setText(String.valueOf(stayBooking.getId()));
                        textHotelName.setText(accommodation.getTen());
                        textCheckIn.setText(stayBooking.getNgayDi().toString());
                        textCheckOut.setText(stayBooking.getNgayve().toString());
                        textPhoneNum.setText(LogInController.UserSession.getPhoneNumber());
                        textName.setText(LogInController.UserSession.getHoTen());
                        textNumRoom.setText(String.valueOf(stayBooking.getSoLuongPhong()));
                        textCapacity.setText(String.valueOf(accommodation.getDoRongPhong()));

                        // Parse giaVe from BigDecimal to int
                        double giaVe = stayBooking.getTongGiaPhong();

                        // Create a NumberFormat instance for the default locale
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                        // Set the group size to 3
                        numberFormat.setGroupingUsed(true);
                        numberFormat.setMaximumFractionDigits(0);

                        // Format the number
                        String formattedGiaVe = numberFormat.format(giaVe);

                        textTotalPayment.setText(formattedGiaVe + " VND");

                        hotelTicketItem.setStyle("-fx-border-color: black");

                        // Add the TourTicketItem.fxml to vBoxTicket
                        vBoxTicket.getChildren().add(hotelTicketItem);

                        // Log the data to ensure it
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if(selectedValue.equals("Transportation tickets")){
                // Clear the vBoxTicket
                vBoxTicket.getChildren().clear();
                System.out.println("Cleared vBoxTicket");

                Query<TransportBooking> query = session.createQuery("from TransportBooking where users = :userID", TransportBooking.class);
                query.setParameter("userID", user);
                List<TransportBooking> transportBookings = query.getResultList();
                System.out.println("Tour bookings count: " + transportBookings.size());

                for (TransportBooking transportBooking : transportBookings) {

                    if(transportBooking.getPlane() != null){

                        try {
                            // Load the FLightTicketItem.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/FLightTicketItem.fxml"));
                            AnchorPane flightTicketItem = loader.load();

                            Label textID = (Label) flightTicketItem.lookup("#textID");
                            Label textGoDate = (Label) flightTicketItem.lookup("#textGoDate");
                            Label textTime = (Label) flightTicketItem.lookup("#textTime");
                            Label textSeats = (Label) flightTicketItem.lookup("#textSeats");
                            Label textPhoneNum = (Label) flightTicketItem.lookup("#textPhoneNum");
                            Label textName = (Label) flightTicketItem.lookup("#textName");
                            Label textAirlines = (Label) flightTicketItem.lookup("#textAirlines");
                            Label textFlightNum = (Label) flightTicketItem.lookup("#textFlightNum");
                            Label textGo = (Label) flightTicketItem.lookup("#textGo");
                            Label textArrive = (Label) flightTicketItem.lookup("#textArrive");
                            Label textTotalPayment = (Label) flightTicketItem.lookup("#textTotalPayment");

                            // Set the text of the fields in the HotelTicketItem.fxml
                            textID.setText(String.valueOf(transportBooking.getId()));


                            //
                            Query<Flight> query4 = session.createQuery("from Flight where id = :Flights_ID", Flight.class);
                            query4.setParameter("Flights_ID", transportBooking.getPlane().getId());
                            Flight flight = query4.getSingleResult();

                            textID.setText(String.valueOf(transportBooking.getId()));
                            textGoDate.setText(flight.getNgayKhoiHanh().toString());
                            textTime.setText(flight.getGioKhoiHanh().toString());
                            textSeats.setText(String.valueOf(flight.getSoGheConLai()+1));
                            textPhoneNum.setText(LogInController.UserSession.getPhoneNumber());
                            textName.setText(LogInController.UserSession.getHoTen());
                            textAirlines.setText(flight.getHangHangKhong());
                            textFlightNum.setText(flight.getMaChuyenBay());
                            textGo.setText(flight.getDiemKhoiHanh());
                            textArrive.setText(flight.getDiemDen());

                            float giaVe = transportBooking.getTotalMoney();

                            // Create a NumberFormat instance for the default locale
                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                            // Set the group size to 3
                            numberFormat.setGroupingUsed(true);
                            numberFormat.setMaximumFractionDigits(0);

                            // Format the number
                            String formattedGiaVe = numberFormat.format(giaVe);

                            textTotalPayment.setText(formattedGiaVe+" VND");

                            System.out.println("transportBooking ID: "+transportBooking.getId());

                            flightTicketItem.setStyle("-fx-border-color: black");

                            // Add the TourTicketItem.fxml to vBoxTicket
                            vBoxTicket.getChildren().add(flightTicketItem);

                            // Log the data to ensure it
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {

                            System.out.println("Processing TourBooking ID: " + transportBooking.getId());

                            // Load the HotelTicketItem.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/TransportTicketItem.fxml"));
                            AnchorPane transTicketItem = loader.load();

                            Label textID = (Label) transTicketItem.lookup("#textID");
                            Label textType = (Label) transTicketItem.lookup("#textType");
                            Label textGoDate = (Label) transTicketItem.lookup("#textGoDate");
                            Label textTime = (Label) transTicketItem.lookup("#textTime");
                            Label textSeats = (Label) transTicketItem.lookup("#textSeats");
                            Label textPhoneNum = (Label) transTicketItem.lookup("#textPhoneNum");
                            Label textName = (Label) transTicketItem.lookup("#textName");
                            Label texCompanyName = (Label) transTicketItem.lookup("#texCompanyName");
                            Label textTotalPayment = (Label) transTicketItem.lookup("#textTotalPayment");

                            // Set the text of the fields in the HotelTicketItem.fxml
                            textID.setText(String.valueOf(transportBooking.getId()));

                            //
                            Query<Schedule> query5 = session.createQuery("from Schedule where id = :schedulesID", Schedule.class);
                            query5.setParameter("schedulesID", transportBooking.getSchedules().getId());
                            Schedule schedule = query5.getSingleResult();

                            Query<Transportation> query6 = session.createQuery("from Transportation where id = :TransportationID", Transportation.class);
                            query6.setParameter("TransportationID", schedule.getTransportations().getId());
                            Transportation transportation = query6.getSingleResult();

                            textID.setText(String.valueOf(transportBooking.getId()));
                            textType.setText(transportation.getLoaiPhuongTien());
                            textGoDate.setText(schedule.getNgayKhoiHanh().toString());
                            textTime.setText(schedule.getGioKhoiHanh().toString());

                            textSeats.setText(String.valueOf(schedule.getSoChoConLai()+1));
                            textPhoneNum.setText(LogInController.UserSession.getPhoneNumber());
                            textName.setText(LogInController.UserSession.getHoTen());
                            texCompanyName.setText(transportation.getTenHang());
                            // Parse giaVe from BigDecimal to int
                            float giaVe = transportBooking.getTotalMoney();

                            // Create a NumberFormat instance for the default locale
                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

                            // Set the group size to 3
                            numberFormat.setGroupingUsed(true);
                            numberFormat.setMaximumFractionDigits(0);

                            // Format the number
                            String formattedGiaVe = numberFormat.format(giaVe);

                            textTotalPayment.setText(formattedGiaVe+" VND");

                            ImageView typeOfTrans = (ImageView) (transTicketItem.lookup("#TypeOfTransport"));
                            if (textType.getText().equals("Bus"))
                            {
                                Image busImage = new Image(getClass().getResourceAsStream("/img/bus.png"));
                                typeOfTrans.setImage(busImage);
                            }

                            transTicketItem.setStyle("-fx-border-color: black");

                            // Add the TourTicketItem.fxml to vBoxTicket
                            vBoxTicket.getChildren().add(transTicketItem);

                            // Log the data to ensure it
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            else {
                buttonController.showErrorAlert("Error","Please select a ticket type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
