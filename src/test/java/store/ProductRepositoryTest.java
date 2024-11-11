package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.repository.ProductRepository;
import store.domain.Product;

import java.util.List;
import store.domain.Receipt;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository = new ProductRepository();
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        assertFalse(products.isEmpty(), "상품 목록이 잘 들어감.");
    }

    @Test
    public void testFindAnyByName() {
        Product product = productRepository.findAnyByName("사이다");
        assertNotNull(product, "사이다 제품이 존재해야 합니다.");
        assertEquals("사이다", product.getName(), "사이다 제품 이름이 일치해야 합니다.");
    }

    @Test
    public void testUpdateRepository() {
        Product product = productRepository.findAnyByName("사이다");
        int initialQuantity = product.getQuantity();

        // 구매 후 재고 반영
        Receipt receipt = new Receipt("사이다", 3,0, product.getPrice(), 1);
        productRepository.updateRepository(receipt);

        // 재고 감소 확인
        assertEquals(initialQuantity - 3, product.getQuantity(), "재고 반영이 적절하게 반영되는지");
    }
}

