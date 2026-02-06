package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.robot.Subsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TaskScheduler {
    private final List<Task> activeTasks = new ArrayList<>();
    private final Set<Subsystem> registeredSubsystems = new HashSet<>();

    public void schedule(Task task) {
        // check for dependencies?
        activeTasks.removeIf(existing -> {
            if (!Collections.disjoint(existing.getRequirements(), task.getRequirements())) {
                existing.end(true); // Interrupted
                return true;
            }
            return false;
        });

        // start new task
        task.init();
        activeTasks.add(task);
    }

    public void run() {
        Iterator<Task> iterator = activeTasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();

            task.update();
            if (task.isFinished()) {
                task.end(false);
                iterator.remove();
            }
        }
    }

    public void registerSubsystem(Subsystem... subsystems) {
        Collections.addAll(registeredSubsystems, subsystems);
    }

    public void reset() {
        activeTasks.clear();
    }
}
