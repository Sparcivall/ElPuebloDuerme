import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ElPuebloDuerme {

	private ArrayList<Personaje> listaPersonajes;
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

		if (listaPersonajes.size() > 2) {
			asignarRol(Rol.LOBO);
		}

		if (listaPersonajes.size() > 4) {// BRUJA
			asignarRol(Rol.BRUJA);
		}

		if (listaPersonajes.size() > 5) {// CURA
			asignarRol(Rol.CURA);
		}

		if (listaPersonajes.size() > 6) {// GUARDIAN
			asignarRol(Rol.GUARDIAN);
		}

		if (listaPersonajes.size() > 7) {// ALCALDE
			asignarRol(Rol.ALCALDE);
		}
	}

	private void asignarRol(Rol rol) {
		int indiceAleatorio = (int) Math.floor(
				Math.random() * ((getAldeanos().size() - 1) - 0 + 1) + 0);
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

	public boolean purgarPersonaje(Personaje p) throws InterruptedException{
		if (!p.estaVivo()) {
			listaPersonajesVivos.remove(listaPersonajesVivos.indexOf(p));
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
			if (p.getNombreJugador().equals(comando)) {
				return "ERROR: No puedes realizar ninguna acción en ti mismo";
			}

			switch (p.getRol()) {
				case ALDEANO :
					votos[listaPersonajesVivos.indexOf(getPersonaje(comando))]++;
					System.out.println("UN ALDEANO HA VOTADO");
					return "Has votado a " + comando;
				case LOBO:
					return "Observas como los humanos votan";
				case BRUJA :
					return "";
				case CURA :
					return "";
				case ALCALDE :
					return "";
				case GUARDIAN :
					return "";
				default :
					return "ERROR: ESTE PERSONAJE NO TIEN UN ROL";
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
		switch (rol) {
			case ALDEANO :
				return ("¿ A quien votas para echar del pueblo ?");
			case LOBO :
				return ("Es de dia, no puedes hacer nada.");
			case BRUJA :
				return ("Revive o mata a quien quieras");
			case CURA :
				return ("¿A quien echas agua bendita? (Si te equivocas moriras)");
			case ALCALDE :
				return ("¿A quien votas para echar del pueblo? (Una vez puedes votar doble)");
			case GUARDIAN :
				return ("¿A quien quieres proteger? (Una vez puedes hacerlo dos veces)");
			default :
				return ("ERROR, ESTE PERSONAJE NO TIEN UN ROL");
		}
	}

	synchronized public void esperarAlResto() throws InterruptedException {
		numeroVotos++;
		System.out.println("Personajes esperando= "+numeroVotos+"/"+(listaPersonajesVivos.size()));
		for(Personaje p:listaPersonajesVivos){
			System.out.println(p);
		}
		if (numeroVotos == listaPersonajesVivos.size()) {
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