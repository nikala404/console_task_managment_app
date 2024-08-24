import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static Map<String, GeneralTask> taskMap;
    private static String userName;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        taskMap = new HashMap<>();
        taskMap.put("taskName1", new LimitedTimeTask( "desc1","taskName1 ", userName, LocalDateTime.now()));
        taskMap.put("taskName2", new RepeatableTask( "desc2","taskName2", userName, 1));


        Scanner scanner = new Scanner(System.in);
        authenticate(scanner);
        int choice=0;

        do {
            System.out.println("[1]- create task");
            System.out.println("[2]- get task list");
            System.out.println("[3]- Update task");
            System.out.println("[4]- Register");
            System.out.println("[5]- Delete task");
            System.out.println("[6]- get task");
            System.out.println("[7]- Exit\n");

            System.out.print("Enter your choice: ");
            String choiceString = scanner.nextLine();
            try {
                choice = Integer.valueOf(choiceString);
            }catch (Exception exception){}
            System.out.println("\n-----------------------------\n");

            switch (choice) {
                case 1:
                    createTask(scanner);
                    break;
                case 2:
                    getTaskList();
                    break;
                case 3:
                    updateTask(scanner);
                    break;
                case 4:
                    authenticate(scanner);
                    break;
                case 5:
                    deleteTask(scanner);
                    break;
                case 6:
                    getTask(scanner);
                    break;
                case 7:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 7);

        scanner.close();

    }

    private static void getTask(Scanner scanner) {
        System.out.print("enter task name: ");
        String taskName = scanner.nextLine();
        if(taskMap.containsKey(taskName)){
            taskMap.get(taskName).print();
        }else {
            System.out.println("ასეთი სახელით ტასკი არ არსებობს\n");
        }
    }

    private static void deleteTask(Scanner scanner) {
        System.out.print("enter task name: ");
        String taskName = scanner.nextLine();
        if(taskMap.containsKey(taskName)){
            taskMap.remove(taskName);
            System.out.println("ტასკი წარმატებით წაიშალა\n");
        }else {
            System.out.println("ასეთი სახელით ტასკი არ არსებობს\n");
        }
    }

    private static void getTaskList() {
        if(taskMap == null || taskMap.isEmpty())
        {
            System.out.println("ტასკების სია ცარიელია\n");
        }
        else
        {
            for (String key : taskMap.keySet()) {
                taskMap.get(key).print();
            }
        }
    }

    private static void createTask(Scanner scanner) {
        System.out.print("enter task type (LimitedTimeTask, RepeateableTask, BasicTask: ");
        String taskType = scanner.nextLine();

        System.out.print("enter task name: ");
        String taskName = scanner.nextLine();

        System.out.print("enter task description: ");
        String taskDesc = scanner.nextLine();

        if(taskMap.containsKey(taskName)){
            System.out.println("ასეთი სახელით ტასკი უკვე არსებობს\n");
            return;
        }

        switch (taskType)
        {
            case "LimitedTimeTask":
                System.out.print("enter task date: ");
                String taskDateString = scanner.nextLine();
                LocalDateTime taskDate = LocalDateTime.parse(taskDateString,formatter);
                taskMap.put(taskName, new LimitedTimeTask( taskDesc,taskName, userName, taskDate));
                System.out.print("task created \n");
                break;
            case "RepeateableTask":
                System.out.print("enter task execute count: ");
                int taskExecuteCount = Integer.parseInt(scanner.nextLine());
                taskMap.put(taskName, new RepeatableTask( taskDesc, taskName, userName, taskExecuteCount));
                System.out.print("task created \n");
                break;
            case "BasicTask":
                taskMap.put(taskName, new BasicTask( taskDesc, taskName, userName));
                System.out.print("task created \n");
                break;
            default:
                System.out.println("task type is unknown");
        }
    }

    public static void authenticate(Scanner scanner){
        System.out.print("enter userName: ");
        userName = scanner.nextLine();
        System.out.print("userName is "+userName+"\n");
    }

    public static void updateTask(Scanner scanner) {

        System.out.print("enter task name: ");
        String taskName = scanner.nextLine();


        if (!taskMap.containsKey(taskName)) {
            System.out.printf("task not found \n");
            return;
        }

        GeneralTask generalTask = taskMap.get(taskName);

        System.out.print("enter task new name: ");
        String taskNewName = scanner.nextLine();


        System.out.print("enter task description: ");
        String taskDesc = scanner.nextLine();

        generalTask.setName(taskNewName);
        generalTask.setUserName(userName);
        generalTask.setDescription(taskDesc);

        if (generalTask instanceof LimitedTimeTask) {
            System.out.print("enter end date, format: yyyy-MM-dd HH:mm \n");

            LocalDateTime date = LocalDateTime.parse(scanner.nextLine(),formatter);
            ((LimitedTimeTask) generalTask).setEndDate(date);
        }
        if (generalTask instanceof RepeatableTask) {
            System.out.print("enter task count: ");
            Integer count = Integer.valueOf(scanner.nextLine());
            ((RepeatableTask) generalTask).setTaskCount(count);
        }

        if(!Objects.equals(taskName, taskNewName)){
            taskMap.remove(taskName);
        }

        taskMap.put(generalTask.getName(), generalTask);

        System.out.printf("task updated\n" );
        generalTask.print();
    }
}