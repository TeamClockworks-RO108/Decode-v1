package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class TeleOpPoses {
    private final TeamColor color;

    public Pose humanBase, shooting, parking;

    public TeleOpPoses(TeamColor color) {
        this.color = color;

        this.humanBase = createPose(9, 12, 180);
        this.shooting = createPose(144-47, 144-47, 225);
        this.parking = createPose(38.5, 33, 180);
    }

    private Pose createPose(double x, double y, double heading) {
        switch (color) {
            case BLUE:
                return new Pose(144-x, 144-y, 180-heading);
            case RED:
                return new Pose(x, y, heading);
        }
        return null;
    }
}
