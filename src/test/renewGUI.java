package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class renewGUI extends JPanel {
    private List<JCheckBox> fromCheckboxes; // 추가: FROM 체크박스 리스트
    private JTextField columnsField;
    private JCheckBox whereCheckBox;
    private JTextField whereField;
    private JCheckBox groupCheckBox;
    private JTextField groupField;
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
    public JButton thisBtn;
    public renewGUI() {
        opsel="search";
        JPanel OpSelPanel = new JPanel(); // FROM 패널 추가
        OpSelPanel.setLayout(new GridLayout(1, 6));
        JLabel OpSelLabel = new JLabel("\t FROM");
        OpSelPanel.add(OpSelLabel);

        optionbtns = new ButtonGroup();
        String[] Options = {"report", "search", "insert", "delete"}; // FROM 항목 리스트
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

        thisBtn=executeButton;
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
                    thisBtn=executeButton;
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


        inputPanel.add(thisBtn, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        
        setLayout(new BorderLayout());

        add(OpSelPanel,BorderLayout.NORTH);
        add(get_report(),BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        

        
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:mysql://localhost/company";
                String user = "root";
                String password = "0000";

                DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);

                String columns = columnsField.getText();
                String query = "SELECT " + columns;

                // 추가: 선택한 FROM 항목을 문자열로 추가
                List<String> selectedFromOptions = new ArrayList<>();
                for (JCheckBox checkbox : fromCheckboxes) {
                    if (checkbox.isSelected()) {
                        selectedFromOptions.add(checkbox.getText());
                    }
                }
                if (!selectedFromOptions.isEmpty()) {
                    String fromClause = " FROM " + String.join(", ", selectedFromOptions);
                    query += fromClause;
                }

                String condition = whereField.getText();
                if (whereCheckBox.isSelected()) {
                    query += " WHERE " + condition;
                }
                String groupBy = groupField.getText();
                if (groupCheckBox.isSelected()) {
                    query += " GROUP BY " + groupBy;
                }
                String orderBy = orderField.getText();
                if (orderCheckBox.isSelected()) {
                    query += " ORDER BY " + orderBy;
                }
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
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String values = insertField.getText();
                // 여기에 튜플 삽입 로직 추가
                // 예: INSERT INTO 테이블명 (컬럼1, 컬럼2, ...) VALUES (값1, 값2, ...)

                // 삽입 후 결과 표시
                resultArea.setText("튜플이 삽입되었습니다.");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String url = "jdbc:mysql://localhost/company";
                String user = "root";
                String password = "0000";
                DatabaseConnection dbConnection = new DatabaseConnection(url, user, password);

                // 추가: 선택한 FROM 항목을 문자열로 추가
                /* delete에서는 FROM에 table이 하나만 작성되어야 함. 
                따라서 첫번째 인텍스만 받고 나머지 무시 */
                String query = "DELETE";
                ArrayList<String> selectedFromOptions =new ArrayList<>();
                for (JCheckBox checkbox : fromCheckboxes) {
                    if (checkbox.isSelected()) {
                        selectedFromOptions.add(checkbox.getText());
                    }
                }
                System.out.println(selectedFromOptions.size());
                if ((!selectedFromOptions.isEmpty())&&(selectedFromOptions.size()<=1)) {
                    String fromClause = " FROM " + String.join(", ", selectedFromOptions);
                    query += fromClause;
                   
                    String condition = whereField.getText();
                    if (whereCheckBox.isSelected()) {
                        query += " WHERE " + condition;
                    }
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
                    // 여기에 튜플 삭제 로직 추가
                    // 예: DELETE FROM 테이블명 WHERE 조건

                    // 삭제 후 결과 표시
                    
                } else{
                    resultArea.setText("무조건 하나의 FROM 선택을 해야합니다.");
                }
            }
        });

    }
    private JPanel get_report(){
        
        

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

        JPanel orderPanel = new JPanel(); // ORDER BY 패널 추가
        orderPanel.setLayout(new GridLayout(1, 2));
        orderCheckBox = new JCheckBox("Use ORDER BY");
        orderField = new JTextField(0);
        orderPanel.add(orderCheckBox);
        orderPanel.add(orderField);

        JPanel thisPanel= new JPanel(new GridLayout(5, 1));
        
        thisPanel.add(selectPanel);
        thisPanel.add(fromPanel);
        thisPanel.add(wherePanel);
        thisPanel.add(groupPanel);
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

        return thisPanel;
    }
    private JPanel get_search(){
        JPanel thisPanel= new JPanel();
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
        whereCheckBox = new JCheckBox("Use WHERE (use AND)");
        whereField = new JTextField(0);
        wherePanel.add(whereCheckBox);
        wherePanel.add(whereField);


        JPanel thisPanel= new JPanel(new GridLayout(5, 1));
    

        thisPanel.add(fromPanel);
        thisPanel.add(wherePanel);

        whereCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whereField.setEnabled(whereCheckBox.isSelected());
            }
        });

        return thisPanel;
    }
    private JPanel get_insert(){
        JPanel thisPanel= new JPanel();
        return thisPanel;
    }


}
