package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "RobotCentricTeleop")
public class RobotCentricTeleop extends OpMode {
//    private Telemetry telemetryA;

    private Movement movement = null;
    private Shooter shooter = null;

    private EdgeDetector toggleShooting = new EdgeDetector(false);
    private EdgeDetector launchGE = new EdgeDetector(false);
    private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    private EdgeDetector toggleIntakeReject = new EdgeDetector(false);
    @Override
    public void init() {
        movement = new Movement(hardwareMap);
        shooter = new Shooter(hardwareMap, false);
        // shooter command setup
        toggleShooting.onPress(() -> shooter.command(Shooter.Command.TOGGLE_SHOOTING));
        launchGE.onPress(() -> shooter.command(Shooter.Command.LAUNCH));
        toggleIntake.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE));
        toggleIdle.onPress(() -> shooter.command(Shooter.Command.TOGGLE_IDLE));
        toggleIntakeReject.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE_REJECT));

        shooter.setupShooter();

//        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    @Override
    public void start() {
        shooter.command(Shooter.Command.TOGGLE_IDLE);
    }

    @Override
    public void loop() {
        // drive loop
        movement.movementLoop(gamepad1);
        // shooter controls
        toggleShooting.update(gamepad1.right_bumper);
        launchGE.update(gamepad1.triangle);
        toggleIntake.update(gamepad1.left_bumper);
        toggleIdle.update(gamepad1.square);
        toggleIntakeReject.update(gamepad1.circle);

        shooter.updateShooter();

//        telemetryA.addData("flywheelVelocity", shooter.getFlywheelVelocity());
//        telemetryA.addData("flywheelTarget", shooter.getTargetVelocity());
//        telemetryA.update();
    }
}