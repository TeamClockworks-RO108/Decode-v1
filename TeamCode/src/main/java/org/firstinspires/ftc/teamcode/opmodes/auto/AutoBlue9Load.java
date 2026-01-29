package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.robot.Shooter;

@Disabled
@Autonomous(name = "Auto BLUE 9 + load")
public class AutoBlue9Load extends AutoBlue{
    @Override
    protected void setupFSM() {
        super.setupFSM();
        setShoot3Artifacts(State.SHOOT_C, State.THIRD_INTAKE);

        // handle third intake
        fsm.onStateEnter(State.THIRD_INTAKE, () -> {
            movement.followPath(paths.goThirdIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.THIRD_INTAKE, () -> {
            if (!movement.isBusy()) {
                return State.GRAB_D;
            }
            return null;
        });

        fsm.onStateEnter(State.GRAB_D, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.GRAB_D, (current, timeSinceTransition) -> {
            if (timeSinceTransition > grabTime)
                return State.SHOOT_THIRD_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_THIRD_INTAKE, () -> {
            movement.followPath(paths.goShootThirdIntake);

        });
        fsm.onStateUpdate(State.SHOOT_THIRD_INTAKE, () -> {
            if (!movement.isBusy()) {
                shooter.command(Shooter.Command.RAPID_FIRE);
                return State.SHOOT_D;

            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_D, State.GO_HOME);

        fsm.onStateEnter(State.GO_HOME, () -> {
            movement.followPath(paths.goGoalHome);
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_HOME, () -> {
            if (!movement.isBusy())
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            return null;
        });

        fsm.init();
    }
}
