package org.firstinspires.ftc.teamcode.control;

import java.util.function.Supplier;

public class PIDF extends PID {
    Supplier<Double> feedforward;

    public PIDF(double kp, double ki, double kd, Supplier<Double> feedforward) {
        super(kp, ki, kd);

        this.feedforward = feedforward;
    }

    @Override
    public double calculate(double inputValue) {
        return super.calculate(inputValue) + feedforward.get();
    }
}
