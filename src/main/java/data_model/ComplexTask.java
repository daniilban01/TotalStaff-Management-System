package data_model;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {
    private List<Task> tasks = new ArrayList<>();
    public ComplexTask(int idTask, String statusTask) {
        super(idTask, statusTask);
    }
    public void addTask(Task task) {
        tasks.add(task);
    }
    public void deleteTask(Task task) {
        tasks.remove(task);
    }
    public List<Task> getSubTasks() {
        return tasks;
    }
    @Override
    public int estimateDuration(){
        int duration = 0;
        for (Task task : tasks){
            duration += task.estimateDuration();
        }
        return duration;
    }
}
