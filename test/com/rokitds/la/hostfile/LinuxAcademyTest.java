package com.rokitds.la.hostfile;

import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.disappears;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

public class LinuxAcademyTest {
    private static String laUsername = System.getProperty("lauser");
    private static String laPassword = System.getProperty("lapassword");

    @Rule
    public ScreenShooter screenShooter = ScreenShooter.failedTests();

    @BeforeClass
    public static void openCloudMachines() {
        if (laUsername.length()==0){
            System.err.println("ERROR : linuxacademy.username not set properly on the credentials store. Exiting.");
            System.exit(100);
        } else {
            System.out.print("laUsername : ");
        }

        System.out.println(laUsername);
        timeout = 10000;
        baseUrl = "http://linuxacademy.com";
        startMaximized = false;
        browser = "chrome";

//        addListener(new Highlighter());

        open("/");
        loginUsingGoogle();

        $(By.xpath("//*[@id=\"cloud-servers-link\"]/a")).click();

    }

    protected static void waitUntilPagesIsLoaded() {
        $(byText("email")).waitUntil(disappears, 2000000);
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
