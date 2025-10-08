package org.firstinspires.ftc.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        ThreeWheelIMUConstants.forwardTicksToInches  = .002;
        ThreeWheelIMUConstants.strafeTicksToInches = -.002;
        ThreeWheelIMUConstants.turnTicksToInches = -.002;
        ThreeWheelIMUConstants.rightY = -103/(2*25.4);
        ThreeWheelIMUConstants.leftY = 103/(2*25.4);
        ThreeWheelIMUConstants.strafeX = -80/(25.4);
        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = "rightFront";
        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = "leftFront";
        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = "strafeEncoder";
        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.FORWARD;
        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP);


//        TwoWheelConstants.forwardTicksToInches = .001989436789;
//        TwoWheelConstants.strafeTicksToInches = .001989436789;
//        TwoWheelConstants.forwardY = 0;
//        TwoWheelConstants.strafeX = 0;
//        TwoWheelConstants.strafeX = -2.5;
//        TwoWheelConstants.forwardEncoder_HardwareMapName = "rightRear";
//        TwoWheelConstants.strafeEncoder_HardwareMapName = "liftMotor1";
//        TwoWheelConstants.forwardEncoderDirection = Encoder.FORWARD;
//        TwoWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
//        TwoWheelConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP);
//        TwoWheelConstants.IMU_HardwareMapName = "imu";
    }
}




