package com.epam.Transform;

import com.epam.entity.Activity;
import com.epam.entity.Event;
import com.epam.function.CountActivitiesFn;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.values.PCollection;

public class CountActivities extends PTransform<PCollection<Event>, PCollection<Activity>> {

    @Override
    public PCollection<Activity> expand(PCollection<Event> input) {
        return input
                .apply(WithKeys.of(e -> e.getCity() + e.getEventSubject().getId() + e.getEventType()))
                .setCoder(KvCoder.of(StringUtf8Coder.of(), SerializableCoder.of(Event.class)))
                .apply(GroupByKey.create())
                .apply(ParDo.of(new CountActivitiesFn()));
    }
}
