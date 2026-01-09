package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class PosesAuto extends Poses {
    // intake positions
    protected static final double
            INTAKE_START_X = 95, INTAKE_ANGLE = 0,
            FIRST_INTAKE_END_X = 144-19.2, FIRST_INTAKE_Y = 84.5,
            SECOND_INTAKE_END_X = 144-11.2, SECOND_INTAKE_Y = FIRST_INTAKE_Y - 24.7,
            THIRD_INTAKE_END_X = 144-11.2, THIRD_INTAKE_Y = FIRST_INTAKE_Y - 48;

    public final Pose
            shootAuto,
            goalStart, goalHome, goalHomeFromIntake,
            parkStart, parkHome,
            firstIntake, firstIntakeEnd,
            secondIntake, secondIntakeEnd,
            thirdIntake, thirdIntakeEnd,
            gateCorner,
            gateOpen, gateOpenEnd;

    public PosesAuto(TeamColor color) {
        super(color);

        goalStart = createPose(144-23.6, 144-21.6, 225);
        goalHome = createPose(144-24, 144-54, 0);
        goalHomeFromIntake = createPose(THIRD_INTAKE_END_X-24, THIRD_INTAKE_Y, 0);

        parkStart = createPose(0, 0, 0);
        parkHome = createPose(24, 0, 0);

        shootAuto = createPose(144-47.7, 144-47.7, 220);
        shootAuto.setHeading(shootAuto.getHeading() + SHOOT_OFFSET);

        firstIntake = createPose(INTAKE_START_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        firstIntakeEnd = createPose(FIRST_INTAKE_END_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        secondIntake = createPose(INTAKE_START_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        secondIntakeEnd = createPose(SECOND_INTAKE_END_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        thirdIntake = createPose(INTAKE_START_X, THIRD_INTAKE_Y, INTAKE_ANGLE);
        thirdIntakeEnd = createPose(THIRD_INTAKE_END_X, THIRD_INTAKE_Y, INTAKE_ANGLE);

        gateCorner = createPose(124, 60, INTAKE_ANGLE);
        gateOpenEnd = createPose(FIRST_INTAKE_END_X + 1.6, 75, INTAKE_ANGLE);
        gateOpen = createPose (101.8, 75, INTAKE_ANGLE);
    }
}
