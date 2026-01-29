package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.Shooter;

@Autonomous(name = "Auto BLUE 6969")
public class AutoBlue6969 extends AutoBlue69 {
    @Override
    protected void setupFSM() {
        super.setupFSM();

        setShoot3Artifacts(State.SHOOT_FROM_GATE, State.GATE_INTAKE_2);

        fsm.onStateEnter(State.GATE_INTAKE_2, () -> {
            movement.followPath(paths.turnCollectFromGate69);
        });
        fsm.onStateUpdate(State.GATE_INTAKE_2, () -> {
            if(!movement.isBusy()){
                return State.GATE_COLLECT_2;
            }
            return null;
        });

        fsm.onStateEnter(State.GATE_COLLECT_2, () -> {
            movement.followPath(paths.goCollectFromGate69);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
            return State.WAIT_TO_COLLECT_2;
        });
        fsm.onStateUpdate(State.WAIT_TO_COLLECT_2, (current, timeSinceTransition) -> {
            if(timeSinceTransition > timeToCollectFromGate){
                return State.SHOOT_GATE_INTAKE_2;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_GATE_INTAKE_2, () -> {
            movement.followPath(paths.goShootMiddleFromGate69);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);

        });
        fsm.onStateUpdate(State.SHOOT_GATE_INTAKE_2, () -> {
            if(!movement.isBusy()){
                return State.SHOOT_FROM_GATE_2;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_FROM_GATE_2, State.FOURTH_INTAKE);
    }
}
