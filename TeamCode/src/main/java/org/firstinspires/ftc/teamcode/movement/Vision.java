package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.ftc.PoseConverter;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    public void update() {
        LLResult result = limelight.getLatestResult();

        if (result == null)
            return;
        if (!result.isValid())
            return;

        List<LLResultTypes.FiducialResult> aprilTags = result.getFiducialResults();
        for (LLResultTypes.FiducialResult aprilTag : aprilTags) {
            int id = aprilTag.getFiducialId();
            if (id != 20)
                continue;
            anglex = aprilTag.getTargetXDegrees();
        }

        lastResult = result;
    }
    public double getAngleX() {
        return anglex;
    }

    public LLResult getLastResult() {
        return lastResult;
    }
}
