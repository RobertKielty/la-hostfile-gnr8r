package com.rokitds.la.hostfile;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

/**
 * Create a latesthostfile with current IP Addresses of your running Linux Academy hosts.
 * TODO add the server role as a comment
 */
public class LinuxAcademyLocalHostFileGenerator extends LinuxAcademyTest {


    private static final int MAX_NUMBER_OF_LA_HOSTS = 6;

    @Test
    public void getLatestIPaddresses(){
        Map <String, String> latestIPMap = new HashMap<>();
        for (int machineNumber = 1; machineNumber < MAX_NUMBER_OF_LA_HOSTS; machineNumber++) {
                SelenideElement machineStatus = $(By.xpath("//*[@id=\"status_" + machineNumber + "\"]"));
                if (machineStatus.innerHtml().equalsIgnoreCase("running")) {

                    SelenideElement machinePublicIP = $(By.xpath("//*[@id=\"public_ip_" + machineNumber + "\"]/a"));
                    SelenideElement publicHostname = $(By.xpath("//*[@id=\"hostname_" + machineNumber + "\"]"));

                    String hostname = publicHostname.innerHtml().substring(0, publicHostname.innerHtml().indexOf("|"));
                    String ipAddr = machinePublicIP.innerHtml();

                    latestIPMap.put(hostname,ipAddr);

                }
                generateLatestHostfile(latestIPMap);
        }
    }

    private static void generateLatestHostfile(Map<String, String> latestPublicIPmap) {
        String systemHostsfile = "/etc/hosts";
        String latestHostsfile = "./hosts.latest";
        String newLine = System.getProperty("line.separator");
        java.nio.charset.Charset charset =
                StandardCharsets.UTF_8;

        try (Stream<String> stream = Files.lines(Paths.get(systemHostsfile));
             java.io.BufferedWriter writer = Files.newBufferedWriter(Paths.get(latestHostsfile), charset)
        ) {
            stream.forEach(line -> {
                for (Map.Entry<String, String> entry : latestPublicIPmap.entrySet()) {
                    String hostname = entry.getKey();
                    String ip = entry.getValue();
                    try {
                        if (line.contains(hostname)) {
                            writer.write(ip + " " + hostname + newLine);
                        } else {
                            writer.write(line);
                        }
                    } catch (IOException writeEx) {
                        writeEx.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
