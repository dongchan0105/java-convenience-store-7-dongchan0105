package store.controller;

import static store.ENUM.ErrorCode.DUPLICATE_PRODUCT_NAME;

import java.util.HashSet;
import java.util.Set;
import store.repository.ProductRepository;
import store.validation.Validation;
import store.view.InputView;

public class InputController {
    private final Validation validation;
    private final InputView inputView;

    public InputController(ProductRepository productRepository) {
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
        Set<String> productNames = new HashSet<>();
        try {
            for (String item : purchase) {
                String productName = item.replaceAll("[\\[\\]]", "").split("-")[0].trim();
                if (productNames.contains(productName)) {
                    throw new IllegalArgumentException(DUPLICATE_PRODUCT_NAME + productName);
                }
                productNames.add(productName);
                validation.purchaseGoodsValidator(item);
            }
            return true;
        } catch (IllegalArgumentException e) {
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
