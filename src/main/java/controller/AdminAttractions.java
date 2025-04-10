package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.AdminController;
import controller.AbstractController.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import model.Images;
import model.TouristAttraction;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javafx.stage.FileChooser;
import model.Images;
import model.TouristAttraction;
import org.hibernate.Session;
import org.hibernate.Transaction;
import Util.HibernateUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.Arrays;

public class AdminAttractions extends AdminController implements AccountTextController {

    @FXML
    private Button btAccount;

    @FXML
    private Button btSave;

    @FXML
    private ImageView imgViewData;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button menuDes;

    @FXML
    private Button menuHome;

    @FXML
    private Button menuStay;

    @FXML
    private Button menuTicket;

    @FXML
    private TextArea textContent;

    @FXML
    private TextField textLocation;

    @FXML
    private TextField textPlace;

    @FXML
    private TextField textPrice;

    private model.Images images;

    private byte[] imgData;

    private Session session;

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
    public void handleBtAccountAction(ActionEvent event) throws IOException {
        super.handleBtAccountAction(event);
    }
    //setText ACCOUNT BUTTON
    public void setBtAccountText(String hoTen) {
        btAccount.setText(hoTen);
        btAccount.setMinWidth(Button.USE_PREF_SIZE);
        btAccount.setMaxWidth(Double.MAX_VALUE);
    }

    public AdminAttractions() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @FXML
    void initialize() {

//Add value

        images = new Images();

        imgViewData.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try {
                    imgData = Files.readAllBytes(selectedFile.toPath());
                    Image image = new Image(selectedFile.toURI().toString());
                    imgViewData.setImage(image);
                    String fileName = selectedFile.getName();
                    String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
                    System.out.println("File name: " + fileName);
                    System.out.println("File format: " + fileFormat);
//                    System.out.println("Image data: " + Arrays.toString(imgData));

                    // Set the image data to the Images table (without saving)
                    // Assuming you have an Images object named images
                    images.setTenFile(fileName);
                    images.setLoaiFile(fileFormat);
                    images.setDuLieuAnh(imgData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a valid image file.");
                alert.showAndWait();
            }
        });

    }


    @FXML
    void handleBtSaveAction(ActionEvent event) {


        if (textContent.getText().isEmpty() || textLocation.getText().isEmpty() || textPlace.getText().isEmpty() || textPrice.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }
        else {

            String moTa = textContent.getText();
            String diaChi = textLocation.getText();
            String ten = textPlace.getText();
            BigDecimal giaVe = new BigDecimal(textPrice.getText());

            if (imgData != null) {
                try {

                    // Kiểm tra nếu tệp không tồn tại hoặc không đọc được
                            TouristAttraction attraction = new TouristAttraction();
                            attraction.setMoTa(moTa);
                            attraction.setDiaChi(diaChi);
                            attraction.setTen(ten);
                            attraction.setGiaVe(giaVe);
                            attraction.setImages(images);

                            Transaction transaction = session.beginTransaction();
                            session.save(images);
                            session.save(attraction);
                            transaction.commit();

                            // Show a success message
                            showAlert("Success", "Data has been saved successfully.");

                            // Clear the input fields
                            textContent.clear();
                            textLocation.clear();
                            textPlace.clear();
                            textPrice.clear();
                            imgViewData.setImage(null);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                    showAlert("Error", "File is not a valid image.");
            }
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
