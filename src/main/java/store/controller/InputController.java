package store.controller;

import store.repository.ProductRepository;
import store.validation.Validation;
import store.view.InputView;

public class InputController {
    private final Validation validation;
    private final ProductRepository productRepository;
    private final InputView inputView;

    public InputController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        inputView = new InputView(productRepository);
        validation = new Validation(productRepository);
    }

    public String[] getPurchaseList() {
        inputView.openConvenienceStore();
        return getValidPurchaseList();
    }

    private String[] getValidPurchaseList() {
        while (true) {
            String[] purchase = InputView.getPurchaseList().split(",");
            if (isValidPurchase(purchase)) {
                return purchase;
            }
        }
    }

    private boolean isValidPurchase(String[] purchase) {
        try {
            for (String item : purchase) {
                validation.purchaseGoodsValidator(item);
            }
            return true;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static void showShortageMessage(String productName, int shortage) {
        InputView.showShortageMessage(productName, shortage);
    }

    public boolean isHaveMembership() {
        String input = InputView.isHaveMembership();
        while (true) {
            try {
                validation.yesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input = InputView.isHaveMembership();
            }
        }
    }

    public boolean getUserConfirm() {
        String input = InputView.getUserConfirmation();
        while (true) {
            try {
                validation.yesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input = InputView.getUserConfirmation();
            }
        }
    }

    public boolean getAdditionalUserConfirm(String productName, int additionalQuantity) {
        String input = InputView.showAdditionalQuantityMessage(productName, additionalQuantity);
        while (true) {
            try {
                validation.yesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input = InputView.showAdditionalQuantityMessage(productName, additionalQuantity);
            }
        }
    }
}
