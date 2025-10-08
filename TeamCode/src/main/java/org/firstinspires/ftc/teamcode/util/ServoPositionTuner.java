package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.util.StateMachine;

public class ServoPositionTuner {

    enum State{
        IDLE,
        NO_SIGNAL,
        BINARY_SEARCH,
    }


    private final StateMachine<State> fsm = new StateMachine<>(State.IDLE);


}
