package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
@TeleOp(name = "TeleOp")
public class NormalTeleOp extends Teleop{
    @Override
    protected Gamepad getGamepad1() {
        return gamepad1;
    }

    @Override
    protected Gamepad getConfigGamepad() {
        return new Gamepad();
    }

    @Override
    protected Gamepad getGamepad2() {
        return gamepad2;
    }
}
