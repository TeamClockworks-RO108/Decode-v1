package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Signal {

    private final Telemetry telemetry;
    private final String signalName;
    private boolean signal = false;
    private boolean newValue = false;

    public Signal(Telemetry telemetry, String signalName) {
        this.telemetry = telemetry;
        this.signalName = signalName;
    }

    public Signal() {
        this(null, null);
    }

    public void activate() {
        newValue = true;
    }

    public void clear() {
        newValue= false;
    }

    public boolean check() {
        return signal;
    }

    public boolean checkAndClear() {
        if (check()) {
            clear();
            return true;
        }
        return false;
    }

    /**
     * Update only calls telemetry. Use this to help with debugging
     */
    public void update() {
        signal = newValue;
        if (telemetry != null && signalName != null) {
            telemetry.addLine("Signal " + signalName + ": " + (signal ? "ACTIVATED" : "----"));
        }
    }


}
