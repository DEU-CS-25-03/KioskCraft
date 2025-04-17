import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class CSVDataLoader {
    public static DataModel loadData() throws IOException {
        DataModel dataModel = new DataModel();
        //경로 설정, 일단 바탕화면\kiosk_data.csv로 해놨습니다.
        Path path = Paths.get(System.getProperty("user.home"), "Desktop", "kiosk_data.csv");
        if (!Files.exists(path)) {
            throw new FileNotFoundException("CSV 파일을 찾을 수 없습니다: " + path);
        }
        //데이터 존재 확인
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new IOException("CSV 파일이 비어 있습니다");
        }
        //형식 확인
        String header = lines.get(0).trim();
        if (!header.equals("카테고리,메뉴명,가격,품절")) {
            throw new IOException("CSV 헤더 형식 오류. 필요한 컬럼: 카테고리,메뉴명,가격,품절");
        }

        //각 카테고리별 데이터 불러오기
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",");
            if (values.length < 4) continue;

            //카테고리, 메뉴, 가격, 품절여부
            String category = values[0].trim();
            String menu = values[1].trim();
            int price = Integer.parseInt(values[2].trim());
            boolean soldOut = Boolean.parseBoolean(values[3].trim());

            //csv에서 읽어온 데이터를 모델이 추가
            dataModel.addMenuItem(category, menu, price, soldOut);
        }
        //데이터 반환
        return dataModel;
    }
}
