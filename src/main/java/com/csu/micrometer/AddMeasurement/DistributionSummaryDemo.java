package com.csu.micrometer.AddMeasurement;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.distribution.CountAtBucket;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.lang.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public interface DistributionSummaryDemo extends DistributionSummary {


    static DistributionSummaryDemo.Builder builder(String name) {
        return new DistributionSummaryDemo.Builder(name);
    }

    void record(double amount);

    long count();

    double totalAmount();

    default double mean() {
        return count() == 0 ? 0 : totalAmount() / count();
    }

    double max();

    @Deprecated
    default double histogramCountAtValue(long value) {
        for (CountAtBucket countAtBucket : takeSnapshot().histogramCounts()) {
            if ((long) countAtBucket.bucket(TimeUnit.NANOSECONDS) == value) {
                return countAtBucket.count();
            }
        }
        return Double.NaN;
    }

    @Deprecated
    default double percentile(double percentile) {
        for (ValueAtPercentile valueAtPercentile : takeSnapshot().percentileValues()) {
            if (valueAtPercentile.percentile() == percentile) {
                return valueAtPercentile.value();
            }
        }
        return Double.NaN;
    }

    @Override
    default Iterable<Measurement> measure() {
        return Arrays.asList(
                new Measurement(() -> (double) count(), Statistic.COUNT),
                new Measurement(this::totalAmount, Statistic.TOTAL),
                new Measurement(() -> (double) mean(),Statistic.VALUE)
        );
    }

    class Builder {
        private final String name;
        private Tags tags = Tags.empty();
        private DistributionStatisticConfig.Builder distributionConfigBuilder = DistributionStatisticConfig.builder();

        @Nullable
        private String description;

        @Nullable
        private String baseUnit;

        private double scale = 1.0;

        private Builder(String name) {
            this.name = name;
        }

        public DistributionSummaryDemo.Builder tags(String... tags) {
            return tags(Tags.of(tags));
        }

        public DistributionSummaryDemo.Builder tags(Iterable<Tag> tags) {
            this.tags = this.tags.and(tags);
            return this;
        }

        public DistributionSummaryDemo.Builder tag(String key, String value) {
            this.tags = tags.and(key, value);
            return this;
        }

        public DistributionSummaryDemo.Builder description(@Nullable String description) {
            this.description = description;
            return this;
        }

        public DistributionSummaryDemo.Builder baseUnit(@Nullable String unit) {
            this.baseUnit = unit;
            return this;
        }

        public DistributionSummaryDemo.Builder publishPercentiles(@Nullable double... percentiles) {
            this.distributionConfigBuilder.percentiles(percentiles);
            return this;
        }

        public DistributionSummaryDemo.Builder percentilePrecision(@Nullable Integer digitsOfPrecision) {
            this.distributionConfigBuilder.percentilePrecision(digitsOfPrecision);
            return this;
        }

        public DistributionSummaryDemo.Builder publishPercentileHistogram() {
            return publishPercentileHistogram(true);
        }

        public DistributionSummaryDemo.Builder publishPercentileHistogram(@Nullable Boolean enabled) {
            this.distributionConfigBuilder.percentilesHistogram(enabled);
            return this;
        }

        public DistributionSummaryDemo.Builder sla(@Nullable long... sla) {
            this.distributionConfigBuilder.sla(sla);
            return this;
        }

        public DistributionSummaryDemo.Builder minimumExpectedValue(@Nullable Long min) {
            this.distributionConfigBuilder.minimumExpectedValue(min);
            return this;
        }

        public DistributionSummaryDemo.Builder maximumExpectedValue(@Nullable Long max) {
            this.distributionConfigBuilder.maximumExpectedValue(max);
            return this;
        }

        public DistributionSummaryDemo.Builder distributionStatisticExpiry(@Nullable Duration expiry) {
            this.distributionConfigBuilder.expiry(expiry);
            return this;
        }

        public DistributionSummaryDemo.Builder distributionStatisticBufferLength(@Nullable Integer bufferLength) {
            this.distributionConfigBuilder.bufferLength(bufferLength);
            return this;
        }

        public DistributionSummaryDemo.Builder scale(double scale) {
            this.scale = scale;
            return this;
        }

//        public DistributionSummaryDemo register(MeterRegistry registry) {
//            return (DistributionSummaryDemo) registry.summary(new Id(name, tags, baseUnit, description, Type.DISTRIBUTION_SUMMARY), distributionConfigBuilder.build(), scale);
//        }
    }
}
