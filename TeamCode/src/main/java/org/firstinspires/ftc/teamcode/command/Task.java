package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.robot.Subsystem;

import java.util.HashSet;
import java.util.Set;

public abstract class Task {
    protected Set<Subsystem> requirements = new HashSet<>();

    public abstract void init();
    public abstract void update();
    public abstract boolean isFinished();
    public abstract void end(boolean interrupted);

    public void addRequirements(Subsystem... subsystems) {
        for (Subsystem subsystem : subsystems)
            requirements.add(subsystem);
    }

    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}
