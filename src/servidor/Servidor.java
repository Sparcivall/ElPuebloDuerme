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
			// hiloServidor.arrancar();
		}

		// hiloServidor.arrancar // PARA ARRANCAR LA PARTIDA USAR LOS METODOS
		// DEL HILO SERVIDOR

		// EL SERVIDOR SOLO EXISTE PARA SABER QUIEN ENTRA Y PARA QUE EL USUARIO
		// HAGA START CUANDO CONSIDERE QUE SE UNIÓ SUFICENTE GENTE

		// EL HiloServidor EXISTE PARA HACER LOS ACCEPT Y QUE SE UNAN X CLIENTES
		// ESTE VA A QUEDAR BLOQUEADO POR CULPA DEL ACCEPT
		// (HiloServidor es el Servidor de los apuntes, y HiloJuego es el
		// HiloServidor, basicamente es un paso extra)

		// ESTA CLASE DEBERIA QUEDAR DE FORMA QUE SE MUERA PERO LA TERMINAL SE
		// QUEDA ABIERTA

	}
}
