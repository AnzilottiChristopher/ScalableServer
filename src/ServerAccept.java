import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerAccept implements Runnable
{
    private ServerSocket serverSocket;
    public static int numClients = 0;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    int ports;

    public ServerAccept(int ports)
    {
        this.ports = ports;
        try
        {
            serverSocket = new ServerSocket(ports);
            System.out.println("Server is running");
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run()
    {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorService newClient = Executors.newCachedThreadPool();
        try
        {
            while (true)
            {
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();
                System.out.println("Client Connected: " + clientSocket);

                if (clientSocket != null)
                {
                    //make the thread of ClientHandler
                    addNum();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    addClient(clientHandler);
                    //clientHandler.broadCast();
                    sendClients();
                    executorService.execute(clientHandler);
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public synchronized void addNum()
    {
        numClients++;
    }

    public synchronized void addClient(ClientHandler clientHandler)
    {
        clientHandlers.add(clientHandler);
    }

    public void sendClients()
    {
        for(ClientHandler clientHandler : clientHandlers)
        {
            clientHandler.sendData();
        }
    }
}
