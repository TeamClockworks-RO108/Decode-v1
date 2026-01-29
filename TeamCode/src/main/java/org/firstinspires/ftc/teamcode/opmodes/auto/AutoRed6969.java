package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Auto RED 6969")
public class AutoRed6969 extends AutoBlue6969 {
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
