package com.example.vietnamtravel;

import Util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Test2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminAttractions.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DestinationView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AttractionBookingView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TransportationBookingView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FlightBookingView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUpView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StayView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyTicketView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1350, 730);
        stage.setTitle("Vietnam Travel");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            // Gọi shutdown() của HibernateUtil khi ứng dụng kết thúc
            HibernateUtil.shutdown();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}