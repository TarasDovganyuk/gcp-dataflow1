package com.epam.common;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface EventAggregatorOptions extends PipelineOptions {

    @Description("Path of the files to read from")
    @Default.String("src/main/resources/input/*.jsonl")
    String getInputFiles();

    void setInputFiles(String value);

    @Description("Path of the file to write to")
    @Validation.Required
    String getOutput();

    void setOutput(String value);
}
