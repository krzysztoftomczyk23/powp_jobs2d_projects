package edu.kis.powp.jobs2d.features;

import edu.kis.powp.jobs2d.drivers.RecordingDriver;

public class RecordingFeature {

    private static RecordingDriver recordingDriver;

    public static void setup(RecordingDriver recordingDriverGiven) {
        recordingDriver = recordingDriverGiven;
    }

    public static RecordingDriver getRecordingDriver() {
        return recordingDriver;
    }
}