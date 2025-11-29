package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto RED")
public class AutoRed extends AutoBlue {
    @Override
    public void init() {
        // mirror all robot poses
        startPosition = new Pose(startPosition.getX(),-startPosition.getY(), -startPosition.getHeading());
        shootPosition = new Pose( shootPosition.getX(), -shootPosition.getY(), -shootPosition.getHeading());
        firstIntakePosition = new Pose(firstIntakePosition.getX(), -firstIntakePosition.getY(), -firstIntakePosition.getHeading());
        firstIntakeTake = new Pose(firstIntakeTake.getX(), -firstIntakeTake.getY(), -firstIntakePosition.getHeading());
        homePosition = new Pose(homePosition.getX(), -homePosition.getY(), -homePosition.getHeading());

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
