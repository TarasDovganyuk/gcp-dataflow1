package com.epam.function;

import com.epam.entity.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.beam.sdk.transforms.DoFn;

public class ParseJsonFn extends DoFn<String, Event> {
    @ProcessElement
    public void processElement(@Element String element, OutputReceiver<Event> receiver) {

        Event event = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(element, Event.class);
        receiver.output(event);
    }
}
