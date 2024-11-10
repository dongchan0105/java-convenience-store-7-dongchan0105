package store.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Promotion {
    private static final Map<String, Promotion> promotionMapping = new HashMap<>();
    private Clock clock;
    private String name;
    private final int buyQuantity;
    private final int giveAwayQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyQuantity, int giveAwayQuantity, LocalDate startDate, LocalDate endDate) {
        this(name, buyQuantity, giveAwayQuantity, startDate, endDate, Clock.systemDefaultZone());
    }

    // Clock을 받는 생성자
    public Promotion(String name, int buyQuantity, int giveAwayQuantity, LocalDate startDate, LocalDate endDate, Clock clock) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.giveAwayQuantity = giveAwayQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clock = clock;
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


    public boolean isActive() {
        LocalDate today = LocalDate.now(clock);
        return (today.isEqual(startDate) || today.isAfter(startDate)) && (today.isEqual(endDate) || today.isBefore(endDate));
    }


    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGiveAwayQuantity() {
        return giveAwayQuantity;
    }
}
