package store.Controller;

import java.util.HashMap;
import java.util.Map;
import store.service.ProductService;

public class StoreController {
    private final ProductService productService;
    private final InputController inputController;

    public StoreController(ProductService productService, InputController inputController) {
        this.productService = productService;
        this.inputController = inputController;
    }

    public void processPurchase() {
        String[] purchaseList = inputController.getPurchaseList();
        Map<String, Integer> purchaseMap = parsePurchaseList(purchaseList);

        productService.processPurchase(purchaseMap);
    }

    private Map<String, Integer> parsePurchaseList(String[] purchaseList) {
        Map<String, Integer> purchaseMap = new HashMap<>();
        for (String item : purchaseList) {
            String[] parts = item.split("-");
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            purchaseMap.put(productName, quantity);
        }
        return purchaseMap;
    }
}

