import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AdminUI extends JFrame{
    public AdminUI(){
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(865, 675);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setLayout(null);

        //버튼 세팅(위치, 크기, 폰트) 이벤트는 추후 추가 예정
        JButton registMenuBtn = new JButton("메뉴 등록");
        registMenuBtn.setBounds(10, 10, 200, 50);
        registMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(registMenuBtn);

        JButton modifyMenuBtn = new JButton("메뉴 수정");
        modifyMenuBtn.setBounds(220, 10, 200, 50);
        modifyMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(modifyMenuBtn);

        JButton orderedCheckBtn = new JButton("매출 분석");
        orderedCheckBtn.setBounds(430, 10, 200, 50);
        orderedCheckBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(orderedCheckBtn);

        JButton setDefaultDesignBtn = new JButton("디자인 변경");
        setDefaultDesignBtn.setBounds(640, 10, 200, 50);
        setDefaultDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(setDefaultDesignBtn);

        JButton registCategoryBtn = new JButton("카테고리 등록");
        registCategoryBtn.setBounds(10, 580, 270, 50);
        registCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(registCategoryBtn);

        JButton modifyCategoryBtn = new JButton("카테고리 수정");
        modifyCategoryBtn.setBounds(290, 580, 270, 50);
        modifyCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(modifyCategoryBtn);

        JButton deleteCategoryBtn = new JButton("카테고리 삭제");
        deleteCategoryBtn.setBounds(570, 580, 270, 50);
        deleteCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(deleteCategoryBtn);
        
        //테이블 선언
        JTable table = getJTable();

        //폰트 지정
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        // 버튼 렌더러, 에디터 추가
        table.getColumn("카테고리").setPreferredWidth(150);
        table.getColumn("메뉴").setPreferredWidth(290);
        table.getColumn("가격").setPreferredWidth(30);
        table.getColumn("").setPreferredWidth(30);
        table.getColumn("").setCellRenderer(new ButtonRenderer());
        table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));

        //스크롤 추가
        JScrollPane scrollPane = new JScrollPane(table);

        //행 두께 지정
        table.setRowHeight(35);

        //위치&크기 지정
        scrollPane.setBounds(10, 70, 830, 500);

        //행 선택 비활성화
        table.setRowSelectionAllowed(false);

        add(scrollPane);

        //모니터 중앙에 위치
        setLocationRelativeTo(null);
        
        //크기 변경 기능 해제
        setResizable(false);
    }

    //테이블 데이터 설정
    private static JTable getJTable() {
        Object[][] rowData = {
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"},
                {"커피", "아메리카노", "4,000원", "X"},
                {"커피", "카푸치노", "6,000원", "X"},
                {"논커피", "딸기라떼", "7,000원", "X"}
        };
        String[] columnNames = {"카테고리", "메뉴", "가격", ""};

        // 테이블 모델 생성
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            // 마지막 열만 버튼 표시
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // 4번째 열(작업)만 수정 가능(=버튼 클릭 가능)
            }
        };
        return new JTable(model);
    }

    // JTable 셀에 버튼을 보이게 하는 스윙 내부 렌더러 클래스
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true); // 배경 불투명(색상 변경 등 가능)
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X"); // 버튼에 표시될 텍스트
            return this;  // 버튼 자체를 반환 (각 셀마다 보임)
        }
    }

    // JTable 셀을 실제로 "버튼"으로 편집(클릭) 가능하게 하는 스윙 내부 에디터 클래스
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button; // 실제로 눌릴 삭제 버튼
        private int selectedRow;  // 현재 클릭된 행 번호

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox); // DefaultCellEditor는 JCheckBox 필요(안 써도 됨)
            button = new JButton();
            button.setOpaque(true); // 배경 불투명
            // 버튼 클릭 이벤트 리스너
            button.addActionListener(_ -> {
                fireEditingStopped(); // 에디터 종료(포커스 반환)
                // 버튼 클릭 시 실행할 코드 (여기선 메시지 박스)
                JOptionPane.showMessageDialog(button, (selectedRow+1) + "버튼 이벤트");
            });
        }

        // 셀이 편집(클릭)될 때 호출됨
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("X");   // 버튼에 표시될 텍스트
            selectedRow = row;     // 현재 행 번호 저장
            return button;         // 버튼 반환 (셀에 표시됨)
        }

        // 편집 종료 후 값 반환 (여기선 따로 의미 없음)
        @Override
        public Object getCellEditorValue() {
            return null;
        }

        // 편집(클릭) 중단 시
        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }
}

