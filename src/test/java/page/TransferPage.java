package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public TransferPage() {
        amountField.shouldBe(Condition.visible);
    }

    // Метод для валидного перевода
    public DashBoardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        return new DashBoardPage();
    }

    public TransferPage setAmount(int amount) {
        amountField.setValue(String.valueOf(amount));
        return this;
    }

    public TransferPage setFromCard(DataHelper.CardInfo cardInfo) {
        fromField.setValue(cardInfo.getNumber());
        return this;
    }

    public DashBoardPage makeTransfer() {
        transferButton.click();
        return new DashBoardPage();
    }

    public DashBoardPage cancelTransfer() {
        cancelButton.click();
        return new DashBoardPage();
    }

    public boolean isErrorMessageVisible() {
        return errorMessage.is(Condition.visible);
    }

    public String getErrorMessageText() {
        return errorMessage.getText();
    }
}