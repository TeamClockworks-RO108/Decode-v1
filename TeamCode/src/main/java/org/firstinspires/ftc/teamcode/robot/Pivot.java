package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Pivot {
    public enum State {
        OFF,
        SHOOT,
        IDLE,
        INTAKE
    }

    private State state = State.OFF;
    private final Servo pivot;
    private final Servo pivot1;

    private final double pivotIntake = 0.68;
    private final double pivotIdle = 0.88;
    private final double pivotShoot = 0.96;

    public Pivot(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "pivot");
        pivot1 = hardwareMap.get(Servo.class, "pivot1");
    }

    public void idle() {
        pivot.setPosition(pivotIdle);
        pivot1.setPosition(pivotIdle);
        state = State.IDLE;
    }
    public void shoot() {
        pivot.setPosition(pivotShoot);
        pivot1.setPosition(pivotShoot);
        state = State.SHOOT;
    }
    public void intake() {
        pivot.setPosition(pivotIntake);
        pivot1.setPosition(pivotIntake);
        state = State.INTAKE;
    }

    public State getState() {
        return state;
    }
}
