import java.util.ArrayList;

public class ElPuebloDuerme {

	private ArrayList<Personaje> listaPersonajes;

	public ElPuebloDuerme() {
		this.listaPersonajes = new ArrayList<>();
	}

	public void anadirPersonaje(Personaje p) {
		listaPersonajes.add(p);
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

	// HACER QUE LOS HILOS ESPEREN CON WAIT Y DESPERTARLOS CON NOTIFY
	synchronized public void esperarComienzo() throws InterruptedException {
		wait();
	}

	synchronized public void despertarHilos() {
		notifyAll();
	}

}
