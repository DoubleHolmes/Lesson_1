import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MtsCheck {
    private WebDriver driver;
    private HomePage homePage;

    @BeforeEach
    public void driverSetUp() {
        driver = new ChromeDriver();
        driver.get("https://www.mts.by/");
        homePage = new HomePage(driver);
        homePage.closeCookieBanner(); // Закрываем баннер с куками
    }

    @Test
    @Description("Проверка текста в блоке")
    public void testOpen() {
        String actualText = homePage.getHeaderText();
        Assertions.assertEquals("Онлайн пополнение\nбез комиссии", actualText);
    }

    @Test
    @Description("Проверяем наличие логотипов на странице")
    public void testLogo() {
        Assertions.assertTrue(homePage.isLogoDisplayed(), "Логотип не найден!");
    }

    @Test
    @Description("Проверяем работу ссылки на странице")
    public void testLink() {
        String originalUrl = homePage.clickLinkAndGetUrl();
        // Добавьте проверки для ссылки, если нужно
    }

    @Test
    @Description("Проверить надписи в незаполненных полях в Услуги связи")
    public void testEmptyField() {
        Assertions.assertEquals("Номер телефона", homePage.getPlaceholderPhone());
        Assertions.assertEquals("Сумма", homePage.getPlaceholderSum());
        Assertions.assertEquals("E-mail для отправки чека", homePage.getPlaceholderEmail());
    }

    @Test
    @Description("Проверить надписи в незаполненных полях в Домашний интернет")
    public void testSelectInternetTabAndVerifyPlaceholders() {
        homePage.selectInternetTabAndVerifyPlaceholders();
    }

    @Test
    @Description("Проверить надписи в незаполненных полях в Рассрочка")
    public void testSelectInstallmentAndVerifyPlaceholders() {
        homePage.selectInstallmentAndVerifyPlaceholders();
    }

    @Test
    @Description("Проверить надписи в незаполненных полях в Задолженность")
    public void testSelectDebitAndVerifyPlaceholders() {
        homePage.selectDebitAndVerifyPlaceholders();
    }

    @Test
    @Description("Заполняем поля и проверяем работу кнопки Продолжить")
    public void testContinue() throws InterruptedException {
        homePage.enterPhoneNumber("297777777");
        homePage.enterSum("10.00 BYN");
        homePage.clickContinueButton(); // Нажимаем кнопку "Продолжить"
// Задержка на 5 секунд
        Thread.sleep(5000);
        // Переключаемся на фрейм
        homePage.setFrame(); // Вызов метода для переключения на iframe

        // Проверки внутри фрейма
        String expectedAmount = "10.00 BYN";
        String expectedPhoneNumber = "Оплата: Услуги связи Номер:375297777777";

        homePage.verifyDisplayedAmount(expectedAmount);
        homePage.verifyButtonText("Оплатить " + expectedAmount);
        homePage.verifyPaymentNumber(expectedPhoneNumber);

        homePage.verifyLabel("cardNumber", "Номер карты");
        homePage.verifyLabel("period", "Срок действия");
        homePage.verifyLabel("cvc", "CVC");
        homePage.verifyLabel("name", "Имя держателя (как на карте)");

        homePage.verifyLogosPresent();

        driver.switchTo().defaultContent();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Закрытие драйвера
        }
    }
}