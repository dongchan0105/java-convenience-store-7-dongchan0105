package store.controller;

import static store.ENUM.ErrorCode.DUPLICATE_PRODUCT_NAME;
import static store.constant.Constants.DELIMITER_PATTERN;
import static store.constant.Constants.QUANTITY_SEPARATOR;

import java.util.HashSet;
import java.util.Set;
import store.repository.ProductRepository;
import store.validation.Validation;
import store.view.InputView;
import store.view.OutputView;

public class I_OController {
    private final Validation validation;
    private final InputView inputView;

    public I_OController(ProductRepository productRepository) {
        inputView = new InputView(productRepository);
        validation = new Validation(productRepository);
    }

    public String[] getPurchaseList() {
        OutputView.displayMessage(store.constant.Constants.CONVENIENCE_ENTER);
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
                String productName = extractProductName(item);
                if (isDuplicateProduct(productNames, productName)) {
                    return false;
                }
                productNames.add(productName);
                validation.purchaseGoodsValidator(item);
            }
            return true;
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return false;
        }
    }

    private String extractProductName(String item) {
        return item.replaceAll(DELIMITER_PATTERN, "").split(QUANTITY_SEPARATOR)[0].trim();
    }

    private boolean isDuplicateProduct(Set<String> productNames, String productName) {
        if (productNames.contains(productName)) {
            OutputView.printErrorMessage(DUPLICATE_PRODUCT_NAME + productName);
            return true;
        }
        return false;
    }

    public boolean checkMembershipStatus() {
        return validateYesOrNoInput(InputView.isHaveMembership());
    }

    public boolean getAdditionalUserConfirm(String productName, int additionalQuantity) {
        return validateYesOrNoInput(InputView.showAdditionalQuantityMessage(productName, additionalQuantity));
    }

    public boolean getUserConfirm() {
        return validateYesOrNoInput(InputView.getUserConfirmation());
    }

    private boolean validateYesOrNoInput(String input) {
        while (true) {
            try {
                Validation.yesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
                input = InputView.getUserConfirmation();
            }
        }
    }

    public static void showShortageMessage(String name, int shortage){
        InputView.showShortageMessage(name,shortage);
    }
}