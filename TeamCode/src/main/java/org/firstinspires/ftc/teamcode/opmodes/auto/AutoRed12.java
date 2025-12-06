package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Auto RED 12")
public class AutoRed12 extends AutoBlue12 {
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
