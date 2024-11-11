package store;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import store.domain.Receipt;
import store.service.MakeReceiptService;

import java.util.List;

class MakeReceiptServiceTest {

    @Test
    void 프로모션_할인_계산() {
        List<Receipt> receipts = List.of(
                new Receipt("콜라", 5, 3, 1000, 2),
                new Receipt("사이다", 8, 5, 1200, 3)
        );
        int promoDiscount = MakeReceiptService.calculatePromoDiscount(receipts);
        assertThat(promoDiscount).isEqualTo(1000 * 2 + 1200 * 3);
    }

    @Test
    void 멤버십_할인_계산() {
        List<Receipt> receipts = List.of(
                new Receipt("콜라", 5, 2, 1000, 0)
        );
        int membershipDiscount = MakeReceiptService.calculateMembershipDiscount(receipts, true);
        assertThat(membershipDiscount).isEqualTo(600);  // 최대 할인 한도
    }

    @Test
    void 총구매액_계산() {
        List<Receipt> receipts = List.of(
                new Receipt("콜라", 5, 3, 1000, 2),
                new Receipt("사이다", 8, 5, 1200, 3)
        );
        int totalPurchase = MakeReceiptService.calculateTotalPurchase(receipts);
        assertThat(totalPurchase).isEqualTo((5 * 1000) + (8 * 1200));
    }

    @Test
    void 최종_결제_금액_계산() {
        List<Receipt> receipts = List.of(
                new Receipt("콜라", 5, 3, 1000, 2),
                new Receipt("사이다", 8, 5, 1200, 3)
        );
        int promoDiscount = MakeReceiptService.calculatePromoDiscount(receipts);
        int membershipDiscount = MakeReceiptService.calculateMembershipDiscount(receipts, true);
        int finalPrice = MakeReceiptService.calculateFinalPrice(receipts, promoDiscount, membershipDiscount);

        assertThat(finalPrice).isEqualTo((5 * 1000 + 8 * 1200) - promoDiscount - membershipDiscount);
    }
}
