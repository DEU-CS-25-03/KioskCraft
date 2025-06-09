package Controller;

import DataTransferObject.Entity;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * OrderStateControl 클래스
 * - 주문 상태를 관리하기 위한 데이터 모델과 테이블 모델을 제공
 */
public class OrderStateControl {
    /**
     * OrderTableModel 클래스
     * - AbstractTableModel을 상속하여 주문 리스트를 JTable에 표시
     * - 컬럼: 주문번호, 고객명, 상태
     */
    public static class OrderTableModel extends AbstractTableModel {
        private final String[] columns = { "주문번호", "고객명", "상태" };
        private final List<Entity.Order> orders;  // Order 객체 리스트

        public OrderTableModel(List<Entity.Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();  // 데이터 리스트 크기만큼 행 반환
        }

        @Override
        public int getColumnCount() {
            return columns.length; // 세 개의 컬럼
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Entity.Order order = orders.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> order.orderId;       // 0번 컬럼: 주문번호
                case 1 -> order.customerCode;  // 1번 컬럼: 고객명
                case 2 -> order.status;        // 2번 컬럼: 상태
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columns[column]; // 컬럼 이름 반환
        }
    }
}
