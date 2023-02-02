import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloJuego extends Thread {
	private Socket socketServidor = null;
	private ElPuebloDuerme pueblo;

	private BufferedReader input;
	private PrintWriter output;

	private Personaje personaje;

	public HiloJuego(Socket s, ElPuebloDuerme pueblo) {
		this.socketServidor = s;
		this.pueblo = pueblo;

		try {

			this.output = new PrintWriter(socketServidor.getOutputStream(),
					true);

			this.input = new BufferedReader(
					new InputStreamReader(socketServidor.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {// ESTE HILO SE COMUNICA CON CADA CLIENTE
		try {

			System.out.println("COMUNICO CON: " + socketServidor.toString());

			output.println("Escribe el nombre de tu personaje");

			String nombreJugador = input.readLine();

			this.personaje = new Personaje(nombreJugador);
			pueblo.anadirPersonaje(personaje);

			output.println("Tu personaje ha sido registrado!");

			System.out.println(Thread.currentThread().getName()
					+ " se va a dormir esperando al notify()");
			pueblo.esperarComienzo();

			// EMPIEZA LA PARTIDA

			while (true) {

				Thread.sleep(100);

				imprimirListaJugadores();

				Thread.sleep(500);

				output.println(pueblo.getPreguntaPersonaje(personaje.getRol()));

				boolean errorComando = false;
				while (true) {

					String comando = input.readLine();

					if (errorComando == false) {
						pueblo.esperarAlResto();
					}

					String mensajeAccion = pueblo
							.accionPersonaje(this.personaje, comando);

					output.println(mensajeAccion);

					if (!mensajeAccion.contains("ERROR")) {
						break;
					} else {
						errorComando = true;
					}
				}

				// ANTES DE SEGUIR: RECORDAR QUE POR LA MAÑANA SON LAS
				// VOTACIONES Y POR LA NOCHE MATA EL LOBO

				pueblo.esperarAlResto();

				if (pueblo.purgarPersonaje(personaje)) {
					output.println("UN JUGADOR TE HA ASESINADO!");
					break;
				}
			}

			// FIN DE LA PARTIDA

			System.out.println("FIN CON: " + socketServidor.toString());

			output.close();
			input.close();
			socketServidor.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void imprimirListaJugadores() {
		for (Personaje p : pueblo.getListaJugadores()) {
			output.println(p);
		}
	}

}
