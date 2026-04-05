package business_logic;

import data_access.Serialization;
import data_model.ComplexTask;
import data_model.Employee;
import data_model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  TaskManagement {
    private Map<Employee, List<Task>> map;

    public TaskManagement() {
        this.map = new HashMap<>();
    }

    public Map<Employee, List<Task>> getMap() {
        return map;
    }

    public void setMap(Map<Employee, List<Task>> map) {
        this.map = map;
    }

    public void addEmployeeToMap(Employee employee) {
        map.put(employee, new ArrayList<>());
    }

    public void assignTaskToEmployee(int idEmployee, Task task) {
        for (Employee employee : map.keySet()) {
            if (employee.getIdEmployee()==idEmployee) {
                map.get(employee).add(task);
                break;
            }
        }
    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        int duration = 0;
        for (Employee employee : map.keySet()) {
            if (employee.getIdEmployee()==idEmployee) {
                for (Task task : map.get(employee)) {
                    if (task.getStatusTask().equals("Completed"))
                        duration += task.estimateDuration();
                }
                break;
            }
        }
        return duration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask){
        for (Employee employee : map.keySet()) {
            if (employee.getIdEmployee()==idEmployee) {
                for (Task task : map.get(employee)) {
                    Task found = findTaskRecursive(task, idTask);
                    if(found!=null){
                        if(found.getStatusTask().equals("Uncompleted"))
                            found.setStatusTask("Completed");
                        else
                            found.setStatusTask("Uncompleted");
                        return;
                    }
                }
                break;
            }
        }
    }

    private Task findTaskRecursive(Task currentTask, int idTask){
        if(currentTask.getIdTask()==idTask)
            return currentTask;
        if(currentTask instanceof ComplexTask){
            for(Task sub : ((ComplexTask) currentTask).getSubTasks()){
                Task found = findTaskRecursive(sub, idTask);
                if(found!=null)
                    return found;
            }
        }
        return null;
    }

    Serialization  serialization = new Serialization();
    public void loadData(){
        try {
            map = serialization.loadData("DataFile.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void saveData(){
        try {
            serialization.saveData(map, "DataFile.txt");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
