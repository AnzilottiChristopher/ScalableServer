import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    public static void main(String[] args)
    {
        ServerAccept serverAccept = new ServerAccept(5000);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(serverAccept);



        executorService.shutdown();
    }
}
