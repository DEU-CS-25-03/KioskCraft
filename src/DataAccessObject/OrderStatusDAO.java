package DataAccessObject;
import java.sql.*;
import java.util.*;
//
public class OrderStatusDAO {
    public OrderStatusDAO(Connection connection) {

    }
//    public List<Order> getAllOrders() {
//        List<Order> orders = new ArrayList<>();
//        String sql = "SELECT order_id, customer_name, status, order_time FROM orders";
//        try (Connection conn = DataAccessObject.DBManager.getInstance().getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//            while (rs.next()) {
//                String orderId = rs.getString("order_id");
//                String customerName = rs.getString("customer_name");
//                Order.Status status = Order.Status.valueOf(rs.getString("status"));
//                long orderTime = rs.getLong("order_time");
//                orders.add(new Order(orderId, customerName, status, orderTime));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
//    public boolean insertOrder(Order order) {
//        String sql = "INSERT INTO orders (order_id, customer_name, status, order_time) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DataAccessObject.DBManager.getInstance().getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, order.getOrderId());
//            pstmt.setString(2, order.getCustomerName());
//            pstmt.setString(3, order.getStatus().name());
//            pstmt.setLong(4, order.getOrderTime());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean updateOrderStatus(String orderId, Order.Status status) {
//        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
//        try (Connection conn = DataAccessObject.DBManager.getInstance().getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, status.name());
//            pstmt.setString(2, orderId);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean deleteOrder(String orderId) {
//        String sql = "DELETE FROM orders WHERE order_id = ?";
//        try (Connection conn = DataAccessObject.DBManager.getInstance().getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, orderId);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
