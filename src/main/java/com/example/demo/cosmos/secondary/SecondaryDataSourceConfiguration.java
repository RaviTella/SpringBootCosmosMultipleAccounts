package com.example.demo.cosmos.secondary;

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
public class SecondaryDataSourceConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("secondaryDataSourcePropertiesBean")
    CosmosProperties secondaryDataSourceProperties;

    @Bean("secondaryDataSourcePropertiesBean")
    @ConfigurationProperties(prefix = "azure.cosmos.secondary")
    public CosmosProperties secondary() {
        return new CosmosProperties();
    }

    @Bean("secondaryCosmosClient")
    public CosmosAsyncClient getCosmosAsyncClient(@Qualifier("secondaryDataSourcePropertiesBean") CosmosProperties secondaryDataSourceProperties) {
        return CosmosFactory.createCosmosAsyncClient(new CosmosClientBuilder()
                .key(secondaryDataSourceProperties.getKey())
                .endpoint(secondaryDataSourceProperties.getUri()));
    }

    @Bean("secondaryCosmosConfig")
    public CosmosConfig getCosmosConfig() {
        return CosmosConfig.builder()
                           .enableQueryMetrics(secondaryDataSourceProperties.isPopulateQueryMetrics())
                           .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                           .build();
    }

    @EnableCosmosRepositories(basePackages = "com.example.demo.cosmos.secondary",
            cosmosTemplateRef  = "secondaryTemplate")
    public class DatabaseConfiguration {
        @Bean
        public CosmosTemplate secondaryTemplate(@Qualifier("secondaryCosmosClient") CosmosAsyncClient client,
                                                         @Qualifier("secondaryCosmosConfig") CosmosConfig cosmosConfig,
                                                         MappingCosmosConverter mappingCosmosConverter) {
            return new CosmosTemplate(client, secondaryDataSourceProperties.getDatabase(), cosmosConfig, mappingCosmosConverter);
        }
    }

    private class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
