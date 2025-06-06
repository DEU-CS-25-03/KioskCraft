package Control;

import Boundary.OrderTypeSelectionUI;
import DataTransferObject.Entity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * AdminControl 클래스
 * - Entity.menus를 바탕으로 테이블 모델 생성
 * - 각 행의 마지막 컬럼에 삭제 버튼 제공
 */
public class AdminControl extends JFrame {

    public static DefaultTableModel getTableModel() {
        String[] columnNames = { "카테고리", "메뉴명", "가격", "품절여부", "" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 4; // 마지막 컬럼(삭제 버튼)만 편집 가능
            }
        };
        for (Object[] row : Entity.menus) {
            Object[] rowData = Arrays.copyOf(row, row.length + 1);
            rowData[rowData.length - 1] = ""; // 빈 값으로 버튼 자리 확보
            model.addRow(rowData);
        }
        return model;
    }

    /**
     * ButtonRenderer 클래스
     * - 테이블의 마지막 컬럼에 'X' 텍스트 버튼 렌더링
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    /**
     * ButtonEditor 클래스
     * - 삭제 버튼 클릭 시 Entity.menus와 모델에서 해당 행 제거
     */
    public static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
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
                        Entity.menus.remove(rowToDelete);
                        model.removeRow(rowToDelete);
                        JOptionPane.showMessageDialog(null, "메뉴가 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            });
        }

        @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("X");
            selectedRow = row;
            return button;
        }

        @Override public Object getCellEditorValue() {
            return null;
        }
    }

    /**
     * 프레임 가시성 설정 시 F10 키 입력 감지 디스패처 등록
     * - F10을 누르면 OrderTypeSelectionUI로 전환
     */
    @Override public void setVisible(boolean b) {
        if (b) {
            KioskUIUtils.KeyHoldActionDispatcher dispatcher = new KioskUIUtils.KeyHoldActionDispatcher(
                    KeyEvent.VK_F10, 100, () -> {
                new OrderTypeSelectionUI().setVisible(true);
                dispose();
            }
            );
            dispatcher.register();
        }
        super.setVisible(b);
    }
}
