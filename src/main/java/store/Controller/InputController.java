package store.Controller;

import static store.ENUM.ErrorCode.RETRY_MESSAGE;

import store.Repository.ProductRepository;
import store.Validation.Validation;
import store.View.InputView;

public class InputController {
    private final Validation validation;
    private final ProductRepository productRepository;

    public InputController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        InputView inputView = new InputView(productRepository);
        validation=new Validation(productRepository);
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

    public static boolean isHaveMembership(){
        return InputView.isHaveMembership().equalsIgnoreCase("Y");
    }

    public static boolean getUserConfirm(){
        return InputView.getUserConfirmation().equalsIgnoreCase("Y");
    }

    public static void showAdditionalQuantityMessage (String productName, int additionalQuantity){
        InputView.showAdditionalQuantityMessage(productName, additionalQuantity);
    }
}
