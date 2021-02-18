package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Client;
import com.oopclass.breadapp.services.impl.ClientService;
import com.oopclass.breadapp.views.FxmlView;

//import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
@Controller
public class ClientController implements Initializable {

    @FXML
    private Label clientId;

    @FXML
    private TextField clientFirstName;

    @FXML
    private TextField clientLastName;

    @FXML
    private TextField contactNo;

    @FXML
    private TextField email;

    @FXML
    private Button reset;

    @FXML
    private Button saveClient;
    
    @FXML
    private Button openProject;
    
    @FXML
    private Button openContract;
    
    @FXML
    private Button deleteClient;

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, Long> colClientId;

    @FXML
    private TableColumn<Client, String> colClientFirstName;

    @FXML
    private TableColumn<Client, String> colClientLastName;

    @FXML
    private TableColumn<Client, String> colContactNo;

    @FXML
    private TableColumn<Client, String> colEmail;

    @FXML
    private TableColumn<Client, Boolean> colEdit;
    
    @FXML
    private TableColumn<Client, Boolean> colCreatedAt;
    
    @FXML
    private TableColumn<Client, Boolean> colUpdatedAt;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ClientService clientService;

    private ObservableList<Client> clientList = FXCollections.observableArrayList();

//    @FXML
//    private void exit(ActionEvent event) {
//        Platform.exit();
//    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }
    
    @FXML
    private void  openProject(ActionEvent event){
        stageManager.switchScene(FxmlView.PROJECT);
    }
    
    @FXML
    private void  openContract(ActionEvent event){
        stageManager.switchScene(FxmlView.CONTRACT);
    }

    @FXML
    private void saveClient(ActionEvent event) {

        if (validate("Client First Name", getClientFirstName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Client Last Name", getClientLastName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Contact No", getContactNo(), "(^(09|\\+639)\\d{9}$)+")
                && validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")) {

            if (clientId.getText() == null || "".equals(clientId.getText())) {
                if (true) {

                    Client client = new Client();
                    client.setClientFirstName(getClientFirstName());
                    client.setClientLastName(getClientLastName());
                    client.setContactNo(getContactNo());
                    client.setEmail(getEmail());
                    client.setCreatedAt();
                    client.setUpdatedAt();

                    Client newClient = clientService.save(client);

                    saveAlert(newClient);
                }

            } else {
                Client client = clientService.find(Long.parseLong(clientId.getText()));
                client.setClientFirstName(getClientFirstName());
                client.setClientLastName(getClientLastName());
                client.setContactNo(getContactNo());
                client.setEmail(getEmail());
                client.setUpdatedAt();
                Client updatedClient = clientService.update(client);
                updateAlert(updatedClient);
            }

            clearFields();
            loadClientDetails();
        }

    }

    @FXML
    private void deleteClient(ActionEvent event) {
        List<Client> clients = clientTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            clientService.deleteInBatch(clients);
        }

        loadClientDetails();
    }

    private void clearFields() {
        clientId.setText(null);
        clientFirstName.clear();
        clientLastName.clear();
        contactNo.clear();
        email.clear();
    }

    private void saveAlert(Client client) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Client saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The client " + client.getClientFirstName() + " " + client.getClientLastName() + " " + client.getContactNo() + " " + client.getEmail() + " has been created and \n" + " id is " + client.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Client client) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Client updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The client " + client.getClientFirstName() + " " + client.getClientLastName() + " " + client.getContactNo() + " " + client.getEmail() + " has been updated.");
        alert.showAndWait();
    }

    public String getClientFirstName() {
        return clientFirstName.getText();
    }

    public String getClientLastName() {
        return clientLastName.getText();
    }

    public String getContactNo() {
        return contactNo.getText();
    }

    public String getEmail() {
        return email.getText();
    }


    /*
	 *  Set All clientTable column properties
     */
    private void setColumnProperties() {
        /* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/

        colClientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientFirstName.setCellValueFactory(new PropertyValueFactory<>("clientFirstName"));
        colClientLastName.setCellValueFactory(new PropertyValueFactory<>("clientLastName"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> cellFactory
            = new Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>>() {
        @Override
        public TableCell<Client, Boolean> call(final TableColumn<Client, Boolean> param) {
            final TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Client client = getTableView().getItems().get(getIndex());
                            updateClient(client);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateClient(Client client) {
                    clientId.setText(Long.toString(client.getId()));
                    clientFirstName.setText(client.getClientFirstName());
                    clientLastName.setText(client.getClientLastName());
                    contactNo.setText(client.getContactNo());
                    email.setText(client.getEmail());
                }
            };
            return cell;
        }
    };

    /*
	 *  Add All clients to observable list and update table
     */
    private void loadClientDetails() {
        clientList.clear();
        clientList.addAll(clientService.findAll());

        clientTable.setItems(clientList);
    }

    /*
	 * Validations
     */
    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clientTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//        // Add all clients into table
        loadClientDetails();
    }
}
