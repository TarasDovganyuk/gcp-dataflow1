package com.epam;

import com.epam.Transform.CountActivities;
import com.epam.Transform.MapToOutput;
import com.epam.common.EventAggregatorOptions;
import com.epam.entity.Output;
import com.epam.function.ParseJsonFn;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Contextful;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class EventAggregator {

    static void runEventAggregator(EventAggregatorOptions options) {
        Pipeline p = Pipeline.create(options);

        p.apply("ReadLines", TextIO.read().from(options.getInputFiles()))
                .apply("Parse json", ParDo.of(new ParseJsonFn()))
                .apply("Filter events by timestamp", Filter.by(e -> e.getTimestamp().toInstant().isBefore(Instant.now().plus(30, ChronoUnit.DAYS))))
                .apply("Count activities",new CountActivities())
                .apply("Map to output format",new MapToOutput())
                .apply("WriteJson",
                        FileIO.<String, KV<String, Output>>writeDynamic().withNumShards(1)
                                .by(KV::getKey)
                                .withDestinationCoder(StringUtf8Coder.of())
                                .via(Contextful.fn(
                                        (SerializableFunction<KV<String, Output>, Output>) KV::getValue), AvroIO.sink(Output.class))
                                .to(options.getOutput())
                                .withNaming(key -> FileIO.Write.defaultNaming(key, ".avro")))
        ;

        p.run().waitUntilFinish();
    }

    public static void main(String[] args) {
        EventAggregatorOptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation().as(EventAggregatorOptions.class);
        runEventAggregator(options);
    }

}