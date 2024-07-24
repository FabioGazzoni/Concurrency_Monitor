import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class SensitizedVector{
    private HashMap<String, Boolean> listTransitionSensitized = new HashMap<>();
    private ArrayList <Long> vectorTime = new ArrayList<>();
    static private int alpha = 1;
    static private int beta = 10000;

    public SensitizedVector(){
        Date now = new Date();

        listTransitionSensitized.put("T1", true);
        listTransitionSensitized.put("T2", false);
        listTransitionSensitized.put("T3", false);
        listTransitionSensitized.put("T4", false);
        listTransitionSensitized.put("T5", false);
        listTransitionSensitized.put("T6", false);
        listTransitionSensitized.put("T7", true);
        listTransitionSensitized.put("T8", false);
        listTransitionSensitized.put("T9", false);
        listTransitionSensitized.put("T10", false);

        for(int i = 0; i < 10; i++){
            vectorTime.add(now.getTime());
        }
    }

    /**
     * Actualiza el vector de transiciones sensibilizadas.
     * Cuando una transición se sensibiliza se guarda la marca de tiempo.
     */
    public void updateSensitizedVector(boolean [] transitionsSensitized){
        
        for (HashMap.Entry<String, Boolean> entry : listTransitionSensitized.entrySet()){
            int index = Integer.parseInt(entry.getKey().substring(1)) - 1;
            updateTimeSensitized(transitionsSensitized[index], entry.getKey());
            entry.setValue(transitionsSensitized[index]);
        }
    }

    /**
     * Actualiza el vector con las marcas de tiempo.
     */
    private void updateTimeSensitized(boolean isSensitizedCurrent, String nameTransition){
        // Si antes no estaba sensibilizado y ahora se sensibliza
        if (!listTransitionSensitized.get(nameTransition) && isSensitizedCurrent){
            Date now = new Date();
            int index = Integer.parseInt(nameTransition.substring(1)) - 1;
            vectorTime.set(index, now.getTime());
        }
    }

    /**
     * Retorna true si la transición está sensibilizada por tokens, sino false.
     */
    public boolean isSensitizedTransitionByTokens(String nameTransition){
        return listTransitionSensitized.get(nameTransition);
    }

    /**
     * Retorna true si la transición esta sensibilizada por los tiempos alfa y beta, sino false.
     */
    public boolean isSensitizedTransitionByTime(String nameTransition, long time){  
        if(nameTransition.equals("T1") || nameTransition.equals("T7")){
            return true;
        }
        return (time - getTimeSensitized(nameTransition) >= alpha) && (time - getTimeSensitized(nameTransition) <= beta);
    }

    /**
     * Devuelve la cantidad de tiempo transcurrido desde que se sensibilizó la transición.
     */
    public long getTimeElapsed(String nameTransition, long time){
        return time - getTimeSensitized(nameTransition);
    }

    /**
     * Devuelve el valor de alfa.
     */
    public int getAlpha(){
        return alpha;
    }

    /**
     * Devuelve el valor de beta.
     */
    public int getBeta(){
        return beta;
    }
    
    /**
     * Devuelve el tiempo en que se sensibilizó la transición.
     */
    public long getTimeSensitized(String nameTransition){
        int index = Integer.parseInt(nameTransition.substring(1)) - 1;
        return vectorTime.get(index);
    }
}
