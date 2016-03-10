package com.lendou.file.io;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final class FileSizeHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSizeHelper.class);

    private static final String CONTENT_LENGTH = "Content-Length";

    public static int getFileSize(String url) {
        Validate.notEmpty(url);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(url);

        for (int i = 0; i < 5; i++) {
            CloseableHttpResponse response = null;
            try {
                response = client.execute(httpHead);
                int status = response.getStatusLine().getStatusCode();
                if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
                    LOGGER.warn("Unexpected response status: {}", status);
                    continue;
                }

                Header[] headers = response.getHeaders(CONTENT_LENGTH);
                Validate.isTrue(headers.length == 1);

                return NumberUtils.toInt(headers[0].getValue(), 0);

            } catch (Exception e) {
                LOGGER.warn("Unexpected exception", e);
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        LOGGER.error("Failed to close response", e);
                    }
                }
            }
        }

        LOGGER.error("Failed to get file size for {}", url);
        return 0;
    }
}
