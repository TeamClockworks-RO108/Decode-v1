package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.algo.MotionProfiler;
import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.movement.MovementRobotCentric;
import org.firstinspires.ftc.teamcode.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;
import org.firstinspires.ftc.teamcode.util.ParameterChanger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


//adb connect 192.168.43.1:5555

public abstract class Teleop extends OpMode {




    private Movement movement;

    private MotionProfiler xMotionProfiler = new MotionProfiler(300);
    private MotionProfiler yMotionProfiler = new MotionProfiler(300);
    private MotionProfiler  headingMotionProfiler = new MotionProfiler(300);

    private Shooter shooter;

    private MovementRobotCentric movementRC;

    private EdgeDetector toggleRotation = new EdgeDetector(false);
    private EdgeDetector launchGameElement = new EdgeDetector(false);


    private ParameterChanger parameterChanger = null;
    private Gamepad prevGamepad = new Gamepad();



    @Override
    public void init() {
        parameterChanger = new ParameterChanger(getConfigGamepad(), telemetry);
        this.movement = new Movement(hardwareMap);
        shooter = new Shooter(hardwareMap, false);

        shooter.setupStateMachine();

        setupCommands();
        setupParameterChanger();
        parameterChanger.enable(true);
    }

    @Override
    public void start() {
       movement.startMovement();
    }


    @Override
    public void loop() {
        parameterChanger.update();
        long time = System.currentTimeMillis();
        double x = xMotionProfiler.feed(getGamepad1().left_stick_x,  time);
        double y = yMotionProfiler.feed(-getGamepad1().left_stick_y,  time);
        double heading;
        heading = headingMotionProfiler.feed(getGamepad1().right_stick_x,  time);
        movement.loopMovement(y, x, heading, telemetry);

        shooter.updateStateMachine();
        updateCommands();

    }


    private void setupCommands() {
        toggleRotation.onPress(() -> {
            shooter.command(Shooter.Command.START_ROTATION);
        });
        launchGameElement.onPress(() -> {
            shooter.command(Shooter.Command.LAUNCH);
        });
    }

    private void updateCommands(){
        toggleRotation.update(getGamepad1().left_bumper);
        launchGameElement.update(getGamepad1().right_bumper);
    }

    private void setupParameterChanger(){
        parameterChanger.register("up position: ", shooter::RAMP_UP, shooter::RAMP_UP);
        parameterChanger.register("down position: ", shooter::RAMP_DOWN, shooter::RAMP_DOWN);
        parameterChanger.register("launching power: ", shooter::launchingSpeed, shooter::launchingSpeed);
    }


    protected abstract Gamepad getGamepad1();
    protected abstract Gamepad getGamepad2();
    protected abstract Gamepad getConfigGamepad() ;

}