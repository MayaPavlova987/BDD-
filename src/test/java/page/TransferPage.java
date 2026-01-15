package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.getSelectedText;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public TransferPage() {
        amountField.shouldBe(visible);
    }

    // Метод для валидного перевода
    public DashBoardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        return new DashBoardPage();
    }

    public void verifyErrorMessage(String expectedText) {
        errorMessage
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text(expectedText));
    }
}

