package com.rokitds.la.hostfile;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static com.codeborne.selenide.Selenide.$;

/**
 * For each of the running hosts on Linux Academy outputs public IP address at the time that this is run followed by
 * the name of the host.
 * TODO add the server role as a comment
 */
public class LinuxAcademyLocalHostFileGenerator extends LinuxAcademyTest {


    public static final int MAX_NUMBER_OF_LA_HOSTS = 6;
    public static final String SED_SCRIPT_TO_UPDATE_LA_HOSTENTRIES = "./update_la_hostentries.sed";

    // Thanks to https://stackoverflow.com/users/3874768/repzero
    // for https://stackoverflow.com/questions/28457543/sed-replace-ip-in-hosts-file-using-hostname-as-pattern
    // which was modified slightly
    public static final String SED_COMMAND="s/^ *[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+ (%s)/%s \\1/%s";

    @Test
    /**
     *
     * Generates a sed script named SED_SCRIPT_TO_UPDATE_LA_HOSTENTRIES
     *
     * The sed script should be run against /etc/hosts as follows
     * <code>sed -r -f update_la_hosts /etc/hosts</code>
     *
     * Matches on Linux Academy Hostnames and replaces their IP addresses with the new IP addresses found on
     * LinuxAcademy.com > Cloud Servers
     *
     * <h1>Assumptions</h1>
     * Your Linux Academy hosts are entries already present in your /etc/hosts file
     * However, hostfile entries with you latest IP addresses are sent to STDOUT
     */
    public void generateLocalHostFileEntryForAllMachinesRunning(){
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SED_SCRIPT_TO_UPDATE_LA_HOSTENTRIES))){
            for (int machineNumber = 1; machineNumber < MAX_NUMBER_OF_LA_HOSTS; machineNumber++) {
                SelenideElement machineStatus = $(By.xpath("//*[@id=\"status_" + machineNumber + "\"]"));
                if (machineStatus.innerHtml().equalsIgnoreCase("running")) {

                    SelenideElement machinePublicIP = $(By.xpath("//*[@id=\"public_ip_" + machineNumber + "\"]/a"));
                    SelenideElement publicHostname = $(By.xpath("//*[@id=\"hostname_" + machineNumber + "\"]"));

                    String hostname = publicHostname.innerHtml().substring(0, publicHostname.innerHtml().indexOf("|"));
                    String ipAddr = machinePublicIP.innerHtml();

                    System.out.println(ipAddr + " " + hostname + System.getProperty("line.separator"));

                    br.write(String.format(SED_COMMAND,
                                    hostname,
                                    ipAddr,
                                    System.getProperty("line.separator")));

                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
