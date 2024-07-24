import java.util.ArrayList;

public class QueuesTransitions{
    private final ArrayList<Transition> queue = new ArrayList<>();

    /**
     * Agrega una transición a la cola.
     */
    public void setTransition(Transition transition){
        queue.add(transition);
    }

    /**
     * Elimina una transición de la cola.
     */
    public void removeTransition(int index){
        queue.remove(index);
    }

    /**
     * Devuelve una transición de la cola según el indice.
     */
    public Transition getTransition(int index){
        return queue.get(index);
    }

    /**
     * Retorna el tamaño de la cola.
     */
    public int getSize(){
        return queue.size();
    }

    /**
     * Retorna la cola de transiciones.
     */
    public ArrayList<Transition> getArrayTransitions(){
        return queue;
    }
}