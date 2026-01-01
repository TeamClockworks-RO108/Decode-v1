package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class TeleOpPoses {
    private final TeamColor color;
    private final double SHOOT_OFFSET = -5;

    public Pose start, humanBase, shooting, parking, gate;

    public TeleOpPoses(TeamColor color) {
        this.color = color;

        start = createPose(0, 0, 0);
        humanBase = createPose(9, 9, 180);
        shooting = createPose(144-48.5, 144-48.5, 235);
        parking = createPose(38.5, 33, 180);
        gate = createPose(127, 69, 180);

        shooting.setHeading(shooting.getHeading() + SHOOT_OFFSET);
    }

    private Pose createPose(double x, double y, double heading) {
        switch (color) {
            case BLUE:
                return new Pose(144-x, y, Math.toRadians(180-heading));
            case RED:
                return new Pose(x, y, Math.toRadians(heading));
        }
        return null;
    }
}
