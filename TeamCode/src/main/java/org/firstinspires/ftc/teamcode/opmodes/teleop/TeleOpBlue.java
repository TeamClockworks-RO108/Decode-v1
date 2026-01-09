package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.movement.PedroMovement;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesTeleOp;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@TeleOp(name = "TeleOp BLUE", group = "Field Centric")
public class TeleOpBlue extends OpMode {
    protected TeamColor color = TeamColor.BLUE;
    private PosesTeleOp poses;

    private PedroMovement movement = null;
    private Shooter shooter = null;

    private EdgeDetector fieldCentricReset = new EdgeDetector(false);
    private EdgeDetector resetBase = new EdgeDetector(false);
    private EdgeDetector goToGoal = new EdgeDetector(false);
    private EdgeDetector goToPark = new EdgeDetector(false);
    private EdgeDetector releasePath = new EdgeDetector(false);
    private EdgeDetector resetToCamera = new EdgeDetector(false);
    private EdgeDetector resetGate = new EdgeDetector(false);

    private EdgeDetector toggleShooting = new EdgeDetector(false);
    private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    private EdgeDetector toggleIntakeReject = new EdgeDetector(false);
    private EdgeDetector fire = new EdgeDetector(false);
    private EdgeDetector rapidFire = new EdgeDetector(false);

    private enum TeleopStates {
        TELEOP,
        AIMING,
        HOLD_AIM,
    }

    public enum Command {
        START_AIMING,
        RELEASE_AIM
    }

    private Command unexecutedCommand = null;
    private final StateMachine<TeleopStates> fsm  = new StateMachine<>(TeleopStates.TELEOP);

    @Override
    public void init() {
        poses = new PosesTeleOp(color);

        movement = new PedroMovement(hardwareMap, telemetry, poses.start);
        shooter = new Shooter(hardwareMap, telemetry, false);

        // movement command setup
        fieldCentricReset.onPress(() -> movement.resetHeading(poses.start.getHeading()));
        resetBase.onPress(() -> {
            movement.setPose(poses.humanBase);
            gamepad2.rumble(150);
        });
        resetGate.onPress(() -> {
            movement.setPose(poses.gate);
            gamepad2.rumble(150);
        });
        resetToCamera.onPress(() -> {
            try {
                movement.updateToCameraPose();
                gamepad2.rumble(150);
            } catch (Exception e) {
                gamepad2.rumble(400);
            }
        });

        goToGoal.onPress(() -> {
            command(Command.START_AIMING);
        });
        goToPark.onPress(() -> movement.goToPose(poses.parking));
        releasePath.onPress(()-> {
            command(Command.RELEASE_AIM);
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
        setupTeleopFSM();
    }

    @Override
    public void start() {
        movement.startTeleop();
        shooter.command(Shooter.Command.TOGGLE_SHOOTING);
    }

    @Override
    public void loop() {
        fsm.update();

        movement.updateTeleOp(gamepad1, gamepad2);

        // movement options
        fieldCentricReset.update(gamepad1.dpad_up);

//        resetBase.update(gamepad2.dpad_down);
        resetGate.update(gamepad2.dpad_right);
        resetToCamera.update(gamepad2.dpad_up);
        goToGoal.update(gamepad2.triangle);
        goToPark.update(gamepad2.square);
        releasePath.update(gamepad2.circle);

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

    private void command(Command command) {
        unexecutedCommand = command;
    }

    private void setupTeleopFSM(){
        fsm.onStateEnter(TeleopStates.TELEOP, () -> {
            movement.startTeleop();
        });
        fsm.onStateUpdate(TeleopStates.TELEOP, () -> {
            if (unexecutedCommand == Command.START_AIMING) {
                unexecutedCommand = null;
                return TeleopStates.AIMING;
            }
            return null;
        });

        fsm.onStateEnter(TeleopStates.AIMING, () -> {
            movement.goToPose(poses.shootTeleOp);
        });
        fsm.onStateUpdate(TeleopStates.AIMING, () -> {
            if (!movement.isBusy())
                return TeleopStates.HOLD_AIM;
            else if(unexecutedCommand == Command.RELEASE_AIM){
                unexecutedCommand = null;
                return TeleopStates.TELEOP;
            }
            return null;
        });

        fsm.onStateEnter(TeleopStates.HOLD_AIM, () -> {
            movement.hold(poses.shootTeleOp);
        });
        fsm.onStateUpdate(TeleopStates.HOLD_AIM, () -> {
            if (unexecutedCommand == Command.RELEASE_AIM ){
                unexecutedCommand = null;
                return TeleopStates.TELEOP;
            }
            return null;
        });

        fsm.init();
    }
}
