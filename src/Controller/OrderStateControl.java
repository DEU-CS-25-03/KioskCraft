package Controller;

import DataTransferObject.Order;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class OrderStateControl {
    public static class OrderTableModel extends AbstractTableModel {
        private final String[] columns = { "주문번호", "고객명", "상태" };
        private final List<Order> orders;

        public OrderTableModel(List<Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> order.orderId;
                case 1 -> order.customerCode;
                case 2 -> order.status;
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
    }
}
