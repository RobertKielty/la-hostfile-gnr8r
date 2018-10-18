package com.rokitds.la.hostfile;

import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenide.tools.Highlighter;

import java.time.Duration;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.addListener;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LinuxAcademyTest {

    public static final int TIME_OUT_IN_SECONDS = 5;
    public static final int POLLING_INTERVAL_IN_MILLI_S = 500;
    public static final String XPATH_OF_FIFTH_LA_HOST = "//*[@id=\"status_5\"]";
    public static final String ID_CLOUD_SERVERS_ANCHOR = "//*[@id=\"cloud-servers-link\"]/a";
    public static final String HTTP_LINUXACADEMY_COM = "http://linuxacademy.com";

    private static String laUsername = System.getProperty("linuxacademy.username");
    private static String laPassword = System.getProperty("linuxacademy.password");

    @Rule
    public ScreenShooter screenShooter = ScreenShooter.failedTests();

    @BeforeClass
    public static void openCloudMachines() {
        if (laUsername.length()==0){
            System.err.println("ERROR : linuxacademy.username not set properly on the credentials store. Exiting.");
            System.exit(100);
        } else {
            System.out.print("laUsername : " + laUsername);
        }

        baseUrl = HTTP_LINUXACADEMY_COM;
        startMaximized = false;
        browser = "chrome";

        addListener(new Highlighter());

        open("/");
        loginUsingGoogle();

        $(By.xpath(ID_CLOUD_SERVERS_ANCHOR)).click();
        // Add an explicit wait with a 5 second timeout that polls every 500 ms to find the status of the fifth machine
        new WebDriverWait(getWebDriver(), TIME_OUT_IN_SECONDS)
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL_IN_MILLI_S))
                .ignoring(NoSuchElementException.class)
                .withMessage("Couldn't find status of the 5th host")
                .until(visibilityOfElementLocated(By.xpath(XPATH_OF_FIFTH_LA_HOST)));

    }


    @AfterClass
    public static void logout() {
        closeWebDriver();
    }

    private static void loginUsingGoogle() {
        $("#nav-la-public > li:nth-child(6) > a").click();
        $("#auth0-lock-container-1 > div > div.auth0-lock-center > form > div > div > div:nth-child(3) > span > div > div > div > div > div > div > div > div > div.auth-lock-social-buttons-pane > div > button:nth-child(3) > div.auth0-lock-social-button-icon").click();
        $(byXpath("//*[@id=\"identifierId\"]")).val(laUsername).pressEnter();
        $(byXpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")).val(laPassword).pressEnter();
    }

}
