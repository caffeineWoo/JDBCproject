import java.sql.ResultSet;
import java.sql.SQLException;

public class Output {
    public static String printResults(ResultSet resultSet, String columns) throws SQLException {
        StringBuilder result = new StringBuilder();

        String[] columnNames = columns.split(",");
        for (String columnName : columnNames) {
            result.append(columnName.trim()).append("\t");
        }
        result.append("\n");
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
