package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class AutoPaths {
    private final AutoPoses poses;
    private final Follower follower;

    public final PathChain
            goShootPreload,
            goFirstIntake, goShootFirstIntake,
            goSecondIntake, goShootSecondIntake,
            goGoalHome;
    public final PathChain
            goPark;

    public AutoPaths(Follower follower, AutoPoses autoPoses) {
        this.poses = autoPoses;
        this.follower = follower;

        goShootPreload = createPath(poses.goalStart, poses.shoot);
        goFirstIntake = createPath(poses.shoot, poses.firstIntake, poses.firstIntakeEnd);
        goShootFirstIntake = createPath(poses.firstIntakeEnd, poses.shoot);
        goSecondIntake = createPath(poses.shoot, poses.secondIntake, poses.secondIntakeEnd);
        goShootSecondIntake = createPath(poses.secondIntakeEnd, poses.firstIntakeEnd, poses.shoot);
        goGoalHome = createPath(poses.shoot, poses.goalHome);

        goPark = createPath(poses.parkStart, poses.parkHome);
    }

    private PathChain createPath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }
    private PathChain createPath(Pose start, Pose inter, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(start, inter))
                .setLinearHeadingInterpolation(start.getHeading(), inter.getHeading())
                .addPath(new BezierCurve(inter, end))
                .setLinearHeadingInterpolation(inter.getHeading(), end.getHeading())
                .build();
    }
}
