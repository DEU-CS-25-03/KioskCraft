package Boundary.Admin;

import javax.swing.*;

public class SalesAnalysisUI extends JFrame {
    // 생성자
    public SalesAnalysisUI(AdminUI adminUI, String 매출_분석, boolean b) {
        setTitle(매출_분석);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 추가 UI 구성 코드 작성
    }

    // 버튼 추가 메서드 예시
    public void addAdminButton(String text, int x, int y, Runnable action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 30); // 버튼 크기 임의 지정
        button.addActionListener(e -> action.run());
        add(button);
        setLayout(null); // 절대 위치 사용
    }

    // 예시로 메인 메서드 추가 (테스트용)
    public static void main(String[] args) {
        AdminUI adminUI = new AdminUI(); // AdminUI는 별도 구현 필요
        SalesAnalysisUI ui = new SalesAnalysisUI(adminUI, "매출 분석", true);
        ui.addAdminButton("매출 분석", 430, 10, () -> {
            SalesAnalysisUI newUI = new SalesAnalysisUI(adminUI, "매출 분석", true);
            newUI.setVisible(true);
        });
        ui.setVisible(true);
    }
}
