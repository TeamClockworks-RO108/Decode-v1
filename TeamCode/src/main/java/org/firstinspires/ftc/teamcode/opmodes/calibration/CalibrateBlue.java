package org.firstinspires.ftc.teamcode.opmodes.calibration;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.movement.PedroMovement;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesAuto;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesTeleOp;
import org.firstinspires.ftc.teamcode.util.EdgeDetector;

@TeleOp(name = "Xx Calibrate BLUE xX", group = "Xx Calibrate")
public class CalibrateBlue extends OpMode {
    protected TeamColor color = TeamColor.BLUE;
    private PosesTeleOp posesTeleOp;
    private PosesAuto posesAuto;
    private PedroMovement movement = null;

    private EdgeDetector resetToCamera = new EdgeDetector(false);
    private EdgeDetector resetGate = new EdgeDetector(false);

    @Override
    public void init() {
        posesTeleOp = new PosesTeleOp(color);
        posesAuto = new PosesAuto(color);

        movement = new PedroMovement(hardwareMap, telemetry, posesAuto.goalStart);

        resetGate.onPress(() -> {
            movement.setPose(posesTeleOp.gate);
            gamepad2.rumble(150);
        });
        resetToCamera.onPress(() -> {
            try {
                movement.updateToCameraPose(color);
                gamepad2.rumble(150);
            } catch (Exception ignored) {

            }
        });
    }

    @Override
    public void loop() {
        movement.update();

        resetGate.update(gamepad2.dpad_right);
        resetToCamera.update(gamepad2.dpad_up);

        telemetry.update();
    }
}
