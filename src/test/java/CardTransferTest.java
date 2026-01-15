import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashBoardPage;
import page.LoginPage;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.generateValidAmount;
import static data.DataHelper.generateInvalidAmount;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CardTransferTest {

    DashBoardPage dashBoardPage;
    DataHelper.CardInfo firstCard;
    DataHelper.CardInfo secondCard;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        dashBoardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(DataHelper.getAuthInfo()));

        firstCard = DataHelper.getFirstCardInfo();
        secondCard = DataHelper.getSecondCardInfo();

        // Получаем актуальные балансы
        firstCardBalance = dashBoardPage.getCardBalance(firstCard);
        secondCardBalance = dashBoardPage.getCardBalance(secondCard);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;

        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        dashBoardPage.reloadDashboardPage();

        assertAll(
                () -> dashBoardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCard, expectedBalanceSecondCard)
        );
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var amount = generateValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;

        var transferPage = dashBoardPage.selectCardToTransfer(firstCard);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);
        dashBoardPage.reloadDashboardPage();

        assertAll(
                () -> dashBoardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCard, expectedBalanceSecondCard)
        );
    }

    @Test
    void shouldTransferMinimumAmount() {
        var amount = 1;
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;

        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        dashBoardPage.reloadDashboardPage();

        assertAll(
                () -> dashBoardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCard, expectedBalanceSecondCard)
        );
    }

    @Test
    void shouldNotTransferWhenAmountExceedsBalance() {
        var invalidAmount = generateInvalidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance; // Баланс не должен измениться
        var expectedBalanceSecondCard = secondCardBalance; // Баланс не должен измениться

        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);

        // Пытаемся перевести сумму больше баланса
        // Это может вызвать ошибку, но мы проверяем, что балансы не изменились
        transferPage.makeValidTransfer(String.valueOf(invalidAmount), firstCard);
        dashBoardPage.reloadDashboardPage();

        assertAll(
                () -> dashBoardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCard, expectedBalanceSecondCard)
        );
    }
}