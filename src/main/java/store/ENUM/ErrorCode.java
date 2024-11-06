package store.ENUM;

public enum ErrorCode {
    STOCK_SHORTAGE ("[ERROR] 수량이 부족합니다!"),
    NON_ARTICLE("[ERROR] 존재하지 않는 물품입니다"),
    RETRY_MESSAGE("[ERROR] 다시 입력해주세요."),
    IO_ERROR("[ERROR] 파일을 읽는 중 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {this.message = message;}

    public String getMessage() {return message;}
}
