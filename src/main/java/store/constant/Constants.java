package store.constant;

public class Constants {

    public static final String CONVENIENCE_ENTER = "안녕하세요. W편의점입니다." + "\n"
            + "현재 보유하고 있는 상품입니다.";
    public static final String PURCHASE_QUANTITY = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String HAS_MEMBERSHIP_DISCOUNT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    public static final String ADD_PROMOTION_QUANTITY = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    public static final String SHORTAGE_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    public static final String RECEIPT_START_FORM = "==============W 편의점================\n"
            + "상품명\t\t수량\t\t\t금액";
    public static final String ORDER_FORM = "%-10s %5d %10d%n"; // 상품명 왼쪽 정렬, 수량과 금액 오른쪽 정렬
    public static final String PRESENTATION_DIVISION_LINE = "=============증	정===============";
    public static final String GIVEAWAY_FORM = "%-10s %5d%n";
    ; // 증정 상품 정렬
    public static final String DIVISION_LINE = "====================================";
    public static final String TOTAL_PURCHASE = "총구매액       %5d %15d%n";
    public static final String EVENT_DISCOUNT = "행사할인              %15s%n";
    public static final String MEMBERSHIP_DISCOUNT = "멤버십할인          %15s%n";
    public static final String TOTAL_PAYMENT = "내실돈               %,15d%n";
    public static final String ADDITIONAL_STATUS = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public static final String DELIMITER_PATTERN = "[\\[\\]]";
    public static final String QUANTITY_SEPARATOR = "-";
}
