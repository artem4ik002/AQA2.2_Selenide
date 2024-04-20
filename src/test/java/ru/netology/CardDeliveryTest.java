
package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    public String dayMeeting(int daysToMeeting) {
        LocalDate dateOrder = LocalDate.now().plusDays(daysToMeeting);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String newDate = dtf.format(dateOrder);
        return newDate;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
    @Test
    void shouldFormCardDeliveryTest() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79533512085");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + dayMeeting(3)));
    }
    @Test
    void shouldTestFieldCity() {
        $("[data-test-id=city] input").setValue("Воркута").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79533512085");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestFieldName() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Ivanov Ivan");
        $("[data-test-id=phone] input").setValue("+79533512085");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestFieldPhone() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("89533512085");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestFieldAgreement() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79990000000");
        $(withText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid ").shouldBe(visible);
        $(".input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldTestFieldNonInputPhone() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

}
