package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;


@TeleOp(name = "Config Gamepad 2")
public class ConfigGamepad2 extends  Teleop{
    @Override
    protected Gamepad getGamepad1() {
        return gamepad1;
    }

    @Override
    protected Gamepad getConfigGamepad() {
        return gamepad2;
    }

    @Override
    protected Gamepad getGamepad2() {
        return new Gamepad();
    }
}
