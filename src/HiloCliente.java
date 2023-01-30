import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloCliente extends Thread {

	private Socket cliente = null;

	private BufferedReader input;

	public HiloCliente(Socket cliente) throws IOException {
		this.cliente = cliente;

		this.input = new BufferedReader(
				new InputStreamReader(this.cliente.getInputStream()));
	}

	@Override
	public void run() {
		try {
			while (true) {
				String mensajeRecibido = input.readLine();

				System.out.println("[Servidor] " + mensajeRecibido.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("{ CONEXIÓN CON EL SERVIDOR TERMINADA }");
		}
	}
}
