package io.github.manhtu1997.vng_cloud_client.logger;

import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private Logger logger = LogManager.getLogger(HttpLogger.class);
    @Override
    public void log(String s) {
        logger.info(s);
    }
}
