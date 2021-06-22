package com.epam.Transform;

import com.epam.entity.Activity;
import com.epam.entity.Output;
import com.epam.function.MapToOutputFn;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class MapToOutput extends PTransform<PCollection<Activity>, PCollection<KV<String, Output>>> {
    @Override
    public PCollection<KV<String, Output>> expand(PCollection<Activity> input) {
        return input.apply(WithKeys.of(Activity::getCity))
                .setCoder(KvCoder.of(StringUtf8Coder.of(), SerializableCoder.of(Activity.class)))
                .apply(GroupByKey.create())
                .apply(ParDo.of(new MapToOutputFn()));
    }
}
