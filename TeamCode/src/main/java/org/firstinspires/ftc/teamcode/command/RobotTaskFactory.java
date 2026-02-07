package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.command.tasks.InstantTask;
import org.firstinspires.ftc.teamcode.command.tasks.Task;
import org.firstinspires.ftc.teamcode.command.tasks.SequenceTask;
import org.firstinspires.ftc.teamcode.command.tasks.WaitTask;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Outtake;
import org.firstinspires.ftc.teamcode.robot.Pivot;

public class RobotTaskFactory {
    private final Intake intake;
    private final Outtake outtake;
    private final Pivot pivot;

    public RobotTaskFactory(Intake intake, Outtake outtake, Pivot pivot) {
        this.intake = intake;
        this.outtake = outtake;
        this.pivot = pivot;
    }

    // PIVOT TASKS
    public Task pivotShoot() {
        return new SequenceTask(
                new InstantTask(() -> {
                    intake.shoot();
                    outtake.charge();
                }, pivot, intake, outtake),
                new WaitTask(200, pivot),
                new InstantTask(() -> pivot.shoot(), pivot)
        );
    }
    public Task pivotIdle() {
        return new InstantTask(() -> {
            pivot.idle();
            intake.idle();
            outtake.off();
        }, pivot, intake, outtake);
    }
    public Task pivotIntake() {
        return new InstantTask(() -> {
            pivot.intake();
            intake.intake();
            outtake.off();
        }, pivot, intake, outtake);
    }
}
