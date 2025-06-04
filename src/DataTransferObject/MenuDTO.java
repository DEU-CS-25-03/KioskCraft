package DataTransferObject;

public class MenuDTO {
    private String category;
    private String menuName;
    private int price;
    private String imagePath;
    private boolean isSoldOut;

    public MenuDTO() {}

    public MenuDTO(String category, String menuName, int price, String imagePath,boolean isSoldOut) {
        this.category = category;
        this.menuName = menuName;
        this.price = price;
        this.imagePath = imagePath;
        this.isSoldOut = isSoldOut;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}
