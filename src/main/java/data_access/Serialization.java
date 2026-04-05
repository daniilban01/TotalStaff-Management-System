package data_access;

import data_model.Employee;
import data_model.Task;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Serialization {
    public Map<Employee, List<Task>> loadData (String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Map<Employee, List<Task>> map = (Map<Employee, List<Task>>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return map;
    }

    public void saveData (Map<Employee, List<Task>> map, String fileName) throws IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
