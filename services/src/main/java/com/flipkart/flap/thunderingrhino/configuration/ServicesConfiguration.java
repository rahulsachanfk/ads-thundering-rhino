package com.flipkart.flap.thunderingrhino.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by jagmeet.singh on 02/01/15.
 */
@Getter
public class ServicesConfiguration extends Configuration {

    @NotEmpty
    private String healthCheckProperty;

    @NotEmpty
    private String externalAPIBaseURL;

    @NotEmpty
    @JsonProperty
    private String npsProductVipURL;

    @NotEmpty
    private String slackFilePath;

    @NotEmpty
    private String redisHost;

    @NotEmpty
    private String redisPort;

    private Flipper campaignDetailsAPIFlipper;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private DataSourceFactory reportdatabase = new DataSourceFactory();

    @Valid
    @NotNull
    private DataSourceFactory camdatabase = new DataSourceFactory();

    @Valid
    @NotNull
    private DataSourceFactory anydatabase = new DataSourceFactory();

    @Valid
    @NotNull
    private DataSourceFactory madidatabase = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("reportdatabase")
    public DataSourceFactory getReportDataSourceFactory() {
        return reportdatabase;
    }

    @JsonProperty("camdatabase")
    public DataSourceFactory getCamDataSourceFactory() {
        return camdatabase;
    }

    @JsonProperty("anydatabase")
    public DataSourceFactory getAnyDataSourceFactory() {return anydatabase;}

    @JsonProperty("madidatabase")
    public DataSourceFactory getMadiDataSourceFactory() {return madidatabase;}
}
