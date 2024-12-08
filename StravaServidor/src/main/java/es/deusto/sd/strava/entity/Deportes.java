package es.deusto.sd.strava.entity;

public enum Deportes {
	CICLISMO, CICLISMO_AND_RUNNING, RUNNING;
	
	public static Deportes parse(String text){
        if(CICLISMO.name().equalsIgnoreCase(text)){
            return CICLISMO ;
        }else if (RUNNING.name().equalsIgnoreCase(text)) {
        	return RUNNING;
        }else {
            return CICLISMO_AND_RUNNING;
        }
    }

}
