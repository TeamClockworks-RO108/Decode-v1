package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;
import java.util.Optional;

public class Vision {
    private final Limelight3A limelight;
    private final Telemetry telemetry;
    private double anglex = 0;
    private LLResult lastResult;

    public Vision(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);
        lastResult = limelight.getLatestResult();
    }

    public void update() throws Exception {
        LLResult result = limelight.getLatestResult();

        if (result == null)
            throw new Exception("Invalid Camera Data");
        if (!result.isValid())
            throw new Exception("Invalid Camera Data");

        lastResult = result;
    }

    public LLResult getLastResult() {
        return lastResult;
    }

    public Pose processVisionPose() throws Exception {
        update();
        if (getLastResult() == null)
            throw new Exception("Invalid Result");

        Pose3D pose = getLastResult().getBotpose();
        Pose pose2 = new Pose(
                pose.getPosition().x * 39.37008 + 144.0/2,
                -pose.getPosition().y * 39.37008 + 144.0/2,
                pose.getOrientation().getYaw(AngleUnit.RADIANS) + Math.PI * 3/2
        );

        telemetry.addData("Pedro Pose", pose2);

        return pose2;
    }
}
