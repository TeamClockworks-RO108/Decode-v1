package org.firstinspires.ftc.teamcode.movement;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MovementRobotCentric {




    private DcMotor frontRight;
    private DcMotor rearRight;
    private DcMotor frontLeft;
    private DcMotor rearleft;

    public MovementRobotCentric(HardwareMap hardwareMap){
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        rearRight = hardwareMap.get(DcMotor.class, "rightRear");
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        rearleft = hardwareMap.get(DcMotor.class, "leftRear");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearleft.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public void updateMovementRC(double x,double  y ,  double heading ){
         x = x * 1.1; // Counteract imperfect strafing
        double rx = heading;
        double frontLeftPower = (y + x + rx) ;
        double backLeftPower = (y - x + rx) ;
        double frontRightPower = (y - x - rx);
    double backRightPower = (y + x - rx);

    frontRight.setPower(frontRightPower);
    frontLeft.setPower(frontLeftPower);
    rearRight.setPower(backRightPower);
    rearleft.setPower(backLeftPower);
    }


}
