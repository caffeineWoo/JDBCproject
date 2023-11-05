//package test;
//import javax.swing.*;
//import java.awt.*;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.Vector;
//
//public class Output {
//    public static String printResults(ResultSet resultSet, String columns) throws SQLException {
//        StringBuilder result = new StringBuilder();
//        System.out.println(resultSet);
//        try {
//            String[] columnNames = columns.split(",");
//            for (String columnName : columnNames) {
//                result.append(columnName.trim()).append("\t");
//                System.out.println(columnName.trim());
//                if(columnName.trim().equals("Address")) result.append("\t");
//            }
//            result.append("\n");
//            while (resultSet.next()) {
//                for (String column : columns.split(",")) {
//                    String columnValue = resultSet.getString(column.trim());
//                    result.append(columnValue).append("\t");
////                    System.out.println(columnValue);
//                }
//                result.append("\n");
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            return "에러 발생: " + ex.getMessage();
//        }
//
//        return result.toString();
//    }
//}
