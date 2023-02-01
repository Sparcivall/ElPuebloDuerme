
public class Personaje {

	private String nombreJugador;
	private boolean estaVivo;
	private Rol rol;

	public Personaje(String nombreJugador) {
		this.nombreJugador = nombreJugador;
		this.estaVivo = true;
	}

	public void asignarRol(Rol rol) {
		this.rol = rol;
	}

	public Rol getRol() {
		return rol;
	}

	@Override
	public String toString() {
		return nombreJugador + " " + rol;// quitar de aqui el rol
	}

	public void morir() {
		this.estaVivo = false;
	}

	public boolean estaVivo() {
		return this.estaVivo;
	}

	public String getNombreJugador() {
		return this.nombreJugador;
	}
}