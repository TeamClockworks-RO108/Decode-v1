package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.command.RobotTaskFactory;
import org.firstinspires.ftc.teamcode.command.TaskScheduler;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Movement;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesTeleOp;
import org.firstinspires.ftc.teamcode.robot.Brakes;
import org.firstinspires.ftc.teamcode.robot.Outtake;
import org.firstinspires.ftc.teamcode.robot.Pivot;
import org.firstinspires.ftc.teamcode.robot.Vision;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "TeleOp BLUE")
public class TeleOpBlue extends OpMode {
    protected TeamColor color = TeamColor.BLUE;
    private PosesTeleOp poses;

    private final TaskScheduler scheduler = new TaskScheduler();
    private RobotTaskFactory robotTasks;

    private Brakes brakes;
    private Movement movement;
    private Vision vision;
    private Pivot pivot;
    private Intake intake;
    private Outtake outtake;

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
        if (color == TeamColor.RED) movement.flipControls();

        poses = new PosesTeleOp(color);

        brakes = new Brakes(hardwareMap);
        movement = new Movement(hardwareMap, telemetry, poses.start);
        vision = new Vision(hardwareMap, telemetry);
        pivot = new Pivot(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap, telemetry);

        robotTasks = new RobotTaskFactory(movement, pivot, intake, outtake);

        // movement command setup
        fieldCentricReset.onPress(() -> movement.resetHeading(poses.none.getHeading()));

        brake.onHold(() -> {
            brakes.on();
        });
        brake.onRelease(() -> {
            brakes.off();
        });

        resetGate.onPress(() -> {
            movement.getFollower().setPose(poses.gate);
            gamepad2.rumble(150);
        });
        resetToCamera.onPress(() -> {
            try {
                movement.getFollower().setPose(vision.processVisionPose(color));
                gamepad2.rumble(150);
            } catch (RuntimeException ignored) {

            }
        });

        goToGoal.onPress(() -> {
            scheduler.schedule(robotTasks.driveToAim(poses.shootTeleOp));
        });
        releasePath.onPress(scheduler::reset);

        // shooter command setup
        toggleShooting.onPress(() -> scheduler.schedule(robotTasks.pivotShoot()));
        toggleIntake.onPress(() -> scheduler.schedule(robotTasks.pivotIntake()));
        toggleIdle.onPress(() -> scheduler.schedule(robotTasks.pivotIdle()));
        toggleIntakeReject.onPress(() -> scheduler.schedule(robotTasks.intakeRejectToggle()));
        fire.onPress(() -> scheduler.schedule(robotTasks.outtakeFire()));
        rapidFire.onPress(() -> scheduler.schedule(robotTasks.outtakeRapidFire()));
    }

    @Override
    public void start() {
        movement.getFollower().startTeleopDrive();
        scheduler.schedule(robotTasks.pivotShoot());
    }

    @Override
    public void loop() {
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

        // run updates
        // Sensors first, then the scheduler
        // Actuators last
        vision.update();
        scheduler.run();
        intake.update();
        outtake.update();
        movement.update(gamepad1, gamepad2);

        panelsTelemetry.update();
    }
}
