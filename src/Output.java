import java.sql.ResultSet;
import java.sql.SQLException;

public class Output {
    public static String printResults(ResultSet resultSet, String columns) throws SQLException {
        StringBuilder result = new StringBuilder();

        String[] columnNames = columns.split(",");
        int numColumns = columnNames.length;

        // 출력 테이블 헤더
        for (String columnName : columnNames) {
            result.append(String.format("| %-20s ", columnName.trim()));
        }
        result.append("|\n");

        // 헤더와 데이터 구분선
        for (int i = 0; i < numColumns; i++) {
            result.append("+---------------------");
        }
        result.append("+\n");

        while (resultSet.next()) {
            // 데이터 행
            for (String column : columns.split(",")) {
                String columnValue = resultSet.getString(column.trim());
                result.append(String.format("| %-20s ", columnValue));
            }
            result.append("|\n");
        }

        return result.toString();
    }
}