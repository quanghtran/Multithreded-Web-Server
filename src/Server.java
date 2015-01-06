/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tranq
 */
import java.io.* ;
import java.net.* ;
import java.util.* ;
public class Server 
{
    public static void main(String argv[]) throws Exception
    {
      Server server = new Server();
      server.run();
    }
    public void run()
    {
        try{
		ServerSocket servSock = new ServerSocket(5976);
		while (true)
                {
		   Socket connection = servSock.accept();
        	   HttpRequest request = new HttpRequest(connection);
        	   Thread thread = new Thread(request);
        	   thread.start();
		}
        }
        catch ( Exception ex)
        {
            System.out.println(ex);
        }
        }
    }
