import java.util.ArrayList;

public class Policy{
    private int valueTI1, valueTI2, valueTI3;

    /**
     * Retorna una transición que está sensibilizada y que cumpla con los TI balanceados.
     */
    public Transition getTransitionToUnlock(ArrayList <QueuesTransitions> queueTransitionsEnable){
        QueuesTransitions queueTransition = queueTransitionsEnable.get(0);

        for (QueuesTransitions queueT : queueTransitionsEnable){
            if(checkBalance(queueT.getTransition(0).getTransitionName())){
                queueTransition = queueT;
            }
        }

        return queueTransition.getTransition(0);
    } 

    /**
     * Retorna true si los TI estan balanceados, sino false.
     */
    public boolean checkBalance(String nameTransition){
        if(((valueTI1 > valueTI2) || (valueTI1 > valueTI3))){ 
            if(nameTransition.equals("T2")){
                return false; // T2 no se va a disparar
            }
        }
        if((valueTI2 > valueTI1) || (valueTI2 > valueTI3)){
            if(nameTransition.equals("T3")){
                return false; // T3 no se va a disparar
            }
        }
        if((valueTI3 > valueTI2) || (valueTI3 > valueTI1)){
            if(nameTransition.equals("T7")){
                return false; // T7 no se va a disparar
            }
        }
        return true;
    }

    /**
     * Actualiza la cuenta de los TI cuando se dispara T2, T3 o T7.
     */
    public void updateTICount(String nameTransition){
        if(nameTransition.equals("T2")){
            valueTI1++;
        }else if(nameTransition.equals("T3")){
            valueTI2++;
        }else if(nameTransition.equals("T7")){
            valueTI3++;
        }
        System.out.printf("TI1: %d - TI2: %d - TI3: %d\n",valueTI1, valueTI2,valueTI3);
    }

    /**
     * @return true si cada TI se disparó más de 1000 veces, sino false.
     */
    public boolean isEndOfProgram(){
        return (valueTI1 + valueTI2 + valueTI3) >= 1000;
    }
}

