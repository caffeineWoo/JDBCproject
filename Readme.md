Delete 추가 버전
===============
Option
--------
- delete도 from, where가 필요함
    - [ ] 기존꺼 밑에 동일한 방식으로 패널추가 </br>
   [쉬움, gui 못생겨짐]
    - [x] select와 패널 공유 </br>
   [중간정도, group by, orber by 무시] </br>
   delete는 여러 table을 한번에 못하므로, 
       - [ ] 1개만 선택함을 가정
       - [ ] 맨 처음 요소만 사용
       - [x] 무조건 하나만 선택해야 동작
    - [ ] 동작을 선택하는 checkbox 만들어서 선택할 때마다 유동적으로 패널 올림 </br>
   [될지 모르겠음, gui 깔끔해짐]

- on cascade </br> 
    구현 안하는 것이 좋을 것 같음. employee만 본다면 구현안해도 됨.

변경된 부분
--------
#### DatabaseConnection.java ####
`DatabaseConnection.executeUpdate(String)` </br> 함수 추가. update, delete 시에 사용

#### MainPanel.java #### 
`deleteButton.addActionListener(ActionListener())` </br>
기존 `FROM`, `WHERE`의 값을 읽어 sql요청처리. </br> 
`FROM`에 1개초과, 미만의 테이블이 선택되면 작동 안하게 설정
