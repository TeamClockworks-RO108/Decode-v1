package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Parking BLUE", group = "Far Start")
public class ParkingBlue extends AutoBase {
    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        GO_PARKING
    }

    @Override
    public void loop() {
        updateFSM();
        movement.update();
        shooter.updateShooter();
    }

    @Override
    protected void setColor() {
        color = TeamColor.BLUE;
    }
    @Override
    protected void setStartingPose() {
        startingPose = poses.parkStart;
    }

    protected void setupFSM(){
        // go to parking position
        fsm.onStateEnter(State.GO_PARKING, () -> {
            movement.followPath(paths.goPark);
        });
        fsm.onStateUpdate(State.GO_PARKING, () -> {
            if (!movement.isBusy()) {
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
