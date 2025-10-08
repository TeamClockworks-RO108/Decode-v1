package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Signals {
    private final Signal gripperSignal;
    private final Signal vacuumSignal;
    private final Signal transferOuttakeSignal;
    private final Signal initiateTransfer;

    public Signals(Telemetry telemetry) {
        gripperSignal = new Signal(telemetry, "Gripper transfer ready");
        vacuumSignal = new Signal(telemetry, "Intake transfer done");
        transferOuttakeSignal = new Signal(telemetry, "Gripper transfer capable");
        initiateTransfer = new Signal(telemetry, "Initiated transfer");
    }

    public void update() {
        gripperSignal.update();
        vacuumSignal.update();
        transferOuttakeSignal.update();
        initiateTransfer.update();
    }

    public Signal gripperSignal() {
        return gripperSignal;
    }

    public Signal vacuumSignal() {
        return vacuumSignal;
    }


    public Signal transferOuttakeSignal() {
        return transferOuttakeSignal;
    }

    public Signal initiateTransfer() {
        return initiateTransfer;
    }

}
