package es.deusto.sd.strava.entity;

public enum TipoUsuario {
	GOOGLE, META;

    public static TipoUsuario parse(String text) {
        if (GOOGLE.name().equalsIgnoreCase(text)) {
            return GOOGLE;
        } else if (META.name().equalsIgnoreCase(text)) {
            return META;
        } else {
            return null;
        }
    }

}
