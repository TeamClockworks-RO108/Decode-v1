package org.firstinspires.ftc.teamcode.opmodes.positions;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public class PosesAuto extends Poses {
    // intake positions
    protected static final double
            INTAKE_START_X = 98
            , INTAKE_ANGLE = 0,
            FIRST_INTAKE_END_X = 144-18.2, FIRST_INTAKE_Y = 84,
            SECOND_INTAKE_END_X = 144-9.8, SECOND_INTAKE_Y = FIRST_INTAKE_Y - 24.3,
            THIRD_INTAKE_END_X = 144-9.8, THIRD_INTAKE_Y = FIRST_INTAKE_Y - 48;

    private double calibratedXBLUE = 1.5, calibratedYBLUE= 0, calibratedHEADINGBLUE = 0;
    private double calibratedXRED = 0 , calibratedYRED = 0 , calibratedHEADINGRED= 0 ;

    public Pose goalStart, shootAuto;

    public Pose farStart, leaveFar;
    public final Pose
            goalHome;
    public final Pose goalHomeTurned;
    public Pose shootMiddleAuto;
    public final Pose goalHomeFromIntake;
    public final Pose parkStart;
    public final Pose parkHome;
    public final Pose firstIntake;
    public final Pose firstIntakeEnd;
    public final Pose secondIntake;
    public final Pose secondIntakeEnd;
    public final Pose thirdIntake;
    public final Pose thirdIntakeEnd;
    public final Pose middleOftheFieldBLUE;
    public final Pose middleOftheFieldRED;
    public final Pose gateOpen69;
    public final Pose gateCorner;
    public final Pose gateOpen;
    public final Pose gateOpenEnd;
    public final Pose turnCollectFromGate;
    public final Pose goCollectFromGate;
    public final Pose goCollectFromGateOffic;
    public final Pose firstIntake69;
    public final Pose firstIntakeEnd69;

    public final Pose secondIntake69;
    public final Pose secondIntakeEnd69;
    public final Pose thirdIntake69;
    public final Pose thirdIntakeEnd69;



    public PosesAuto(TeamColor color) {
        super(color);

        goalStart = createPose(144-21, 144-21, 225);
        farStart = createPose(57, 10, 225);
        leaveFar = createPose(77, 10, 225);
        goalHome = createPose(144-24, 144-54, 225);
        goalHomeTurned = createPose(144-24, 144-54, 0);
        goalHomeFromIntake = createPose(THIRD_INTAKE_END_X-24, THIRD_INTAKE_Y, 0);

        parkStart = createPose(0, 0, 0);
        parkHome = createPose(24, 0, 0);

        shootAuto = createPose(144-48.6, 144-48.6, 226.5);
        shootAuto.setHeading(shootAuto.getHeading() + SHOOT_OFFSET);

        shootMiddleAuto = createPose(144-55, 144-55, 225.0);
        shootMiddleAuto.setHeading(shootMiddleAuto.getHeading() + SHOOT_OFFSET);

        middleOftheFieldBLUE = createPose(144-52.7, 144-66, 220); // pose so as to not touch the first a ball
        middleOftheFieldRED = createPose(144-54.5, 144-66, 220); // pose so as to not touch the first a ball

        firstIntake = createPose(INTAKE_START_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        firstIntakeEnd = createPose(FIRST_INTAKE_END_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        secondIntake = createPose(INTAKE_START_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        secondIntakeEnd = createPose(SECOND_INTAKE_END_X, SECOND_INTAKE_Y, INTAKE_ANGLE);
        thirdIntake = createPose(INTAKE_START_X, THIRD_INTAKE_Y, INTAKE_ANGLE);
        thirdIntakeEnd = createPose(THIRD_INTAKE_END_X, THIRD_INTAKE_Y, INTAKE_ANGLE);

      firstIntake69 = createPose(INTAKE_START_X, SECOND_INTAKE_Y + 1, INTAKE_ANGLE);
       firstIntakeEnd69 = createPose(SECOND_INTAKE_END_X - 2.5, SECOND_INTAKE_Y + 1, INTAKE_ANGLE);
    // red  firstIntake69 = createPose(INTAKE_START_X, SECOND_INTAKE_Y -2.25, INTAKE_ANGLE);
   // red  firstIntakeEnd69 = createPose(SECOND_INTAKE_END_X - 2.5, SECOND_INTAKE_Y -2.25, INTAKE_ANGLE);

        secondIntake69 = createPose(INTAKE_START_X, THIRD_INTAKE_Y + 1.5, INTAKE_ANGLE);
        secondIntakeEnd69 = createPose(SECOND_INTAKE_END_X, THIRD_INTAKE_Y + 1.5, INTAKE_ANGLE);
        thirdIntake69 = createPose(INTAKE_START_X, FIRST_INTAKE_Y, INTAKE_ANGLE);
        thirdIntakeEnd69 = createPose(THIRD_INTAKE_END_X - 7.5, FIRST_INTAKE_Y, INTAKE_ANGLE);

        gateCorner = createPose(122, 60, INTAKE_ANGLE);
        gateOpenEnd = createPose(144-16.8, 72.5, INTAKE_ANGLE);
        gateOpen = createPose (115, 74.7, INTAKE_ANGLE);
        gateOpen69 = createPose (114  , 74.7, INTAKE_ANGLE);

        turnCollectFromGate = createPose(144-47.7, 61.5, 32.5);
        goCollectFromGateOffic = createPose (135.2, 61.5, 32.5 );
        goCollectFromGate = createPose (134.2, 59.7, 32.5 );
      // red    goCollectFromGate = createPose (134.2, 58.5, 32.5 );

        //
        // TO BE CHANGED BASED ON THE FIELD BEFORE EVERY EVENT!!!
        // CALIBRATION NEEDED!!!
        if (color == TeamColor.RED)
            calibrateStart(0, -1.5, -5);
        if (color == TeamColor.BLUE)
            calibrateStart(0, -1.5  , 0);

   }

    public void calibrateStart(double x, double y, double shootHeading) {
        goalStart = new Pose(goalStart.getX() + x, goalStart.getY() + y, goalStart.getHeading());
        shootAuto = new Pose(shootAuto.getX() + x, shootAuto.getY() + y,
                shootAuto.getHeading() + Math.toRadians(shootHeading));
        shootMiddleAuto = new Pose(shootMiddleAuto.getX() + x, shootMiddleAuto.getY() + y,
                shootMiddleAuto.getHeading() + Math.toRadians(shootHeading));


    }
}
