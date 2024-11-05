package store.Controller;

import static store.ENUM.ErrorCode.RETRY_MESSAGE;

import java.util.Arrays;
import store.Validation.Validation;
import store.View.InputView;

public class InputController {
    Validation validation = new Validation();

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
