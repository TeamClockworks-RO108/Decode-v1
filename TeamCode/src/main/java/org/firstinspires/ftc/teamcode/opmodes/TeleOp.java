package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop")
public class TeleOp extends OpMode {

     private Movement movement = null;
     private Shooter shooter = null;

     private EdgeDetector toggleShooter = new EdgeDetector(false);
     private EdgeDetector launchGE = new EdgeDetector(false);
     private EdgeDetector toggleIntake = new EdgeDetector(false);
    private EdgeDetector toggleIdle = new EdgeDetector(false);
    @Override
    public void init() {
        movement = new Movement(hardwareMap);
        shooter = new Shooter(hardwareMap);
        // shooter command setup
        toggleShooter.onPress(() -> shooter.command(Shooter.Command.TOGGLE_SHOOTER));
        launchGE.onPress(() -> shooter.command(Shooter.Command.ACTIVATE_FLAP));
        toggleIntake.onPress(() -> shooter.command(Shooter.Command.TOGGLE_INTAKE));
        toggleIdle.onPress(() -> shooter.command(Shooter.Command.TOGGLE_IDLE));

        shooter.setupShooter();
    }
    @Override
    public void start() {

    }

    @Override
    public void loop() {
        // drive loop
        movement.movementLoop(gamepad1);
        // shooter controls
        toggleShooter.update(gamepad1.cross);
        launchGE.update(gamepad1.triangle);
        toggleIntake.update(gamepad1.square);
        toggleIdle.update(gamepad1.left_bumper);

        shooter.updateShooter();
    }
}