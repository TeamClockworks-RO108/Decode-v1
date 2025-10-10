package org.firstinspires.ftc.teamcode.movement;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Movement {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;


    public Movement (HardwareMap hardwareMap){
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
}

    public void movementLoop (Gamepad gamepad){
         double x = gamepad.left_stick_x;
        double y = -gamepad.left_stick_y;
        double heading = -gamepad.right_stick_x;

        x = x * 1.1; // Counteract imperfect strafing
        double rx = heading;
        double frontLeftPower = (y + x + rx) ;
        double backLeftPower = (y - x + rx) ;
        double frontRightPower = (y - x - rx);
        double backRightPower = (y + x - rx);

        leftFront.setPower(frontLeftPower);
        rightFront.setPower(frontRightPower);
        leftRear.setPower(backLeftPower);
        rightRear.setPower(backRightPower);

    }


}
