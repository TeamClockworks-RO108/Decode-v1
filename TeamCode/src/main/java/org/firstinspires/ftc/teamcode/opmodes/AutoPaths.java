package org.firstinspires.ftc.teamcode.opmodes;

import android.graphics.Point;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class AutoPaths {
    private final AutoPoses poses;
    private final Follower follower;

    public final PathChain
            goShootPreload,
            goFirstIntake, goOpenGate, goGateGoal, goShootFirstIntake,
            goSecondIntake, goShootSecondIntake,
            goThirdIntake, goShootThirdIntake,
            goGoalHome, goIntakeGoalHome;
    public final PathChain
            goPark;

    public AutoPaths(Follower follower, AutoPoses autoPoses) {
        this.poses = autoPoses;
        this.follower = follower;

        goShootPreload = createPath(poses.goalStart, poses.shoot);
        goFirstIntake = createPath(
                poses.shoot, poses.firstIntake, poses.firstIntakeEnd);
        goShootFirstIntake = createPath(poses.firstIntakeEnd, poses.shoot);
        goSecondIntake = createPath(
                poses.shoot, poses.secondIntake, poses.secondIntakeEnd);
        goShootSecondIntake = createPath(poses.secondIntakeEnd, poses.gateCorner, poses.shoot);
        goThirdIntake = createPath(
                poses.shoot, poses.thirdIntake, poses.thirdIntakeEnd);
        goShootThirdIntake = createPath(poses.thirdIntakeEnd, poses.shoot);
        goGoalHome = createPath(poses.shoot, poses.goalHome);

        goIntakeGoalHome = createPath(poses.thirdIntakeEnd, poses.goalHomeFromIntake);

        goPark = createPath(poses.parkStart, poses.parkHome);

        goOpenGate = follower.pathBuilder()
                .addPath(new BezierCurve(poses.firstIntakeEnd, poses.offsetPosition, poses.gateOpen))
                .setConstantHeadingInterpolation(poses.firstIntakeEnd.getHeading())
                .build();
        goGateGoal = createPath(poses.gateOpen, poses.shoot);
    }

    private PathChain createPath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }
    private PathChain createPath(Pose start, Pose inter, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(start, inter, end))
                .setLinearHeadingInterpolation(start.getHeading(), inter.getHeading())
                .addPath(new BezierCurve(inter, end))
                .setLinearHeadingInterpolation(inter.getHeading(), end.getHeading())
                .build();
    }
//    private PathChain createPathWithCorner(Pose start, Pose inter, Pose end) {
//        return follower.pathBuilder()
//                .addPath(new BezierLine(start, inter))
//                .setLinearHeadingInterpolation(start.getHeading(), inter.getHeading())
//                .addPath(new BezierLine(inter, end))
//                .setLinearHeadingInterpolation(inter.getHeading(), end.getHeading())
//                .build();
//    }

}
