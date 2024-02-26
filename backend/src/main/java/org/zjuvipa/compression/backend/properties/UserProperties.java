package org.zjuvipa.compression.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "modelcompression.auth")
public class UserProperties {

    private List<String> noAuthUrls;
}
