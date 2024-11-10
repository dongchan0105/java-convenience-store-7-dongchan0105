package store.ENUM;

public enum ErrorCode {
    STOCK_SHORTAGE("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    NON_ARTICLE("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    RETRY_MESSAGE("[ERROR] 다시 입력해주세요."),
    IO_ERROR("[ERROR] 파일을 읽는 중 오류가 발생했습니다."),
    INVALID_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
