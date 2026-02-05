package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class AutoPaths {
    private final PosesAuto poses;
    private final Follower follower;

    public final PathChain
            goShootPreload,  leaveFar,
            goFirstIntake, goOpenGate, goGateGoal, goShootFirstIntake,
            goSecondIntake, goShootSecondIntake,
            goThirdIntake, goShootThirdIntake,
            goGoalHome, goTurnHome, goIntakeGoalHome,
    goShootMiddlePreload,

    goFirstIntake69, goShootFirstIntake69,

    goSecondIntake69, goShootSecondIntake69,
    goShootThirdIntake69,
            goThirdIntake69,


    goOpenGate69, goGateGoal69, turnCollectFromGate69, goCollectFromGate69, goShootMiddleFromGate69;


    public final PathChain
            goPark;

    public AutoPaths(Follower follower, PosesAuto posesAuto) {
        this.poses = posesAuto;
        this.follower = follower;

        goShootPreload = createPath(poses.goalStart, poses.shootAuto);
        goShootMiddlePreload = createPath(poses.goalStart, poses.shootMiddleAuto);

        leaveFar = createPath(poses.farStart, poses.leaveFar);
        goFirstIntake = createPath(
                poses.shootAuto, poses.firstIntake, poses.firstIntakeEnd);
        goShootFirstIntake = createPath(poses.firstIntakeEnd, poses.shootAuto);
        goSecondIntake = createPath(
                poses.shootAuto, poses.secondIntake, poses.secondIntakeEnd);
        goShootSecondIntake = createPath(poses.secondIntakeEnd, poses.gateCorner, poses.shootAuto);
        goThirdIntake = createPath(
                poses.shootAuto, poses.thirdIntake, poses.thirdIntakeEnd);
        goShootThirdIntake = createPath(poses.thirdIntakeEnd, poses.shootAuto);
        goGoalHome = createPath(poses.shootAuto, poses.goalHome);

        goIntakeGoalHome = createPath(poses.thirdIntakeEnd, poses.goalHomeFromIntake);

        goPark = createPath(poses.parkStart, poses.parkHome);

        goOpenGate = follower.pathBuilder()
                .addPath(new BezierCurve(poses.firstIntakeEnd, poses.gateOpen, poses.gateOpenEnd))
                .setConstantHeadingInterpolation(poses.firstIntakeEnd.getHeading())
                .build();
        goGateGoal = createPath(poses.gateOpenEnd, poses.shootAuto);

        goTurnHome = createPath(poses.goalHome, poses.goalHomeTurned);


        goOpenGate69 = follower.pathBuilder()
                .addPath(new BezierCurve(poses.secondIntakeEnd, poses.gateOpen69, poses.gateOpenEnd))
                .setConstantHeadingInterpolation(poses.secondIntakeEnd.getHeading())
                .build();
        goGateGoal69 = createPath(poses.gateOpenEnd, poses.shootMiddleAuto);

        turnCollectFromGate69 =createPath(poses.shootMiddleAuto, poses.turnCollectFromGate);
        goCollectFromGate69 =createPath(poses.turnCollectFromGate, poses.goCollectFromGate);
        goShootMiddleFromGate69 = createPath(poses.goCollectFromGate, poses.shootMiddleAuto);


        goFirstIntake69 = createPath(poses.shootMiddleAuto, poses.firstIntake69, poses.firstIntakeEnd69);
        goShootFirstIntake69 = createPath(poses.firstIntakeEnd69, poses.middleOftheFieldBLUE ,  poses.shootMiddleAuto);

        goSecondIntake69 = createPath(poses.shootMiddleAuto, poses.secondIntake69, poses.secondIntakeEnd69);
        goShootSecondIntake69 = createPath(poses.secondIntakeEnd69, poses.shootMiddleAuto);

        goThirdIntake69 = createPath(poses.shootMiddleAuto, poses.thirdIntake69, poses.thirdIntakeEnd69);
        goShootThirdIntake69 = createPath(poses.thirdIntakeEnd69, poses.shootAuto);




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
//    private PathChain createPathWithCorner(Pose start, Pose inter, Pose end) {
//        return follower.pathBuilder()
//                .addPath(new BezierLine(start, inter))
//                .setLinearHeadingInterpolation(start.getHeading(), inter.getHeading())
//                .addPath(new BezierLine(inter, end))
//                .setLinearHeadingInterpolation(inter.getHeading(), end.getHeading())
//                .build();
//    }

}
