import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import servidor.Servidor;

public class Cliente {

	public static void main(String[] args) {

		try {
			Socket cliente = new Socket("localhost", Servidor.PUERTO);

			PrintWriter output = new PrintWriter(cliente.getOutputStream(),
					true);

			HiloCliente hiloCliente = new HiloCliente(cliente);
			// Hacemos un hilo que este imprimiendo constantemente cualquier
			// mensaje que mande el servidor

			hiloCliente.start();

			Scanner sc = new Scanner(System.in);

			System.out.print("Introduce un mensaje: ");
			String cadena = sc.nextLine();

			while (cadena != null) {

				output.println(cadena);
				System.out.println("Enviado al servidor!");

				Thread.sleep(10);

				System.out.print("Vuelve a introducir un mensaje: ");
				cadena = sc.nextLine();

			}

			output.close();
			sc.close();
			cliente.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
