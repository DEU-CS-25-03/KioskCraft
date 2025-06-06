package Control;

import Boundary.MenuCardPanel;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class KioskControl {
    /**
     * 카테고리에 해당하는 메뉴 카드만 보여주기
     */
    public static void showMenuByCategory(String category, JPanel gridPanel, DefaultTableModel cartModel) {
        gridPanel.removeAll();
        for (Object[] item : Entity.menus) {
            String cat = (String) item[0];
            String name = (String) item[1];
            String priceStr = (String) item[2];
            boolean soldOut = (boolean) item[3];

            if (!cat.equals(category)) continue;

            String imagePath = "images/" + name + ".png";
            boolean imageExists = new File(imagePath).exists();
            if (!imageExists) imagePath = "images/default.png";

            MenuCardPanel card = new MenuCardPanel(name, priceStr, imagePath, !imageExists, soldOut, () -> addToCart(name, priceStr, cartModel));
            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * 장바구니에 메뉴 추가 (Entity.cartList와 테이블 모두 업데이트)
     * → Entity.cartList에 {name, unitPrice, quantity, total} 형태로 저장
     */
    public static void addToCart(String name, String priceStr, DefaultTableModel cartModel) {
        // priceStr 예시: "3,000원" → 정수 단가로 파싱
        int unitPrice = Integer.parseInt(priceStr.replace(",", "").replace("원", ""));

        boolean found = false;
        // 1) cartList에 이미 있는지 확인 → 수량만 +1하고 총액 갱신
        for (Object[] cartItem : Entity.cartList) {
            String existingName = (String) cartItem[0];
            if (existingName.equals(name)) {
                // 수량 증가
                int prevQty = (int) cartItem[2];
                int newQty  = prevQty + 1;
                cartItem[2] = newQty;                       // quantity 항목만 갱신
                cartItem[3] = unitPrice * newQty;           // total = 단가 * 수량
                found = true;
                break;
            }
        }

        if (!found) {
            // 2) 새로 추가: {name, unitPrice, 1, unitPrice*1}
            Object[] newItem = new Object[]{name, unitPrice, 1, unitPrice};
            Entity.cartList.add(newItem);
        }

        // 3) 테이블 모델 갱신
        int rowCount = cartModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String existingName = (String) cartModel.getValueAt(i, 0);
            if (existingName.equals(name)) {
                // 테이블에서도 단가·수량·총액만 갱신
                for (Object[] cartItem : Entity.cartList) {
                    if (cartItem[0].equals(name)) {
                        int updatedQty   = (int) cartItem[2];
                        int updatedTotal = (int) cartItem[3];
                        cartModel.setValueAt(updatedQty,   i, 2); // 수량(컬럼 인덱스 2)
                        cartModel.setValueAt(updatedTotal, i, 3); // 총액(컬럼 인덱스 3)
                        break;
                    }
                }
                return;
            }
        }

        // 4) 테이블에 새 행 추가 시에도 “총액”을 포함
        Vector<Object> row = new Vector<>();
        row.add(name);             // 메뉴명
        row.add(unitPrice);        // 단가
        row.add(1);                // 수량
        row.add(unitPrice);        // 총액(단가*수량)
        row.add("-");              // 수량 감소 버튼
        row.add("x");              // 행 삭제 버튼
        cartModel.addRow(row);
    }

    /**
     * 메뉴명 → 단가(정수) 조회
     */
    private static int getUnitPriceByName(String name) {
        for (Object[] item : Entity.menus) {
            if (item[1].equals(name)) {
                return Integer.parseInt(((String) item[2]).replace(",", "").replace("원", ""));
            }
        }
        return 0;
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final boolean isDecrease;
        private int editingRow;  // 클릭된 행 번호

        public ButtonEditor(JCheckBox checkBox, String label, boolean isDecrease, DefaultTableModel cartModel) {
            super(checkBox);
            this.isDecrease = isDecrease;
            button = new JButton(label);
            button.addActionListener(_ -> handleClick(cartModel));
        }

        private void handleClick(DefaultTableModel cartModel) {
            String name = (String) cartModel.getValueAt(editingRow, 0);
            int unitPrice = getUnitPriceByName(name);

            if (isDecrease) {
                // 수량 감소
                for (int i = 0; i < Entity.cartList.size(); i++) {
                    Object[] cartItem = Entity.cartList.get(i);
                    if (cartItem[0].equals(name)) {
                        int prevQty = (int) cartItem[2];
                        if (prevQty > 1) {
                            int newQty = prevQty - 1;
                            cartItem[2] = newQty;               // 수량 갱신
                            cartItem[3] = unitPrice * newQty;   // 총액 갱신
                            // 테이블 행 업데이트 (수량=2번, 총액=3번)
                            cartModel.setValueAt(newQty,             editingRow, 2);
                            cartModel.setValueAt(unitPrice * newQty, editingRow, 3);
                        } else {
                            // 수량이 1일 때 ↓ 삭제
                            Entity.cartList.remove(i);
                            cartModel.removeRow(editingRow);
                        }
                        break;
                    }
                }
            } else {
                // 행 삭제(x 버튼)
                for (int i = 0; i < Entity.cartList.size(); i++) {
                    Object[] cartItem = Entity.cartList.get(i);
                    if (cartItem[0].equals(name)) {
                        Entity.cartList.remove(i);
                        cartModel.removeRow(editingRow);
                        break;
                    }
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.editingRow = row;
            return button;
        }
    }
}
