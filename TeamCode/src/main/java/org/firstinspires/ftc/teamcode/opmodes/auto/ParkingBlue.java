package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.AutoPoses;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Parking BLUE")
public class ParkingBlue extends AutoBase {
    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        GO_PARKING
    }

    @Override
    public void loop() {
        updateFSM();
        follower.update();
        shooter.updateShooter();
    }

    @Override
    protected void setColor() {
        color = TeamColor.BLUE;
    }
    @Override
    protected void setStartingPose() {
        follower.setStartingPose(poses.parkStart);
    }

    protected void setupFSM(){
        // go to parking position
        fsm.onStateEnter(State.GO_PARKING, () -> {
            follower.followPath(paths.goPark);
        });
        fsm.onStateUpdate(State.GO_PARKING, () -> {
            if (!follower.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();
    }

    @Override
    protected void startFSM() {
        fsm.onStateUpdate(State.INIT, () -> State.GO_PARKING);
    }
    protected void updateFSM() {
        fsm.update();
    }
}
