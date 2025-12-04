package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

@Autonomous(name = "Parking RED")
public class ParkingRed extends ParkingBlue {
    @Override
    public void init() {
        color = TeamColor.RED;
        super.init();
    }
}
