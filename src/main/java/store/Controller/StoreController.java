package store.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.Receipt;
import store.service.ProductService;

public class StoreController {
    private final ProductService productService;
    private final InputController inputController;
    private List<Receipt> receiptList;

    public StoreController(ProductService productService, InputController inputController) {
        this.productService = productService;
        this.inputController = inputController;
        receiptList = new ArrayList<Receipt>();
    }

    public void processPurchase() {
        String[] purchaseList = inputController.getPurchaseList();
        Map<String, Integer> purchaseMap = parsePurchaseList(purchaseList);

        receiptList=productService.processPurchase(purchaseMap);
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

