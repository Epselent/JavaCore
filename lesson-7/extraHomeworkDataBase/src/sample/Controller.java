package sample;

import database.Datebase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



public class Controller {
    @FXML
    TextField name;
    @FXML
    TextField dateBirthD;
    @FXML
    Button addToBD;
    @FXML
    TableView<User> table;
    @FXML
    TableColumn<User, String> nameColumn;
    @FXML
    TableColumn<User, String> dateColumn;

    Datebase datebase;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        datebase = new Datebase();
        datebase.connect();
        initData();
/*
    В следующих двух строчках ошибка(((
 */
        nameColumn.cellValueFactoryProperty(new PropertyValueFactory<User, String>("userName"));

        dateColumn.cellValueFactoryProperty(new PropertyValueFactory<User, String>("userBirthday"));
        table.setItems(usersData);

    }

    // метод для теста...
    private void initData() {
        usersData.add(new User("Denis","denis"));
        usersData.add(new User("Denis","denis"));
        usersData.add(new User("Denis","denis"));
        usersData.add(new User("Denis","denis"));
        usersData.add(new User("Denis","denis"));
    }


    public void addToDataBase(ActionEvent actionEvent){
        String name1 = name.getText();
        String date = dateBirthD.getText();
        datebase.insert(name1,date);
        name.clear();
        dateBirthD.clear();
    }


}
