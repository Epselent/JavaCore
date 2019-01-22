package sample;

import javafx.beans.property.SimpleStringProperty;


public class User {
    public SimpleStringProperty userName;
    public SimpleStringProperty userBirthday;
        public User (String userName, String userBirthday){
            this.userName = new SimpleStringProperty(userName);
            this.userBirthday = new SimpleStringProperty(userBirthday);
        }


    public String getUserName() {
        return userName.get();
    }

    public String getUserBirthday() {
        return userBirthday.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday.set(userBirthday);
    }


}
