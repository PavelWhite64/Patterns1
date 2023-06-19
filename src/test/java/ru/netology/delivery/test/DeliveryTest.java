package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.Info;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure",new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        Info info = DataGenerator.generateInfo("ru");
        String city = info.getCity();
        String name = info.getName();
        String phone = info.getPhone();
        String date = DataGenerator.generateDate(4);
        String newDate = DataGenerator.generateDate(6);

//         Configuration.holdBrowserOpen = true;
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(2000));
        $x("//*[@class='notification__content']").shouldHave(exactText("Встреча успешно запланирована на " + date), Duration.ofMillis(2000));

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(newDate);
        $$("button").find(exactText("Запланировать")).click();
        $$("button").find(exactText("Перепланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(2000));
        $x("//*[@class='notification__content']").shouldHave(exactText("Встреча успешно запланирована на " + newDate), Duration.ofMillis(2000));
    }
}
