package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Promotion {
    private String name;
    private final int buyQuantity;
    private final int giveAwayQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyQuantity, int giveAwayQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.giveAwayQuantity = giveAwayQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate today = getCurrentDate();
        return (today.isEqual(startDate) || today.isAfter(startDate)) && (today.isEqual(endDate) || today.isBefore(endDate));
    }

    private LocalDate getCurrentDate() {
        LocalDateTime now = DateTimes.now();
        return now.toLocalDate();
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGiveAwayQuantity() {
        return giveAwayQuantity;
    }

    public static Promotion createPromotion(String name, int buyQuantity, int giveQuantity, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        return new Promotion(name, buyQuantity, giveQuantity, start, end);
    }
}
