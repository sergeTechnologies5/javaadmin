/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.User;
import utils.ConnectionUtil;


public class HomeController implements Initializable {

    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField usernametxt;

    @FXML
    private ComboBox<String> txtrole;
    @FXML
    Label lblStatus;
    @FXML
    Label lblStatus1;

    //Displaying the student details table
    @FXML
    private TableView<User> tbData; //student details table
    //table columns
    @FXML
    private TableColumn<User, Integer> id;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> firstname;
    @FXML
    private TableColumn<User, String> lastname;
    @FXML
    private TableColumn<User, String> role;
    @FXML
    private TableColumn<User, String> email;
    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    static Connection connection;
    int currentuser;

    public HomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtrole.getItems().addAll("Admin", "Student", "Company");
        txtrole.getSelectionModel().select("Admin");

        lblStatus1.setText(LoginController.username+" is Online");

        fetColumnList();
        try {
            fetRowList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void upDate(ActionEvent event) throws SQLException {
        //check if not empty
        if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() || usernametxt.getText().isEmpty()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            updateData();
            tbData.setItems(null);
            fetRowList();
        }
    }
    @FXML
    private void delete(ActionEvent event) throws SQLException {
        clearFields();
        delete();
        lblStatus.setText("Updated Successfully");
    }


    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtEmail.clear();
        usernametxt.clear();
    }

    private void delete () throws SQLException{
        String sqldel = "DELETE FROM users where id=?";
        preparedStatement = (PreparedStatement) connection.prepareStatement(sqldel);
        preparedStatement.setInt(1, currentuser);
        preparedStatement.executeUpdate();
        tbData.setItems(null);
        fetRowList();

    }

    private String updateData() {

        try {

            String st = "update users set first_name=?, last_name=?, email=?, username=? where id=?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, usernametxt.getText());
            preparedStatement.setInt(5, currentuser);
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Updated Successfully");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private static List<User> getAllUsers() throws SQLException {

        String allusessql = "SELECT * from users";
        List<User> users = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(allusessql);

            while (rs.next()) {
                //Iterate Row
                users.add(new User(""+rs.getString("id"),""+rs.getString("email"),"createdAt",""+rs.getString("username"),""+rs.getString("last_name"),""+rs.getString("first_name")));
            }


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
            return users;


    }

    //only fetch columns
    private void fetColumnList() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    //fetches rows and data from the list
    private void fetRowList() throws SQLException {
        tbData.setItems(readAllUsers());

        clickTable();
    }

    private void clickTable(){
        if(tbData != null){
           tbData.setOnMousePressed(event -> {
               User user =  tbData.getSelectionModel().selectedItemProperty().get();
               int index = tbData.getSelectionModel().selectedIndexProperty().get();
               if (user!=null){
                   currentuser = Integer.valueOf(user.getId());
                   txtFirstname.setText(user.getFirstname());
                   txtLastname.setText(user.getLastname());
                   usernametxt.setText(user.getUsername());
                   txtEmail.setText(user.getEmail());
               }

           });
        }
    }
    public static ObservableList<User> readAllUsers() throws SQLException {

        ObservableList<User> allusers = FXCollections.observableArrayList();
        allusers.addAll(getAllUsers());
        return allusers;
    }

    double x = 0, y =0;
    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

}
