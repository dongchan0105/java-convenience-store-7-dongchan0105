package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;
import store.Controller.InputController;
import store.repository.ProductRepository;
import store.service.ProductService;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest extends NsTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private InputController inputController;

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

    @Test
    public void testGetReceiptInfo() {
        // Given: 사용자가 사이다 2개를 구매하고자 함
        String purchaseInput = "[사이다-2]";
        String hasMembership = "y";
        String additionalPurchase = "y";

        // When: 해당 입력으로 테스트 실행
        assertSimpleTest(() -> {
            run(purchaseInput, hasMembership, additionalPurchase,"n");

            // Then: 예상 결과가 출력에 포함되어 있는지 확인
            assertThat(output().replaceAll("\\s", "")).contains("내실돈2,000");
        });
    }
}





