package com.example.demo.cosmos.tertiary;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.CosmosFactory;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.core.convert.MappingCosmosConverter;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.example.demo.cosmos.CosmosProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
public class TertiaryDataSourceConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @Qualifier("tertiaryDataSourcePropertiesBean")
    CosmosProperties tertiaryDataSourceConfiguration;

    @Bean("tertiaryDataSourcePropertiesBean")
    @ConfigurationProperties(prefix = "azure.cosmos.tertiary")
    public CosmosProperties tertiary() {
        return new CosmosProperties();
    }

    @Bean("tertiaryCosmosClient")
    public CosmosAsyncClient getCosmosAsyncClient(@Qualifier("tertiaryDataSourcePropertiesBean") CosmosProperties tertiaryDataSourceProperties) {
        return CosmosFactory.createCosmosAsyncClient(new CosmosClientBuilder()
                .key(tertiaryDataSourceProperties.getKey())
                .endpoint(tertiaryDataSourceProperties.getUri()));
    }

    @Bean("tertiaryCosmosConfig")
    public CosmosConfig getCosmosConfig() {
        return CosmosConfig.builder()
                           .enableQueryMetrics(tertiaryDataSourceConfiguration.isPopulateQueryMetrics())
                           .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                           .build();
    }

    @EnableCosmosRepositories(basePackages = "com.example.demo.cosmos.tertiary",
            cosmosTemplateRef  = "tertiaryTemplate")
    public class DatabaseConfiguration {
        @Bean
        public CosmosTemplate tertiaryTemplate(@Qualifier("tertiaryCosmosClient") CosmosAsyncClient client,
                                                         @Qualifier("tertiaryCosmosConfig") CosmosConfig cosmosConfig,
                                                         MappingCosmosConverter mappingCosmosConverter) {
            return new CosmosTemplate(client, tertiaryDataSourceConfiguration.getDatabase(), cosmosConfig, mappingCosmosConverter);
        }
    }

    private class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
