package store.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Promotion {
    private static final Map<String, Promotion> promotionMapping = new HashMap<>();

    private String name;
    private int buyQuantity;
    private int giveAwayQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private  Clock clock;

    private Promotion(String name, int buyQuantity, int giveAwayQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.giveAwayQuantity = giveAwayQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        clock=Clock.systemDefaultZone();
    }


    public static void addPromotion(String name, int buyQuantity, int getQuantity, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        Promotion promotion = new Promotion(name, buyQuantity, getQuantity, start, end);
        promotionMapping.put(name, promotion);
    }

    public static Promotion getPromotion(String promotionName) {
        return promotionMapping.get(promotionName);
    }

    public boolean isActive(LocalDate currentDate) {
        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGiveAwayQuantity() {
        return giveAwayQuantity;
    }
}
