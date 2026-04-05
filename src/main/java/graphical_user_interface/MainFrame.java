package graphical_user_interface;

import business_logic.TaskManagement;
import data_model.ComplexTask;
import data_model.Employee;
import data_model.SimpleTask;
import data_model.Task;

import javax.swing.*;


import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class   MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel panelButoane;
    private JButton btnTopOre;
    private JButton btnStatus;
    private JTextArea reportArea;
    private JPanel leftPanel;
    private JTextField nameField;
    private JTextField idField;
    private JButton btnAddEmployee;
    private JPanel rightPanel;
    private JTextField taskIdField;
    private JComboBox statusCombo;
    private JPanel typePanel;
    private JRadioButton radioSimple;
    private JRadioButton radioComplex;
    private JPanel hoursPanel;
    private JTextField startHourField;
    private JTextField endHourField;
    private JButton btnCreateTask;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton btnAssignTask;
    private JTable taskTable;
    private JComboBox comboSimpleTask;
    private JComboBox comboComplexTask;
    private JButton btnAddSubToComplex;

    private TaskManagement taskManagement = new TaskManagement();
    private List<Task> createdTasks = new ArrayList<>();

    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Task Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initGUI();
        setVisible(true);
    }

    private void initGUI(){
        initTable();
        initListeners();
        taskManagement.loadData();
        updateDashboardTable();
        populateComboBoxesAfterLoad();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                taskManagement.saveData();
            }
        });
    }

    private void initTable() {
        String[] columns = {"Hierarchy / Name", "ID", "Type", "Status", "Duration", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable.setModel(model);
    }

    private Employee getEmployeeByTask(Task targetTask) {
        for (Map.Entry<Employee, List<Task>> entry : taskManagement.getMap().entrySet()) {
            if (entry.getValue().contains(targetTask)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isTaskInHierarchy(Task parent, Task target) {
        if (parent instanceof ComplexTask) {
            for (Task sub : ((ComplexTask) parent).getSubTasks()) {
                if (sub.equals(target)) {
                    return true;
                }
                if (isTaskInHierarchy(sub, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeTaskFromEverywhere(Task taskToRemove) {
        for (List<Task> employeeTasks : taskManagement.getMap().values()) {
            if (employeeTasks.contains(taskToRemove)) {
                employeeTasks.remove(taskToRemove);
                break;
            }
        }
    }

    private void initListeners() {

        ButtonGroup tipTaskGroup = new ButtonGroup();
        tipTaskGroup.add(radioSimple);
        tipTaskGroup.add(radioComplex);

        radioSimple.setSelected(true);
        radioSimple.addActionListener(e -> hoursPanel.setVisible(true));
        radioComplex.addActionListener(e -> hoursPanel.setVisible(false));

        btnAddEmployee.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String idText = idField.getText();

                if (name.isEmpty() || !name.matches("[a-zA-Z\\s]+")) {
                    JOptionPane.showMessageDialog(null, "Error: Name must contain only LETTERS!", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int id = Integer.parseInt(idText);

                Employee emp = new Employee(id, name);
                taskManagement.addEmployeeToMap(emp);

                comboBox1.addItem(id + " - " + name);

                nameField.setText("");
                idField.setText("");
                JOptionPane.showMessageDialog(null, "Employee added successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: ID must be a valid number!", "Invalid ID", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage());
            }
        });

        btnCreateTask.addActionListener(e -> {
            try {
                int id = Integer.parseInt(taskIdField.getText());
                String status = (String) statusCombo.getSelectedItem();
                Task t;

                for(Task existing : createdTasks) {
                    if(existing.getIdTask() == id) {
                        JOptionPane.showMessageDialog(null, "ID already exists!");
                        return;
                    }
                }

                if (radioSimple.isSelected()) {
                    t = new SimpleTask(id, status, Integer.parseInt(startHourField.getText()), Integer.parseInt(endHourField.getText()));
                    comboSimpleTask.addItem(id);
                } else {
                    t = new ComplexTask(id, status);
                    comboComplexTask.addItem(id);
                    comboSimpleTask.addItem(id);
                }

                createdTasks.add(t);
                comboBox2.addItem(id);

                taskIdField.setText("");
                startHourField.setText("");
                endHourField.setText("");

                JOptionPane.showMessageDialog(null, "Task created successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Please enter valid numbers for ID and Hours!");
            }
        });

        btnAssignTask.addActionListener(e -> {
            try {
                String empInfo = (String) comboBox1.getSelectedItem();
                int empId = Integer.parseInt(empInfo.split(" - ")[0]);
                int taskId = (int) comboBox2.getSelectedItem();

                Task toAssign = null;
                for (Task t : createdTasks) {
                    if (t.getIdTask() == taskId) { toAssign = t; break; }
                }

                if (toAssign != null) {
                    taskManagement.assignTaskToEmployee(empId, toAssign);
                    updateDashboardTable();

                    comboBox2.removeItem(taskId);

                    JOptionPane.showMessageDialog(null, "Task Assigned Successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error!");
            }
        });

        btnAddSubToComplex.addActionListener(e -> {
            try {
                Integer idSub = (Integer) comboSimpleTask.getSelectedItem();
                Integer idParent = (Integer) comboComplexTask.getSelectedItem();

                if (idSub == null || idParent == null) return;

                if (idSub.equals(idParent)) {
                    JOptionPane.showMessageDialog(null,
                            "Error: A task cannot be added to itself (ID: " + idSub + ")!",
                            "Hierarchy Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Task subObj = null;
                Task parentObj = null;
                for (Task t : createdTasks) {
                    if (t.getIdTask() == idSub) subObj = t;
                    if (t.getIdTask() == idParent) parentObj = t;
                }

                if (subObj != null && parentObj instanceof ComplexTask) {

                    if (isTaskInHierarchy(subObj, parentObj)) {
                        JOptionPane.showMessageDialog(null,
                                "Circular reference! Task " + idParent + " is already a parent of Task " + idSub,
                                "Hierarchy Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Employee empSub = getEmployeeByTask(subObj);
                    Employee empParent = getEmployeeByTask(parentObj);

                    removeTaskFromEverywhere(subObj);

                    ((ComplexTask) parentObj).addTask(subObj);

                    if (empSub != null && empParent == null) {
                        taskManagement.assignTaskToEmployee(empSub.getIdEmployee(), parentObj);
                        comboBox2.removeItem(idParent);
                    }

                    comboBox2.removeItem(idSub);
                    comboSimpleTask.removeItem(idSub);

                    updateDashboardTable();
                    JOptionPane.showMessageDialog(null, "Hierarchy Updated!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = taskTable.getSelectedRow();
                int col = taskTable.getSelectedColumn();

                if (col == 5 && row != -1) {
                    String type = (String) taskTable.getValueAt(row, 2);

                    if (!type.equals("EMPLOYEE")) {
                        int idTask = (int) taskTable.getValueAt(row, 1);

                        int idEmployee = -1;
                        for (int i = row; i >= 0; i--) {
                            String rType = (String) taskTable.getValueAt(i, 2);
                            if (rType.equals("EMPLOYEE")) {
                                idEmployee = (int) taskTable.getValueAt(i, 1);
                                break;
                            }
                        }

                        if (idEmployee != -1) {
                            taskManagement.modifyTaskStatus(idEmployee, idTask);
                            updateDashboardTable();
                        }
                    }
                }
            }
        });

        btnTopOre.addActionListener(e -> {
            business_logic.Utility util = new business_logic.Utility();
            String raport = util.sortByDuration(taskManagement);

            reportArea.setText("=== EMPLOYEES WITH OVER 40 WORK HOURS ===\n\n" + raport);
        });

        btnStatus.addActionListener(e -> {
            business_logic.Utility util = new business_logic.Utility();
            Map<String, Map<String, Integer>> statusMap = util.tasksToMap(taskManagement);

            StringBuilder sb = new StringBuilder("=== TASK STATUS PER EMPLOYEE ===\n\n");

            if (statusMap.isEmpty()) {
                sb.append("No data available. Please add employees and tasks in the Management tab!");
            } else {
                for (String numeAngajat : statusMap.keySet()) {
                    sb.append("Employee: ").append(numeAngajat).append("\n");
                    sb.append("   ✔ Completed: ").append(statusMap.get(numeAngajat).get("Completed")).append("\n");
                    sb.append("   ✖ Uncompleted: ").append(statusMap.get(numeAngajat).get("Uncompleted")).append("\n");
                    sb.append("---------------------------------\n");
                }
            }

            reportArea.setText(sb.toString());
        });
    }

    private String formatDuration(Task t) {
        if (t instanceof SimpleTask) {
            SimpleTask st = (SimpleTask) t;
            return st.estimateDuration() + "h (" + st.getStartHour() + " - " + st.getEndHour() + ")";
        } else if (t instanceof ComplexTask) {
            return t.estimateDuration() + "h (Total)";
        }
        return "-";
    }

    private void addTaskToTable(DefaultTableModel model, Task t, String indent) {
        String durationField = formatDuration(t);
        model.addRow(new Object[]{
                indent + "↳ " + t.getIdTask(),
                t.getIdTask(),
                t.getClass().getSimpleName(),
                t.getStatusTask(),
                durationField,
                "MODIFY"
        });

        if (t instanceof ComplexTask) {
            for (Task sub : ((ComplexTask) t).getSubTasks()) {
                addTaskToTable(model, sub, indent + "        ");
            }
        }
    }

    private void updateDashboardTable() {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.setRowCount(0);

        for (Map.Entry<Employee, List<Task>> entry : taskManagement.getMap().entrySet()) {
            Employee emp = entry.getKey();
            model.addRow(new Object[]{emp.getName().toUpperCase(), emp.getIdEmployee(), "EMPLOYEE", "-", "-", "-"});

            for (Task t : entry.getValue()) {
                addTaskToTable(model, t, "     ");
            }
        }
    }

    private void populateComboBoxesAfterLoad() {
        createdTasks.clear();

        comboBox1.removeAllItems();
        comboBox2.removeAllItems();
        comboSimpleTask.removeAllItems();
        comboComplexTask.removeAllItems();

        for (Map.Entry<Employee, List<Task>> entry : taskManagement.getMap().entrySet()) {
            Employee emp = entry.getKey();
            comboBox1.addItem(emp.getIdEmployee() + " - " + emp.getName());

            for (Task t : entry.getValue()) {
                if (!createdTasks.contains(t)) {
                    createdTasks.add(t);
                }

                if (t instanceof ComplexTask) {
                    comboComplexTask.addItem(t.getIdTask());
                    comboSimpleTask.addItem(t.getIdTask());
                } else if (t instanceof SimpleTask) {
                    comboSimpleTask.addItem(t.getIdTask());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
