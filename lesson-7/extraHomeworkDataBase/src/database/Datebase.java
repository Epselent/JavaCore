package database;

import javafx.collections.ObservableList;

import java.sql.*;

public class Datebase {
    private static Connection connections;
    private static Statement stmt;
    public static void connect () {
        try {
            Class.forName("org.sqlite.JDBC");
            connections = DriverManager.getConnection("jdbc:sqlite:people.db");
            stmt = connections.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void disconnect(){
        try {
            connections.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insert(String name, String date){
        String sql = String.format("insert into nameAndBirthday (name, date)\n" +
                "values ('%s','%s');", name, date);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public static String select(String column){
//        String sql =String.format("select %s from nameAndBirthday", column);
//        try {
//            ResultSet rs = stmt.executeQuery(sql);
//            if (rs.next()){
//                 return rs.getString(1);
//            }else
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
