package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import store.domain.Promotion;

public class PromotionRepository {

    public PromotionRepository() {
        loadPromotions();
    }

    private void loadPromotions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md"))) {
            reader.readLine();
            reader.lines().forEach(PromotionRepository::getPromotions);
        } catch (IOException e) {
            System.out.println("[ERROR] 프로모션 파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    private static void getPromotions(String line) {
        PromotionComponent promoComponent = getPromotionComponent(line);

        Promotion.addPromotion(promoComponent.name(), promoComponent.buyQuantity(), promoComponent.getQuantity(), promoComponent.startDate(),
                promoComponent.endDate());
    }

    private static PromotionComponent getPromotionComponent(String line) {
        String[] parts = line.split(",");
        String name = parts[0].trim();
        int buyQuantity = Integer.parseInt(parts[1].trim());
        int getQuantity = Integer.parseInt(parts[2].trim());
        String startDate = parts[3].trim();
        String endDate = parts[4].trim();
        return new PromotionComponent(name, buyQuantity, getQuantity, startDate, endDate);
    }

    private record PromotionComponent(String name, int buyQuantity, int getQuantity, String startDate, String endDate) {
    }

}
