import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class OrderPanel extends JPanel {
    private final JTable orderTable;
    private final JLabel totalPriceLabel;
    private final DefaultTableModel tableModel;
    private final DataModel dataModel;
    private final Map<String, Integer> selectedItems;


    public OrderPanel(DataModel dataModel, Map<String, Integer> selectedItems) {
        //전달받은 데이터 모델 및 선택된 아이템 맵 저장
        this.dataModel = dataModel;
        this.selectedItems = selectedItems;

        //레이아웃 기본설정
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

        //테이블 모델 생성 컬럼명 ["메뉴","수량","금액"]
        tableModel = new DefaultTableModel(new String[]{"메뉴", "수량", "금액"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // 모든 셀 편집 불가 처리
                return false;
            }
        };

        //모델 생성 및 기본설정
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(30);
        orderTable.setShowGrid(true);
        orderTable.setGridColor(new Color(220, 220, 220));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        //스크롤 설정
        JScrollPane tableScroll = new JScrollPane(orderTable);
        add(tableScroll, BorderLayout.CENTER);

        //총 가격 레이블 생성 및 기본설정
        totalPriceLabel = new JLabel("총 가격: 0 원", JLabel.RIGHT);
        totalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(totalPriceLabel, BorderLayout.SOUTH);

        //더블클릭 이벤트: 수량 차감 또는 항목 제거 처리
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //클릭 수가 2번일 때만 동작
                if (e.getClickCount() == 2) {
                    int row = orderTable.getSelectedRow();
                    if (row >= 0) {
                        //선택된 행의 메뉴명 가져오기
                        String menu = (String) orderTable.getValueAt(row, 0);
                        //현재 맵에 저장된 수량 조회
                        int currentQuantity = selectedItems.getOrDefault(menu, 0);
                        if (currentQuantity > 1) {
                            //수량 1초과 시 1 감소
                            selectedItems.put(menu, currentQuantity - 1);
                        } else {
                            //수량 1이거나 없으면 맵에서 제거
                            selectedItems.remove(menu);
                        }
                        //테이블 및 총 가격 갱신
                        updateOrderTable();
                    }
                }
            }
        });
    }


    // 주문 내역 테이블을 새로 고침하는 메서드
    public void updateOrderTable() {
        //기존 테이블 모델의 모든 행 삭제
        tableModel.setRowCount(0);

        //총합 변수 초기화
        int total = 0;

        //선택된 아이템 맵을 순회하며 테이블에 각 항목 추가
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            //선택된 메뉴 이름, 수량, 총 가격 설정
            String menu = entry.getKey();
            int quantity = entry.getValue();
            int price = dataModel.getPriceData().get(menu);
            int sum = price * quantity;

            //테이블 모델에 새 행 추가(메뉴 이름, 수량, 총 가격)
            tableModel.addRow(new Object[]{
                    menu,
                    quantity,
                    String.format("%,d 원", sum)
            });

            //전체 합계에 더하기
            total += sum;
        }

        //총 가격 레이블에 전체 합계 반영 (천 단위 구분 쉼표 포함)
        totalPriceLabel.setText("총 가격: " + String.format("%,d 원", total));
    }


    // 결제 처리(아래의 saveOrderHistory 추가하면 여기에서 사용하면 됩니다.)
    public boolean processPayment() {
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "주문한 메뉴가 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int option = JOptionPane.showConfirmDialog(this,
                totalPriceLabel.getText() + "\n결제하시겠습니까?",
                "결제 확인", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }
}
