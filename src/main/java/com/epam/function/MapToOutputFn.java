package com.epam.function;

import com.epam.entity.Activity;
import com.epam.entity.Output;
import com.epam.entity.Subject;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MapToOutputFn extends DoFn<KV<String, Iterable<Activity>>, KV<String, Output>> {
    @ProcessElement
    public void processElement(ProcessContext processContext) {
        KV<String, Iterable<Activity>> element = processContext.element();
        String city = element.getKey();

        Map<KV<String, String>, List<Activity>> map = StreamSupport.stream(element.getValue().spliterator(), false)
                .collect(Collectors.groupingBy(a -> KV.of(a.getSubjectId(), a.getSubjectType())));

        List<Subject> subjects = map.entrySet().stream().map(e -> {
            List<Activity> value = e.getValue();


            Subject subject = new Subject()
                    .setId(e.getKey().getKey())
                    .setType(e.getKey().getValue());
            List<Activity> activities = value.stream().map(a -> new Activity()
                    .setCity(a.getCity())
                    .setSubjectId(a.getSubjectId())
                    .setSubjectType(a.getSubjectType())
                    .setType(a.getType())
                    .setPast7daysCount(a.getPast7daysCount())
                    .setPast7daysUniqueCount(a.getPast7daysUniqueCount())
                    .setPast30daysCount(a.getPast30daysCount())
                    .setPast30daysUniqueCount(a.getPast30daysUniqueCount())).collect(Collectors.toList());
            subject.setActivities(Iterables.toArray(activities, Activity.class));

            return subject;
        }).collect(Collectors.toList());
        Output result = new Output().setSubjects(Iterables.toArray(subjects, Subject.class));
        processContext.output(KV.of(city, result));
    }
}
