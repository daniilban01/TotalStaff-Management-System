package business_logic;

import data_model.Employee;
import data_model.Task;

import java.util.*;

public class Utility {

    public String sortByDuration(TaskManagement taskManagement){
        Map<Employee, List<Task>> map = taskManagement.getMap();
        Map<Employee, Integer> durationMap = new HashMap<>();
        List<Employee> aux = new ArrayList<Employee>();

        for (Employee employee : map.keySet()){
            int duration = taskManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            if (duration > 40) {
                durationMap.put(employee, duration);
                aux.add(employee);
            }
        }
        Collections.sort(aux, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                int e1_duration = durationMap.get(e1);
                int e2_duration = durationMap.get(e2);
                if (e1_duration > e2_duration)
                    return 1;
                if (e1_duration < e2_duration)
                    return -1;
                return 0;
            }
        });

        StringBuilder rezultat = new StringBuilder();
        if (aux.isEmpty()) {
            return "No employees have more than 40 completed work hours.";
        }
        for (Employee employee : aux){
            rezultat.append(employee.getName()).append(" -> ").append(durationMap.get(employee)).append("h\n");
        }
        return rezultat.toString();
    }

    public Map<String, Map<String, Integer>> tasksToMap(TaskManagement taskManagement){
        Map <String, Map<String, Integer>> finalMap = new HashMap<>();
        Map<Employee, List<Task>> map = taskManagement.getMap();
        for (Employee employee : map.keySet()){
            Map<String, Integer> aux = new HashMap<>();
            int completed = 0, uncompleted = 0;
            for (Task task : map.get(employee)){
                if (task.getStatusTask().equals("Completed"))
                    completed++;
                else uncompleted++;
            }
            aux.put("Completed", completed);
            aux.put("Uncompleted", uncompleted);
            finalMap.put(employee.getName(), aux);
        }
        return finalMap;
    }
}
