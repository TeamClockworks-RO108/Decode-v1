package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.pedroPathing.constants.LConstants;

public class Movement {

    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    public Movement(HardwareMap hardwareMap)
    {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
    }

    public void startMovement() {
        follower.startTeleopDrive();
    }


    public void loopMovement(double y, double x, double heading, Telemetry telemetry){
        follower.setTeleOpMovementVectors(y, -x, -heading, false);
        follower.update();

       telemetry.addData("X", follower.getPose().getX());
       telemetry.addData("Y", follower.getPose().getY());
       telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

    }
}
