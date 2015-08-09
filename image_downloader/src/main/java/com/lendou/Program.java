package com.lendou;

import org.apache.commons.lang.Validate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Program {
    private static final String SEP = "src='http";
    private static final String SPLITTER = "src='";

    private static final String folderName = "/home/dyl/pictures/temp";

    public static void main( String[] args) {
        final String url = "";
        final List<String> list = downloadUrl(url);

        final long curTime = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + "/" + list.size() + " : " + list.get(i));
            downloadImage(curTime, i, list.get(i));
        }

    }

    private static void downloadImage(long curTime, int index, final String imgUrl) {
        Validate.notEmpty(imgUrl);

        final String fileName = folderName + File.separator + curTime + "-" + index + imgUrl.substring(imgUrl.lastIndexOf("."));

        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(imgUrl);

            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(false);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");

            final BufferedInputStream bis = new BufferedInputStream(httpConn.getInputStream());
            final DataInputStream dis = new DataInputStream(bis);

            final FileOutputStream fos = new FileOutputStream(new File(fileName));
            byte[] buffer = new byte[1024];
            int length;

            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            bis.close();
            dis.close();
            fos.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }

    private static List<String> downloadUrl(final String webUrl) {
        Validate.notEmpty(webUrl);

        HttpURLConnection httpConn = null;
        try {
            final URL url = new URL(webUrl);

            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
            httpConn.setConnectTimeout(10 * 1000);
            httpConn.setReadTimeout(10 * 1000);

            final BufferedReader bf = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line = null;
            int pos;
            boolean status = false;
            while ((line = bf.readLine()) != null) {
                if ((pos = line.indexOf(SEP)) != -1
                    && (line.indexOf(SEP, pos + 10) != -1)) {
                    status = true;
                    break;
                }
            }

            if (!status) {
                System.out.println("Does not found images to be downloaded");
                return new ArrayList<String>();
            }

            final List<String> list = new ArrayList<String>();
            for (final String item : line.split(SPLITTER)) {
                if (!item.startsWith("http")) {
                    continue;
                }

                list.add(item.substring(0, item.indexOf("'")));
            }

            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return new ArrayList<String>();
    }
}
