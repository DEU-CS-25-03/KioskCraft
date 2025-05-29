import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class AdminUI extends JFrame {
    // 홈으로 화면 전환할 때 쓰는 디스패쳐
    private KioskUIUtils.KeyHoldActionDispatcher keyDispatcher;
    public AdminUI() {
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(865, 675);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // 테이블 모델 생성 및 테이블 초기화
        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.getColumn("카테고리").setPreferredWidth(150);
        table.getColumn("메뉴").setPreferredWidth(260);
        table.getColumn("가격").setPreferredWidth(30);
        table.getColumn("품절여부").setPreferredWidth(30);
        table.getColumn("").setPreferredWidth(30);
        table.getColumn("").setCellRenderer(new ButtonRenderer());
        table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox(), model));

        // 테이블 스크롤 패널 추가 및 세부 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 관리 버튼(메뉴 등록/수정 등) 추가
        addAdminButtons(table);

        // 창 닫힐 때 키 디스패처 해제 보장
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                unregisterKeyDispatcher();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                unregisterKeyDispatcher();
            }
        });
    }

    // 창이 열리거나 닫힐 때 키 디스패쳐 등록 및 해제
    @Override
    public void setVisible(boolean b) {
        if (b) {
            // 등록
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(
                    KeyEvent.VK_F10, // 트리거 키: F10
                    100,             // 100ms 이상 누르면 동작
                    () -> {          // 콜백: 홈 화면으로 이동
                        new OrderTypeSelectionUI().setVisible(true);
                        dispose();
                    }
            );
            keyDispatcher.register();
        } else {
            // 해제
            unregisterKeyDispatcher();
        }
        super.setVisible(b);
    }

    // 디스패쳐 해제 함수
    private void unregisterKeyDispatcher() {
        if (keyDispatcher != null) {
            keyDispatcher.unregister();
            keyDispatcher = null;
        }
    }

    // AdminUI에 들어가는 버튼들 관리하기 쉽게 따로 모아둠
    private void addAdminButtons(JTable table) {
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
//주문현황 DisignUI로 연결 수정
        JButton showOrderedListBtn = new JButton("주문현황 확인");
        showOrderedListBtn.setBounds(640, 580, 200, 50);
        showOrderedListBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        showOrderedListBtn.addActionListener(_ -> new OrderStatusUI(this, "주문현황", true).setVisible(true));
        add(showOrderedListBtn);
    }

    /*
       관리 테이블에 들어갈 모델 생성
     - 5개 컬럼(카테고리, 메뉴, 가격, 품절여부, X버튼)
     - 메뉴 삭제버튼(4번째 컬럼)만 수정 가능
     */
    public static DefaultTableModel getTableModel() {
        Object[][] rowData = new DataSet().menus;
        String[] columnNames = {"카테고리", "메뉴", "가격", "품절여부", ""};
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
    }

    /*
     테이블 마지막 컬럼에 메뉴 삭제버튼을 보여주기 위한 렌더러
     버튼에 항상 X 텍스트를 표시
    */
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    /*
      메뉴 삭제버튼 클릭 시 해당 행(메뉴)을 삭제하는 에디터
      삭제 후 알림 메시지 출력
    */
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(_ -> {
                int rowToDelete = selectedRow;
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    if (rowToDelete >= 0 && rowToDelete < model.getRowCount()) {
                        model.removeRow(rowToDelete);
                        JOptionPane.showMessageDialog(null, "메뉴가 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("X");
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
