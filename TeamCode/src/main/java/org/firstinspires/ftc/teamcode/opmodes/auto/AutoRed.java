package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Disabled
@Autonomous(name = "Auto RED 9")
public class AutoRed extends AutoBlue {
    @Override
    protected void setColor() {
        color = TeamColor.RED;
    }
}
