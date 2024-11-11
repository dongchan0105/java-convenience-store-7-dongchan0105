package store;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.repository.PromotionRepository;
import store.domain.Promotion;

class PromotionRepositoryTest {

    private PromotionRepository promotionRepository;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepository();
    }

    @Test
    void 프로모션_파일_읽어졌나_확인() {
        assertThat(promotionRepository.getPromotion("탄산2+1")).isNotNull();
    }

    @Test
    void 프로모션_정보_조회() {
        Promotion promotion = promotionRepository.getPromotion("탄산2+1");
        assertThat(promotion.getBuyQuantity()).isEqualTo(2);
        assertThat(promotion.getGiveAwayQuantity()).isEqualTo(1);
    }
}
