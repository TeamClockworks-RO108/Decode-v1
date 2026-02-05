package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Movement;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesTeleOp;
import org.firstinspires.ftc.teamcode.robot.Brakes;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "TeleOp BLUE")
public class TeleOpBlue extends OpMode {
    protected TeamColor color = TeamColor.BLUE;
    private PosesTeleOp poses;

    private Movement movement = null;
    private Shooter shooter = null;

    private Brakes brakes = null;

    private EdgeDetector fieldCentricReset = new EdgeDetector(false);
    private EdgeDetector goToGoal = new EdgeDetector(false);
    private EdgeDetector releasePath = new EdgeDetector(false);
    private EdgeDetector resetToCamera = new EdgeDetector(false);
    private EdgeDetector resetGate = new EdgeDetector(false);

    private EdgeDetector toggleShooting = new EdgeDetector(false);
    private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    private EdgeDetector toggleIntakeReject = new EdgeDetector(false);
    private EdgeDetector fire = new EdgeDetector(false);
    private EdgeDetector rapidFire = new EdgeDetector(false);

    private EdgeDetector brake = new EdgeDetector(false);

    private Telemetry panelsTelemetry = null;

    @Override
    public void init() {

        this.panelsTelemetry = new MultipleTelemetry(this.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());

        poses = new PosesTeleOp(color);

        brakes = new Brakes(hardwareMap);

        movement = new Movement(hardwareMap, panelsTelemetry, poses.start);
        shooter = new Shooter(hardwareMap, panelsTelemetry, false);

        // movement command setup
        fieldCentricReset.onPress(() -> movement.resetHeading(poses.none.getHeading()));

        brake.onHold(() -> {
            brakes.on();
        });
        brake.onRelease(() -> {
            brakes.off();
        });

        resetGate.onPress(() -> {
            movement.setPose(poses.gate);
            gamepad2.rumble(150);
        });
        resetToCamera.onPress(() -> {
            try {
                movement.updateToCameraPose(color);
                gamepad2.rumble(150);
            } catch (Exception ignored) {

            }
        });

        goToGoal.onPress(() -> {
            movement.goToPose(poses.shootTeleOp);
        });
        releasePath.onPress(()-> {
            movement.breakFollowing();
        });

        if (color == TeamColor.RED) movement.flipControls();

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
        shooter.command(Shooter.Command.TOGGLE_SHOOTING);
    }

    @Override
    public void loop() {
        movement.updateTeleOp(gamepad1, gamepad2);

        // movement options
        fieldCentricReset.update(gamepad1.dpad_up);
        brake.update(gamepad2.right_trigger > 0.01);

        // localizer options
        resetGate.update(gamepad2.left_bumper);
        resetToCamera.update(gamepad2.dpad_up);
        goToGoal.update(gamepad2.triangle);
        releasePath.update(gamepad2.circle);

        // shooter options
        toggleShooting.update(gamepad1.right_bumper);
        toggleIntake.update(gamepad1.left_bumper);
        toggleIdle.update(gamepad1.square);
        toggleIntakeReject.update(gamepad1.circle);
        fire.update(gamepad1.triangle);
        rapidFire.update(gamepad1.cross);

        shooter.updateShooter();

        panelsTelemetry.update();
    }
}
