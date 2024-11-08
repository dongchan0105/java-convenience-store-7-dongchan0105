package store.domain;

import java.util.HashMap;
import java.util.Map;

public class Promotion {
    private static final Map<String, String> promotionMapping = new HashMap<>();

    static {
        promotionMapping.put("MD추천상품", "1+1");
        promotionMapping.put("반짝할인", "1+1");
        promotionMapping.put("탄산2+1", "2+1");
        promotionMapping.put("null",null);
        // 다른 프로모션 이름을 추가할 수 있습니다.
    }

    public static String getPromotionPolicy(String promotionName) {
        return promotionMapping.get(promotionName);
    }
}
