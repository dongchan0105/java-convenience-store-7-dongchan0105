package store.validation;

import static store.ENUM.ErrorCode.*;
import static store.constant.Constants.DELIMITER_PATTERN;
import static store.constant.Constants.QUANTITY_SEPARATOR;

import store.repository.ProductRepository;
import store.domain.Product;

public class Validation {
    private final ProductRepository productRepository;



    public Validation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void purchaseGoodsValidator(String input){
        bracketsFormat(input);
        validateInputFormat(input);
        validateExist(input);
        isEnough(input);
    }

    private void bracketsFormat(String request) {
        if (!request.startsWith("[") || !request.endsWith("]")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }


    public void validateInputFormat(String request) {
        String[] requirements = request.replaceAll(DELIMITER_PATTERN, "").split(QUANTITY_SEPARATOR);
        if (requirements.length != 2) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
        try {
            Integer.parseInt(requirements[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }


    public void validateExist(String request){
        String[] requirements = request.replaceAll(DELIMITER_PATTERN, "").split(QUANTITY_SEPARATOR);
        String productName = requirements[0].trim();
        if(productRepository.findAnyByName(productName)==null){
            throw new IllegalArgumentException(NON_ARTICLE.getMessage());
        }
    }

    public static void yesOrNo(String input){
        if(!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")){
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    public void isEnough(String request){
        String[] requirements = request.replaceAll(DELIMITER_PATTERN, "").split(QUANTITY_SEPARATOR);
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
