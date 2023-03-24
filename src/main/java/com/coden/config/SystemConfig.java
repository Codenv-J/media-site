package com.coden.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 **/
@Data
@Component
@ConfigurationProperties(prefix = "me-docs.config")
public class SystemConfig {

    private Boolean userUpload;

    private Boolean adminReview;

    private Boolean prohibitedWord;

    private Boolean userRegistry;

    private String initialUsername;

    private String initialPassword;

    private Boolean coverAdmin;


}
