package com.example.vietnamtravel;

//import controller.OpenSessionHB.Global;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Util.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class Test extends Application {

    private Session session;

    @Override
    public void start(Stage stage) throws IOException {
        // Tạo và khởi động một Task để mở Hibernate Session
        Task<Session> openSessionTask = new Task<Session>() {
            @Override
            protected Session call() throws Exception {
                return HibernateUtil.getSessionFactory().openSession();
            }
        };
        new Thread(openSessionTask).start();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1350, 730);
        stage.setTitle("Vietnam Travel");

        // Thêm logo cho cửa sổ ứng dụng
        Image logo = new Image(getClass().getResourceAsStream("/img/MainLogo.png"));
        stage.getIcons().add(logo);

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