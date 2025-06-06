package Boundary;

import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;
import Control.KioskControl;

import static Control.KioskControl.showMenuByCategory;

/**
 * KioskUI 클래스
 * - 메뉴 카테고리 버튼, 메뉴 카드(그리드), 장바구니(테이블) UI를 구성
 * - Entity.cartList를 데이터 소스로 사용하여 장바구니 내용을 표시 및 제어
 */
public class KioskUI extends JFrame {
    // 메뉴 카드를 5열 그리드로 배치할 패널
    private final JPanel gridPanel;

    // 장바구니 테이블의 모델 (컬럼: 메뉴명, 단가, 수량, 총액, "-", "x")
    private final DefaultTableModel cartModel;

    public KioskUI() {
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // 그리드 패널 생성: 5열, 수평·수직 간격 10px
        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));

        // 장바구니 테이블 컬럼 정의
        String[] columns = {"메뉴명", "단가", "수량", "총액", "", ""};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 0~3번 컬럼(메뉴명, 단가, 수량, 총액)은 편집 불가
                // 4~5번 컬럼("-", "x")만 편집 가능
                return column >= 4;
            }
        };

        // 1) 카테고리 버튼 생성
        for (int i = 0; i < Entity.categories.size(); i++) {
            String category = Entity.categories.get(i);
            JButton categoryBtn = new JButton(category);
            categoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            categoryBtn.setBounds(10 + i * 130, 10, 120, 40);
            // 버튼 클릭 시 KioskControl.showMenuByCategory 호출
            categoryBtn.addActionListener(_ -> showMenuByCategory(category, gridPanel, cartModel));
            add(categoryBtn);
        }

        // 홈 버튼 생성(🏠 아이콘)
        JButton goHome = new JButton("\uD83C\uDFE0");
        goHome.setFont(new Font("", Font.PLAIN, 16));
        goHome.setBounds(1425, 10, 50, 50);
        // 클릭 시 OrderTypeSelectionUI를 열고 현재 창 닫기
        goHome.addActionListener(_ -> {
            new OrderTypeSelectionUI().setVisible(true);
            dispose();
        });
        add(goHome);

        // 2) 메뉴 카드 그리드를 스크롤 가능한 영역에 추가
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(gridPanel, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(menuPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 60, 1080, 690);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // 3) Entity.cartList에 저장된 장바구니 항목을 테이블에 초기 로딩
        for (Object[] item : Entity.cartList) {
            String name      = (String) item[0];
            int unitPrice    = (int) item[1];
            int quantity     = (int) item[2];
            int total        = (int) item[3];

            Vector<Object> row = new Vector<>();
            row.add(name);
            row.add(unitPrice);
            row.add(quantity);
            row.add(total);
            row.add("-");
            row.add("x");
            cartModel.addRow(row);
        }

        // 테이블 생성 및 스타일 설정
        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cartTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
        cartTable.setRowHeight(30);
        cartTable.setRowSelectionAllowed(false);

        // 4) 버튼 렌더러/에디터 연결: 4번 컬럼("-", 수량 감소), 5번 컬럼("x", 행 삭제)
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new KioskControl.ButtonRenderer("-"));
        cartTable.getColumnModel().getColumn(4).setCellEditor(new KioskControl.ButtonEditor(new JCheckBox(), "-", true, cartModel));
        cartTable.getColumnModel().getColumn(5).setCellRenderer(new KioskControl.ButtonRenderer("x"));
        cartTable.getColumnModel().getColumn(5).setCellEditor(new KioskControl.ButtonEditor(new JCheckBox(), "x", false, cartModel));

        // 5) 테이블 컬럼 너비 조정
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(200); // 메뉴명
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // 단가
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // 수량
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(130); // 총액
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(40);  // "-"
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(40);  // "x"

        // 장바구니 테이블을 스크롤 패널에 담아 추가
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBounds(1100, 60, 375, 620);
        add(cartScrollPane);

        // 6) 결제 버튼 생성 및 동작 정의
        JButton payBtn = new JButton("결제하기");
        payBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        payBtn.setBounds(1100, 690, 375, 60);
        payBtn.addActionListener(_ -> {
            if (Entity.cartList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "장바구니가 비어있습니다.\n메뉴를 선택해주세요.");
            } else {
                JOptionPane.showMessageDialog(this, "결제 완료!");

                for(Object[] row : Entity.cartList) System.out.println(Arrays.toString(row));

                // 장바구니 데이터 및 테이블 초기화
                Entity.cartList.clear();
                cartModel.setRowCount(0);
            }
        });
        add(payBtn);

        // 7) 최초 카테고리 선택 시 첫 번째 카테고리 보여주기
        if (!Entity.categories.isEmpty()) {
            showMenuByCategory(Entity.categories.getFirst(), gridPanel, cartModel);
        }

        setVisible(true);

        // 프레임이 보인 후 포커스 요청
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
}