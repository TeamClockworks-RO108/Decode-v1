package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Auto RED 9 + load")
public class AutoRed9Load extends AutoBlue9Load {
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
