package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.geometry.Pose;

public class AutoPoses {
    private static final double
            // goal start specifics
            GOAL_START_X = 10, GOAL_START_Y = 10, GOAL_START_ANGLE = 45,
            GOAL_HOME_X = 45, GOAL_HOME_Y = 15, GOAL_HOME_ANGLE = -90,
            // park start specifics
            PARK_START_X = 0, PARK_START_Y = 0, PARK_START_ANGLE = 0,
            PARK_HOME_X = 24, PARK_HOME_Y = 0, PARK_HOME_ANGLE = 0,
            // shoot
            SHOOT_X = 43.4, SHOOT_Y = 43.4, SHOOT_ANGLE = 45,
            // intake positions
            INTAKE_START_Y = 42, INTAKE_ANGLE = -90,
            FIRST_INTAKE_X = 49.6, FIRST_INTAKE_END_Y = -3,
            SECOND_INTAKE_X = 73.6, SECOND_INTAKE_END_Y = -3,
            THIRD_INTAKE_X = 100, THIRD_INTAKE_END_Y = -3;

    public final Pose
            shoot,
            goalStart, goalHome,
            parkStart, parkHome,
            firstIntake, firstIntakeEnd,
            secondIntake, secondIntakeEnd,
            thirdIntake, thirdIntakeEnd;

    private final TeamColor color;

    private Pose createPose(double x, double y, double heading) {
        switch (color){
            case BLUE:
                return new Pose(x, y, Math.toRadians(heading));
            case RED:
                return new Pose(x, -y, Math.toRadians(-heading));
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

        firstIntake = createPose(FIRST_INTAKE_X, INTAKE_START_Y, INTAKE_ANGLE);
        firstIntakeEnd = createPose(FIRST_INTAKE_X, FIRST_INTAKE_END_Y, INTAKE_ANGLE);
        secondIntake = createPose(SECOND_INTAKE_X, INTAKE_START_Y, INTAKE_ANGLE);
        secondIntakeEnd = createPose(SECOND_INTAKE_X, SECOND_INTAKE_END_Y, INTAKE_ANGLE);
        thirdIntake = createPose(THIRD_INTAKE_X, INTAKE_START_Y, INTAKE_ANGLE);
        thirdIntakeEnd = createPose(THIRD_INTAKE_X, THIRD_INTAKE_END_Y, INTAKE_ANGLE);
    }
}
