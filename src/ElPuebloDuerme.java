import java.util.ArrayList;

public class ElPuebloDuerme {

	private final ArrayList<Personaje> listaPersonajes;
	private ArrayList<Personaje> listaPersonajesVivos;
	private int[] votos;

	private int numeroVotos = 0;

	public ElPuebloDuerme() {
		this.listaPersonajes = new ArrayList<>();
	}

	public void anadirPersonaje(Personaje p) {
		listaPersonajes.add(p);
	}

	@SuppressWarnings("unchecked")
	public void replicarListaVivos() {
		listaPersonajesVivos = (ArrayList<Personaje>) listaPersonajes.clone();
		this.votos = new int[listaPersonajesVivos.size()];
	}

	public void asignarRoles() {
		for (Personaje p : listaPersonajes) {
			p.asignarRol(Rol.ALDEANO);
		}

		if (listaPersonajes.size() > 2) {asignarRol(Rol.LOBO);}
		if (listaPersonajes.size() > 4) {asignarRol(Rol.BRUJA);}// BRUJA
		if (listaPersonajes.size() > 5) {asignarRol(Rol.CURA);}// CURA
		if (listaPersonajes.size() > 6) {asignarRol(Rol.GUARDIAN);}// GUARDIAN
		if (listaPersonajes.size() > 7) {asignarRol(Rol.ALCALDE);}// ALCALDE
	}

	private void asignarRol(Rol rol) {
		int indiceAleatorio = (int) Math.floor(
				Math.random() * ((getAldeanos().size() - 1) + 1) + 0);
		getAldeanos().get(indiceAleatorio).asignarRol(rol);
	}

	private ArrayList<Personaje> getAldeanos() {
		ArrayList<Personaje> listaAldeanos = new ArrayList<>();

		for (Personaje p : listaPersonajes) {
			if (p.getRol() == Rol.ALDEANO) {
				listaAldeanos.add(p);
			}
		}
		return listaAldeanos;
	}

	private Personaje getPersonaje(String nombre) {
		for (Personaje p : listaPersonajes) {
			if (p.getNombreJugador().equals(nombre)) {
				return p;
			}
		}
		return null;
	}

	public boolean purgarPersonaje(Personaje p){
		if (!p.estaVivo()) {
			listaPersonajesVivos.remove(p);
			return true;
		}
		return false;
	}

	public boolean ataqueLobo(Personaje p, String comando){
		if (p.getNombreJugador().equals(comando)) {
			return false;
		}
		if (getPersonaje(comando).estaVivo()) {
			getPersonaje(comando).morir();
			return true;
		}
		return false;
	}

	public String comprobarFinPartida(){
		boolean hayLobo=false;
		for(Personaje p:listaPersonajesVivos){
			if(p.getRol()==Rol.LOBO){
				hayLobo=true;
				break;
			}
		}
		if(!hayLobo){
			return "loboMuerto";
		}else if(listaPersonajesVivos.size()<=2){
			return "ganaLobo";
		}
		return "";
	}

	synchronized public String accionPersonaje(Personaje p, String comando) {
		try {
			String nombreJugadorVoto=comando;
			String nombreJugadorAccion="";

			if(comando.contains("/")){
				nombreJugadorVoto=comando.substring(0,comando.indexOf('/'));
				nombreJugadorAccion=comando.substring(comando.indexOf('/')+1);
			}

			if (p.getNombreJugador().equals(nombreJugadorVoto) || p.getNombreJugador().equals(nombreJugadorAccion)) {
				return "ERROR: No puedes votarte ni realizar acciones sobre ti mismo";
			}

			switch (p.getRol()) {
				case ALDEANO -> {
					votos[listaPersonajesVivos.indexOf(getPersonaje(nombreJugadorVoto))]++;
					System.out.println("UN ALDEANO HA VOTADO");
					return "Has votado a " + nombreJugadorVoto;
				}
				case LOBO -> {return "Observas como los humanos votan";}
				case BRUJA -> {
					votos[listaPersonajesVivos.indexOf(getPersonaje(nombreJugadorVoto))]++;
					System.out.println("UNA BRUJA HA VOTADO");
					if (getPersonaje(nombreJugadorAccion) == null) {
						return "Has votado a " + nombreJugadorVoto;
					} else if (getPersonaje(nombreJugadorAccion).estaVivo()) {
						getPersonaje(nombreJugadorAccion).morir();
						return "Vas a matar a " + nombreJugadorAccion;
					} else {// CUANDO EL JUGADOR MUERE NO HAY QUE CERRAR SU SOCKET
						getPersonaje(nombreJugadorAccion).revivir();
						listaPersonajesVivos.add(getPersonaje(nombreJugadorAccion));
						return "Has revivido a " + nombreJugadorAccion;
					}
				}
				case CURA -> {return "";}
				case ALCALDE -> {return "";}
				case GUARDIAN -> {return "";}
				default -> {return "ERROR: ESTE PERSONAJE NO TIEN UN ROL";}
			}
		} catch (NullPointerException e) {
			return "ERROR: La persona que quieres matar no está en el pueblo";
		} catch (IndexOutOfBoundsException e) {
			return "ERROR: La persona a la que quieres votar no está en el pueblo";
		}
	}

	public void eliminarJugadorMasVotado(){

		int indice=0;
		int maximoVotos=0;
		boolean empate=false;
		for(int i=0;i<votos.length;i++){
			if(votos[i]>maximoVotos) {
				maximoVotos = votos[i];
				indice=i;
				empate=false;
			}else if(votos[i]==maximoVotos){
				empate=true;
			}
		}

		if(empate){
			System.out.println("Empate en los votos");
		}else{
			System.out.println("El jugador mas votado es= "+listaPersonajesVivos.get(indice));
			listaPersonajesVivos.get(indice).morir();
		}

		this.votos = new int[listaPersonajesVivos.size()];
	}

	public ArrayList<Personaje> getListaJugadores() {
		return listaPersonajes;
	}

	public String getPreguntaPersonaje(Rol rol) {
		return switch (rol) {
			case ALDEANO -> ("¿ A quien votas para echar del pueblo ?");
			case LOBO -> ("Es de dia, no puedes hacer nada.");
			case BRUJA -> ("Vota y elige a otro jugador para matarlo o revivirlo. <nombreVoto>/<nombreAccion>");
			case CURA -> ("Vota y intenta averiguar quien es el lobo, si te equivocas morirás. <nombreVoto>/<nombreAccion>");
			case ALCALDE -> ("Vota una vez o dos veces, pero dos solo una vez por partida. <nombreVoto>/<nombreVoto>");
			case GUARDIAN -> ("Vota y elige a quien quieres proteger, no puedes repetir persona. <nombreVoto>/<nombreAccion>");
		};
	}

	synchronized public void esperarAlResto(String mensaje) throws InterruptedException {
		numeroVotos++;
		System.out.println(mensaje+", esperando= "+numeroVotos+"/"+(listaPersonajes.size()));
		if (numeroVotos == listaPersonajes.size()) {
			this.despertarHilos();
			numeroVotos = 0;
		} else {
			wait();
		}
	}

	// HACER QUE LOS HILOS ESPEREN CON WAIT Y DESPERTARLOS CON NOTIFY
	synchronized public void esperarComienzo() throws InterruptedException {
		wait();
	}

	synchronized public void despertarHilos() {
		notifyAll();
	}
}