package shuaicj.example.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config common tags.
 *
 * @author shuaicj 2020/03/20
 */
@Configuration
public class AppConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("author", "shuaicj", "country", "china");
    }
}
