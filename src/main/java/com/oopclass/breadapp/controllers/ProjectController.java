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
import com.oopclass.breadapp.models.Project;
import com.oopclass.breadapp.services.impl.ProjectService;
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
public class ProjectController implements Initializable {

    @FXML
    private Label projectId;

    @FXML
    private TextField companyName;

    @FXML
    private TextField projectName;

    @FXML
    private TextField province;

    @FXML
    private TextField city;

    @FXML
    private Button reset;

    @FXML
    private Button saveProject;
    
    @FXML
    private Button openClient;
    
    @FXML
    private Button openContract;
    
    @FXML
    private Button deleteProject;

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, Long> colProjectId;

    @FXML
    private TableColumn<Project, String> colCompanyName;

    @FXML
    private TableColumn<Project, String> colProjectName;

    @FXML
    private TableColumn<Project, String> colProvince;

    @FXML
    private TableColumn<Project, String> colCity;

    @FXML
    private TableColumn<Project, Boolean> colEdit;
    
    @FXML
    private TableColumn<Project, Boolean> colCreatedAt;
    
    @FXML
    private TableColumn<Project, Boolean> colUpdatedAt;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ProjectService projectService;

    private ObservableList<Project> projectList = FXCollections.observableArrayList();

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
    private void  openContract(ActionEvent event){
        stageManager.switchScene(FxmlView.CONTRACT);
    }

    @FXML
    private void saveProject(ActionEvent event) {

        if (validate("Company Name", getCompanyName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Project Name", getProjectName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Province", getProvince(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("City", getCity(), "([a-zA-Z]{3,30}\\s*)+")) {

            if (projectId.getText() == null || "".equals(projectId.getText())) {
                if (true) {

                    Project project = new Project();
                    project.setCompanyName(getCompanyName());
                    project.setProjectName(getProjectName());
                    project.setProvince(getProvince());
                    project.setCity(getCity());
                    project.setCreatedAt();
                    project.setUpdatedAt();
                    

                    Project newProject = projectService.save(project);

                    saveAlert(newProject);
                }

            } else {
                Project project = projectService.find(Long.parseLong(projectId.getText()));
                project.setCompanyName(getCompanyName());
                project.setProjectName(getProjectName());
                project.setProvince(getProvince());
                project.setCity(getCity());
                project.setUpdatedAt();
                Project updatedProject = projectService.update(project);
                updateAlert(updatedProject);
            }

            clearFields();
            loadProjectDetails();
        }

    }

    @FXML
    private void deleteProject(ActionEvent event) {
        List<Project> projects = projectTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            projectService.deleteInBatch(projects);
        }

        loadProjectDetails();
    }

    private void clearFields() {
        projectId.setText(null);
        companyName.clear();
        projectName.clear();
        province.clear();
        city.clear();
    }

    private void saveAlert(Project project) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Project saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The project " + project.getCompanyName() + " " + project.getProjectName() + " " + project.getProvince() + " " + project.getCity() + " has been created and \n" + " id is " + project.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Project project) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Project updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The project " + project.getCompanyName() + " " + project.getProjectName() + " " + project.getProvince() + " " + project.getCity() + " has been updated.");
        alert.showAndWait();
    }

    public String getCompanyName() {
        return companyName.getText();
    }

    public String getProjectName() {
        return projectName.getText();
    }

    public String getProvince() {
        return province.getText();
    }

    public String getCity() {
        return city.getText();
    }


    /*
	 *  Set All projectTable column properties
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

        colProjectId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Project, Boolean>, TableCell<Project, Boolean>> cellFactory
            = new Callback<TableColumn<Project, Boolean>, TableCell<Project, Boolean>>() {
        @Override
        public TableCell<Project, Boolean> call(final TableColumn<Project, Boolean> param) {
            final TableCell<Project, Boolean> cell = new TableCell<Project, Boolean>() {
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
                            Project project = getTableView().getItems().get(getIndex());
                            updateProject(project);
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

                private void updateProject(Project project) {
                    projectId.setText(Long.toString(project.getId()));
                    companyName.setText(project.getCompanyName());
                    projectName.setText(project.getProjectName());
                    province.setText(project.getProvince());
                    city.setText(project.getCity());
                }
            };
            return cell;
        }
    };

    /*
	 *  Add All projects to observable list and update table
     */
    private void loadProjectDetails() {
        projectList.clear();
        projectList.addAll(projectService.findAll());

        projectTable.setItems(projectList);
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

        projectTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//        // Add all projects into table
        loadProjectDetails();
    }
}
