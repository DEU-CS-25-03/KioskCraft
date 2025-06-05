package Boundary;

import Control.KioskUIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import Control.AdminControl;

public class AdminUI extends JFrame {
    public DefaultTableModel model = AdminControl.getTableModel();
    private static KioskUIUtils.KeyHoldActionDispatcher keyDispatcher;

    public AdminUI() {
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(865, 675);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // 테이블 모델 생성 및 테이블 초기화
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        // 컬럼 너비 설정
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150); // 카테고리
        colModel.getColumn(1).setPreferredWidth(260); // 메뉴명
        colModel.getColumn(2).setPreferredWidth(80);  // 가격
        colModel.getColumn(3).setPreferredWidth(60);  // 품절여부
        colModel.getColumn(4).setPreferredWidth(40);  // 삭제 버튼

        // 삭제 버튼 렌더러/에디터 설정
        table.getColumnModel().getColumn(4).setCellRenderer(new AdminControl.ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AdminControl.ButtonEditor(new JCheckBox(), model));

        // 스크롤 및 테이블 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 버튼들 추가
        addAdminButtons();

        // 창 닫힐 때 키 디스패처 해제
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { unregisterKeyDispatcher(); }
            @Override public void windowClosing(WindowEvent e) { unregisterKeyDispatcher(); }
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
        modifyMenuBtn.addActionListener(_ -> new ModifyMenuUI(this, "메뉴 등록", true, model).setVisible(true));
        add(modifyMenuBtn);

        JButton orderedCheckBtn = new JButton("매출 분석");
        orderedCheckBtn.setBounds(430, 10, 200, 50);
        orderedCheckBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        orderedCheckBtn.addActionListener(_ -> new SalesAnalysisUI(this, "매출 분석", true).setVisible(true));
        add(orderedCheckBtn);

        JButton setDefaultDesignBtn = new JButton("디자인 변경");
        setDefaultDesignBtn.setBounds(640, 10, 200, 50);
        setDefaultDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setDefaultDesignBtn.addActionListener(_ -> new DesignUI(this, "디자인 변경", true).setVisible(true));
        add(setDefaultDesignBtn);

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

        JButton deleteCategoryBtn = new JButton("카테고리 삭제");
        deleteCategoryBtn.setBounds(430, 580, 200, 50);
        deleteCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        deleteCategoryBtn.addActionListener(_ -> new RemoveCategoryUI(this, "카테고리 삭제", true).setVisible(true));
        add(deleteCategoryBtn);

        JButton showOrderedListBtn = new JButton("주문현황 확인");
        showOrderedListBtn.setBounds(640, 580, 200, 50);
        showOrderedListBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        showOrderedListBtn.addActionListener(_ -> new OrderStatusUI(this, "주문현황", true).setVisible(true));
        add(showOrderedListBtn);
    }
    @Override
    public void setVisible(boolean b) {
        if (b) {
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(
                    KeyEvent.VK_F10,
                    100,
                    () -> { new OrderTypeSelectionUI().setVisible(true); dispose(); }
            );
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
