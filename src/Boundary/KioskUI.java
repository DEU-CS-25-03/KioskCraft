package Boundary;

import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;
import Control.KioskControl;

import static Control.KioskControl.showMenuByCategory;

public class KioskUI extends JFrame {
    // 메뉴 그리드 패널 (5열)
    private final JPanel gridPanel;

    // 장바구니 테이블 모델 (컬럼: 메뉴명, 단가, 수량, 총액, -, x)
    private final DefaultTableModel cartModel;

    public KioskUI() {
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        String[] columns = {"메뉴명", "단가", "수량", "총액", "", ""};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 0~3(메뉴명, 단가, 수량, 총액) 컬럼은 편집 불가
                // 4번 인덱스("-"), 5번 인덱스("x")만 편집 가능
                return column >= 4;
            }
        };
        // --- 1) 카테고리 버튼 생성 ---
        for (int i = 0; i < Entity.categories.size(); i++) {
            String category = Entity.categories.get(i);
            JButton categoryBtn = new JButton(category);
            categoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            categoryBtn.setBounds(10 + i * 130, 10, 120, 40);
            categoryBtn.addActionListener(_ -> showMenuByCategory(category, gridPanel, cartModel));
            add(categoryBtn);
        }

        // 홈 버튼 (🏠)
        JButton goHome = new JButton("\uD83C\uDFE0");
        goHome.setFont(new Font("", Font.PLAIN, 16));
        goHome.setBounds(1425, 10, 50, 50);
        goHome.addActionListener(_ -> {
            new OrderTypeSelectionUI().setVisible(true);
            dispose();
        });
        add(goHome);

        // --- 2) 메뉴 그리드패널 + 스크롤패널 ---
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(gridPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(
                menuPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBounds(10, 60, 1080, 690);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // --- 3) 장바구니 테이블 생성 및 초기화 ---


        // Entity.cartList에 담긴 {메뉴명, 단가, 수량, 총액} 꺼내서 테이블에 뿌리기
        for (Object[] item : Entity.cartList) {
            String name = (String) item[0];
            int unitPrice = (int) item[1];
            int quantity  = (int) item[2];
            int total     = (int) item[3];

            Vector<Object> row = new Vector<>();
            row.add(name);         // 메뉴명
            row.add(unitPrice);    // 단가
            row.add(quantity);     // 수량
            row.add(total);        // 총액
            row.add("-");          // 수량 감소 버튼
            row.add("x");          // 행 삭제 버튼
            cartModel.addRow(row);
        }

        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cartTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
        cartTable.setRowHeight(30);
        cartTable.setRowSelectionAllowed(false);

        // 버튼 렌더러/에디터 연결 (컬럼 인덱스: "-"=4, "x"=5)
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new KioskControl.ButtonRenderer("-"));
        cartTable.getColumnModel().getColumn(4).setCellEditor(new KioskControl.ButtonEditor(new JCheckBox(), "-", true, cartModel));
        cartTable.getColumnModel().getColumn(5).setCellRenderer(new KioskControl.ButtonRenderer("x"));
        cartTable.getColumnModel().getColumn(5).setCellEditor(new KioskControl.ButtonEditor(new JCheckBox(), "x", false, cartModel));

        // 컬럼 너비 설정
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(200); // 메뉴명
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // 단가
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // 수량
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(130); // 총액
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(40);  // "-"
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(40);  // "x"

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBounds(1100, 60, 375, 620);
        add(cartScrollPane);

        // --- 4) 결제 버튼: 결제 완료 후 Entity.cartList와 테이블 모두 비우기 ---
        JButton payBtn = new JButton("결제하기");
        payBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        payBtn.setBounds(1100, 690, 375, 60);
        payBtn.addActionListener(_ -> {
            if (Entity.cartList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "장바구니가 비어있습니다.\n메뉴를 선택해주세요.");
            } else {
                JOptionPane.showMessageDialog(this, "결제 완료!");

                for(Object[] row : Entity.cartList) {
                    System.out.println(Arrays.toString(row));
                }
                // Entity.cartList 비우기
                Entity.cartList.clear();
                // 테이블 모델도 비우기
                cartModel.setRowCount(0);
            }
        });
        add(payBtn);

        // --- 5) 초기 카테고리 표시 ---
        if (!Entity.categories.isEmpty()) {
            showMenuByCategory(Entity.categories.getFirst(), gridPanel, cartModel);
        }
        setVisible(true);

        // (원한다면) 프레임 뜨자마자 프레임에 포커스 요청
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
}