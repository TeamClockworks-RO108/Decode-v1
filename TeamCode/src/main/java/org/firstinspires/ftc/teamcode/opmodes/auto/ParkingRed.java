package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Parking RED")
public class ParkingRed extends ParkingBlue {
    @Override
    public void init() {
        startPosition = new Pose(startPosition.getX(), -startPosition.getY(), -startPosition.getHeading());
        parkingPosition = new Pose(parkingPosition.getX(), -parkingPosition.getY(), -parkingPosition.getHeading());

        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    public void setupFSM(){
        super.setupFSM();
    }

    @Override
    public void updateFSM() {
        super.updateFSM();
    }

    @Override
    public void setupPaths(){
        super.setupPaths();
    }
}
