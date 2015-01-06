/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
/**
 *
 * @author tranq
 */
public class HttpRequest extends Thread 
{
   private final static String CLRF = "\r\n";
   private Socket sock;
   public HttpRequest(Socket client){
       this.sock = client;   
   }
   
   public void run(){
       try
       {
           processRequest();
       }
       catch(Exception e)
       {
          System.out.println(e);         
       }
   }  
   
   private void processRequest() throws Exception
   {
       InputStream is = sock.getInputStream();
       DataOutputStream outStream = new DataOutputStream(sock.getOutputStream());
       BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
       
      String requestLine = br.readLine();
      StringTokenizer tokens = new StringTokenizer(requestLine);
      tokens.nextToken();
      String fileName = "." + tokens.nextToken();
      FileInputStream fileInput = null;
      boolean exist = true;
      try{
          fileInput = new FileInputStream(fileName);
          
      }
      catch( Exception ex)
      {
          exist = false;
      }
      String statusLine = null;
      String contentTypeLine = null;
      String entityBody = null;
      if(exist) 
      {
         statusLine = "Responding to existing file";
         contentTypeLine = "Content-type:" + contentType( fileName ) + CLRF;
         sendBytes(fileInput,outStream );
         fileInput.close();
      }
      else 
      {
         statusLine = "File doesn't exist\n";
         contentTypeLine = "no contents\n";
         entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
      // Send the status line.
         outStream.writeBytes(statusLine);
      // Send the content type line.
         outStream.writeBytes(contentTypeLine);
      // Send a blank line to indicate the end of the header lines.
         outStream.writeBytes(CLRF);
      // Send the entity body.	

         outStream.writeBytes(entityBody);
      
      }     
      outStream.close();
      br.close();
      sock.close();
      
      
      

   }
   
   
   private static String contentType(String fileName)
   {
      if(fileName.endsWith(".htm") || fileName.endsWith(".html"))
      {
         return "text/html";
      }
      if(fileName.endsWith(".gif") || fileName.endsWith(".GIF"))
      {
         return "image/gif";
      }
      if(fileName.endsWith(".jpeg"))
      {
         return "image/jpeg";
      }
      if(fileName.endsWith(".java"))
      {
         return "java file";
      }
      if(fileName.endsWith(".sh"))
      {
         return "bourne/awk";
      }
      return "application/octet-stream";
    }
   private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
{
	// Construct a 1K buffer to hold bytes on their way to the socket.
	byte[] buffer = new byte[1024];
	int bytes = 0;
	// Copy requested file into the socket's output stream.
	while((bytes=fis.read(buffer)) != -1) {
		os.write(buffer, 0, bytes);
	}
}
}
