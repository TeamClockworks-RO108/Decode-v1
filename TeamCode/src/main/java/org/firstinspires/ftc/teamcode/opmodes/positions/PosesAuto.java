package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class PosesAuto extends Poses {
    // intake positions
    protected static final double
            INTAKE_START_X = 98, INTAKE_ANGLE = 0,
            FIRST_INTAKE_END_X = 144-18.2, FIRST_INTAKE_Y = 84,
            SECOND_INTAKE_END_X = 144-9.8, SECOND_INTAKE_Y = FIRST_INTAKE_Y - 24.3,
            THIRD_INTAKE_END_X = 144-9.8, THIRD_INTAKE_Y = FIRST_INTAKE_Y - 48;

    public Pose goalStart, shootAuto;
    public final Pose
            goalHome, goalHomeTurned,
            goalHomeFromIntake,
            parkStart, parkHome,
            firstIntake, firstIntakeEnd,
            secondIntake, secondIntakeEnd,
            thirdIntake, thirdIntakeEnd,
            gateCorner, gateOpen, gateOpenEnd;

    public PosesAuto(TeamColor color) {
        super(color);

        goalStart = createPose(144-21, 144-21, 225);
        goalHome = createPose(144-24, 144-54, 225);
        goalHomeTurned = createPose(144-24, 144-54, 0);
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

        gateCorner = createPose(122, 60, INTAKE_ANGLE);
        gateOpenEnd = createPose(144-16.8, 72.5, INTAKE_ANGLE);
        gateOpen = createPose (115, 74, INTAKE_ANGLE);

        //
        // TO BE CHANGED BASED ON THE FIELD BEFORE EVERY EVENT!!!
        // CALIBRATION NEEDED!!!
        if (color == TeamColor.RED)
            calibrateStart(0.85, 0, 0);
        if (color == TeamColor.BLUE)
            calibrateStart(-0.85, 0, -5);

   }

    public void calibrateStart(double x, double y, double shootHeading) {
        goalStart = new Pose(goalStart.getX() + x, goalStart.getY() + y, goalStart.getHeading());
        shootAuto = new Pose(shootAuto.getX() + x, shootAuto.getY() + y,
                shootAuto.getHeading() + Math.toRadians(shootHeading));

    }
}
