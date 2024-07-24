import java.util.concurrent.Semaphore;

public class Transition extends Semaphore{
    private final String transition;

    /**
     * Constructor
     */
    public Transition(String transition){   
        super(0);
        this.transition = transition;
    }

    /**
     * Devuelve el nombre de la transición.
     */
    public String getTransitionName(){
        return transition;
    }
    
    @Override
    public void acquire() throws InterruptedException{
        super.acquire();
    }

    @Override
    public void release(){
        super.release();
    }
}
