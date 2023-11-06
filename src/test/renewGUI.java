package test;

import test.DatabaseConnection;
import test.Output;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;


public class renewGUI extends JPanel {
    String url = "jdbc:mysql://localhost/company";
    String user = "root";
    String password = "admin123";
    private List<JCheckBox> fromCheckboxes; // 추가: FROM 체크박스 리스트
    private JTextField columnsField;
    private JCheckBox whereCheckBox;
    private JTextField whereField;
    private JCheckBox groupCheckBox;
    private JTextField groupField;
    private JCheckBox havingCheckBox;
    private JTextField havingField;
    private JCheckBox orderCheckBox;
    private JTextField orderField;
    private JTextArea resultArea;
    private JTextField insertField; // 튜플 삽입을 위한 입력 필드
    private JButton insertButton; // 튜플 삽입 버튼
    private JTextField deleteField; // 튜플 삭제를 위한 입력 필드
    private JButton deleteButton; // 튜플 삭제 버튼
    private ButtonGroup optionbtns;
    private String opsel;
    private JButton executeButton;
    private JButton reportButton;
    public JButton thisBtn;
    private JComboBox<String> tableComboBox;
    private JTextField columnField;
    public renewGUI() {
        opsel="report";
        JPanel OpSelPanel = new JPanel(); // MODE 패널 추가
        OpSelPanel.setLayout(new GridLayout(1, 6));
        JLabel OpSelLabel = new JLabel("\t MODE");
        OpSelPanel.add(OpSelLabel);

        optionbtns = new ButtonGroup();
        String[] Options = {"report", "search", "insert", "delete"}; // MODE 항목 리스트
        for (String option : Options) {
            JRadioButton optionbtn = new JRadioButton(option);
            optionbtns.add(optionbtn);
            OpSelPanel.add(optionbtn);
        }
        JButton Opbutten = new JButton("OK");
        OpSelPanel.add(Opbutten);


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout()); // 레이아웃 크기 수정
        insertButton = new JButton("Insert Tuple");
        deleteButton = new JButton("Delete Tuple");
        executeButton = new JButton("Query");
        reportButton = new JButton("report");

        thisBtn=reportButton;
        resultArea = new JTextArea(25, 20);
        resultArea.setEditable(false);
        Opbutten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enumeration<AbstractButton> enums = optionbtns.getElements();
                while(enums.hasMoreElements()){ //
                    AbstractButton ab = enums.nextElement();
                    JRadioButton jb = (JRadioButton)ab;       
                    if(jb.isSelected()) {
                        opsel = jb.getText();
                        System.out.print(opsel);
                    }

                }

                inputPanel.removeAll();
                remove(2);
                remove(1);
                if (opsel.equals("report")){
                    thisBtn=reportButton;
                    add(get_report(),BorderLayout.CENTER);
                }else if (opsel.equals("search")){
                    thisBtn=executeButton;
                    add(get_search(),BorderLayout.CENTER);
                }else if (opsel.equals("insert")){
                    thisBtn=insertButton;
                    add(get_insert(),BorderLayout.CENTER);
                }else{
                    thisBtn=deleteButton;
                    add(get_delete(),BorderLayout.CENTER);
                }
                add(inputPanel, BorderLayout.SOUTH);
                
                revalidate();
                repaint();
                inputPanel.add(thisBtn, BorderLayout.NORTH);
                inputPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);
                inputPanel.revalidate();
                inputPanel.repaint();

            }
        });


        inputPanel.add(thisBtn, BorderLayout.NORTH);//input 패널의 상단에 버튼 설치
        inputPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);//input 패널의 하단에 결과 텍스트

        
        setLayout(new BorderLayout());

        add(OpSelPanel,BorderLayout.NORTH);
        add(get_report(),BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        //하단 버튼을 눌렀을 떄 JPanel에서 가져온 정보로 쿼리를 만들고 쿼리를 전송한다.
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //커넥션
                DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);

                String query = "SELECT Fname, Minit, Lname, Ssn, Bdate, Address, Sex, Salary, Super_ssn, dname FROM EMPLOYEE, department WHERE dno = dnumber";
                String columns = "Fname, Minit, Lname, Ssn, Bdate, Address, Sex, Salary, Super_ssn, dname";
                System.out.println(query);

                try {
                    ResultSet resultSet = dbConnection.executeQuery(query);
                    String resultText = Output.printResults(resultSet, columns);
                    resultArea.setText(resultText);

                    dbConnection.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    resultArea.setText("에러 발생: " + ex.getMessage());
                }
            }
        });
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);

                List<String> selectedFromOptions = new ArrayList<>();
                for (JCheckBox checkbox : fromCheckboxes) {
                    if (checkbox.isSelected()) {
                        selectedFromOptions.add(checkbox.getText());
                    }
                }
                String columns = columnsField.getText();

                String query = "";
                String fromClause = "";

                if (!selectedFromOptions.isEmpty()) {
                    fromClause = " FROM " + String.join(", ", selectedFromOptions);

                    if(columns.equals("*")) {
                        columns = "";
                        Map<String, String[]> tableColumns = new HashMap<>();
                        tableColumns.put("EMPLOYEE", new String[]{"Fname", "Minit", "Lname", "Ssn", "Bdate", "Address", "Sex", "Salary", "Super_ssn", "Dno"});
                        tableColumns.put("WORKS_ON", new String[]{"Essn", "Pno", "Hours"});
                        tableColumns.put("DEPARTMENT", new String[]{"Dname", "Dnumber", "Mgr_ssn", "Mgr_start_date"});
                        tableColumns.put("DEPT_LOCATIONS", new String[]{"Dnumber", "Dlocation"});
                        tableColumns.put("PROJECT", new String[]{"Pname", "Pnumber", "Plocation", "Dnum"});
                        tableColumns.put("DEPENDENT", new String[]{"Essn", "Dependent_name", "Sex", "Bdate", "Relationship"});

                        for (String selectedTable : selectedFromOptions) {
                            if (tableColumns.containsKey(selectedTable)) {
                                String[] columnList = tableColumns.get(selectedTable);
                                if ( !columns.equals("") ) columns += ", ";
                                columns += String.join(", ", columnList);
                            }
                        }

                    }
                }
                query += " SELECT " + columns;
                query += fromClause;
                String condition = whereField.getText();
                if (whereCheckBox.isSelected()) {
                    query += " WHERE " + condition;
                }
                String groupBy = groupField.getText();
                if (groupCheckBox.isSelected()) {
                    query += " GROUP BY " + groupBy;
                }
                String having = havingField.getText();
                if (havingCheckBox.isSelected()) {
                    query += " HAVING " + having;
                }
                String orderBy = orderField.getText();
                if (orderCheckBox.isSelected()) {
                    query += " ORDER BY " + orderBy;
                }
                System.out.println(query);
                System.out.println(columns);
                try {
                    ResultSet resultSet = dbConnection.executeQuery(query);
                    String resultText = Output.printResults(resultSet, columns);
                    resultArea.setText(resultText);
                    dbConnection.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    resultArea.setText("에러 발생: " + ex.getMessage());
                }
            }
        });
        this.insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 테이블 선택 드랍다운으로부터 선택한 테이블 이름 가져오기
                String selectedTable = (String) tableComboBox.getSelectedItem();
                // Select Columns 텍스트 필드에서 컬럼 목록을 가져오기
                String selectedColumns = columnField.getText();
                // Enter Values 텍스트 필드에서 값을 가져오기
                String values = insertField.getText();

                // 선택한 테이블, 컬럼 및 값으로 INSERT 쿼리를 생성 및 실행
                String[] columns = selectedColumns.split(",");
                String insertQuery = "INSERT INTO " + selectedTable + " (" + selectedColumns + ") VALUES (" + values + ")";

                try {
                    // DB 연결 세팅하기
                    DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);
                    // INSERT 쿼리 실행 및 추가한 행의 수 가져오가
                    int rowsAffected = dbConnection.executeUpdate(insertQuery);

                    // 결과를 resultArea에 표시
                    resultArea.setText("INSERT Query: " + insertQuery + "\n" + rowsAffected + " row(s) inserted.");
                    // DB 연결 닫기
                    dbConnection.closeConnection();
                } catch (SQLException ex) {
                    // 오류에 대한 메세지를 표시하기 위한 부분
                    ex.printStackTrace();
                    resultArea.setText("Error: " + ex.getMessage());
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);

                // 추가: 선택한 FROM 항목을 문자열로 추가
                /* delete에서는 FROM에 table이 하나만 작성되어야 함. 
                따라서 1개 초과, 1개 미만이 선택된 경우 예외처리 */
                String query = "DELETE";
                ArrayList<String> selectedFromOptions =new ArrayList<>();
                for (JCheckBox checkbox : fromCheckboxes) {
                    if (checkbox.isSelected()) {
                        selectedFromOptions.add(checkbox.getText());
                    }
                }
                if ((!selectedFromOptions.isEmpty())&&(selectedFromOptions.size()<=1)) {
                    String fromClause = " FROM " + String.join(", ", selectedFromOptions);
                    query += fromClause;
                   
                    String condition = whereField.getText();
                    
                    query += " WHERE " + condition;
                    
                    System.out.println(query);
                    try {
                        
                        int state = dbConnection.executeUpdate(query);
                        //String resultText = Output.printResults(resultSet, columns);
                        //resultArea.setText(resultText);
                        System.out.print(state);
                        dbConnection.closeConnection();
                        if (state==0){
                            resultArea.setText("아무 튜플도 삭제되지 않았습니다. 잘못된 입력이거나 없는 튜플입니다.");
                        } else{
                            resultArea.setText(state+"개의 튜플이 삭제되었습니다.");
                        }
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        resultArea.setText("에러 발생: " + ex.getMessage());
                    }
                    
                } else{
                    resultArea.setText("무조건 하나의 FROM 선택을 해야합니다.");
                }
            }
        });

    }

    //패널에 표현될 정보를 JPanel에 담는다.
    private JPanel get_search(){

        JPanel selectPanel = new JPanel(); // SELECT 패널 추가
        selectPanel.setLayout(new GridLayout(1, 2));
        JLabel columnsLabel = new JLabel("\t SELECT");
        columnsField = new JTextField(20);
        selectPanel.add(columnsLabel);
        selectPanel.add(columnsField);

        JPanel fromPanel = new JPanel(); // FROM 패널 추가
        fromPanel.setLayout(new GridLayout(1, 7));
        JLabel fromLabel = new JLabel("\t FROM");
        fromPanel.add(fromLabel);
        fromCheckboxes = new ArrayList<>();
        String[] fromOptions = {"EMPLOYEE", "DEPARTMENT", "WORKS_ON", "DEPT_LOCATIONS", "PROJECT","DEPENDENT"}; // FROM 항목 리스트
        for (String option : fromOptions) {
            JCheckBox checkbox = new JCheckBox(option);
            fromCheckboxes.add(checkbox);
            fromPanel.add(checkbox);
        }


        JPanel wherePanel = new JPanel(); // WHERE 패널 추가
        wherePanel.setLayout(new GridLayout(1, 2));
        whereCheckBox = new JCheckBox("Use WHERE (use AND)");
        whereField = new JTextField(0);
        wherePanel.add(whereCheckBox);
        wherePanel.add(whereField);

        JPanel groupPanel = new JPanel(); // GROUP BY 패널 추가
        groupPanel.setLayout(new GridLayout(1, 2));
        groupCheckBox = new JCheckBox("Use GROUP BY");
        groupField = new JTextField(0);
        groupPanel.add(groupCheckBox);
        groupPanel.add(groupField);

        JPanel havingPanel = new JPanel(); // GROUP BY 패널 추가
        havingPanel.setLayout(new GridLayout(1, 2));
        havingCheckBox = new JCheckBox("HAVING");
        havingField = new JTextField(0);
        havingPanel.add(havingCheckBox);
        havingPanel.add(havingField);

        JPanel orderPanel = new JPanel(); // ORDER BY 패널 추가
        orderPanel.setLayout(new GridLayout(1, 2));
        orderCheckBox = new JCheckBox("Use ORDER BY");
        orderField = new JTextField(0);
        orderPanel.add(orderCheckBox);
        orderPanel.add(orderField);

        JPanel thisPanel= new JPanel(new GridLayout(6, 1));
        
        thisPanel.add(selectPanel);
        thisPanel.add(fromPanel);
        thisPanel.add(wherePanel);
        thisPanel.add(groupPanel);
        thisPanel.add(havingPanel);
        thisPanel.add(orderPanel);

        whereCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whereField.setEnabled(whereCheckBox.isSelected());
            }
        });
        groupCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupField.setEnabled(groupCheckBox.isSelected());
            }
        });
        orderCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderField.setEnabled(orderCheckBox.isSelected());
            }
        });
        havingCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                havingField.setEnabled(havingCheckBox.isSelected());
            }
        });
        return thisPanel;
    }
    private JPanel get_report() {
        JPanel thisPanel = new JPanel();
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'><br>데이터 베이스 기초 105조<br>EMPLOYEE TABLE REPORT</div></html>");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thisPanel.add(titleLabel);
        return thisPanel;
    }



    private JPanel get_delete(){

        JPanel fromPanel = new JPanel(); // FROM 패널 추가
        fromPanel.setLayout(new GridLayout(1, 7));
        JLabel fromLabel = new JLabel("\t FROM");
        fromPanel.add(fromLabel);
        fromCheckboxes = new ArrayList<>();
        String[] fromOptions = {"EMPLOYEE", "DEPARTMENT", "WORKS_ON", "DEPT_LOCATIONS", "PROJECT","DEPENDENT"}; // FROM 항목 리스트
        for (String option : fromOptions) {
            JCheckBox checkbox = new JCheckBox(option);
            fromCheckboxes.add(checkbox);
            fromPanel.add(checkbox);
        }


        JPanel wherePanel = new JPanel(); // WHERE 패널 추가
        wherePanel.setLayout(new GridLayout(1, 2));
        JLabel whereLabel = new JLabel("WHERE (use AND)");
        whereField = new JTextField(0);
        wherePanel.add(whereLabel);
        wherePanel.add(whereField);


        JPanel thisPanel= new JPanel(new GridLayout(5, 1));
    

        thisPanel.add(fromPanel);
        thisPanel.add(wherePanel);

        return thisPanel;
    }
    private JPanel get_insert() {

        // JPanel 생성하고 튜플 삽입하기 위한 패널 생성
        JPanel thisPanel = new JPanel();

        // Select Table 레이블과 테이블을 선택할 수 있는 드랍다운 목록 만들기
        JPanel tableSelectionPanel = new JPanel();
        JLabel tableLabel = new JLabel("Select Table: ");
        JComboBox<String> tableComboBox = new JComboBox<>();

        // 테이블 이름 추가
        tableComboBox.addItem("EMPLOYEE");
        tableComboBox.addItem("DEPARTMENT");
        tableComboBox.addItem("WORKS_ON");
        tableComboBox.addItem("DEPT_LOCATIONS");
        tableComboBox.addItem("PROJECT");
        tableComboBox.addItem("DEPENDENT");
        tableSelectionPanel.add(tableLabel);
        tableSelectionPanel.add(tableComboBox);

        // Column을 선택하고 값을 입력할 수 있는 패널 생성
        JPanel columnSelectionPanel = new JPanel();
        JLabel columnLabel = new JLabel("Select Columns (comma-separated): ");
        JTextField columnField = new JTextField(20);
        columnSelectionPanel.add(columnLabel);
        columnSelectionPanel.add(columnField);

        // 테이블에 추가할 튜플에 대한 Values들 입력 받을 패널 생성
        JPanel valuesPanel = new JPanel();
        JLabel valuesLabel = new JLabel("Enter Values (comma-separated): ");
        JTextField valuesField = new JTextField(20);
        valuesPanel.add(valuesLabel);
        valuesPanel.add(valuesField);


        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 테이블 선택 드랍다운으로부터 선택한 테이블 이름 가져오기
                String selectedTable = (String) tableComboBox.getSelectedItem();
                // Select Columns 텍스트 필드에서 컬럼 목록을 가져오기
                String selectedColumns = columnField.getText();
                // Enter Values 텍스트 필드에서 값을 가져오기
                String values = valuesField.getText();

                // 선택한 테이블, 컬럼 및 값으로 INSERT 쿼리를 생성 및 실행
                String[] columns = selectedColumns.split(",");
                String insertQuery = "INSERT INTO " + selectedTable + " (" + selectedColumns + ") VALUES (" + values + ")";

                try {
                    // DB 연결 세팅하기
                    DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);
                    // INSERT 쿼리 실행 및 추가한 행의 수 가져오가
                    int rowsAffected = dbConnection.executeUpdate(insertQuery);

                    // 결과를 resultArea에 표시
                    resultArea.setText("INSERT Query: " + insertQuery + "\n" + rowsAffected + " row(s) inserted.");
                    // DB 연결 닫기
                    dbConnection.closeConnection();
                } catch (SQLException ex) {
                    // 오류에 대한 메세지를 표시하기 위한 부분
                    ex.printStackTrace();
                    resultArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        thisPanel.add(tableSelectionPanel);
        thisPanel.add(columnSelectionPanel);
        thisPanel.add(valuesPanel);
        thisPanel.add(insertButton);

        return thisPanel;
    }


}
