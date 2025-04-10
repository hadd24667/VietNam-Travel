package controller.AbstractController;

import controller.LogInController;
import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class MenuController {
    private Map<String, FXMLLoader> loaders = new HashMap<>();

    protected void openView(String fxmlPath, ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        FXMLLoader loader = loaders.get(fxmlPath);
        if (loader == null) {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loaders.put(fxmlPath, loader);  //put: khong hho tro loader nay
        }

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();


        String hoTen = LogInController.UserSession.getHoTen();
        AccountTextController controller = loader.getController();
        controller.setBtAccountText(hoTen);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleMenuHomeAction(ActionEvent event) throws IOException {
        openView("/com/example/vietnamtravel/MainView.fxml", event);
    }

    @FXML
    public void handleMenuDestiAction(ActionEvent event) throws IOException {
        openView("/com/example/vietnamtravel/DestinationView.fxml", event);
    }

    @FXML
    public void handleMenuStayAction(ActionEvent event) throws IOException {
        openView("/com/example/vietnamtravel/StayView.fxml", event);
    }

    @FXML
    public void handleMenuTicketAction(ActionEvent event) throws IOException{
        // Implement this method in the child classes
        openView("/com/example/vietnamtravel/MyTicketView.fxml", event);
    }

    @FXML
    public void handleBtAccountAction(ActionEvent event) throws IOException {
        // Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to log out?");

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicked "Yes"
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Load the Login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/Login.fxml"));
            Scene scene = new Scene(loader.load(), 1350, 730);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            // Set the new scene to the stage
            stage.setScene(scene);

            // Center the stage
            stage.centerOnScreen();

            // Show the stage
            stage.show();
        }
    }

}
