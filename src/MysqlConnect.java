//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Scanner;
//
//public class MysqlConnect {
//
//    public static void main(String[] args) {
//        Connection conn = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            String url = "jdbc:mysql://localhost/company";
//
//            conn = DriverManager.getConnection(url, "root", "admin123");
//            System.out.println("연결 성공");
//
//            Scanner scanner = new Scanner(System.in);
//
//            // 사용자로부터 선택할 컬럼들 입력받기 (컬럼 이름을 쉼표로 구분)
//            System.out.print("  SELECT: ");
//            String columns = scanner.nextLine();
//
//            // 사용자로부터 테이블 이름 입력받기
//            System.out.print("  FROM: ");
//            String tableName = scanner.nextLine();
//
//            // 사용자로부터 조건 입력받기
//            System.out.print("  WHERE: ");
//            String condition = scanner.nextLine();
//
//            // SQL 쿼리 생성
//            String query = "SELECT " + columns + " FROM " + tableName + "  " + condition;
//            System.out.println();
//            for (String column : columns.split(",")){
//                System.out.print(column + "\t");
//            }System.out.println();
//
//            // 쿼리 실행 및 결과 출력
//            Statement statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//
//
//
//            while (resultSet.next()) {
//                for (String column : columns.split(",")) {
//                    String columnValue = resultSet.getString(column.trim());
//                    System.out.print(columnValue + "\t");
//                }
//                System.out.println();
//            }
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("드라이버 로딩 실패");
//        } catch (SQLException e) {
//            System.out.println("에러: " + e);
//        } finally {
//            try {
//                if (conn != null && !conn.isClosed()) {
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
