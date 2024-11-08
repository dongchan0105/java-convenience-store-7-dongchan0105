package store.Validation;

import static store.ENUM.ErrorCode.*;

import store.Repository.ProductRepository;
import store.domain.Product;

public class Validation {
    private final ProductRepository productRepository;

    public Validation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void purchaseGoodsValidator(String input){
        isEnough(input);
        isEXIST(input);
    }

    public void isEXIST(String request){
        request = request.replace("[", "").replace("]", "");
        String[] requirements = request.split("-");
        String productName = requirements[0].trim();
        if(productRepository.findAnyByName(productName)==null){
            throw new IllegalArgumentException(NON_ARTICLE.getMessage());
        }
    }

    public void isEnough(String request){
        request = request.replace("[", "").replace("]", "");
        String[] requirements = request.split("-");
        // 제품명과 수량을 분리하여 저장
        String productName = requirements[0].trim();
        int quantity = Integer.parseInt(requirements[1].trim());
        if(productRepository.getAllProducts().stream()
                .filter(product -> product.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum()-quantity< 0){
            throw new IllegalArgumentException(STOCK_SHORTAGE.getMessage());
        }
    }
}
