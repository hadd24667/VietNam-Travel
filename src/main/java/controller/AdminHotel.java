package controller;

import Util.HibernateUtil;
import controller.AbstractController.AccountTextController;
import controller.AbstractController.AdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

import model.Accommodation;
import model.Images;
import model.StayBookings;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AdminHotel extends AdminController implements AccountTextController {

    @FXML
    private TextField addressTF;

    @FXML
    private Button btAccount;

    @FXML
    private Button btAdd;

    @FXML
    private TextField capacityTF;

    @FXML
    private ImageView imgHotel;

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
    private TextField nameHotelTF;

    @FXML
    private TextField priceTF;

    @FXML
    private TextArea contentTA;

    @FXML
    private Button btRefresh;

    @FXML
    private VBox vBoxHotel;

    private byte[] imgData;

    private model.Images images;

    private Session session;

    private ButtonController bc = new ButtonController();

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
    public AdminHotel() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @FXML
    void initialize() {

//Add value

        images = new Images();

        imgHotel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try {
                    imgData = Files.readAllBytes(selectedFile.toPath());
                    Image image = new Image(selectedFile.toURI().toString());
                    imgHotel.setImage(image);
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
    void handleAddAction(ActionEvent event) {
        // Lấy văn bản từ TextField
        String text = capacityTF.getText();
        if (addressTF.getText().isEmpty() || capacityTF.getText().isEmpty() || contentTA.getText().isEmpty()  || nameHotelTF.getText().isEmpty() || priceTF.getText().isEmpty() || imgData == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields and select an image.");
            alert.showAndWait();




        }
        else if(!isInteger(text)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You can only enter numbers into capacity.");
            alert.showAndWait();
        }
        else {


            // Begin a Transaction
            Transaction transaction = session.beginTransaction();

            // Save the Images object
            session.save(images);

            // Create and set the Accommodations object
            Accommodation accommodations = new Accommodation();
            accommodations.setTen(nameHotelTF.getText());
            accommodations.setDiaChi(addressTF.getText());
            accommodations.setDoRongPhong(Integer.parseInt(capacityTF.getText()));
            accommodations.setGiaPhong(new BigDecimal(priceTF.getText()));
            accommodations.setImages(images);
            accommodations.setMoTa(contentTA.getText());

            // Save the Accommodations object
            session.save(accommodations);

            // Commit the Transaction
            transaction.commit();
            ButtonController bc = new ButtonController();
            bc.showInformationAlert("Success", "Data has been saved successfully.");
        }
    }

    @FXML
    public void handleRefreshAction(ActionEvent event) {
        loadData();
    }

    // Phương thức kiểm tra số nguyên hợp lệ
    private boolean isInteger(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (i == 0 && text.charAt(i) == '-') {
                if (text.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.isDigit(text.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
//Add Item
private void loadData() {
    // Clear the vBoxHotel
    vBoxHotel.getChildren().clear();

    // Query the Accommodations table
    Query<Accommodation> query = session.createQuery("from Accommodation", Accommodation.class);
    List<Accommodation> accommodations = query.getResultList();

    for (Accommodation accommodation : accommodations) {
        try {
            // Load the Admin_HT_Item.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vietnamtravel/Admin_HT_Item.fxml"));
            AnchorPane adminHotelPane = loader.load();

            Pane imgViewHotel = (Pane) adminHotelPane.lookup("#imgViewHotel");
            Label textNameHotel = (Label) adminHotelPane.lookup("#textNameHotel");
            Label textAddress = (Label) adminHotelPane.lookup("#textAddress");
            Label textContent = (Label) adminHotelPane.lookup("#textContent");
            Label textPrice = (Label) adminHotelPane.lookup("#textPrice");
            Label textCapacity = (Label) adminHotelPane.lookup("#textCapacity");
            Button btDelete = (Button) adminHotelPane.lookup("#btDelete");

            // Set the text of the fields in the Admin_HT_Item.fxml
            textNameHotel.setText(accommodation.getTen());
            textAddress.setText(accommodation.getDiaChi());
            textCapacity.setText(String.valueOf(accommodation.getDoRongPhong()));
            textPrice.setText(accommodation.getGiaPhong().toString());
            textContent.setText(accommodation.getMoTa());

            btDelete.setOnAction(e -> {
                Transaction transaction = null;
                try {
                    // Start a new transaction
                    transaction = session.beginTransaction();

                    Accommodation accommodation2 = session.get(Accommodation.class, accommodation.getId());

                    // Find all StayBooking records related to the Accommodation
                    Query<StayBookings> querySB = session.createQuery("from StayBookings where accommodationID = :accommodationId", StayBookings.class);
                    querySB.setParameter("accommodationId", accommodation2);
                    List<StayBookings> stayBookings = querySB.getResultList();

                    // Delete all found StayBooking records
                    for (StayBookings stayBooking : stayBookings) {
                        session.delete(stayBooking);
                    }

                    // Delete the Accommodation record
                    session.delete(accommodation);

                    // Delete the Images record
                    session.delete(accommodation.getImages());

                    // Commit the transaction
                    transaction.commit();

                    // Remove the adminHotelPane from the vBox
                    vBoxHotel.getChildren().remove(adminHotelPane);

                    bc.showInformationAlert("Success", "Data has been deleted successfully.");
                } catch (Exception ex) {
                    // If there are any exceptions, roll back the changes
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    ex.printStackTrace();
                    // And print the error message
                    bc.showErrorAlert("Error", "Can't delete this item.");
                }
            });

            try{
                // Get the image data
                Images image = accommodation.getImages();
                byte[] imageData = image.getDuLieuAnh();

                // Decode the image data
                Image image2 = new Image(new ByteArrayInputStream(imageData));

                // Set the background of the root
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
                Background background = new Background(backgroundImage);
                imgViewHotel.setBackground(background);

                adminHotelPane.setStyle("-fx-background-color: #ffffff");

                // Add the Admin_HT_Item.fxml to vBoxHotel
                vBoxHotel.getChildren().add(adminHotelPane);
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


}
