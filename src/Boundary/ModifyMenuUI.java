package Boundary;

import Controller.MenuControl;
import DataTransferObject.Menu;
import DataTransferObject.Category;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;

/**
 * ModifyMenuUI 클래스
 * - AdminUI의 JTable 모델을 전달받아,
 *   테이블에는 {카테고리, 메뉴명, 단가, 품절여부, 삭제} 5개 컬럼만 있고,
 *   여기선 Entity.menus에서 이미지경로까지 읽어온 뒤 수정한다.
 * - 다이얼로그가 닫히면, AdminUI의 모델 값도 자동으로 업데이트되어 화면이 갱신된다.
 * - RegistMenuUI와 동일한 여백과 레이아웃을 적용하되, 하단에 ‘품절여부’ 체크박스를 배치함.
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
        for (Object[] row : Menu.menus) {
            Object[] rowData = new Object[5];
            rowData[0] = row[0];    // 카테고리
            rowData[1] = row[1];    // 메뉴명
            rowData[2] = row[2];    // 단가 (예: "1,000원")
            rowData[3] = row[3];    // 품절여부 (Boolean)
            rowData[4] = row[4];    // 이미지경로 (String)
            editModel.addRow(rowData);
        }

        setLayout(new BorderLayout());
        setSize(650, 600);
        setLocationRelativeTo(owner);
        setResizable(false);

        // 2) JTable 생성
        table = new JTable(editModel);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 컬럼 너비 조정 (생략 가능)
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // 카테고리
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // 메뉴명
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // 단가
        table.getColumnModel().getColumn(3).setPreferredWidth(60);  // 품절여부
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // 이미지경로

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
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        closeBtn.addActionListener(_ -> dispose());
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * 선택된 editModel의 행(rowIndex) 데이터를 이용해 “메뉴 수정” 모달을 띄웁니다.
     * 수정 완료 시 AdminUI의 모델(adminModel)도 동일 인덱스에 반영되어 테이블이 리프레시됩니다.
     * RegistMenuUI와 동일한 위치 및 크기로 컴포넌트를 배치하며,
     * 하단에 ‘품절여부’ 체크박스를 추가했습니다.
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
        editDlg.setLayout(null);
        editDlg.setSize(325, 250);
        editDlg.setLocationRelativeTo(this);
        editDlg.setResizable(false);
        SwingUtilities.invokeLater(editDlg::requestFocusInWindow);

        // --- 메뉴명 입력 필드 ---
        JLabel nameLabel = new JLabel("메뉴 이름:");
        nameLabel.setBounds(10, 10, 80, 30);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(nameLabel);

        JTextField nameField = new JTextField(oldName);
        nameField.setBounds(110, 10, 190, 30);
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(nameField);

        // --- 카테고리 선택 콤보박스 ---
        JLabel categoryLabel = new JLabel("카테고리:");
        categoryLabel.setBounds(10, 50, 80, 30);
        categoryLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(categoryLabel);

        JComboBox<String> categoryCombo = new JComboBox<>(Category.categories.toArray(new String[0]));
        categoryCombo.setBounds(110, 50, 190, 30);
        categoryCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        categoryCombo.setSelectedItem(oldCategory);
        editDlg.add(categoryCombo);

        // --- 가격 입력 필드 ---
        JLabel priceLabel = new JLabel("가격:");
        priceLabel.setBounds(10, 90, 80, 30);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(priceLabel);

        JTextField priceField = new JTextField(oldPriceStr.replaceAll("[^0-9]", ""));
        priceField.setBounds(110, 90, 190, 30);
        priceField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(priceField);

        // --- 이미지 경로 입력 및 버튼 ---
        JLabel imgPathLabel = new JLabel("이미지 경로:");
        imgPathLabel.setBounds(10, 130, 100, 30);
        imgPathLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(imgPathLabel);

        JTextField imgPathField = new JTextField(oldImagePath);
        imgPathField.setBounds(110, 130, 150, 30);
        imgPathField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(imgPathField);

        JButton imgPathBtn = new JButton("..");
        imgPathBtn.setBounds(270, 130, 30, 30);
        imgPathBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        imgPathBtn.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("이미지 파일 (JPG, PNG, JPEG)", "jpg", "jpeg", "png"));
            if (chooser.showOpenDialog(editDlg) == JFileChooser.APPROVE_OPTION) {
                File sel = chooser.getSelectedFile();
                imgPathField.setText(sel.getAbsolutePath());
            }
        });
        editDlg.add(imgPathBtn);

        // --- 품절여부 체크박스 (맨 아래) ---
        JLabel soldOutLabel = new JLabel("품절여부:");
        soldOutLabel.setBounds(10, 170, 80, 30);
        soldOutLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(soldOutLabel);

        JCheckBox soldOutCheck = new JCheckBox();
        soldOutCheck.setBounds(110, 170, 30, 30);
        soldOutCheck.setSelected(oldSoldOut);
        soldOutCheck.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(soldOutCheck);

        // --- 등록/취소 버튼 ---
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setBounds(140, 170, 75, 30);
        confirmBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        editDlg.add(confirmBtn);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(225, 170, 75, 30);
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cancelBtn.addActionListener(_ -> editDlg.dispose());
        editDlg.add(cancelBtn);

        // --- 확인 버튼 리스너: 입력 검증 및 DB, Entity, 모델 업데이트 ---
        confirmBtn.addActionListener(_ -> {
            String newName = nameField.getText().trim();
            String priceInput = priceField.getText().trim();
            boolean newSoldOut = soldOutCheck.isSelected();
            String newCategory = (String) categoryCombo.getSelectedItem();
            String newImagePath = imgPathField.getText().trim();

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
            for (Object[] menu : Menu.menus) {
                if (newName.equals(menu[1]) && !newName.equals(oldName)) {
                    JOptionPane.showMessageDialog(editDlg, "중복된 메뉴가 존재합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try {
                // MenuControl.modifyMenu 호출
                MenuControl.modifyMenu(
                        oldName,
                        newCategory,
                        newName,
                        priceInput,
                        newImagePath,
                        newSoldOut,
                        rowIndex,
                        editModel,
                        adminModel
                );
                JOptionPane.showMessageDialog(editDlg, "메뉴가 성공적으로 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
                editDlg.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editDlg, "메뉴 수정 중 오류 발생:\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        editDlg.setVisible(true);
    }
}
