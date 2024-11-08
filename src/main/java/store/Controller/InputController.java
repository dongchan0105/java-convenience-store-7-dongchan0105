package store.Controller;

import static store.ENUM.ErrorCode.RETRY_MESSAGE;

import store.Repository.ProductRepository;
import store.Validation.Validation;
import store.View.InputView;

public class InputController {
    private final Validation validation;
    private final ProductRepository productRepository;
    private final InputView inputView;

    public InputController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        inputView = new InputView(productRepository);
        validation=new Validation(productRepository);
    }

    public String[] getPurchaseList(){
        inputView.openConvenienceStore();
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

    public static void showShortageMessage(String productName, int shortage){
        InputView.showShortageMessage(productName, shortage);
    }

    public static boolean isHaveMembership(){
        return InputView.isHaveMembership().equalsIgnoreCase("Y");
    }

    public static boolean getUserConfirm(){
        return InputView.getUserConfirmation().equalsIgnoreCase("Y");
    }

    public static boolean getAdditionalUserConfirm (String productName, int additionalQuantity){
        return InputView.showAdditionalQuantityMessage(productName, additionalQuantity).equalsIgnoreCase("Y");
    }
}
