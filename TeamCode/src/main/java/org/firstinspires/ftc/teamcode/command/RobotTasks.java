package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Outtake;
import org.firstinspires.ftc.teamcode.robot.Pivot;

public class RobotTasks {
    private final Intake intake;
    private final Outtake outtake;
    private final Pivot pivot;

    public RobotTasks(Intake intake, Outtake outtake, Pivot pivot) {
        this.intake = intake;
        this.outtake = outtake;
        this.pivot = pivot;
    }

    public Task pivotShoot() {
        return new Task() {
            @Override
            public void init() {
                pivot.shoot();
                intake.shoot();
                outtake.charge();
            }

            @Override
            public void update() {

            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public void end(boolean interrupted) {

            }
        };
    }

    public Task pivotIdle() {
        return new Task() {
            @Override
            public void init() {
                pivot.idle();
                intake.idle();
                outtake.off();
            }

            @Override
            public void update() {

            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public void end(boolean interrupted) {

            }
        };
    }

    public Task pivotIntake() {
        return new Task() {
            @Override
            public void init() {
                pivot.intake();
                intake.intake();
                outtake.off();
            }

            @Override
            public void update() {

            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public void end(boolean interrupted) {

            }
        };
    }
}
