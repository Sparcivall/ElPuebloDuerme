import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {

	private ServerSocket servidor;

	private ArrayList<HiloJuego> hilosJuego;// SI PUEDO EVITARLO MEJOR UNO USAR
											// ARRAY DE HILOS

	private BufferedReader input;
	private PrintWriter output;

	private ElPuebloDuerme elPuebloDuerme;

	public HiloServidor(ServerSocket servidor) {
		this.servidor = servidor;
		this.hilosJuego = new ArrayList<>();
		this.elPuebloDuerme = new ElPuebloDuerme();
	}

	@Override
	public void run() {
		try {

			System.out.println("HILO SERVIDOR INICIADO");

			while (true) {

				Socket socketServidor = new Socket();

				socketServidor = this.servidor.accept();// esperando a un
														// cliente

				HiloJuego hilo = new HiloJuego(socketServidor, elPuebloDuerme);

				hilosJuego.add(hilo);

				hilo.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void empezarPartida() {

	}
}