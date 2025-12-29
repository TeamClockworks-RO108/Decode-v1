package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Vision {
    private final Limelight3A limelight;
    private final Telemetry telemetry;
    private double anglex = 0;

    public Vision(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);
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
            anglex = aprilTag.getTargetXDegrees();
//            double angley = aprilTag.getTargetYDegrees();
//            Pose3D cameraPose = aprilTag.getCameraPoseTargetSpace();

//            telemetry.addData("Camera position:   ",
//                    cameraPose.getPosition());
//            telemetry.addData("Camera orientation:",
//                    cameraPose.getOrientation());
        }

    }
    public double getAngleX() {
        return anglex;
    }
}
