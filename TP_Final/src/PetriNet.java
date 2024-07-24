import java.util.HashMap;

public class PetriNet{

    private static PetriNet petriNet = null;
    private SensitizedVector sensitizedVector = new SensitizedVector();
    private final HashMap<String, int[]> mapTransition = new HashMap<>();
    private static final int[][] INCIDENCE_MATRIX = {
            // T1,T2,T3,T4,T5,T6,T7,T8,T9,T10
            { 1,-1,-1, 0, 0, 0, 0, 0, 0, 0 }, // P1
            { 0, 1, 0,-1, 0, 0, 0, 0, 0, 0 }, // P2
            { 0, 0, 1, 0,-1, 0, 0, 0, 0, 0 }, // P3
            { 0, 0, 0, 1, 1,-1, 0, 0, 0, 0 }, // P4
            { 0, 0, 0, 0, 0, 0, 1,-1, 0, 0 }, // P5
            { 0, 0, 0, 0, 0, 0, 0, 1,-1, 0 }, // P6
            { 0, 0, 0, 0, 0, 0, 0, 0, 1,-1 }, // P7
            {-1, 1, 1, 0, 0, 0, 0, 0,-1, 1 }, // P8
            { 0, 0,-1, 0, 1, 0, 0,-1, 1, 0 }, // P9
            { 0, 0, 0,-1,-1, 1,-1, 1, 0, 0 }, // P10
            { 0,-1, 0, 1, 0, 0, 0, 0, 0, 0 }, // P11
            {-1, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // P12
            { 0, 0, 0, 0, 0, 0,-1, 0, 0, 1 }, // P13
            { 0, 0,-1, 0, 1, 0,-1, 0, 1, 0 }  // P14
    };
    private static final int[] INITIAL_TOKENS = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 3, 3, 1};

    private final int[] current_tokens;

    /**
     * Constructor
     */
    private PetriNet(){
        current_tokens = INITIAL_TOKENS;

        mapTransition.put("T1", new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        mapTransition.put("T2", new int[] { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 });
        mapTransition.put("T3", new int[] { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 });
        mapTransition.put("T4", new int[] { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 });
        mapTransition.put("T5", new int[] { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 });
        mapTransition.put("T6", new int[] { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 });
        mapTransition.put("T7", new int[] { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 });
        mapTransition.put("T8", new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 });
        mapTransition.put("T9", new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 });
        mapTransition.put("T10", new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 });
    }

    /**
     * Singleton de PetriNet
     * @return PetriNet Cuando se llama por primera vez instancia una nueva, luego devuelve la ya creada.
     */
    public static PetriNet getPetriNet(){
        if (petriNet == null) {
            petriNet = new PetriNet();
        }
        return petriNet;
    }

    /**
     * Retorna true si la transición esta sensibilizada por tokens, sino false.
     */
    public boolean [] isSensitized(){
        boolean [] transitionsSensitized = new boolean[10];
        
        for(int j=0; j<10; j++){
            transitionsSensitized[j] = true; //Si todas las plazas asociadas a la transición tienen tokens -> transición sensibilizada
            for(int i = 0; i < INCIDENCE_MATRIX.length; i++){
                if(INCIDENCE_MATRIX[i][j] == -1){  // Encuentra la plaza asociada a la transición
                    if(!(current_tokens[i] >= 1)){
                        transitionsSensitized[j] = false;   // Si la plaza encontrada NO tiene tokens           
                    }
                }
            }
        }
        return transitionsSensitized;
    }
    /**
     * Actualiza el marcado actual y el vector de transiciones sensibilizadas.
     * @param vTransitions Arreglo de la transición disparada
     */
    public void updateTokens(String nameTransition){
        
        int[] multMatrix = multMatrix(mapTransition.get(nameTransition));

        for (int i = 0; i < INITIAL_TOKENS.length; i++) {
            current_tokens[i] += multMatrix[i];
        }
        sensitizedVector.updateSensitizedVector(isSensitized());
    }

    /**
     * Multiplica un vector con la matriz de incidencia.
     * @param vTransitions Vector de transición disparada
     * @return vector con el resultado
     */
    private int[] multMatrix(int[] vTransitions){
        int[] result = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for(int i = 0; i < INCIDENCE_MATRIX.length; i++){
            for(int j = 0; j < INCIDENCE_MATRIX[0].length; j++){
                result[i] += INCIDENCE_MATRIX[i][j] * vTransitions[j];
            }
        }

        return result;
    }

    public boolean isSensitizedTransitionByTokens(String nameTransition){
        return sensitizedVector.isSensitizedTransitionByTokens(nameTransition);
    }

    public boolean isSensitizedTransitionByTime(String nameTransition, long time){  
        return sensitizedVector.isSensitizedTransitionByTime(nameTransition, time);
    }

    public long getTimeElapsed(String nameTransition, long time){
        return sensitizedVector.getTimeElapsed(nameTransition, time);
    }

    public int getAlpha(){
        return sensitizedVector.getAlpha();
    }
}
