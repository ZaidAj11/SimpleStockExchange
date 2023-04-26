package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMatchingEngine {
    private static final int PORT = 80;
    private static final int THREAD_POOL_SIZE = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static MatchingEngine _matchingEngine = new MatchingEngine();
    private static int idCounter = 0;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostName());

                executor.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            	out.write(idCounter); // give id to client
            	out.flush();
            	idCounter ++;
            	
                while (true) {
                    String order = (String)in.readObject();

                    if (order.equals("q|Q")) {
                        break;
                    }

                    System.out.println("Processing request from client " + socket.getInetAddress().getHostName() + " for order " + order);

                    TradeResponse tradeResponse = handleOrder(order);
                    
                    
                    out.writeObject(tradeResponse.toString());
                    out.flush();

                    System.out.println("Returning result to client " + socket.getInetAddress().getHostName());
                }

                in.close();
                out.close();
                socket.close();
                System.out.println("Client disconnected: " + socket.getInetAddress().getHostName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        private TradeResponse handleOrder(String stringOrder) {
        	var splitOrder = stringOrder.split(" ");
        	
        	Order order = new Order(Side.valueOf(splitOrder[0]) , Double.parseDouble(splitOrder[1]), Double.parseDouble(splitOrder[2]));
        	TradeResponse response = new TradeResponse(); 
        	switch(order.Side) {
	        	case Buy: response = _matchingEngine.addBuyOrder(order); break;
	        	case Sell: response =  _matchingEngine.addSellOrder(order); break;
        	}
        	
            return response;
        }
    }
}


