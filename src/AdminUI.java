import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminUI extends JFrame{
    private long keyPressedTime = 0L;
    private final int TRIGGER_KEY = KeyEvent.VK_F10; // 원하는 키 지정
    private Timer holdTimer;
    public AdminUI(){
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(865, 675);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setLocationRelativeTo(null);
        setFocusable(true);
        setResizable(false);
        setLayout(null);

        //테이블 선언 + 모델 따로 분리해서 받음
        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);

        //폰트 지정
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        //열 크기 지정, 취소 버튼 렌더링
        table.getColumn("카테고리").setPreferredWidth(150);
        table.getColumn("메뉴").setPreferredWidth(260);
        table.getColumn("가격").setPreferredWidth(30);
        table.getColumn("품절여부").setPreferredWidth(30);
        table.getColumn("").setPreferredWidth(30);
        table.getColumn("").setCellRenderer(new ButtonRenderer());
        table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox(), model));

        //스크롤 추가
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        JButton registMenuBtn = new JButton("메뉴 등록");
        registMenuBtn.setBounds(10, 10, 200, 50);
        registMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registMenuBtn.addActionListener(_ -> new RegistMenuUI(this, "메뉴 등록", true).setVisible(true));
        add(registMenuBtn);

        JButton modifyMenuBtn = new JButton("메뉴 수정");
        modifyMenuBtn.setBounds(220, 10, 200, 50);
        modifyMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyMenuBtn.addActionListener(_ -> new MenuControlUI(false, table, this, "메뉴 수정", true).setVisible(true));
        add(modifyMenuBtn);

        JButton orderedCheckBtn = new JButton("매출 분석");
        orderedCheckBtn.setBounds(430, 10, 200, 50);
        orderedCheckBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        orderedCheckBtn.addActionListener(_ -> new DesignUI(this, "매출 분석", true).setVisible(true));
        add(orderedCheckBtn);

        JButton setDefaultDesignBtn = new JButton("디자인 변경");
        setDefaultDesignBtn.setBounds(640, 10, 200, 50);
        setDefaultDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setDefaultDesignBtn.addActionListener(_ -> new DesignUI(this, "디자인 변경", true).setVisible(true));
        add(setDefaultDesignBtn);

        JButton registCategoryBtn = new JButton("카테고리 등록");
        registCategoryBtn.setBounds(10, 580, 200, 50);
        registCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registCategoryBtn.addActionListener(_ -> new RegistCategoryUI(this, "카테고리 등록", true).setVisible(true));
        add(registCategoryBtn);

        JButton modifyCategoryBtn = new JButton("카테고리 수정");
        modifyCategoryBtn.setBounds(220, 580, 200, 50);
        modifyCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyCategoryBtn.addActionListener(_ -> new CategoryControlUI(this, "카테고리 수정", true).setVisible(true));
        add(modifyCategoryBtn);

        JButton deleteCategoryBtn = new JButton("카테고리 삭제");
        deleteCategoryBtn.setBounds(430, 580, 200, 50);
        deleteCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        deleteCategoryBtn.addActionListener(_ -> new CategoryControlUI(this, "카테고리 삭제", true).setVisible(true));
        add(deleteCategoryBtn);

        JButton showOrderedListBtn = new JButton("주문현황 확인");
        showOrderedListBtn.setBounds(640, 580, 200, 50);
        showOrderedListBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        showOrderedListBtn.addActionListener(_ -> new DesignUI(this, "주문현황", true).setVisible(true));
        add(showOrderedListBtn);
        // -------------- 여기에 키 리스너 추가 --------------
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY && keyPressedTime == 0L) {
                    keyPressedTime = System.currentTimeMillis();
                    //테스트 때문에 딜레이 100ms로 줄여놓음 나중에 3000ms으로 변경 예정
                    holdTimer = new Timer(100, _ -> {
                        if ((System.currentTimeMillis() - keyPressedTime) >= 100) {
                            openOrderTypeSelectionUI();
                        }
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY) {
                    keyPressedTime = 0L;
                    if (holdTimer != null) holdTimer.stop();
                }
            }

            private void openOrderTypeSelectionUI() {
                new OrderTypeSelectionUI().setVisible(true);
                dispose();
            }
        });
        // -------------- 키 리스너 끝 --------------
    }

    // 테이블 모델을 반환 (버튼 에디터에 넘길 목적)
    public static DefaultTableModel getTableModel() {
        Object[][] rowData = new DataSet().Menus;
        String[] columnNames = {"카테고리", "메뉴", "가격", "품절여부", ""};

        // 테이블 모델 생성
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // 4번째 열(작업)만 수정 가능(=버튼 클릭 가능)
            }
        };
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

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(_ -> {
                // 선택 메뉴 행 삭제
                int rowToDelete = selectedRow; // 반드시 사본으로 저장 selectedRow 바로 사용 시 마지막 행 삭제 시 에러남
                fireEditingStopped(); // 먼저 편집 종료(이벤트 루프에서 나감)
                SwingUtilities.invokeLater(() -> {
                    if(rowToDelete >= 0 && rowToDelete < model.getRowCount()) {
                        model.removeRow(rowToDelete); // 그 다음에 행 삭제
                        JOptionPane.showMessageDialog(
                        null, "메뉴가 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                });
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
