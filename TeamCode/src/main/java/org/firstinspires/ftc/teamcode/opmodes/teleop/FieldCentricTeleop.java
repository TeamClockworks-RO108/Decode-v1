package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.movement.PedroMovement;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "FieldCentricTeleop")
public class FieldCentricTeleop extends OpMode {
    private PedroMovement movement = null;
    private Shooter shooter = null;

    private EdgeDetector fieldCentricReset = new EdgeDetector(false);
    private EdgeDetector setBasePose = new EdgeDetector(false);
    private EdgeDetector rotateToGoal = new EdgeDetector(false);

    private EdgeDetector releaseAim = new EdgeDetector(false);

    private EdgeDetector toggleShooting = new EdgeDetector(false);
    private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    private EdgeDetector toggleIntakeReject = new EdgeDetector(false);
    private EdgeDetector fire = new EdgeDetector(false);
    private EdgeDetector rapidFire = new EdgeDetector(false);

    @Override
    public void init() {
        movement = new PedroMovement(hardwareMap, telemetry, new Pose(0, 0, 0));
        shooter = new Shooter(hardwareMap, telemetry, false);

        // movement command setup
        fieldCentricReset.onPress(() -> movement.resetPose());
        rotateToGoal.onPress(() -> movement.command(PedroMovement.Command.START_AIMING));
        setBasePose.onPress(() -> movement.setHomePose());
        releaseAim.onPress(()-> movement.command(PedroMovement.Command.RELEASE_AIM));


        shooter = new Shooter(hardwareMap, telemetry ,false);

        // shooter command setup
        toggleShooting.onPress(() -> shooter.command(Shooter.Command.TOGGLE_SHOOTING));
        toggleIntake.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE));
        toggleIdle.onPress(() -> shooter.command(Shooter.Command.TOGGLE_IDLE));
        toggleIntakeReject.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE_REJECT));
        fire.onPress(() -> shooter.command(Shooter.Command.FIRE));
        rapidFire.onPress(() -> shooter.command(Shooter.Command.RAPID_FIRE));

        shooter.setupShooter();
    }

    @Override
    public void start() {
        movement.startTeleop();
        shooter.command(Shooter.Command.TOGGLE_IDLE);
    }

    @Override
    public void loop() {
        movement.updateTeleOp(gamepad1, gamepad2);

        // movement options
        fieldCentricReset.update(gamepad1.dpad_up);
        rotateToGoal.update(gamepad2.dpad_left);
        setBasePose.update(gamepad2.dpad_down);
        releaseAim.update(gamepad2.circle);

        // shooter options
        toggleShooting.update(gamepad1.right_bumper);
        toggleIntake.update(gamepad1.left_bumper);
        toggleIdle.update(gamepad1.square);
        toggleIntakeReject.update(gamepad1.circle);
        fire.update(gamepad1.triangle);
        rapidFire.update(gamepad1.cross);

        shooter.updateShooter();
        telemetry.update();
    }
}
