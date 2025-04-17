import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class MenuPanel extends JPanel {

    //메뉴패널 생성
    public MenuPanel() {
        setLayout(new GridLayout(0, 5, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    // 메뉴 패널 갱신 – 선택한 카테고리의 메뉴 목록을 표시
    public void displayCategory(String category, DataModel dataModel, MenuSelectionListener listener) {
        //원래 있던 데이터 삭제 후 갱신
        removeAll();
        Map<String, List<String>> menuData = dataModel.getMenuData();
        Map<String, Integer> priceData = dataModel.getPriceData();
        Map<String, Boolean> soldOutMap = dataModel.getSoldOutMap();

        //메뉴데이터에 카테고리가 없다면  화면 갱신
        if (!menuData.containsKey(category)) {
            revalidate();
            repaint();
            return;
        }

        // 선택된 카테고리에 해당하는 메뉴 목록 순회
        for (String menu : menuData.get(category)) {
            // 메뉴 이름으로 가격 및 품절상태 불러오기
            int price = priceData.get(menu);
            boolean isSoldOut = soldOutMap.get(menu);

            //메뉴 패널 생성 후 여백 설정
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // 품절 시 배경을 연회색, 판매 가능 시 흰색으로 설정
            panel.setBackground(isSoldOut ? new Color(240, 240, 240) : Color.WHITE);

            //이미지 자리표시자 생성
            // 가로 180, 세로 120 크기의 기본 아이콘을 중앙정렬 후 표시
            JLabel imgLabel = new JLabel(createDefaultIcon(180, 120));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imgLabel, BorderLayout.CENTER);

            /*
            메뉴 정보(이름 + 가격 또는 “품절” 표시) 생성
            HTML 포맷으로 중앙 정렬, 메뉴명 굵게, 가격은 천 단위 구분 쉼표 후 원 단위로 설정
            품절인 경우 회색 글씨로 “품절” 표시
            */
            String html = String.format(
                    "<html><center><b>%s</b><br>%s</center></html>",
                    menu,
                    isSoldOut
                            ? "<font color='#888888'>품절</font>"
                            : "<font color='#E74C3C'>" + String.format("%,d 원", price) + "</font>"
            );
            JLabel infoLabel = new JLabel(html, SwingConstants.CENTER);
            infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));  // 폰트 설정
            panel.add(infoLabel, BorderLayout.SOUTH);

            //판매 가능 상태인 경우 클릭 리스너 등록
            if (!isSoldOut) {
                //커서를 손 모양으로 변경해 클릭 가능함을 시각적으로 표시
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //메뉴 선택 시 등록된 리스너에 메뉴명 전달
                        listener.menuSelected(menu);
                    }
                });
            }
            //최종적으로 프레임 혹은 부모 컨테이너에 패널 추가
            add(panel);
        }

        //모든 메뉴 패널이 추가된 후 화면 갱신
        revalidate();
        repaint();
    }
    /*
    기본 이미지 아이콘 생성 함수
    param w: 아이콘의 가로 너비(pixel)
    param h: 아이콘의 세로 높이(pixel)
    return: 지정된 크기의 흰색 배경에 '메뉴 이미지' 텍스트가 적힌 ImageIcon
     */
    private ImageIcon createDefaultIcon(int w, int h) {
        //w*h 크기의 빈 이미지 생성

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //그리기 도구생성
        Graphics2D g2d = img.createGraphics();

        //배경을 흰색으로 채우기
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);

        //텍스트 색상을 연회색으로 설정
        g2d.setColor(Color.LIGHT_GRAY);

        //'맑은 고딕', 기본(PLAIN), 크기 12로 텍스트 설정
        g2d.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        //이미지 중앙에 '메뉴 이미지' 문자열 그리기
        g2d.drawString("메뉴 이미지", w / 2 - 30, h / 2);

        //Graphics2D 해제
        g2d.dispose();

        //BufferedImage를 ImageIcon으로 변환하여 반환
        return new ImageIcon(img);
    }


    //메뉴 선택 리스너 인터페이스
    public interface MenuSelectionListener {
        void menuSelected(String menu);
    }
}
