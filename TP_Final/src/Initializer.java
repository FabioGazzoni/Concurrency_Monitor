import java.util.concurrent.ThreadFactory;

public class Initializer implements ThreadFactory{
    private static int counter = 0; // Numero de thread
    private final String name;

    /**
     * Constructor
     * @param name Nombre del thread
     */
    private Initializer(String name){
        this.name = name;
    }

    /**
     * Crea un thread
     * @param runnable Objeto que se le asigna al thread
     * @return Thread creado con un nombre especifico
     */
    @Override
    public Thread newThread(Runnable runnable){
        Thread t = new Thread(runnable, "Thread " + counter + " - " + name);
        counter++;

        return t;
    }

    /**
     * Crea una task (runnable) y le asigna un thread
     * @param monitor
     * @param transitions Transiciones asociadas a cada segmento
     * @param segment 
     * @return Thread creado
     */
    public static Thread initThread(Monitor monitor, String[] transitions, String segment){
        Task task = new Task(monitor, transitions);
        Initializer init = new Initializer(segment);

        return init.newThread(task);
    }
}
