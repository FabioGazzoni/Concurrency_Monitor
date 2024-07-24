public class Task implements Runnable {

    private final String[] transitions;
    private int nextTransition;
    private final Monitor monitor;

    /**
     * Constructor
     * @param monitor
     * @param transitions Array con las transiciones de cada thread
     */
    public Task(Monitor monitor, String[] transitions){
        this.transitions = transitions;
        this.monitor = monitor;
        nextTransition = 0;
    }

    /**
     * Método run que ejecuta cada thread de los segmentos.
     */
    @Override
    public void run(){
        try{
            while(true){
                if(!monitor.launchTransition(transitions[nextTransition])){
                    return; // Se termina el programa
                }
                nextTransition++;
                if(nextTransition == transitions.length){ // El segmento ya no tiene transiciones asociadas
                    nextTransition = 0;
                }
            }
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}