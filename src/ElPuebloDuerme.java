import java.util.ArrayList;

public class ElPuebloDuerme {

	private final ArrayList<Personaje> listaPersonajes;
	private ArrayList<Personaje> listaPersonajesVivos;
	private ArrayList<Personaje> personajesYaProtegidos;
	private int[] votos;

	private int numeroVotos = 0;

	private boolean dobleVotoAlcalde=true;

	public ElPuebloDuerme() {
		this.listaPersonajes = new ArrayList<>();
		this.personajesYaProtegidos=new ArrayList<>();
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
		if (listaPersonajes.size() > 4) {asignarRol(Rol.BRUJA);}
		if (listaPersonajes.size() > 5) {asignarRol(Rol.CURA);}
		if (listaPersonajes.size() > 6) {asignarRol(Rol.ALCALDE);}
		if (listaPersonajes.size() > 7) {asignarRol(Rol.GUARDIAN);}
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
		try {
			Personaje personajeAccion = getPersonaje(comando);
			if (personajeAccion.estaVivo() && !personajeAccion.estaProtegido()) {
				personajeAccion.morir();
				return true;
			}
			return false;
		}catch(NullPointerException e){
			return false;
		}
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

			votos[listaPersonajesVivos.indexOf(getPersonaje(nombreJugadorVoto))]++;

			Personaje personajeAccion=getPersonaje(nombreJugadorAccion);

			switch (p.getRol()) {// FUNCIONAMIENTO DE CADA ROL.
				case ALDEANO -> {
					System.out.println("UN ALDEANO HA VOTADO");
					return "Has votado a " + nombreJugadorVoto;
				}
				case BRUJA -> {
					System.out.println("UNA BRUJA HA VOTADO");
					if (personajeAccion == null) {
						return "Has votado a " + nombreJugadorVoto;
					} else if (personajeAccion.estaVivo()) {
						if(personajeAccion.estaProtegido()){
							return "No puedes matar a "+personajeAccion.getNombreJugador()+" porque esta protegido, pero has realizado tu voto.";
						}
						personajeAccion.morir();
						return "Vas a matar a " + nombreJugadorAccion;
					} else {
						personajeAccion.revivir();
						listaPersonajesVivos.add(personajeAccion);
						return "Has revivido a " + nombreJugadorAccion;
					}
				}
				case CURA -> {
					System.out.println("UN CURA HA VOTADO");
					if (personajeAccion == null) {
						return "Has votado a " + nombreJugadorVoto;
					}else if(personajeAccion.estaProtegido()){
						return "No puedes matar a "+personajeAccion.getNombreJugador()+" porque esta protegido, pero has realizado tu voto.";
					}else if (personajeAccion.getRol()==Rol.LOBO) {
						personajeAccion.morir();
						return "HAS CONSEGUIDO MATAR AL LOBO";
					} else {
						p.morir();
						return "Has lanzado agua bendita a un humano y has muerto por ello.";
					}
				}
				case ALCALDE -> {
					System.out.println("UN ALCALDE HA VOTADO");
					if (personajeAccion == null) {
						return "Has votado a " + nombreJugadorVoto;
					} else if (dobleVotoAlcalde) {
						votos[listaPersonajesVivos.indexOf(personajeAccion)]++;
						dobleVotoAlcalde=false;
						return "Has usado tu voto doble!";
					}else{
						return "Has votado a " + nombreJugadorVoto+", pero yaa no dispones de tu voto doble...";
					}
				}
				case GUARDIAN -> {
					System.out.println("EL GUARDIAN HA VOTADO");
					if(personajeAccion==null) {
						return "Has votado a " + nombreJugadorVoto;
					}else if(personajesYaProtegidos.contains(personajeAccion)){
						return "Has votado a " + nombreJugadorVoto+", pero no puedes repetir protección.";
					}else{
						personajeAccion.proteger();
						personajesYaProtegidos.add(personajeAccion);
						return "Has protegido a "+personajeAccion.getNombreJugador();
					}
				}
				default -> {return "ERROR: ESTE PERSONAJE NO TIEN UN ROL";}
			}
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

		Personaje personajeMasVotado=listaPersonajesVivos.get(indice);
		if(empate){
			System.out.println("Empate en los votos");
		}else{
			System.out.println("El jugador mas votado es= "+personajeMasVotado.getNombreJugador());
			if(personajeMasVotado.estaProtegido()){
				System.out.println("No se ha podido echar al jugador porque estaba siendo protegido.");
				return;
			}
			personajeMasVotado.morir();
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

	synchronized public void esperarComienzo() throws InterruptedException {wait();}
	synchronized public void despertarHilos() {notifyAll();}
}