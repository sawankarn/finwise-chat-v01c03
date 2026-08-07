package com.finwise.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for the multi-tier model fallback chain.
 */
@Component
@ConfigurationProperties(prefix = "finwise")
public class ModelTierProperties {

    public record ModelTier(
            String name,
            String provider,
            String model,
            String label
    ) {}

    private List<ModelTier> modelTiers = new ArrayList<>();

    public List<ModelTier> getModelTiers() {
        return modelTiers;
    }

    public void setModelTiers(List<ModelTier> modelTiers) {
        this.modelTiers = modelTiers;
    }
}
