package store.View;

import static store.Constant.Constants.CONVENIENCE_ENTER;
import static store.Constant.Constants.MEMBERSHIP_DISCOUNT;
import static store.Constant.Constants.PURCHASE_QUANTITY;

import camp.nextstep.edu.missionutils.Console;
import store.Repository.ProductRepository;
import store.domain.Product;

public class InputView {
    private final ProductRepository productRepository;

    public InputView(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void openConvenienceStore() {
        System.out.println(CONVENIENCE_ENTER);
        productRepository.getAllProducts().forEach(product -> {
            String promotionInfo = "";
            OutputInfo outputinfo = getOutputInfo(product, promotionInfo);
            System.out.printf("- %s %d원 %s %s\n", product.getName(), product.getPrice(), outputinfo.stockInfo(),
                    outputinfo.promotionInfo());
        });
    }

    private static OutputInfo getOutputInfo(Product product, String promotionInfo) {
        if (product.getPromotion() != null) {
            promotionInfo = product.getPromotion();}
        String stockInfo;
        if (product.getQuantity() > 0) {
            stockInfo = product.getQuantity() + "개";
        } else {
            stockInfo = "재고 없음";
        }
        return new OutputInfo(promotionInfo, stockInfo);
    }

    private record OutputInfo(String promotionInfo, String stockInfo) {
    }

    public static String getPurchaseList() {
        System.out.println(PURCHASE_QUANTITY);
        return Console.readLine();
    }

    public static void isHaveMembership(){
        System.out.println(MEMBERSHIP_DISCOUNT);
        String membership=Console.readLine();
    }
}
