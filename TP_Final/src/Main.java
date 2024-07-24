import java.util.ArrayList;
import java.util.List;

public class Main{

    public static void main(String[] args){
        Monitor monitor = new Monitor();
        List<Thread> threadList = new ArrayList<>();

        // Crea los 6 threads de los segmentos y los agrega a la lista threadList
        threadList.add(Initializer.initThread(monitor,new String[] {"T1"}, "SA"));
        threadList.add(Initializer.initThread(monitor,new String[] {"T2","T4"}, "SB"));
        threadList.add(Initializer.initThread(monitor,new String[] {"T3","T5"}, "SC"));
        threadList.add(Initializer.initThread(monitor,new String[] {"T6"}, "SD"));
        threadList.add(Initializer.initThread(monitor,new String[] {"T7","T8","T9","T10"}, "SE-1"));
        threadList.add(Initializer.initThread(monitor,new String[] {"T7","T8","T9","T10"}, "SE-2"));

        //Inicia los threads
        for (Thread thread: threadList){
            thread.start();
        }
    }
}