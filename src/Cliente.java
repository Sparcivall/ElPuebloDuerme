import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) {

		try {
			Socket cliente = new Socket("localhost", Servidor.PUERTO);

			HiloCliente hiloCliente = new HiloCliente(cliente);
			// Hacemos un hilo que este imprimiendo constantemente cualquier
			// mensaje que mande el servidor
			hiloCliente.start();

			//

			PrintWriter output = new PrintWriter(cliente.getOutputStream(),
					true);

			Scanner sc = new Scanner(System.in);

			String cadena = "";
			while (cadena != null) {

				cadena = sc.nextLine();

				output.println(cadena);
			}

			output.close();
			sc.close();
			cliente.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
