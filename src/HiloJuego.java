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

			Personaje personaje = new Personaje(nombreJugador);
			pueblo.anadirPersonaje(personaje);

			output.println("Tu personaje ha sido registrado!");

			// String cadena = "";
			// while (!cadena.trim().equals("*")) {
			//
			// cadena = input.readLine();
			//
			// output.println(cadena.trim());
			// }

			System.out.println("FIN CON: " + socketServidor.toString());

			output.close();
			input.close();
			socketServidor.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}