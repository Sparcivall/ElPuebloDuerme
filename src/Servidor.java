import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Servidor {

	public static final int PUERTO = 6000;

	public static void main(String[] args) throws IOException {

		ServerSocket servidor = new ServerSocket(PUERTO);

		HiloServidor hiloServidor = new HiloServidor(servidor);

		Scanner sc = new Scanner(System.in);

		hiloServidor.start();

		System.out.println("ESCRIBE start PARA EMPEZAR!");
		if (sc.nextLine().equals("start")) {
			hiloServidor.empezarPartida();
		}
	}
}
