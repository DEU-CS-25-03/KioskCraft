package DataTransferObject;

import java.util.ArrayList;
import java.util.List;

public class CardPayment {
    /**
     * cardPayment 리스트
     * - 사용자가 카드 결제된 내역을 저장
     */
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvc;
    private double amount;
    private String orderId; // 결제 대상 주문 번호

    public CardPayment(String cardNumber, String cardHolder, String expiryDate, String cvc, double amount, String orderId) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.amount = amount;
        this.orderId = orderId;
    }

    public static List<Object[]> cardPayment = new ArrayList<>();
}
