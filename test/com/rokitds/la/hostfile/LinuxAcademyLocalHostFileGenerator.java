package com.rokitds.la.hostfile;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

/**
 * Create a latesthostfile with current IP Addresses of your running Linux Academy hosts.
 */
public class LinuxAcademyLocalHostFileGenerator extends LinuxAcademyTest {


    private static final int MAX_NUMBER_OF_LA_HOSTS = 6;
    private SelenideElement machineStatus;

    @Test
    public void getLatestIPaddresses(){
        Map <String, String> latestIPMap = new HashMap<>();

        for (int machineNumber = 1; machineNumber < MAX_NUMBER_OF_LA_HOSTS; machineNumber++) {
            try {
                machineStatus = $(By.xpath("//*[@id=\"status_" + machineNumber + "\"]"));
            } catch (Exception e){
                System.err.printf("There a problem reading the status of machine number $s . SKIPPING\n" , machineNumber);
            }
            if (machineStatus.innerHtml().equalsIgnoreCase("running")) {
                SelenideElement machinePublicIP = $(By.xpath("//*[@id=\"public_ip_" + machineNumber + "\"]/a"));
                SelenideElement publicHostname = $(By.xpath("//*[@id=\"hostname_" + machineNumber + "\"]"));

                String hostname = publicHostname.innerHtml().substring(0, publicHostname.innerHtml().indexOf("|")).trim();
                String ipAddr = machinePublicIP.innerHtml();

                latestIPMap.put(hostname,ipAddr);

            } else {
                System.err.printf("WARN: Machine number %s is %s\n",  machineNumber, machineStatus.innerHtml());
            }
        }
        generateLatestHostfile(latestIPMap);

    }

    private static void generateLatestHostfile(Map<String, String> latestPublicIPmap) {
        String systemHostsfile = "/etc/hosts";
        String gnr8dHostsfile = "./hosts.latest";
        java.nio.charset.Charset charset = StandardCharsets.UTF_8;

        try (Stream<String> sytemHostsfile = Files.lines(Paths.get(systemHostsfile));
             java.io.BufferedWriter gnr8dHostfileWriter = Files.newBufferedWriter(Paths.get(gnr8dHostsfile), charset)
        ) {
            sytemHostsfile.forEach(hostsfileEntry -> {
                try {
                    String [] currentHostEntry = (hostsfileEntry.split("\\s+"));

                    if (currentHostEntry.length > 1 && latestPublicIPmap.containsKey(currentHostEntry[1])) {
                        String ipReadFromLa = latestPublicIPmap.get(currentHostEntry[1]);
                        if (ipReadFromLa != null) {
                            gnr8dHostfileWriter.write(ipReadFromLa + " " + currentHostEntry[1] + " # updated by la-hostfile-generator at : " + LocalDateTime.now());
                            gnr8dHostfileWriter.newLine();
                        }
                    } else {
                        gnr8dHostfileWriter.write(hostsfileEntry);
                        gnr8dHostfileWriter.newLine();
                    }
                } catch (IOException  writeEx) {
                    writeEx.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
