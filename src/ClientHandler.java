import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
    //TCP
    private Socket clientSocket;
    private DataOutputStream out = null;
    private DataInputStream in = null;


    //UDP
    private DatagramSocket socketUDP;
    private byte[] buffer;

    private String user;

    private ArrayList<String> questions;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;

        try
        {
            out = new DataOutputStream(this.clientSocket.getOutputStream());
            in = new DataInputStream(this.clientSocket.getInputStream());

            this.user = in.readUTF();
            System.out.println(this.user);

            questions = new ArrayList<>();
            questions.add("1) What is 1 + 1");
            questions.add("2) What is 4 + 4");
            questions.add("3) What is 2 + 2");


            //UDP
            this.socketUDP = new DatagramSocket(5000);
            buffer = new byte[256];

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void closeEverything(Socket clientSocket, DataInputStream in, DataOutputStream out)
    {
        try
        {
            if (clientSocket != null)
            {
                clientSocket.close();
            }
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendData()
    {
        try
        {
            out.writeUTF(questions.get(0));
            out.flush();
            //System.out.println("here");
        } catch (IOException e)
        {
            closeEverything(clientSocket, in, out);
        }
    }
    @Override
    public void run()
    {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(true)
        {
            try
            {
                //System.out.println("Here we are");
                socketUDP.receive(packet);
                //System.out.println("Should receive");
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
        //System.out.println("Here");

    }
}
