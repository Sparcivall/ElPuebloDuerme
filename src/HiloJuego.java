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

			System.out.println(Thread.currentThread().getName() + " se va a dormir esperando al notify()");

			pueblo.esperarComienzo();

			// EMPIEZA LA PARTIDA

			while (true) {

				imprimirListaJugadores();
				Thread.sleep(2000);

				output.println("Se hace de noche...");

				if(personaje.getRol()==Rol.LOBO){
					output.println("Escribe el nombre del jugador al que quieras matar");
					while(true) {
						String comando = input.readLine();
						if (pueblo.ataqueLobo(personaje, comando)) {
							output.println("Te has comido a " + comando);
							break;
						}else{
							output.println("No puedes matarte a ti mismo ni a jugadores ya muertos.");
						}
					}
				}

				pueblo.esperarAlResto();
				Thread.sleep(1000);

				output.println("Se ha hecho de dia!!!\nPero por la noche algo se movio entre las sombras...");

				// PURGAMOS AL PERSONAJE QUE HAYA MATADO EL LOBO
				if (pueblo.purgarPersonaje(personaje)) {output.println("UN JUGADOR TE HA ASESINADO!");break;}

				Thread.sleep(1000);
				if(pueblo.comprobarFinPartida()){output.println("FIN DE LA PARTIDA. EL LOBO GANA");break;}
				imprimirListaJugadores();

				output.println(pueblo.getPreguntaPersonaje(personaje.getRol()));

				String comando="";
				while (true) {

					if(personaje.getRol()!=Rol.LOBO){
						comando = input.readLine();
					}

					String mensajeAccion = pueblo.accionPersonaje(this.personaje, comando);

					output.println(mensajeAccion);

					if (!mensajeAccion.contains("ERROR")) {
						break;
					}
				}
				pueblo.esperarAlResto(); //esta bien asi???

				pueblo.eliminarJugadorMasVotado();

				pueblo.esperarAlResto();

				if (pueblo.purgarPersonaje(personaje)) {output.println("UN JUGADOR TE HA ASESINADO!");break;}
				Thread.sleep(1000);
				if(pueblo.comprobarFinPartida()){output.println("FIN DE LA PARTIDA. EL LOBO GANA");break;}
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
