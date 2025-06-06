package Boundary;

import Control.MenuControl;
import DataAccessObject.DBManager;
import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ModifyMenuUI 클래스
 * - AdminUI의 JTable 모델을 전달받아,
 *   테이블에는 {카테고리, 메뉴명, 단가, 품절여부, 삭제} 5개 컬럼만 있고,
 *   여기선 Entity.menus에서 이미지경로까지 읽어온 뒤 수정한다.
 * - 다이얼로그가 닫히면, AdminUI의 모델 값도 자동으로 업데이트되어 화면이 갱신된다.
 */
public class ModifyMenuUI extends JDialog {
    // AdminUI에서 전달받는 원본 모델 (컬럼: 카테고리, 메뉴명, 단가, 품절여부, 삭제)
    private final DefaultTableModel adminModel;
    private final JTable table;      // 수정 전용 테이블(이미지경로 포함)
    private final DefaultTableModel editModel;

    /**
     * @param owner        부모 JFrame (AdminUI 인스턴스)
     * @param title        다이얼로그 제목
     * @param modal        모달 여부
     * @param adminModel   AdminUI의 JTable이 쓰고 있는 DefaultTableModel
     */
    public ModifyMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel adminModel) {
        super(owner, title, modal);
        this.adminModel = adminModel;

        // 1) editModel 생성: {카테고리, 메뉴명, 단가, 품절여부, 이미지경로}
        String[] columns = { "카테고리", "메뉴명", "단가", "품절여부", "이미지경로" };
        editModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 편집 불가, 더블클릭해서 모달만 띄움
            }
        };

        // Entity.menus로부터 값 채우기
        for (Object[] row : Entity.menus) {
            Object[] rowData = new Object[5];
            rowData[0] = row[0];    // 카테고리
            rowData[1] = row[1];    // 메뉴명
            rowData[2] = row[2];    // 단가 (예: "1,000원")
            rowData[3] = row[3];    // 품절여부 (Boolean)
            rowData[4] = row[4];    // 이미지경로 (String)
            editModel.addRow(rowData);
        }

        setLayout(new BorderLayout());
        setSize(650, 420);
        setLocationRelativeTo(owner);
        setResizable(false);

        // 2) JTable 생성
        table = new JTable(editModel);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 컬럼 너비 조정
        table.getColumnModel().getColumn(0).setPreferredWidth(120); // 카테고리
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // 메뉴명
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // 단가
        table.getColumnModel().getColumn(3).setPreferredWidth(60);  // 품절여부
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // 이미지경로

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 3) 더블클릭 리스너 추가: 해당 행 수정 모달 띄우기
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        showEditDialog(row);
                    }
                }
            }
        });

        // 4) 닫기 버튼
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("닫기");
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        closeBtn.addActionListener(_ -> dispose());
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * 선택된 editModel의 행(rowIndex) 데이터를 이용해 “메뉴 수정” 모달을 띄웁니다.
     * 수정 완료 시 AdminUI의 모델(adminModel)도 동일 인덱스에 반영되어 테이블이 리프레시됩니다.
     */
    private void showEditDialog(int rowIndex) {
        // editModel의 데이터를 읽어옴
        String oldCategory = (String) editModel.getValueAt(rowIndex, 0);
        String oldName = (String) editModel.getValueAt(rowIndex, 1);
        String oldPriceStr = (String) editModel.getValueAt(rowIndex, 2); // “1,000원”
        Boolean oldSoldOut = (Boolean) editModel.getValueAt(rowIndex, 3);
        String oldImagePath = (String) editModel.getValueAt(rowIndex, 4);

        // 다이얼로그 생성
        JDialog editDlg = new JDialog(this, "메뉴 수정", true);
        editDlg.setSize(450, 380);
        editDlg.setLayout(null);
        editDlg.setLocationRelativeTo(this);
        editDlg.setResizable(false);

        // --- 메뉴명 입력 ---
        JLabel nameLabel = new JLabel("메뉴명:");
        nameLabel.setBounds(20, 20, 80, 30);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(nameLabel);

        JTextField nameField = new JTextField(oldName);
        nameField.setBounds(120, 20, 280, 30);
        editDlg.add(nameField);

        // --- 가격 입력 ---
        JLabel priceLabel = new JLabel("가격:");
        priceLabel.setBounds(20, 70, 80, 30);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(priceLabel);

        // “1,000원” -> 숫자만 추출
        String onlyDigits = oldPriceStr.replaceAll("[^0-9]", "");
        JTextField priceField = new JTextField(onlyDigits);
        priceField.setBounds(120, 70, 280, 30);
        editDlg.add(priceField);

        // --- 품절 여부 체크박스 ---
        JLabel soldOutLabel = new JLabel("품절여부:");
        soldOutLabel.setBounds(20, 120, 80, 30);
        soldOutLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(soldOutLabel);

        JCheckBox soldOutCheck = new JCheckBox();
        soldOutCheck.setBounds(120, 120, 20, 30);
        soldOutCheck.setSelected(oldSoldOut);
        editDlg.add(soldOutCheck);

        // --- 카테고리 콤보박스 ---
        JLabel categoryLabel = new JLabel("카테고리:");
        categoryLabel.setBounds(20, 170, 80, 30);
        categoryLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(categoryLabel);

        JComboBox<String> categoryCombo = new JComboBox<>(Entity.categories.toArray(new String[0]));
        categoryCombo.setBounds(120, 170, 280, 30);
        categoryCombo.setSelectedItem(oldCategory);
        editDlg.add(categoryCombo);

        // --- 이미지 경로 입력 ---
        JLabel imgLabel = new JLabel("이미지경로:");
        imgLabel.setBounds(20, 220, 80, 30);
        imgLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(imgLabel);

        JTextField imgField = new JTextField(oldImagePath);
        imgField.setBounds(120, 220, 200, 30);
        editDlg.add(imgField);

        JButton imgBtn = new JButton("...");
        imgBtn.setBounds(330, 220, 70, 30);
        imgBtn.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("이미지 파일(JPG, PNG, JPEG)", "jpg", "jpeg", "png"));
            int result = chooser.showOpenDialog(editDlg);
            if (result == JFileChooser.APPROVE_OPTION) {
                File sel = chooser.getSelectedFile();
                imgField.setText(sel.getAbsolutePath());
            }
        });
        editDlg.add(imgBtn);

        // --- 확인/취소 버튼 ---
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setBounds(120, 280, 100, 40);
        confirmBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(confirmBtn);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(260, 280, 100, 40);
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        cancelBtn.addActionListener(_ -> editDlg.dispose());
        editDlg.add(cancelBtn);
        // --- 확인 버튼 리스너: 입력 검증 및 DB, Entity, 모델 업데이트 ---
        confirmBtn.addActionListener(_ -> {
            String newName = nameField.getText().trim();
            String priceInput = priceField.getText().trim();
            boolean newSoldOut = soldOutCheck.isSelected();
            String newCategory = (String) categoryCombo.getSelectedItem();
            String newImagePath = imgField.getText().trim();
            // 빈 칸 검사
            if (newName.isEmpty() || priceInput.isEmpty() || newCategory == null || newImagePath.isEmpty()) {
                JOptionPane.showMessageDialog(editDlg, "모든 항목을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 가격 숫자 검사
            if (!priceInput.matches("\\d+")) {
                JOptionPane.showMessageDialog(editDlg, "가격은 숫자로만 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 중복 메뉴명 체크
            for (Object[] menu : Entity.menus) {
                if (newName.equals(menu[1])) {
                    JOptionPane.showMessageDialog(editDlg, "중복된 메뉴가 존재합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try {
                // MenuControl.modifyMenu 호출
                MenuControl.modifyMenu(oldName, newCategory, newName, priceInput, newImagePath, newSoldOut, rowIndex, editModel, adminModel);
                JOptionPane.showMessageDialog(editDlg, "메뉴가 성공적으로 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
                editDlg.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editDlg, "메뉴 수정 중 오류 발생:\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        editDlg.setVisible(true);
    }
}
