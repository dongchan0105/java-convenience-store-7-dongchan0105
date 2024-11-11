package store;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.controller.InputController;
import store.domain.Receipt;
import store.domain.Product;
import store.repository.ProductRepository;
import store.service.ProductService;

import java.util.List;
import java.util.Map;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private InputController inputController;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        inputController = new InputController(productRepository);
        productService = new ProductService(productRepository, inputController);
    }

    @Test
    void 프로모션_없는_상품_구매() {
        Product product = productRepository.findAnyByName("비타민워터");
        List<Receipt> receipts = productService.getReceiptInfo(Map.of(product, 3));
        assertThat(receipts).hasSize(1);
        assertThat(receipts.get(0).getProductName()).isEqualTo("비타민워터");
        assertThat(receipts.get(0).getGiveaway()).isEqualTo(0);
    }


}






