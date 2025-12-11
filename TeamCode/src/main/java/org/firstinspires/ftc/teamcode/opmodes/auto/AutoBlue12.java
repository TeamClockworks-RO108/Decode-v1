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

//        fsm.onStateEnter(State.SHOOT_THIRD_INTAKE, () -> {
//            follower.followPath(paths.goShootThirdIntake);
//            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
//        });
//        fsm.onStateUpdate(State.SHOOT_THIRD_INTAKE, () -> {
//            if (!follower.isBusy())
//                return State.SHOOT_D;
//            return null;
//        });
//
//        setShoot3Artifacts(State.SHOOT_D, State.GO_HOME);
    }
}
