package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTrader {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 80;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server on " + SERVER_HOST + ":" + SERVER_PORT);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter an order in the form <Side> <Price> <Size>: ");
                String order = scanner.nextLine();

                out.writeObject(order);
                out.flush();

                if (order.equals("Q|q")) {
                    break;
                }

                String result = (String)in.readObject();

                System.out.println(result);
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

