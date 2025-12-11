package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.geometry.Pose;

public class AutoPoses {
    private static final double
            // goal start specifics
            GOAL_START_X = 144-23.6, GOAL_START_Y = 144-21.6, GOAL_START_ANGLE = 225,
            GOAL_HOME_X = 144-24, GOAL_HOME_Y = 144-54, GOAL_HOME_ANGLE = 0,
            // park start specifics
            PARK_START_X = 0, PARK_START_Y = 0, PARK_START_ANGLE = 0,
            PARK_HOME_X = 24, PARK_HOME_Y = 0, PARK_HOME_ANGLE = 0,
            // shoot
            SHOOT_X = 144-53.8, SHOOT_Y = 144-53.8, SHOOT_ANGLE = 224,
            // intake positions
            INTAKE_START_X = 95, INTAKE_ANGLE = 0,
            FIRST_INTAKE_END_X = 144-19.2, FIRST_INTAKE_Y = 81.5,
            SECOND_INTAKE_END_X = 144-10.2, SECOND_INTAKE_Y = FIRST_INTAKE_Y - 24,
            THIRD_INTAKE_END_X = 144-10.2, THIRD_INTAKE_Y = FIRST_INTAKE_Y - 48,
            // terrain locations
            GATE_CORNER_X = 124, GATE_CORNER_Y = 60;

    public final Pose
            shoot,
            goalStart, goalHome, goalHomeFromIntake,
            parkStart, parkHome,
            firstIntake, firstIntakeEnd,
            secondIntake, secondIntakeEnd,
            thirdIntake, thirdIntakeEnd,
            gateCorner;

    private final TeamColor color;

    private Pose createPose(double x, double y, double heading) {
        switch (color){
            case BLUE:
                return new Pose(144-x, y, Math.toRadians(180-heading));
            case RED:
                return new Pose(x, y, Math.toRadians(heading));
        }
        return null;
    }
    public AutoPoses(TeamColor color) {
        this.color = color;

        goalStart = createPose(GOAL_START_X, GOAL_START_Y, GOAL_START_ANGLE);
        goalHome = createPose(GOAL_HOME_X, GOAL_HOME_Y, GOAL_HOME_ANGLE);
        goalHomeFromIntake = createPose(THIRD_INTAKE_END_X-24, THIRD_INTAKE_Y, GOAL_HOME_ANGLE);

        parkStart = createPose(PARK_START_X, PARK_START_Y, PARK_START_ANGLE);
        parkHome = createPose(PARK_HOME_X, PARK_HOME_Y, PARK_HOME_ANGLE);

        shoot = createPose(SHOOT_X, SHOOT_Y, SHOOT_ANGLE);

        firstIntake = createPose(INTAKE_START_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        firstIntakeEnd = createPose(FIRST_INTAKE_END_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        secondIntake = createPose(INTAKE_START_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        secondIntakeEnd = createPose(SECOND_INTAKE_END_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        thirdIntake = createPose(INTAKE_START_X, THIRD_INTAKE_Y, INTAKE_ANGLE);
        thirdIntakeEnd = createPose(THIRD_INTAKE_END_X, THIRD_INTAKE_Y, INTAKE_ANGLE);

        gateCorner = createPose(GATE_CORNER_X, GATE_CORNER_Y, INTAKE_ANGLE);
    }
}
