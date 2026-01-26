package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Auto Far BLUE (sigma)")
public class AutoFarBlue extends AutoFAR {

    protected final StateMachine<AutoFarBlue.State> fsm = new StateMachine<>(AutoFarBlue.State.INIT);

    protected enum State {
        INIT,
        START_POSITION,

        SHOOT_A,
        LEAVE,
    }


    protected void setColor() {
        color = TeamColor.BLUE;
    }
    protected void setStartingPose() {
        startingPose = poses.farStart;
    }

    @Override
    protected void setupFSM() {

        fsm.onStateEnter(AutoFarBlue.State.START_POSITION, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);
        });

        fsm.onStateUpdate(AutoFarBlue.State.START_POSITION, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 2000) {
                return AutoFarBlue.State.SHOOT_A;
            }
            return null;
        });

        setShoot3Artifacts(AutoFarBlue.State.SHOOT_A, AutoFarBlue.State.LEAVE);

        fsm.onStateEnter(AutoFarBlue.State.LEAVE, () -> {
            movement.followPath(paths.leaveFar);
        });

        fsm.onStateUpdate(AutoFarBlue.State.LEAVE, () -> {
            if(!movement.isBusy()){
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();

    }

    @Override
    protected void startFSM() {
        fsm.onStateUpdate(AutoFarBlue.State.INIT, () -> AutoFarBlue.State.START_POSITION);

    }

    @Override
    protected void updateFSM() { fsm.update();}

    protected void setShoot3Artifacts(AutoFarBlue.State startState, AutoFarBlue.State nextState) {
        fsm.onStateEnter(startState, () -> {
            shooter.command(Shooter.Command.RAPID_FIRE);
        });
        fsm.onStateUpdate(startState, (current, timeSinceTransition) -> {
            if (timeSinceTransition > shooter.getRapidFireTime()) {
                return nextState;
            }
            return null;
        });
    }
}
