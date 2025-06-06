package Boundary;

import Control.CategoryControl;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * 카테고리 목록을 보여주고 수정할 수 있는 다이얼로그 UI 클래스
 */
public class ModifyCategoryUI extends JDialog {

    /**
     * 카테고리 수정 다이얼로그 생성자
     *
     * @param owner 다이얼로그 소유자 프레임
     * @param title 다이얼로그 타이틀
     * @param modal 모달 여부
     */
    public ModifyCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(235, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 테이블 컬럼명 설정: 단일 컬럼 "카테고리"
        String[] columnNames = { "카테고리" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 테이블 셀은 직접 편집 불가
                return false;
            }
        };

        // Entity.categories 리스트에서 카테고리명 가져와 모델에 추가
        List<String> categories = Entity.categories;
        for (String cat : categories) {
            model.addRow(new Object[]{ cat });
        }

        // 테이블 생성 및 폰트, 행 높이 설정
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setCellSelectionEnabled(false);

        // "카테고리" 컬럼 너비 고정
        table.getColumnModel().getColumn(0).setMaxWidth(200);
        table.getColumnModel().getColumn(0).setMinWidth(200);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);

        // 행 더블클릭 시 수정 다이얼로그 호출 리스너
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        showEditDialog(row, model);
                    }
                }
            }
        });

        // 스크롤 패널에 테이블 추가 후 다이얼로그에 배치
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 200, 300);
        add(scrollPane);
    }

    /**
     * 선택된 행의 카테고리를 수정하는 다이얼로그를 표시하고,
     * 수정 완료 시 Entity.categories, 모델, DB를 업데이트한다.
     *
     * @param rowIndex 수정할 카테고리의 테이블 행 인덱스
     * @param model    테이블의 DefaultTableModel 객체
     */
    private void showEditDialog(int rowIndex, DefaultTableModel model) {
        // 기존 카테고리명 가져오기
        String oldName = (String) model.getValueAt(rowIndex, 0);

        // 수정 다이얼로그 생성
        JDialog editDlg = new JDialog(this, "카테고리 수정", true);
        editDlg.setLayout(null);
        editDlg.setSize(255, 130);
        editDlg.setLocationRelativeTo(this);
        editDlg.setResizable(false);
        SwingUtilities.invokeLater(editDlg::requestFocusInWindow);

        // 입력 라벨 설정
        JLabel nameLabel = new JLabel("카테고리 이름:");
        nameLabel.setBounds(10, 10, 100, 30);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(nameLabel);

        // 기존 이름이 채워진 입력 필드 설정
        JTextField nameField = new JTextField(oldName);
        nameField.setBounds(110, 10, 120, 30);
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(nameField);

        // 확인 버튼 설정
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setBounds(10, 50, 105, 30);
        confirmBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        editDlg.add(confirmBtn);

        // 취소 버튼 설정: 클릭 시 다이얼로그 닫기
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(125, 50, 105, 30);
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        cancelBtn.addActionListener(_ -> editDlg.dispose());
        editDlg.add(cancelBtn);

        // 확인 버튼 클릭 시 입력 검증, 중복 검사, DB 및 모델 업데이트
        // showEditDialog 내부 (예시)
        confirmBtn.addActionListener(_ -> {
            String newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(editDlg, "카테고리 이름을 작성해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 중복 검사
            for (int i = 0; i < Entity.categories.size(); i++) {
                if (i != rowIndex && newName.equals(Entity.categories.get(i))) {
                    JOptionPane.showMessageDialog(editDlg, "중복된 카테고리가 존재합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 1) DB에서만 업데이트
            try {
                CategoryControl.modifyCategory(oldName, newName, rowIndex);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editDlg, "카테고리 수정 중 오류 발생:\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2) Entity.categories 및 테이블 모델 업데이트
            Entity.categories.set(rowIndex, newName);
            model.setValueAt(newName, rowIndex, 0);

            JOptionPane.showMessageDialog(editDlg, "카테고리가 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
            editDlg.dispose();
        });

        editDlg.setVisible(true);
    }
}
