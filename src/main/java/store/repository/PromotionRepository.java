package store.repository;

import static store.ENUM.ErrorCode.IO_ERROR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import store.domain.Promotion;

public class PromotionRepository {
    private static final Map<String, Promotion> promotionMapping = new HashMap<>();

    public PromotionRepository() {
        loadPromotions();
    }

    private void loadPromotions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md"))) {
            reader.readLine();
            reader.lines().forEach(this::addPromotionFromLine);
        } catch (IOException e) {
            System.out.println(IO_ERROR);
        }
    }

    private void addPromotionFromLine(String line) {
        PromotionComponent promoComponent = getPromotionComponent(line);
        Promotion promotion = Promotion.createPromotion(
                promoComponent.name(),
                promoComponent.buyQuantity(),
                promoComponent.getQuantity(),
                promoComponent.startDate(),
                promoComponent.endDate()
        );
        addPromotion(promoComponent.name(), promotion);
    }

    private PromotionComponent getPromotionComponent(String line) {
        String[] parts = line.split(",");
        String name = parts[0].trim();
        int buyQuantity = Integer.parseInt(parts[1].trim());
        int getQuantity = Integer.parseInt(parts[2].trim());
        String startDate = parts[3].trim();
        String endDate = parts[4].trim();
        return new PromotionComponent(name, buyQuantity, getQuantity, startDate, endDate);
    }

    private void addPromotion(String name, Promotion promotion) {
        promotionMapping.put(name, promotion);
    }

    public static Promotion getPromotion(String promotionName) {
        return promotionMapping.get(promotionName);
    }

    private record PromotionComponent(String name, int buyQuantity, int getQuantity, String startDate, String endDate) {
    }
}
