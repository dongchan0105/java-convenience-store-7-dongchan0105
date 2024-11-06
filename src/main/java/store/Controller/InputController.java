package store.Controller;

import static store.ENUM.ErrorCode.RETRY_MESSAGE;

import store.Repository.ProductRepository;
import store.Validation.Validation;
import store.View.InputView;

public class InputController {
    private final Validation validation = new Validation();
    private final ProductRepository productRepository;

    public InputController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        InputView inputView = new InputView(productRepository);
    }

    public String[] getPurchaseList(){
        String[] purchase=InputView.getPurchaseList().split(",");
        while(true) {
            try {
                for (String i : purchase) {
                    validation.purchaseGoodsValidator(i);
                }
                return purchase;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println(RETRY_MESSAGE);
            }
        }
    }
}
