package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
@TeleOp(name = "Config Gamepad 1")
public class ConfigGamepad1 extends Teleop{
    @Override
    protected Gamepad getGamepad1() {
        return new Gamepad();
    }

    @Override
    protected Gamepad getConfigGamepad() {
        return gamepad1;
    }

    @Override
    protected Gamepad getGamepad2() {
        return gamepad2;
    }


//    @Override
//    protected Gamepad getGamepad1() {
//        return new Gamepad();
//    }
//    @Override
//    protected Gamepad getGamepad2() {
//        return gamepad2;
//    }
//
//    @Override
//    protected Gamepad getConfigGamepad() {
//        return gamepad1;
//    }
}
