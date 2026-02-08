package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class Vision {
    private final Limelight3A limelight;
    private final Telemetry telemetry;
    private LLResult lastResult;

    public Vision(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        limelight = hardwareMap.get(Limelight3A.class, "Ethernet Device");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);

        lastResult = limelight.getLatestResult();
    }

    public void update() throws RuntimeException {
        LLResult result = limelight.getLatestResult();

        if (result == null)
            throw new RuntimeException("Invalid Camera Data");
        if (!result.isValid())
            throw new RuntimeException("Invalid Camera Data");

        lastResult = result;
    }

    public Pose processVisionPose(TeamColor color) throws RuntimeException {
        update();
        if (lastResult == null)
            throw new RuntimeException("Invalid Result");

        Pose3D poseFTC = lastResult.getBotpose();
        Pose pedroPose = new Pose(
                poseFTC.getPosition().y * 39.37008 + 144.0/2,
                -poseFTC.getPosition().x * 39.37008 + 144.0/2,
                poseFTC.getOrientation().getYaw(AngleUnit.RADIANS) + Math.PI * 3/2
        );

        Pose cameraPose;
        // Fine tuned. Do not touch. Unless the field is way off...
        if (color == TeamColor.BLUE)
            cameraPose = new Pose(pedroPose.getX(), pedroPose.getY()+8, pedroPose.getHeading());
        else if (color == TeamColor.RED)
            cameraPose = new Pose(pedroPose.getX()+8, pedroPose.getY()-4, pedroPose.getHeading());
        else
            cameraPose = new Pose(0, 0, 0);

        return cameraPose;
    }
}
