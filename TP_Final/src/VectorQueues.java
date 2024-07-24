public class VectorQueues{
    private QueuesTransitions [] queueTransitions = new QueuesTransitions[10];

    /**
     * Constructor.
     */
    public VectorQueues(){
        for(int i=0; i<10; i++){
            queueTransitions[i] = new QueuesTransitions();
        }
    }

    /**
     * 
     * Devuelve la cola de transiciones.
     */
    public QueuesTransitions [] getQueueTransitions(){
        return queueTransitions;
    }

    /**
     * Agrega una transición a la cola.
     */
    public void putTransition(Transition transition){
        int numTransition = Integer.parseInt(transition.getTransitionName().substring(1)) - 1; 
        queueTransitions[numTransition].setTransition(transition);
    }

    /**
     * Elimina la transición de su cola.
     */
    public void deleteTransition(Transition transition){
        int index = Integer.parseInt(transition.getTransitionName().substring(1)) - 1;
        queueTransitions[index].removeTransition(0);
    }
}