package store.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.Repository.ProductRepository;
import store.domain.Product;
import store.domain.Receipt;
import store.service.ProductService;

public class StoreController {
    private final ProductService productService;
    private final InputController inputController;
    private final ProductRepository productRepository;
    private List<Receipt> receiptList;

    public StoreController() {
        this.productRepository = new ProductRepository();
        this.productService = new ProductService(productRepository);
        this.inputController = new InputController(productRepository);
        receiptList = new ArrayList<Receipt>();
    }

    public void processPurchase() {
        String[] purchaseList = inputController.getPurchaseList();
        Map<Product, Integer> purchaseMap = parsePurchaseList(purchaseList);
        receiptList=productService.processPurchase(purchaseMap);
    }

    private Map<Product, Integer> parsePurchaseList(String[] purchaseList) {
        Map<Product, Integer> purchaseMap = new HashMap<>();
        for (String item : purchaseList) {
            String[] parts = item.split("-");
            int quantity = Integer.parseInt(parts[1]);
            purchaseMap.put(productRepository.findByName(parts[0]), quantity);
        }
        return purchaseMap;
    }

    public void reflectRepository
}

