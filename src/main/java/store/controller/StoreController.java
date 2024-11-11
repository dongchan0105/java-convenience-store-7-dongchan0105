package store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.OutputView;
import store.domain.Product;
import store.domain.Receipt;
import store.service.ProductService;

public class StoreController {
    private final ProductService productService;
    private final InputController IOController;
    private final ProductRepository productRepository;
    private List<Receipt> receiptList;

    public StoreController() {
        this.productRepository = new ProductRepository();
        PromotionRepository promotionRepository = new PromotionRepository();
        this.IOController = new InputController(productRepository);
        this.productService = new ProductService(productRepository, IOController);
        receiptList = new ArrayList<>();
    }

    public void storeOperation() {
        do {
            processPurchase();
        } while (OutputView.additionalPurchaseStatus());
    }

    private void processPurchase() {
        String[] purchaseList = IOController.getPurchaseList();
        Map<Product, Integer> purchaseMap = parsePurchaseList(purchaseList);
        receiptList = productService.getReceiptInfo(purchaseMap);
        OutputView.printReceipt(receiptList, IOController.checkMembershipStatus());
        reflectRepository(receiptList);
    }

    private Map<Product, Integer> parsePurchaseList(String[] purchaseList) {
        Map<Product, Integer> purchaseMap = new HashMap<>();
        for (String item : purchaseList) {
            String[] parts = item.replaceAll("[\\[\\]]", "").split("-");
            int quantity = Integer.parseInt(parts[1].trim());
            Product product = productRepository.findAnyByName(parts[0].trim());
            purchaseMap.put(product, purchaseMap.getOrDefault(product, 0) + quantity);
        }
        return purchaseMap;
    }

    private void reflectRepository(List<Receipt> receiptList) {
        for (Receipt receipt : receiptList) {
            productRepository.updateRepository(receipt);
        }
    }
}