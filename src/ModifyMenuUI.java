import javax.swing.*;

public class ModifyMenuUI extends JDialog {
    /*
     * 버튼 텍스트("등록" or "수정")
     * - 모드에 따라 자동 결정
     */
    private String btnText = "수정";

    /**
     * 메뉴 등록/수정 UI 생성자
     * @param isRegist true면 등록 모드, false면 수정 모드
     * @param table    메뉴 관리 테이블 (수정/등록용)
     * @param owner    부모 프레임
     * @param title    다이얼로그 타이틀(외부 전달값)
     * @param modal    모달 여부
     */
    public ModifyMenuUI(boolean isRegist, JTable table, JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        // 등록 모드이면 텍스트 및 타이틀 변경
        if (isRegist) {
            btnText = "등록";
            title = "메뉴 등록 시스템";
        }

        setTitle(title);
        setLayout(null);
        setSize(1280, 720);


        // 메뉴명, 카테고리, 가격, 이미지 경로 입력 필드
        JLabel menuLabel = new JLabel("메뉴명: ");
        menuLabel.setBounds(10, 10, 100, 10);
        add(menuLabel);

        JLabel categoryLabel = new JLabel("카테고리: ");
        categoryLabel.setBounds(10, 30, 100, 10);
        add(categoryLabel);

        JLabel priceLabel = new JLabel("가격: ");
        priceLabel.setBounds(10, 50, 100, 10);
        add(priceLabel);

        JLabel imgPathLabel = new JLabel("이미지 경로: ");
        imgPathLabel.setBounds(10, 70, 100, 10);
        add(imgPathLabel);

        /*
         * 등록/수정 버튼
         * - 클릭 시 알림 다이얼로그 출력
         */
        JButton menuControlBtn = new JButton(btnText);
        menuControlBtn.addActionListener(_ -> JOptionPane.showMessageDialog(
                null,
                "메뉴가 " + btnText + " 되었습니다.")
        );
        menuControlBtn.setBounds(10, 600, 200, 50);
        add(menuControlBtn);

        // 취소 버튼 - 다이얼로그 닫기
        JButton cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(_ -> dispose());
        cancelBtn.setBounds(220, 600, 200, 50);
        add(cancelBtn);
    }
}
