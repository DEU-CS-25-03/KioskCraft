package Boundary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SalesAnalysisUI extends JDialog {

    public SalesAnalysisUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setResizable(false);
        setSize(1000, 630);

        // ----------------------------
        // 1) 왼쪽: 채널별 매출 점유율 (Pie Chart + 범례 / 수치)
        // ----------------------------

        // 1-1) PieChart 영역
        PieChartPanel pieChart = new PieChartPanel();
        pieChart.setBounds(20, 20, 400, 250);
        add(pieChart);

        // 1-2) 범례+숫자 영역
        JPanel legendPanel = new JPanel(null);
        legendPanel.setBounds(20, 280, 400, 250);
        legendPanel.setBorder(new LineBorder(Color.GRAY));
        add(legendPanel);

        // 범례: 매장(파랑), 포장(주황)
        JLabel lblLegendStore = new JLabel("■      매장  55%  (₩5,500,000)");
        lblLegendStore.setForeground(new Color(59, 89, 152));
        lblLegendStore.setBounds(20, 20, 300, 30);
        legendPanel.add(lblLegendStore);

        JLabel lblLegendTakeout = new JLabel("■      포장  45%  (₩4,500,000)");
        lblLegendTakeout.setForeground(new Color(242, 153, 74));
        lblLegendTakeout.setBounds(20, 60, 300, 30);
        legendPanel.add(lblLegendTakeout);

        // ----------------------------
        // 2) 오른쪽 상단: 필터 버튼들(어제, 오늘, 이번주, 이번달) + 날짜 표시
        // ----------------------------
        JPanel filterPanel = new JPanel(null);
        filterPanel.setBounds(440, 20, 540, 80);
        filterPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        add(filterPanel);

        String[] filters = { "어제", "오늘", "이번주", "이번달" };
        int btnX = 10;
        for (String f : filters) {
            JButton btn = new JButton(f);
            btn.setBounds(btnX, 20, 80, 30);
            filterPanel.add(btn);
            btnX += 90;
        }
        // 날짜 표시 (예시)
        JLabel lblDate = new JLabel("📅 2025-01-15");
        lblDate.setBounds(400, 25, 120, 30);
        lblDate.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        filterPanel.add(lblDate);

        // ----------------------------
        // 3) 오른쪽 중간: 매출·실매출·주문 건수 통계 박스
        // ----------------------------
        JPanel statsPanel = new JPanel(null);
        statsPanel.setBounds(440, 110, 540, 200);
        statsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        add(statsPanel);

        // 통계 박스 3개를 가로로 배치
        int boxWidth = 160, boxHeight = 180;
        String[] titles = { "총매출", "실매출", "주문 건수" };
        for (int i = 0; i < 3; i++) {
            JPanel box = new JPanel(null);
            box.setBounds(10 + i * (boxWidth + 10), 10, boxWidth, boxHeight);
            box.setBorder(new LineBorder(Color.GRAY));
            statsPanel.add(box);

            // 제목
            JLabel lblTitle = new JLabel(titles[i], SwingConstants.CENTER);
            lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            lblTitle.setBounds(0, 10, boxWidth, 30);
            box.add(lblTitle);

            // 예시 항목 1
            JLabel item1 = new JLabel("1. 매출 금액: ₩0");
            item1.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            item1.setBounds(10, 60, boxWidth - 20, 25);
            box.add(item1);

            // 예시 항목 2
            JLabel item2 = new JLabel("2. 증감 비율: 0%");
            item2.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            item2.setBounds(10, 100, boxWidth - 20, 25);
            box.add(item2);
        }

        // ----------------------------
        // 4) 오른쪽 하단: 기간별 매출 (Line Chart)
        // ----------------------------
        LineChartPanel lineChart = new LineChartPanel();
        lineChart.setBounds(440, 320, 540, 250);
        lineChart.setBorder(new LineBorder(Color.GRAY));
        add(lineChart);
    }

    static class PieChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 배경
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 20;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            // 1) 매장(55%) 파란색
            g2.setColor(new Color(59, 89, 152));
            g2.fillArc(x, y, diameter, diameter, 90, -(int) (360 * 0.55));

            // 2) 포장(45%) 주황색
            g2.setColor(new Color(242, 153, 74));
            g2.fillArc(x, y, diameter, diameter, 90 - (int) (360 * 0.55), -(int) (360 * 0.45));

            // 도넛 중앙을 흰색으로 지우기
            int innerDiameter = diameter / 2;
            int ix = x + (diameter - innerDiameter) / 2;
            int iy = y + (diameter - innerDiameter) / 2;
            g2.setColor(getBackground());
            g2.fillOval(ix, iy, innerDiameter, innerDiameter);

            // 제목 텍스트
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            String title = "채널별 매출 점유율";
            FontMetrics fm = g2.getFontMetrics();
            int tx = (width - fm.stringWidth(title)) / 2;
            g2.drawString(title, tx, 20);
        }
    }

    static class LineChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 제목
            String title = "기간별 매출 (매출에 따른 그래프 생성)";
            g2.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(title)) / 2;
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(title, tx, 20);

            // 실제 차트 그릴 영역 (약간 여유 두기)
            int margin = 40;
            int x0 = margin;
            int y0 = h - margin;
            int x1 = w - margin;
            int y1 = margin + 30; // 제목 아래 여백

            // 축 그리기 (X축, Y축)
            g2.setColor(Color.LIGHT_GRAY);
            // Y축
            g2.drawLine(x0, y0, x0, y1);
            // X축
            g2.drawLine(x0, y0, x1, y0);

            // 예시 데이터 포인트 (임의)
            // 예: 11일(20), 12일(30), 13일(25), 14일(35), 오늘(37)
            int[] data = { 20, 30, 25, 35, 37 };
            String[] labels = { "11일", "12일", "13일", "14일", "오늘" };
            int points = data.length;

            // 데이터 그리기
            int chartWidth = x1 - x0;
            int chartHeight = y0 - y1;
            int gapX = chartWidth / (points - 1);
            int maxValue = 40; // y축 최대값 (예시)
            int prevX = x0;
            int prevY = y0 - (data[0] * chartHeight / maxValue);

            g2.setColor(new Color(34, 139, 34));
            for (int i = 1; i < points; i++) {
                int cx = x0 + gapX * i;
                int cy = y0 - (data[i] * chartHeight / maxValue);
                g2.drawLine(prevX, prevY, cx, cy);
                prevX = cx;
                prevY = cy;
            }

            // X축 레이블
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            g2.setColor(Color.DARK_GRAY);
            for (int i = 0; i < points; i++) {
                int lx = x0 + gapX * i;
                int ly = y0 + 20;
                String lbl = labels[i];
                int strW = g2.getFontMetrics().stringWidth(lbl);
                g2.drawString(lbl, lx - strW / 2, ly);
            }

            // Y축 레이블 (예: 0, 10, 20, 30, 40)
            for (int v = 0; v <= maxValue; v += 10) {
                int ry = y0 - (v * chartHeight / maxValue);
                String vy = String.valueOf(v);
                int strH = g2.getFontMetrics().getHeight();
                g2.drawString(vy, x0 - g2.getFontMetrics().stringWidth(vy) - 5, ry + strH / 2 - 3);
                // 가로 보조선
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawLine(x0, ry, x1, ry);
                g2.setColor(Color.DARK_GRAY);
            }
        }
    }
}
