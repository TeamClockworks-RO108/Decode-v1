package org.firstinspires.ftc.teamcode.opmodes.calibration;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@TeleOp(name = "Xx Calibrate RED xX", group = "Xx Calibrate")
public class CalibrateRed extends CalibrateBlue {
    @Override
    public void init() {
        color = TeamColor.RED;
        super.init();
    }
}
