package Controller;

import Boundary.MenuCardPanel;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.Vector;

/**
 * KioskControl 클래스
 * - Kiosk UI에서 카테고리별 메뉴를 표시하고, 장바구니 기능을 관리
 */
public class KioskControl {

    /**
     * showMenuByCategory 메서드
     * - 지정된 카테고리의 메뉴만 그리드 패널에 카드 형태로 표시
     * @param category   선택된 카테고리명
     * @param gridPanel  메뉴 카드를 추가할 JPanel (GridLayout)
     * @param cartModel  장바구니 JTable의 DefaultTableModel
     */
    public static void showMenuByCategory(String category, JPanel gridPanel, DefaultTableModel cartModel) {
        gridPanel.removeAll(); // 이전 카드를 모두 제거
        for (Object[] item : Entity.menus) {
            String cat = (String) item[0];       // 메뉴의 카테고리
            String name = (String) item[1];      // 메뉴명
            String priceStr = (String) item[2];  // 가격 문자열(예: "3,000원")
            boolean soldOut = (boolean) item[3]; // 품절 여부
            String imgPath = (String) item[4]; // 이미지 경로

            if (!cat.equals(category)) continue; // 카테고리가 다르면 건너뜀

            // 이미지 경로 설정: 해당 이름의 PNG 파일이 없으면 기본 이미지 사용
            String imagePath = imgPath;
            boolean imageExists = new File(imagePath).exists();
            if (!imageExists) imagePath = "img/default.png";

            // MenuCardPanel 생성: 클릭 시 addToCart 실행
            MenuCardPanel card = new MenuCardPanel(name, priceStr, imagePath, !imageExists, soldOut, () -> addToCart(name, priceStr, cartModel));
            gridPanel.add(card);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * addToCart 메서드
     * - 장바구니에 메뉴를 추가하거나, 이미 존재하면 수량/총액을 업데이트
     * - Entity.cartList와 테이블 모델을 함께 갱신
     * @param name       추가할 메뉴명
     * @param priceStr   "3,000원" 형태의 가격 문자열
     * @param cartModel  장바구니 JTable의 DefaultTableModel
     */
    public static void addToCart(String name, String priceStr, DefaultTableModel cartModel) {
        int unitPrice = Integer.parseInt(priceStr.replace(",", "").replace("원", "")); // 정수 단가 파싱

        boolean found = false;
        // 1) Entity.cartList에서 동일 메뉴가 있는지 확인
        for (Object[] cartItem : Entity.cartList) {
            String existingName = (String) cartItem[0];
            if (existingName.equals(name)) {
                int prevQty = (int) cartItem[2];       // 이전 수량
                int newQty  = prevQty + 1;             // 수량 +1
                cartItem[2] = newQty;                  // quantity 업데이트
                cartItem[3] = unitPrice * newQty;      // total = 단가 * 수량
                found = true;
                break;
            }
        }
        if (!found) {
            // 2) 없으면 새로 추가: {name, unitPrice, 1, unitPrice}
            Object[] newItem = new Object[]{ name, unitPrice, 1, unitPrice };
            Entity.cartList.add(newItem);
        }

        // 3) 테이블 모델 갱신
        int rowCount = cartModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String existingName = (String) cartModel.getValueAt(i, 0);
            if (existingName.equals(name)) {
                // 이미 테이블에 있으면 수량(2번 인덱스), 총액(3번 인덱스)만 업데이트
                for (Object[] cartItem : Entity.cartList) {
                    if (cartItem[0].equals(name)) {
                        int updatedQty = (int) cartItem[2];
                        int updatedTotal = (int) cartItem[3];
                        cartModel.setValueAt(updatedQty, i, 2);
                        cartModel.setValueAt(updatedTotal, i, 3);
                        break;
                    }
                }
                return; // 갱신 후 종료
            }
        }

        // 4) 테이블에 새 행 추가: {메뉴명, 단가, 1, 단가, "-", "x"}
        Vector<Object> row = new Vector<>();
        row.add(name);
        row.add(unitPrice);
        row.add(1);
        row.add(unitPrice);
        row.add("-");
        row.add("x");
        cartModel.addRow(row);
    }

    /**
     * getUnitPriceByName 메서드
     * - 메뉴명으로 Entity.menus에서 단가를 조회
     * @param name  조회할 메뉴명
     * @return 단가(정수), 없으면 0 반환
     */
    private static int getUnitPriceByName(String name) {
        for (Object[] item : Entity.menus) {
            if (item[1].equals(name)) {
                return Integer.parseInt(((String) item[2]).replace(",", "").replace("원", ""));
            }
        }
        return 0;
    }

    /**
     * ButtonRenderer 클래스
     * - 장바구니 테이블의 수량 감소("-") 및 삭제("x") 버튼 렌더링
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /**
     * ButtonEditor 클래스
     * - 장바구니 테이블에서 수량 감소와 삭제 기능 수행
     * - isDecrease=true이면 수량 감소, false이면 행 삭제
     */
    public static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final boolean isDecrease;
        private int editingRow;

        /**
         * 생성자
         * @param checkBox    DefaultCellEditor 생성용
         * @param label       버튼에 표시할 텍스트 ("-" or "x")
         * @param isDecrease  true: 수량 감소, false: 행 삭제
         * @param cartModel   장바구니 테이블 모델 (업데이트 시 사용)
         */
        public ButtonEditor(JCheckBox checkBox, String label, boolean isDecrease, DefaultTableModel cartModel) {
            super(checkBox);
            this.isDecrease = isDecrease;
            button = new JButton(label);
            button.addActionListener(_ -> handleClick(cartModel));
        }

        /**
         * 버튼 클릭 시 처리
         * @param cartModel 장바구니 테이블 모델
         */
        private void handleClick(DefaultTableModel cartModel) {
            String name = (String) cartModel.getValueAt(editingRow, 0);
            int unitPrice = getUnitPriceByName(name);

            if (isDecrease) {
                // 수량 감소 로직
                for (int i = 0; i < Entity.cartList.size(); i++) {
                    Object[] cartItem = Entity.cartList.get(i);
                    if (cartItem[0].equals(name)) {
                        int prevQty = (int) cartItem[2];
                        if (prevQty > 1) {
                            int newQty = prevQty - 1;
                            cartItem[2] = newQty;                    // quantity 업데이트
                            cartItem[3] = unitPrice * newQty;        // total 업데이트
                            cartModel.setValueAt(newQty, editingRow, 2);
                            cartModel.setValueAt(unitPrice * newQty, editingRow, 3);
                        } else {
                            // 수량이 1일 때 해당 행 삭제
                            Entity.cartList.remove(i);
                            cartModel.removeRow(editingRow);
                        }
                        break;
                    }
                }
            } else {
                // 행 삭제 로직 (삭제 버튼 클릭 시)
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
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.editingRow = row; // 현재 편집 중인 행 인덱스 저장
            return button;
        }
    }
}
