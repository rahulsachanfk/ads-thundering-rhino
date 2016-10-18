package com.flipkart.flap.thunderingrhino.configuration;

import com.flipkart.flap.thunderingrhino.utils.ConfigNotFoundException;
import com.flipkart.kloud.config.Bucket;
import com.flipkart.kloud.config.ConfigClient;
import com.flipkart.kloud.config.DynamicBucket;
import com.flipkart.kloud.config.error.ConfigServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by sangar.jitendra on 30/06/15.
 */
public class ConfigServiceClient {

    private static final String APP_INSTANCE_ID = "0";
    private static final int CONFIG_SERVICE_API_VERSION = 1;
    private static final String FK_ENV_FILE = "/etc/default/fk-env";
    private static String url = "config-service-sandbox.ch.flipkart.com";
    private static int PORT = 8080;
    private static final int CONNECTION_TIMEOUT = 1000;
    private ConfigClient configuration;
    private String appName;
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceClient.class);
    public Bucket bucket;

    private void init(String appName) throws ConfigNotFoundException {

        String hostEnvironment = getHostEnvironment();

        appName = appName + "-" + hostEnvironment;

        if(hostEnvironment.equals("stage")){
            url = "config-service.nm.flipkart.com";
            PORT = 80;
        }
        else if (hostEnvironment.equals("prod")){
            url ="10.47.0.101";
            PORT = 80;
        }

        logger.info("Initializing config service client for appName: {}, host: {}, url {} ", appName, hostEnvironment, url);

        configuration = new ConfigClient(url, PORT, CONFIG_SERVICE_API_VERSION, CONNECTION_TIMEOUT);
        logger.info("Initialized config service client for job framework.");
        try {
            this.bucket = configuration.getBucket(appName, -1);
        } catch (ConfigServiceException | NullPointerException | IOException e) {
            logger.info("Configuration could not be found");
            throw new ConfigNotFoundException(appName);
        }
    }

    public ConfigServiceClient(String appName) throws ConfigNotFoundException {
        init(appName);
    }

    public int getInt(String key, int default_) {

        Integer value = 0;
        try {
            value = bucket.getInt(key);
        } catch (Exception e) {
            return default_;
        }
        if(value  == null)
            return  default_;

        return value;
    }
    public String getString(String key, String default_) {

        String value = null;
        try {
            value = bucket.getString(key);
        } catch (Exception e) {
            return default_;
        }
        if(value == null) {
            return default_;
        }
        return value;
    }

    public List<Integer> getIntArray(String key, List<Integer> default_) {

        List<Integer> value = null;
        try {
            value = bucket.getIntArray(key);
        } catch (Exception e) {
            return default_;
        }

        if(value == null)
            return  default_;

        return value;
    }

    public List<String> getStringArray(String key, List<String> default_) {

        List<String> value = null;
        try {
            value = bucket.getStringArray(key);
        } catch (Exception e) {
            return default_;
        }

        if(value == null){
            return default_;
        }
        return value;
    }

    public boolean getBoolean(String key, boolean default_) throws  ConfigNotFoundException{

        Boolean value = null;
        try {
            value = bucket.getBoolean(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }

        if(value == null){
            return default_;
        }
        return value;
    }
    public int getInt(String key) throws ConfigNotFoundException {

        Integer value = null;
        try {
            value = bucket.getInt(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }

        if(value == null)
        {
            throw new ConfigNotFoundException(key);
        }
        return value;
    }

    public boolean getBoolean(String key) throws  ConfigNotFoundException{

        Boolean value = null;
        try {
            value = bucket.getBoolean(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }

        if(value == null){
            throw new ConfigNotFoundException(key);
        }
        return value;
    }
    public String getString(String key) throws ConfigNotFoundException{

        String value = null;
        try {
            value = bucket.getString(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }
        if(value == null)
        {
            throw new ConfigNotFoundException(key);
        }
        return value;
    }

    public List<Integer> getIntArray(String key) throws ConfigNotFoundException{

        List<Integer> value = null;
        try {
            value = bucket.getIntArray(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }
        if(value == null)
        {
            throw new ConfigNotFoundException(key);
        }
        return value;
    }

    public List<String> getStringArray(String key) throws ConfigNotFoundException {

        List<String> value = null;
        try {
            value = bucket.getStringArray(key);
        } catch (Exception e) {
            throw new ConfigNotFoundException(key);
        }
        if(value == null)
        {
            throw new ConfigNotFoundException(key);
        }
        return value;
    }

    private String getHostEnvironment() {
        File envFile = new File(FK_ENV_FILE);
        if (envFile.exists()) {
            try {
                String env = getContents(envFile);
                if (env.equals("NM"))
                {
                    return "stage";

                }
                else if (env.equals("prod")) {
                    return "prod";
                }
            }
            catch (IOException e) {
            }
        }
        return "local";
    }

    private String getHostNameFromRuntime() throws IOException {
        return InetAddress.getLocalHost().getCanonicalHostName();
    }

    /**
     * Gets only the first line of any file as a string.
     *
     * @param file      File object to read
     * @return contents
     * @throws java.io.FileNotFoundException
     */
    private String getContents(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        return reader.readLine();
    }

    public static DynamicBucket getAppDynamicBucketFromEnvSettings() throws IOException, ConfigServiceException {
        DynamicBucket dynamicBucket = null;
        String configServiceServerHost = System.getenv("CONFIG_SERVICE_SERVER_HOST");
        String configServiceServerPort = System.getenv("CONFIG_SERVICE_SERVER_PORT");
        String configServiceBucket = System.getenv("CONFIG_SERVICE_BUCKET");
        if(configServiceServerHost != null && configServiceServerPort != null && configServiceBucket != null) {
            int configServiceServerPortNumber = Integer.valueOf(configServiceServerPort);
            ConfigClient client = new ConfigClient(configServiceServerHost, configServiceServerPortNumber, CONFIG_SERVICE_API_VERSION);
            dynamicBucket = client.getDynamicBucket(configServiceBucket);
        }
        return dynamicBucket;
    }
}
