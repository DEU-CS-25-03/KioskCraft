package Boundary;

import Control.KioskUIUtils;
import Control.AdminControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * AdminUI 클래스
 * - 관리자용 UI를 구성하고, 메뉴/카테고리/매출 분석/주문현황 화면을 모달로 띄움
 * - F10 키를 통해 처음 화면(주문 유형 선택)으로 돌아갈 수 있음
 */
public class AdminUI extends JFrame {
    // AdminControl로부터 가져온 테이블 모델
    private final DefaultTableModel model = AdminControl.getTableModel();
    // F10 키 홀드 액션 처리 디스패처
    private static KioskUIUtils.KeyHoldActionDispatcher keyDispatcher;

    public AdminUI() {
        // 프레임 기본 설정
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(865, 675);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // 테이블 생성 및 컬럼 설정
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150); // 카테고리
        colModel.getColumn(1).setPreferredWidth(260); // 메뉴명
        colModel.getColumn(2).setPreferredWidth(80);  // 가격
        colModel.getColumn(3).setPreferredWidth(80);  // 품절 여부
        colModel.getColumn(4).setPreferredWidth(80);  // 삭제 버튼

        // 삭제 버튼 렌더러 및 에디터 설정
        table.getColumnModel().getColumn(4).setCellRenderer(new AdminControl.ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AdminControl.ButtonEditor(model, table));

        // 테이블 스크롤 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 각종 관리자 버튼 추가
        addAdminButtons();

        // 창 닫힐 때 디스패처 해제
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { unregisterKeyDispatcher(); }
            @Override public void windowClosing(java.awt.event.WindowEvent e) { unregisterKeyDispatcher(); }
        });
    }

    private void addAdminButtons() {
        JButton registMenuBtn = new JButton("메뉴 등록");
        registMenuBtn.setBounds(10, 10, 200, 50);
        registMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registMenuBtn.addActionListener(_ -> new RegistMenuUI(this, "메뉴 등록", true, model).setVisible(true));
        add(registMenuBtn);

        JButton modifyMenuBtn = new JButton("메뉴 수정");
        modifyMenuBtn.setBounds(220, 10, 200, 50);
        modifyMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyMenuBtn.addActionListener(_ -> new ModifyMenuUI(this, "메뉴 수정", true, model).setVisible(true));
        add(modifyMenuBtn);

        JButton salesAnalysisBtn = new JButton("매출 분석");
        salesAnalysisBtn.setBounds(430, 10, 200, 50);
        salesAnalysisBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        salesAnalysisBtn.addActionListener(_ -> new SalesAnalysisUI(this, "매출 분석", true).setVisible(true));
        add(salesAnalysisBtn);

        JButton designBtn = new JButton("디자인 변경");
        designBtn.setBounds(640, 10, 200, 50);
        designBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        designBtn.addActionListener(_ -> new DesignUI(this, "디자인 변경", true).setVisible(true));
        add(designBtn);

        JButton registCategoryBtn = new JButton("카테고리 등록");
        registCategoryBtn.setBounds(10, 580, 200, 50);
        registCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registCategoryBtn.addActionListener(_ -> new RegistCategoryUI(this, "카테고리 등록", true).setVisible(true));
        add(registCategoryBtn);

        JButton modifyCategoryBtn = new JButton("카테고리 수정");
        modifyCategoryBtn.setBounds(220, 580, 200, 50);
        modifyCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyCategoryBtn.addActionListener(_ -> new ModifyCategoryUI(this, "카테고리 수정", true).setVisible(true));
        add(modifyCategoryBtn);

        JButton removeCategoryBtn = new JButton("카테고리 삭제");
        removeCategoryBtn.setBounds(430, 580, 200, 50);
        removeCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        removeCategoryBtn.addActionListener(_ -> new RemoveCategoryUI(this, "카테고리 삭제", true).setVisible(true));
        add(removeCategoryBtn);

        JButton orderStatusBtn = new JButton("주문현황 확인");
        orderStatusBtn.setBounds(640, 580, 200, 50);
        orderStatusBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        orderStatusBtn.addActionListener(_ -> new OrderStatusUI(this, "주문현황", true).setVisible(true));
        add(orderStatusBtn);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(KeyEvent.VK_F10, 100, () -> { new OrderTypeSelectionUI().setVisible(true); dispose(); });
            keyDispatcher.register();
        } else {
            unregisterKeyDispatcher();
        }
        super.setVisible(b);
    }

    public static void unregisterKeyDispatcher() {
        if (keyDispatcher != null) {
            keyDispatcher.unregister();
            keyDispatcher = null;
        }
    }
}
