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
import com.oopclass.breadapp.models.Contract;
import com.oopclass.breadapp.services.impl.ContractService;
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
public class ContractController implements Initializable {

    @FXML
    private Label contractId;

    @FXML
    private TextField status;

    @FXML
    private TextField amount;

    @FXML
    private DatePicker deadline;

    @FXML
    private Button reset;

    @FXML
    private Button saveContract;
    
    @FXML
    private Button deleteContract;
    
    @FXML
    private Button openClient;
    
    @FXML
    private Button openProject;

    @FXML
    private TableView<Contract> contractTable;

    @FXML
    private TableColumn<Contract, Long> colContractId;

    @FXML
    private TableColumn<Contract, String> colStatus;

    @FXML
    private TableColumn<Contract, String> colAmount;

    @FXML
    private TableColumn<Contract, LocalDate> colDeadline;

    @FXML
    private TableColumn<Contract, Boolean> colEdit;
    
    @FXML
    private TableColumn<Contract, Boolean> colCreatedAt;
    
    @FXML
    private TableColumn<Contract, Boolean> colUpdatedAt;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ContractService contractService;

    private ObservableList<Contract> contractList = FXCollections.observableArrayList();

//    @FXML
//    private void exit(ActionEvent event) {
//        Platform.exit();
//    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }
    
    @FXML
    private void  openClient(ActionEvent event){
        stageManager.switchScene(FxmlView.CLIENT);
    }
    
    @FXML
    private void  openProject(ActionEvent event){
        stageManager.switchScene(FxmlView.PROJECT);
    }

    @FXML
    private void saveContract(ActionEvent event) {

        if (validate("Status", getStatus(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Amount", getAmount(), "^[0-9]*$")
                && emptyValidation("Deadline", deadline.getEditor().getText().isEmpty())) {

            if (contractId.getText() == null || "".equals(contractId.getText())) {
                if (true) {

                    Contract contract = new Contract();
                    contract.setStatus(getStatus());
                    contract.setAmount(getAmount());
                    contract.setDeadline(getDeadline());
                    contract.setCreatedAt();
                    contract.setUpdatedAt();

                    Contract newContract = contractService.save(contract);

                    saveAlert(newContract);
                }

            } else {
                Contract contract = contractService.find(Long.parseLong(contractId.getText()));
                contract.setStatus(getStatus());
                contract.setAmount(getAmount());
                contract.setDeadline(getDeadline());
                contract.setUpdatedAt();
                Contract updatedContract = contractService.update(contract);
                updateAlert(updatedContract);
            }

            clearFields();
            loadContractDetails();
        }

    }

    @FXML
    private void deleteContract(ActionEvent event) {
        List<Contract> contracts = contractTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            contractService.deleteInBatch(contracts);
        }

        loadContractDetails();
    }

    private void clearFields() {
        contractId.setText(null);
        status.clear();
        amount.clear();
        deadline.getEditor().clear();
    }

    private void saveAlert(Contract contract) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Contract saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The contract " + contract.getStatus() + " " + contract.getAmount() + " has been created and \n" + " id is " + contract.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Contract contract) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Contract updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The contract " + contract.getStatus() + " " + contract.getAmount() + " has been updated.");
        alert.showAndWait();
    }

    private String getGenderTitle(String gender) {
        return (gender.equals("Male")) ? "his" : "her";
    }

    public String getStatus() {
        return status.getText();
    }

    public String getAmount() {
        return amount.getText();
    }

    public LocalDate getDeadline() {
        return deadline.getValue();
    }


    /*
	 *  Set All contractTable column properties
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

        colContractId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Contract, Boolean>, TableCell<Contract, Boolean>> cellFactory
            = new Callback<TableColumn<Contract, Boolean>, TableCell<Contract, Boolean>>() {
        @Override
        public TableCell<Contract, Boolean> call(final TableColumn<Contract, Boolean> param) {
            final TableCell<Contract, Boolean> cell = new TableCell<Contract, Boolean>() {
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
                            Contract contract = getTableView().getItems().get(getIndex());
                            updateContract(contract);
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

                private void updateContract(Contract contract) {
                    contractId.setText(Long.toString(contract.getId()));
                    status.setText(contract.getStatus());
                    amount.setText(contract.getAmount());
                    deadline.setValue(contract.getDeadline());
                }
            };
            return cell;
        }
    };

    /*
	 *  Add All contracts to observable list and update table
     */
    private void loadContractDetails() {
        contractList.clear();
        contractList.addAll(contractService.findAll());

        contractTable.setItems(contractList);
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

        contractTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//        // Add all contracts into table
        loadContractDetails();
    }
}
