package org.firstinspires.ftc.teamcode.opmodes.auto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Shooter;


@Autonomous(name = "Auto BLUE 67")
public class AutoBlue67 extends AutoBlue12 {
    @Override
    protected void setupFSM(){
        super.setupFSM();

        fsm.onStateEnter(State.GRAB_B, () -> {

        });
        fsm.onStateUpdate(State.GRAB_B, (current, timeSinceTransition) -> {
            if (timeSinceTransition > grabTime)
                return State.GO_OPEN_GATE;
            return null;
        });

        fsm.onStateEnter(State.GO_OPEN_GATE, () -> {
            movement.followPath(paths.goOpenGate);
        });

        fsm.onStateUpdate(State.GO_OPEN_GATE, () -> {
            if( !movement.isBusy()){
                return State.SHOOT_FIRST_INTAKE;
            }
            return  null;
        });

        fsm.onStateEnter(State.SHOOT_FIRST_INTAKE, () -> {
            movement.followPath(paths.goGateGoal);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);

        });
        fsm.onStateUpdate(State.SHOOT_FIRST_INTAKE, () -> {
            if (!movement.isBusy())
                return State.SHOOT_B;
            return null;
        });
    }
}
