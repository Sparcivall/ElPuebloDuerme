
public class Personaje {

	private String nombreJugador;
	private Rol rol;

	public Personaje(String nombreJugador) {
		this.nombreJugador = nombreJugador;
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
}