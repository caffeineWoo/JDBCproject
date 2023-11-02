package test;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTextArea;

public class Output {
    public static String printResults(ResultSet resultSet, String columns) throws SQLException {
        StringBuilder result = new StringBuilder();

        while (resultSet.next()) {
            for (String column : columns.split(",")) {
                String columnValue = resultSet.getString(column.trim());
                result.append(columnValue).append("\t");
            }
            result.append("\n");
        }

        return result.toString();
    }
}
