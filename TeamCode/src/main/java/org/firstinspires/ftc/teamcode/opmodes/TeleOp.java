package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.movement.Movement;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop")
public class TeleOp extends OpMode {

     private Movement movement = null;

    @Override
    public void init() {
      movement = new Movement(hardwareMap);
    }
    @Override
    public void start() {

    }

    @Override
    public void loop() {
       movement.movementLoop(gamepad1);
    }

}