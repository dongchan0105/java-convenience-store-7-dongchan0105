package store.View;

import static store.Constant.Constants.CONVENIENCE_ENTER;
import static store.Constant.Constants.MEMBERSHIP_DISCOUNT;
import static store.Constant.Constants.PURCHASE_QUANTITY;

import camp.nextstep.edu.missionutils.Console;
import store.Repository.ProductInventory;

public class InputView {
    private final ProductInventory productInventory;

    public InputView(ProductInventory productInventory) {
        this.productInventory = productInventory;
    }

    public void openConvenienceStore() {
        System.out.println(CONVENIENCE_ENTER);
        productInventory.displayProducts();
    }

    public static String getPurchaseList() {
        System.out.println(PURCHASE_QUANTITY);
        return Console.readLine();
    }

    public static void isHaveMembership(){
        System.out.println(MEMBERSHIP_DISCOUNT);
        String membership=Console.readLine();
    }
}
