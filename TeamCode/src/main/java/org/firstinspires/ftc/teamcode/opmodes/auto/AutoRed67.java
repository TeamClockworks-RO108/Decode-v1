package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Auto RED 67")
public class AutoRed67 extends AutoBlue67{
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
