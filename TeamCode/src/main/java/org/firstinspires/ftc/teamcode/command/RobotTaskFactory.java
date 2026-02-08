package org.firstinspires.ftc.teamcode.command;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.command.tasks.InstantTask;
import org.firstinspires.ftc.teamcode.command.tasks.Task;
import org.firstinspires.ftc.teamcode.command.tasks.SequenceTask;
import org.firstinspires.ftc.teamcode.command.tasks.WaitTask;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Movement;
import org.firstinspires.ftc.teamcode.robot.Outtake;
import org.firstinspires.ftc.teamcode.robot.Pivot;

public class RobotTaskFactory {
    private static final int RAISE_TIME = 150, LAUNCHING_TIME = 175, RELOAD_TIME = 300;

    private final Movement movement;
    private final Pivot pivot;
    private final Intake intake;
    private final Outtake outtake;

    public RobotTaskFactory(Movement movement, Pivot pivot, Intake intake, Outtake outtake) {
        this.movement = movement;
        this.pivot = pivot;
        this.intake = intake;
        this.outtake = outtake;
    }

    // PIVOT TASKS
    public Task pivotShoot() {
        return new SequenceTask(
                new InstantTask(() -> {
                    intake.shoot();
                    outtake.charge();
                }, pivot, intake, outtake),
                new WaitTask(200, pivot),
                new InstantTask(pivot::shoot, pivot)
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

    // OUTTAKE TASKS
    public Task outtakeFire() {
        return new SequenceTask(
                new InstantTask(outtake::raise, outtake), new WaitTask(RAISE_TIME, outtake),
                new InstantTask(() -> {
                    outtake.launch();
                    intake.push();
                }, outtake, intake), new WaitTask(LAUNCHING_TIME + 150, intake, outtake),
                new InstantTask(() -> {
                    outtake.charge();
                    intake.shoot();
                }, outtake, intake)
        ).onCondition(() -> outtake.getState() == Outtake.State.CHARGING);
    }
    public Task outtakeRapidFire() {
        return new SequenceTask(
                new InstantTask(outtake::raise, outtake),  new WaitTask(RAISE_TIME, outtake),
                new InstantTask(outtake::launch, outtake), new WaitTask(LAUNCHING_TIME, outtake),
                new InstantTask( () -> {
                    outtake.reload();
                    intake.push();
                }, outtake, intake), new WaitTask(RELOAD_TIME, outtake),
                new InstantTask(outtake::launch, outtake), new WaitTask(LAUNCHING_TIME, outtake),
                new InstantTask(outtake::reload, outtake), new WaitTask(RELOAD_TIME + 50, outtake),
                new InstantTask(() -> {
                    outtake.launch();
                    intake.shoot();
                }, outtake, intake), new WaitTask(LAUNCHING_TIME + 200, outtake),
                new InstantTask(outtake::charge, outtake)
        ) {
            @Override
            public void end(boolean interrupted) {
                outtake.charge();
                intake.shoot();
                super.end(interrupted);
            }
        }.onCondition(() -> outtake.getState() == Outtake.State.CHARGING) ;
    }

    // INTAKE TASKS
    public Task intakeRejectToggle() {
        return new InstantTask(() -> {
            if (intake.getState() == Intake.State.INTAKE)
                intake.reject();
            else if (intake.getState() == Intake.State.REJECT)
                intake.intake();
        }, intake).onCondition(() -> pivot.getState() == Pivot.State.INTAKE);
    }

    // MOVEMENT TASKS
    public Task driveToAim(Pose shootPose, boolean isTeleOp) {
        return new InstantTask(() -> movement.goToPose(shootPose), movement) {
            @Override
            public boolean isFinished() {
                return !movement.getFollower().isBusy();
            }
            @Override
            public void end(boolean interrupted) {
                if (interrupted) {
                    movement.getFollower().breakFollowing();
                    if (isTeleOp)
                        movement.getFollower().startTeleopDrive();
                }
            }
        }.then(new InstantTask(() -> movement.getFollower().holdPoint(shootPose), movement) {
            @Override
            public boolean isFinished() {
                return !movement.getFollower().isBusy();
            }
            @Override
            public void end(boolean interrupted) {
                movement.getFollower().breakFollowing();
                if (isTeleOp)
                    movement.getFollower().startTeleopDrive();
            }
        });
    }
}
