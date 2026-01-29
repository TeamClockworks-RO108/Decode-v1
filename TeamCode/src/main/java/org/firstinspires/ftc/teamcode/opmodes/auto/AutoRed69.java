package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Auto RED 69")
public class AutoRed69 extends AutoBlue69 {
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
