package store.Validation;

import static store.ENUM.ErrorCode.*;

import java.util.Arrays;

public class Validation {

    public void purchaseGoodsValidator(String input){
        isEnough(input);
        isEXIST(input);
    }

    public void isEXIST(String request){
        request = request.replace("[", "").replace("]", "");
        String[] requirements = request.split("-");
        String productName = requirements[0].trim();
        if(Arrays.stream(Product.values()).noneMatch(P->P.name().equals(productName))){
            throw new IllegalArgumentException(NON_ARTICLE.getMessage());
        }
    }

    public void isEnough(String request){
        request = request.replace("[", "").replace("]", "");
        String[] requirements = request.split("-");
        // 제품명과 수량을 분리하여 저장
        String productName = requirements[0].trim();
        int quantity = Integer.parseInt(requirements[1].trim());
        if(Arrays.stream(Product.values())
                .filter(product -> product.getName().equals(productName))
                .mapToInt(Product::getStock)
                .sum()-quantity< 0){
            throw new IllegalArgumentException(STOCK_SHORTAGE.getMessage());
        }
    }
}
