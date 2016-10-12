package com.lendou;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class Program {

    private static final String URL_HOME = "http://192.168.1.1/ADV_home2.htm";

    private static final String RECEIVER = "ciri.rd@bjciri.com";

    private static MailService mailService = new MailService();

    public static void main( String[] args ) throws Exception {
        setAuth();
        String ip = getIP();

        String lastIP = StringUtils.EMPTY;
        while (true) {
            String curIP = getIP();
            if (StringUtils.isBlank(curIP) || StringUtils.equals(lastIP, curIP)) {
                continue;
            }

            mailService.sendMail(RECEIVER, ip);
            lastIP = ip;

            Thread.sleep(1800 * 1000);
        }
    }

    private static void setAuth() throws Exception {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("admin", "ranking1983".toCharArray());
            }
        });
    }

    private static String getIP() {

        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(URL_HOME);

            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
            httpConn.setConnectTimeout(10 * 1000);
            httpConn.setReadTimeout(10 * 1000);


            final BufferedReader bf = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line = null;
            int count = 0;
            while ((line = bf.readLine()) != null) {
                line = StringUtils.trim(line);
                if (StringUtils.startsWith(line, "<table id=\"page-table2\" ")) {
                    count++;
                    continue;
                }

                if (StringUtils.startsWith(line, "<td class")) {
                    if (count == 0) {
                        continue;
                    }

                    ++count;
                    if (count == 8) {
                        String[] items = StringUtils.split(line, "<>");
                        return items[1];
                    }
                }
            }

        } catch (Throwable e) {

        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return StringUtils.EMPTY;
    }
}
