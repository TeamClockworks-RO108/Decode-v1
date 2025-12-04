package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.geometry.Pose;

public class AutoPoses {
    private static final double
            // goal start specifics
            GOAL_START_X = 22, GOAL_START_Y = 144-22, GOAL_START_ANGLE = -45,
            GOAL_HOME_X = 24, GOAL_HOME_Y = 90, GOAL_HOME_ANGLE = 180,
            // park start specifics
            PARK_START_X = 0, PARK_START_Y = 0, PARK_START_ANGLE = 0,
            PARK_HOME_X = 24, PARK_HOME_Y = 0, PARK_HOME_ANGLE = 0,
            // shoot
            SHOOT_X = 55.4, SHOOT_Y = 144 - 55.4, SHOOT_ANGLE = -45,
            // intake positions
            INTAKE_START_X = 45, INTAKE_ANGLE = 180,
            FIRST_INTAKE_END_X = 16, FIRST_INTAKE_Y = 84,
            SECOND_INTAKE_END_X = 8, SECOND_INTAKE_Y = 60,
            THIRD_INTAKE_END_X = 8, THIRD_INTAKE_Y = 36;

    public final Pose
            shoot,
            goalStart, goalHome,
            parkStart, parkHome,
            firstIntake, firstIntakeEnd,
            secondIntake, secondIntakeEnd,
            thirdIntake, thirdIntakeEnd,
            gateCorner;

    private final TeamColor color;

    private Pose createPose(double x, double y, double heading) {
        switch (color){
            case BLUE:
                return new Pose(x, y, Math.toRadians(heading));
            case RED:
                return new Pose(-x, y, Math.toRadians(-heading));
        }
        return null;
    }
    public AutoPoses(TeamColor color) {
        this.color = color;

        goalStart = createPose(GOAL_START_X, GOAL_START_Y, GOAL_START_ANGLE);
        goalHome = createPose(GOAL_HOME_X, GOAL_HOME_Y, GOAL_HOME_ANGLE);

        parkStart = createPose(PARK_START_X, PARK_START_Y, PARK_START_ANGLE);
        parkHome = createPose(PARK_HOME_X, PARK_HOME_Y, PARK_HOME_ANGLE);

        shoot = createPose(SHOOT_X, SHOOT_Y, SHOOT_ANGLE);

        firstIntake = createPose(INTAKE_START_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        firstIntakeEnd = createPose(FIRST_INTAKE_END_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        secondIntake = createPose(INTAKE_START_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        secondIntakeEnd = createPose(SECOND_INTAKE_END_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        thirdIntake = createPose(INTAKE_START_X, THIRD_INTAKE_Y, INTAKE_ANGLE);
        thirdIntakeEnd = createPose(THIRD_INTAKE_END_X, THIRD_INTAKE_Y, INTAKE_ANGLE);

        gateCorner = createPose(24, 60, INTAKE_ANGLE);
    }
}
