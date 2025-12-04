package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "FieldCentricTeleop")
public class FieldCentricTeleop extends OpMode {
    private Follower follower = null;
    private Shooter shooter = null;

    private EdgeDetector toggleShooting = new EdgeDetector(false);
    private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    private EdgeDetector toggleIntakeReject = new EdgeDetector(false);
    private EdgeDetector fire = new EdgeDetector(false);
    private EdgeDetector rapidFire = new EdgeDetector(false);
    private EdgeDetector fieldCentricReset = new EdgeDetector(false);

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0,0, Math.toRadians(0)));
        follower.update();

        shooter = new Shooter(hardwareMap, false);
        // shooter command setup
        toggleShooting.onPress(() -> shooter.command(Shooter.Command.TOGGLE_SHOOTING));
        toggleIntake.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE));
        toggleIdle.onPress(() -> shooter.command(Shooter.Command.TOGGLE_IDLE));
        toggleIntakeReject.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE_REJECT));
        fire.onPress(() -> shooter.command(Shooter.Command.FIRE));
        rapidFire.onPress(() -> shooter.command(Shooter.Command.RAPID_FIRE));
        fieldCentricReset.onPress(() -> follower.setPose(new Pose(0, 0, 0)));

        shooter.setupShooter();
    }

    @Override
    public void start() {
        follower.startTeleOpDrive();
        shooter.command(Shooter.Command.TOGGLE_IDLE);
    }

    @Override
    public void loop() {
        follower.update();
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                false // ensure field centric
        );

        // shooter controls
        toggleShooting.update(gamepad1.right_bumper);
        toggleIntake.update(gamepad1.left_bumper);
        toggleIdle.update(gamepad1.square);
        toggleIntakeReject.update(gamepad1.circle);
        fire.update(gamepad1.triangle);
        rapidFire.update(gamepad1.cross);
        fieldCentricReset.update(gamepad1.dpad_up);

        shooter.updateShooter();

        // telemetry
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
}
