package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class PosesTeleOp extends Poses{
    public Pose start, humanBase, shootTeleOp, parking, gate;

    public PosesTeleOp(TeamColor color) {
        super(color);

        start = createPose(0, 0, 0);
        humanBase = createPose(9, 9, 180);
        shootTeleOp = createPose(144-47.7, 144-47.7, 220);
        parking = createPose(36, 31, 180);
        gate = createPose(127, 69, 180);

        shootTeleOp.setHeading(shootTeleOp.getHeading() + SHOOT_OFFSET);

        //
        // YOU MAY TWEAK HEADINGS BASED ON FIELD FOR EVENTS!!!
        //
        if (color == TeamColor.RED)
            shootTeleOp = new Pose(shootTeleOp.getX(), shootTeleOp.getY(),
                    shootTeleOp.getHeading()+ Math.toRadians(5));
    }
}
