import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HiloServidor extends Thread {

	private ServerSocket servidor;

	private BufferedReader input;
	private PrintWriter output;

	public HiloServidor(ServerSocket servidor) {
		this.servidor = servidor;
	}

	@Override
	public void run() {
		try {

			System.out.println("HILO SERVIDOR INICIADO");

			while (true) {

				Socket socketServidor = new Socket();

				socketServidor = this.servidor.accept();// esperando a un
														// cliente

				HiloJuego hilo = new HiloJuego(socketServidor);

				hilo.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}