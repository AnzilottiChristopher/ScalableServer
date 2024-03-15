import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client
{

    //TCP data
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    //UDP Data
    private DatagramSocket buzzer = null;
    private byte[] buffer = new byte[256];

    //Clients Data
    private String username;
    private InetAddress ipAddress;
    private int portNumber;

    private int numClients = 0;

    public Client(String address, int port)
    {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            //Initializing Input and Outputs
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            this.ipAddress = InetAddress.getByName("localhost");
            this.portNumber = port;


            //UDP
            this.buzzer = new DatagramSocket();



        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //This method in the project will either be the main game, or main will depending on how it gets structured.
    public void receiveData() throws IOException
    {
        while(socket.isConnected())
        {
            try
            {
                String num = input.readUTF();
                System.out.println(num);

                Scanner scan = new Scanner(System.in);
                DatagramPacket outUDP = new DatagramPacket(buffer, buffer.length, ipAddress, portNumber);
                System.out.println("Press anything to buzz in");
                if (scan.hasNextLine())
                {
                    buzzer.send(outUDP);
                    //System.out.println("In here" + username);
                }
            } catch (IOException e)
            {
                socket.close();
                break;
            }
        }
    }

    public void sendUsername() throws IOException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a username");
        username = sc.nextLine();
        buffer = username.getBytes(StandardCharsets.UTF_8);
        try
        {
            output.writeUTF(username);
        } catch (IOException e)
        {
            socket.close();
        }

    }

    public static void main(String[] args) throws IOException
    {
        System.out.println("What Ip address?");
        Scanner sc = new Scanner(System.in);
        String ip = sc.nextLine();

        Client client = new Client(ip, 5000);

        client.sendUsername();

        client.receiveData();


        sc.close();
    }
}
