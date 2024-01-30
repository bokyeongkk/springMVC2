package hello.itemservice.domain.item;
public enum ItemType {

    // 상품 종류는 'ENUM' 을 사용한다.
    BOOK("도서"), FOOD("식품"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}