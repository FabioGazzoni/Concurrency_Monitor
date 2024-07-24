import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Monitor{
    private final Policy policy = new Policy();
    private final Semaphore mutex = new Semaphore(1);
    private final PetriNet petriNet = PetriNet.getPetriNet();
    private VectorQueues queues = new VectorQueues();
    private BufferedWriter writer;

    /**
     * Constructor
     */
    public Monitor(){
        try{
            writer = new BufferedWriter(new FileWriter("output.txt")); 
        } catch (IOException e){
            System.err.println("Error al abrir el archivo: " + e.getMessage());
        }
    }

    /**
     * Intenta realizar el disparo de la transición.
     * Para poder dispararse se debe cumplir que este sensibilizada por tokens, por tiempo y que los TI essten equilibrados.
     * Si no se cumple alguna condición, se bloqua en una cola.
     * @return true si se pudo disparar ó false si el programa debe terminar.
     */
    public boolean launchTransition(String nameTransition) throws InterruptedException{
        while(true){
            if(checkEndOfProgram()){
                return false;
            }
            mutex.acquire();
            if(petriNet.isSensitizedTransitionByTokens(nameTransition)){
                Date now = new Date();
                long time = now.getTime();

                if(petriNet.isSensitizedTransitionByTime(nameTransition, time)){
                    launchTransitionSensitized(nameTransition);
                    return true;
                  }else{
                    if(petriNet.getTimeElapsed(nameTransition, time) < petriNet.getAlpha()){ // No esta sensiblizada porque todavia no llego al valor de alfa
                        mutex.release();
                        Thread.sleep(petriNet.getAlpha() - petriNet.getTimeElapsed(nameTransition, time));
                    }else{ // No esta sensibilizada porque se pasó del valor de beta
                        mutex.release();
                    }
                }
            }else{ // La transición no se pudo disparar
                blockTransition(nameTransition);
            }
        }
    }

    /**
     * Disparo de la transición.
     * Actualiza el marcado de la red y la politica desbloquea una transición sensiblizada por tokens.
     */
    private void launchTransitionSensitized(String nameTransition){
        System.out.printf("%s: Transición disparada: %s\n", Thread.currentThread().getName(), nameTransition);
        petriNet.updateTokens(nameTransition);
        printToFile(nameTransition);
        policy.updateTICount(nameTransition);
        unlockTransition();
        mutex.release();
    }

    /**
     * Intenta despertar una transición (en caso de que haya una) utilizando la política.
     */
    private void unlockTransition(){
        ArrayList <QueuesTransitions> queueTransitionsEnable = getQueueTransitionsEnable();
        if(queueTransitionsEnable.size() > 0){
            Transition transition = policy.getTransitionToUnlock(queueTransitionsEnable);
            queues.deleteTransition(transition);
            transition.release();        
        }
    }

    /**
     * Devuelve un arraylist de transiciones sensibilizadas por tokens y que estaban en la cola de transiciones bloqueadas.
     */
    private ArrayList <QueuesTransitions> getQueueTransitionsEnable(){
        ArrayList <QueuesTransitions> queueTransitionsEnable = new ArrayList<>();

        QueuesTransitions [] vectorQueues = queues.getQueueTransitions();
        for (QueuesTransitions queue : vectorQueues) {
            if(queue.getSize() > 0 && petriNet.isSensitizedTransitionByTokens(queue.getTransition(0).getTransitionName())){
                queueTransitionsEnable.add(queue);
            }
        }
        return queueTransitionsEnable;
    }

    /**
     * Guarda el texto en un archivo.
     */
    private void printToFile(String text){
        try{
            writer.write(text);
            writer.newLine();
            writer.flush();
        }catch (IOException e){
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Bloquea una transición en la cola.
     */
    private void blockTransition(String nameTransition) throws InterruptedException{
        Transition transition = new Transition(nameTransition);
        queues.putTransition(transition);
        mutex.release();
        transition.acquire(); // Se bloquea el thread
    }

    /**
     * Si cada TI se disparó 1000 veces se termina el programa.
     */
    private boolean checkEndOfProgram(){
        if(policy.isEndOfProgram()){
            for(QueuesTransitions queue : queues.getQueueTransitions()){
                for(Transition transition : queue.getArrayTransitions()){
                    transition.release(); // Desbloquea todos los threads
                }
            }
            try{
                writer.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}