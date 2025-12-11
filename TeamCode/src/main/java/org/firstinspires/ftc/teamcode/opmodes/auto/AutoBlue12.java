package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Shooter;

@Autonomous(name = "Auto BLUE 12")
public class AutoBlue12 extends AutoBlue{
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
                return State.GO_HOME_FROM_INTAKE;
            }
            return null;
        });

        fsm.onStateEnter(State.GRAB_D, () -> {
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_HOME_FROM_INTAKE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > grabTime)
                return State.GO_HOME_FROM_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.GO_HOME_FROM_INTAKE, () -> {
            movement.followPath(paths.goIntakeGoalHome);
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_HOME_FROM_INTAKE, () -> {
            if (!movement.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();
    }
}
