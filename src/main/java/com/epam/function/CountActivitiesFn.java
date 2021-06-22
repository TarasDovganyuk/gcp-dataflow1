package com.epam.function;

import com.epam.entity.Activity;
import com.epam.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class CountActivitiesFn extends DoFn<KV<String, Iterable<Event>>, Activity> {

    @ProcessElement
    public void processElement(ProcessContext ctx) {
        KV<String, Iterable<Event>> element = ctx.element();
        Long past7DaysCount = 0L;
        Long past30DaysCount = 0L;
        Long past7DaysUniqueCount = 0L;
        Long past30DaysUniqueCount = 0L;
        HashSet<String> uniqueLast30days = new HashSet<>();
        HashSet<String> uniqueLast7Days = new HashSet<>();
        String subjectType = null;
        String city = null;
        String subjectId = null;
        String eventType = null;

        for (Event event : element.getValue()) {
            if (city == null) {
                city = event.getCity();
                subjectType = event.getEventSubject().getType();
                subjectId = event.getEventSubject().getId();
                eventType = event.getEventType();
            }

            String eventHash = event.getEventType() + event.getUserId() + event.getEventSubject().getId() + event.getEventSubject().getType();
            if (event.getTimestamp().toInstant().isBefore(Instant.now().plus(7, ChronoUnit.DAYS))) {
                past7DaysCount++;
                past30DaysCount++;
                if (uniqueLast7Days.add(eventHash)) {
                    past7DaysUniqueCount++;
                    past30DaysUniqueCount++;
                }
            } else if (event.getTimestamp().toInstant().isBefore(Instant.now().plus(30, ChronoUnit.DAYS))) {
                past30DaysCount++;
                if (!uniqueLast7Days.contains(eventHash) && uniqueLast30days.add(eventHash)) {
                    past30DaysUniqueCount++;
                }
            }
        }

        Activity result = new Activity()
                .setCity(city)
                .setSubjectId(subjectId)
                .setSubjectType(subjectType)
                .setType(eventType)
                .setPast7daysCount(past7DaysCount)
                .setPast30daysCount(past30DaysCount)
                .setPast7daysUniqueCount(past7DaysUniqueCount)
                .setPast30daysUniqueCount(past30DaysUniqueCount);

        ctx.output(result);
    }
}
