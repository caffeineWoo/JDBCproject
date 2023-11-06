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
    String url = "jdbc:mysql://localhost:3306/companydb";
    String user = "root";
    String password = "renoj1331@";
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
    private JComboBox<String> fromComboBox;
    private String selectedOption;
    private String whereOption;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> genderComboBox;

    private ArrayList<String> selectedCheckboxes = new ArrayList<>();

    private JTextField salaryField;
    private JTextField headField;
    private JTextField addressField;
    private JTextField ssnField;
    private JComboBox<String> dnameComboBox;
    private JTextField mgr_ssnField;
  
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
                String selectedTable = (String) fromComboBox.getSelectedItem();

                String query = "";

                String fromClause = "";
	            String columns = "";

                query += "SELECT ";
	             // 체크된 열 이름들을 쉼표로 구분하여 query에 추가
	             for (String checkbox : selectedCheckboxes) {
	                 query += checkbox;
	                 columns +=checkbox;
	                 if (selectedCheckboxes.indexOf(checkbox) < selectedCheckboxes.size() - 1) {
	                     query += ", ";
	                 }
	                 if (selectedCheckboxes.indexOf(checkbox) < selectedCheckboxes.size() - 1) {
	                	 columns += ", ";
	                 }
	             }
	
	             if (selectedTable != null) {
	                 // 선택된 테이블을 fromClause에 추가
	                 fromClause = " FROM " + selectedTable;
	             }

                query += fromClause;


                String condition = "";

	            if (whereOption != null) {
	            	condition += " Where ";
	            }
	            
                System.out.println(whereOption);
	            
                if ("Department".equals(whereOption)) {
                    // "부서"를 선택한 경우, 부서 드롭다운에서 선택한 값을 가져와 조건을 설정
                    String selectedDepartment = (String) departmentComboBox.getSelectedItem();
                    condition += "Department = '" + selectedDepartment + "'";
 
                } else if ("Sex".equals(whereOption)) {
                    // "성별"을 선택한 경우, 성별 드롭다운에서 선택한 값을 가져와 조건을 설정
                    String selectedGender = (String) genderComboBox.getSelectedItem();
                    condition += "Sex = '" + selectedGender + "'";
                } else if ("Salary".equals(whereOption)) {
                    // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드의 값을 가져와 조건을 설정
                    String selectedSalary = salaryField.getText();
                    condition += "Salary = " + selectedSalary;
                } else if ("Super_ssn".equals(whereOption)) {
                    // "상사"를 선택한 경우, 상사를 입력받는 텍스트 필드의 값을 가져와 조건을 설정
                    String selectedSuperSsn = headField.getText();
                    condition += "Super_ssn = '" + selectedSuperSsn + "'";
                } else if ("Address".equals(whereOption)) {
                    // "주소"를 선택한 경우, 주소를 입력받는 텍스트 필드의 값을 가져와 조건을 설정
                    String selectedAddress = addressField.getText();
                    condition += "Address = '" + selectedAddress + "'";
                } else if ("Ssn".equals(whereOption)) {
                    // "이름"을 선택한 경우, 이름을 입력받는 텍스트 필드의 값을 가져와 조건을 설정
                    String selectedSsn = ssnField.getText();
                    condition += "Ssn = '" + selectedSsn + "'";
                }
                else if ("Dname".equals(whereOption)) {
                    
                    String selectedDname = (String) dnameComboBox.getSelectedItem();
                    condition += "Dname = '" + selectedDname + "'";
                } else if ("Mgr_ssn".equals(whereOption)) {
                   
                    String selectedMgr_ssn = mgr_ssnField.getText();
                    condition += "Mgr_ssn = '" + selectedMgr_ssn + "'";
                }
                
                query+=condition;
                
                System.out.println(query);
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
        JPanel fromPanel = new JPanel(); // FROM 패널 추가
        fromPanel.setLayout(new GridLayout(1, 7));
        JLabel fromLabel = new JLabel("\t 테이블 선택");
        fromPanel.add(fromLabel);
        
        fromCheckboxes = new ArrayList<>();
        String[] fromOptions = {"EMPLOYEE", "DEPARTMENT"}; // FROM 항목 리스트

        // 체크박스 목록과 옵션을 컴보박스에 추가
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (String option : fromOptions) {
            comboBoxModel.addElement(option);
        }

        // 컴보박스 생성
        fromComboBox = new JComboBox<>(comboBoxModel);
        fromPanel.add(fromComboBox);
        
        JPanel thisPanel= new JPanel(new GridLayout(6, 1));
        thisPanel.add(fromPanel);

  
        
        fromComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) fromComboBox.getSelectedItem(); 

                if ("EMPLOYEE".equals(selectedOption)) {
                    // "EMPLOYEE"를 선택한 경우, 사원 관련 필드를 추가

                	JPanel employeeWherePanel = new JPanel(); // SELECT 패널 추가

                	employeeWherePanel.setLayout(new GridLayout(1, 2));
                    JLabel columnsLabel = new JLabel("\t 검색범위");
                    String[] fromSearchOptions = {"Department", "Sex", "Salary", "Super_ssn", "Address", "Ssn"};
                    JComboBox<String> selectComboBox = new JComboBox<>(fromSearchOptions); // 드롭다운 목록 생성
                    
                    String[] checkboxOptions = {"Fname", "Minit", "Lname", "Ssn", "Bdate", "Address", "Sex", "Salary", "Super_ssn", "Dno"};

                    // 체크박스 목록과 옵션을 컴포넌트에 추가
                	fromCheckboxes = new ArrayList<>();
                	 for (String option : checkboxOptions) {
                         JCheckBox checkbox = new JCheckBox(option);
                         fromCheckboxes.add(checkbox);
                         employeeWherePanel.add(checkbox);

                         checkbox.addActionListener(new ActionListener() {
                             @Override
                             public void actionPerformed(ActionEvent e) {
                                 JCheckBox selectedCheckbox = (JCheckBox) e.getSource();
                                 String text = selectedCheckbox.getText();
                                 if (selectedCheckbox.isSelected()) {
                                	 selectedCheckboxes.add(text);
                                 } else {
                                	 selectedCheckboxes.remove(text);
                                 }
                             }
                         });
                     }
                 
                	
                    
                    columnsField = new JTextField(20);
                    employeeWherePanel.add(columnsLabel);
                    employeeWherePanel.add(selectComboBox);

                    selectComboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            whereOption = (String) selectComboBox.getSelectedItem();

                            // 이전 드롭다운 목록을 제거
                            thisPanel.removeAll();
                            thisPanel.add(fromPanel);
                            thisPanel.add(employeeWherePanel);
                            
                            // 추가 필드를 원하는 만큼 계속 추가

                            if ("Department".equals(whereOption)) {
                                // "부서"를 선택한 경우, 부서 드롭다운 목록 추가
                                JPanel departmentPanel = new JPanel();
                                departmentPanel.setLayout(new GridLayout(1, 2));
                                JLabel departmentLabel = new JLabel("부서 선택");
                                String[] departmentOptions = {"Administration", "Research", "Headquarters"}; // 적절한 부서 목록으로 변경
                                departmentComboBox = new JComboBox<>(departmentOptions);
                                departmentPanel.add(departmentLabel);
                                departmentPanel.add(departmentComboBox);
                                thisPanel.add(departmentPanel);

                          
                            
                            } else if ("Sex".equals(whereOption)) {
                                // "성별"을 선택한 경우, 성별 드롭다운 목록 추가
                                JPanel genderPanel = new JPanel();
                                genderPanel.setLayout(new GridLayout(1, 2));
                                JLabel genderLabel = new JLabel("성별 선택");
                                String[] genderOptions = {"M", "F"}; // 적절한 성별 목록으로 변경
                                genderComboBox = new JComboBox<>(genderOptions);
                                genderPanel.add(genderLabel);
                                genderPanel.add(genderComboBox);

                                // SELECT 패널 업데이트
                                thisPanel.add(genderPanel);
                            }
                            
                            else if ("Salary".equals(whereOption)) {
                                // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드 추가
                                JPanel salaryPanel = new JPanel();
                                salaryPanel.setLayout(new GridLayout(1, 2));
                                JLabel salaryLabel = new JLabel("연봉 입력");
                                salaryField = new JTextField(20); // 숫자를 입력받을 텍스트 필드
                                salaryPanel.add(salaryLabel);
                                salaryPanel.add(salaryField);

                                // SELECT 패널 업데이트
                                thisPanel.add(salaryPanel);
                            }
                            
                            else if ("Super_ssn".equals(whereOption)) {
                                // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드 추가
                                JPanel headPanel = new JPanel();
                                headPanel.setLayout(new GridLayout(1, 2));
                                JLabel headLabel = new JLabel("상사 입력");
                                headField = new JTextField(20); // 숫자를 입력받을 텍스트 필드
                                headPanel.add(headLabel); 
                                headPanel.add(headField);

                                // SELECT 패널 업데이트
                                thisPanel.add(headPanel);
                            }

                            
                            else if ("Address".equals(whereOption)) {
                                // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드 추가
                                JPanel addressPanel = new JPanel();
                                addressPanel.setLayout(new GridLayout(1, 2));
                                JLabel addressLabel = new JLabel("주소 입력");
                                addressField = new JTextField(20); // 숫자를 입력받을 텍스트 필드
                                addressPanel.add(addressLabel);
                                addressPanel.add(addressField);

                                // SELECT 패널 업데이트
                                thisPanel.add(addressPanel);
                            }
                            else if ("Ssn".equals(whereOption)) {
                                // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드 추가
                                JPanel ssnPanel = new JPanel();
                                ssnPanel.setLayout(new GridLayout(1, 2));
                                JLabel ssnLabel = new JLabel("이름 입력");
                                ssnField = new JTextField(20); // 숫자를 입력받을 텍스트 필드
                                ssnPanel.add(ssnLabel);
                                ssnPanel.add(ssnField);

                                // SELECT 패널 업데이트
                                thisPanel.add(ssnPanel);
                            }

                            
                            // 다시 그리기
                            thisPanel.revalidate();
                            thisPanel.repaint();
                        }
                    });
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

                    thisPanel.add(employeeWherePanel);

                    thisPanel.add(groupPanel);
                    thisPanel.add(havingPanel);
                    thisPanel.add(orderPanel);

                } else if ("DEPARTMENT".equals(selectedOption)) {

                	JPanel employeeWherePanel = new JPanel(); // SELECT 패널 추가

                	employeeWherePanel.setLayout(new GridLayout(1, 2));
                    JLabel columnsLabel = new JLabel("\t 검색범위");
                    String[] fromSearchOptions = { "Dname", "Mgr_ssn"};
                    JComboBox<String> selectComboBox = new JComboBox<>(fromSearchOptions); // 드롭다운 목록 생성
                    
                    String[] checkboxOptions = {"Dname", "Dnumber", "Mgr_ssn", "Mgr_start_date"};

                    // 체크박스 목록과 옵션을 컴포넌트에 추가
                	fromCheckboxes = new ArrayList<>();
                	 for (String option : checkboxOptions) {
                         JCheckBox checkbox = new JCheckBox(option);
                         fromCheckboxes.add(checkbox);
                         employeeWherePanel.add(checkbox);

                         checkbox.addActionListener(new ActionListener() {
                             @Override
                             public void actionPerformed(ActionEvent e) {
                                 JCheckBox selectedCheckbox = (JCheckBox) e.getSource();
                                 String text = selectedCheckbox.getText();
                                 if (selectedCheckbox.isSelected()) {
                                	 selectedCheckboxes.add(text);
                                 } else {
                                	 selectedCheckboxes.remove(text);
                                 }
                             }
                         });
                     }
                 
                	
                    
                    columnsField = new JTextField(20);
                    employeeWherePanel.add(columnsLabel);
                    employeeWherePanel.add(selectComboBox);

                    selectComboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	whereOption = (String) selectComboBox.getSelectedItem();

                            // 이전 드롭다운 목록을 제거
                            thisPanel.removeAll();
                            thisPanel.add(fromPanel);
                            thisPanel.add(employeeWherePanel);
                            
                            // 추가 필드를 원하는 만큼 계속 추가

                            if ("Dname".equals(whereOption)) {
                                // "부서"를 선택한 경우, 부서 드롭다운 목록 추가
                                JPanel dnamePanel = new JPanel();
                                dnamePanel.setLayout(new GridLayout(1, 2));
                                JLabel dnameLabel = new JLabel("부서 선택");
                                String[] dnameOptions = {"Administration", "Research", "Headquarters"}; // 적절한 부서 목록으로 변경
                                dnameComboBox = new JComboBox<>(dnameOptions);
                                dnamePanel.add(dnameLabel);
                                dnamePanel.add(dnameComboBox);

                                // SELECT 패널 업데이트
                                thisPanel.add(dnamePanel);
                                departmentComboBox.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        
                                    }
                                });
                            
                            }
                            
                              
                            
                            else if ("Mgr_ssn".equals(whereOption)) {
                                // "연봉"을 선택한 경우, 연봉을 입력받는 텍스트 필드 추가
                                JPanel Mgr_ssnPanel = new JPanel();
                                Mgr_ssnPanel.setLayout(new GridLayout(1, 2));
                                JLabel Mgr_ssnLabel = new JLabel("관리자 입력");
                                mgr_ssnField = new JTextField(20); // 숫자를 입력받을 텍스트 필드
                                Mgr_ssnPanel.add(Mgr_ssnLabel); 
                                Mgr_ssnPanel.add(mgr_ssnField);

                                // SELECT 패널 업데이트
                                thisPanel.add(Mgr_ssnPanel);
                            }

                            
                           
                            
                            // 다시 그리기
                            thisPanel.revalidate();
                            thisPanel.repaint();
                        }
                    });
                    
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

                    thisPanel.add(employeeWherePanel);

                    thisPanel.add(groupPanel);
                    thisPanel.add(havingPanel);
                    thisPanel.add(orderPanel);
                }

                

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
