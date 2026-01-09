package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "CALIBRATE Blue", group = "calibrate")
public class CalibrateBlue extends AutoBase {
    protected final StateMachine<State> fsm = new StateMachine<>(State.INIT);

    protected enum State {
        INIT,
        GO_TO_GOAL
    }

    @Override
    public void init() {
        super.init();
    }

    protected void setColor() {
        color = TeamColor.BLUE;
    }
    protected void setStartingPose() {
        startingPose = poses.goalStart;
    }

    protected void setupFSM(){
        fsm.onStateEnter(State.GO_TO_GOAL, () -> {
            movement.goToPose(new Pose(72, 72, 0));
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_TO_GOAL, () -> {
            if (!movement.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
            return null;
        });

        fsm.init();
    }

    protected void startFSM() {
        fsm.onStateUpdate(State.INIT, () -> State.GO_TO_GOAL);
    }
    protected void updateFSM() { fsm.update(); }
}
