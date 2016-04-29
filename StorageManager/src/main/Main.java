package main;
import java.util.Scanner;

import StorageManager.StorageServer;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("         Welcome to Storage Manager :       ");
		System.out.println("-------------------------------------------");
		System.out.println("   Version 2.0, written by Dorian Lecomte  ");
		System.out.println("-------------------------------------------");
		
		StorageServer ss = new StorageServer();
		String input;
	
		
		Scanner sc = new Scanner(System.in);
		
		while (true)
		{
			System.out.println("Please insert a command to proceed : ");
			input = sc.next();
			switch(input)
			{
				case "enable" :
					try
					{
						ss.enable();
					}
					catch (Exception e1)
					{
						System.out.println("Oups... ");
						System.out.println(e1.toString());
						System.out.println("Please consult log file for more information.");
					}
					break;
					
				case "disable" :
					try
					{
						ss.disable();
					}
					catch (Exception e2)
					{
						System.out.println("Oups... ");
						System.out.println(e2.toString());
						System.out.println("Please consult log file for more information.");
					}
					break;
					
				case "reload" :
					try
					{
						ss.reload();
					}
					catch (Exception e3)
					{
						System.out.println("Oups... ");
						System.out.println(e3.toString());
						System.out.println("Please consult log file for more information.");
					}
					break;
				
				case "status" :
					if (ss.isEnable()) System.out.println("Storage server is currently enabled.");
					else System.out.println("Storage server is currently disabled");
					break;
					
				case "help" :
					System.out.println(""
							+ "Here are the arguments you can give to this shell interpreter : \n"
							+ "1) 'enable'  -> Enable Storage Server. \n"
							+ "2) 'disable' -> Disable Storage Server.\n"
							+ "3) 'reload'  -> Reload Storage Server inlcudes parameters in file configuration.\n"
							+ "4) 'help'    -> Display this current message. \n"
							+ "5) 'exit'    -> Exit the current shell and kill the Storage Server.\n"
							+ "6) 'status'  -> Display the Storage server's state.\n");
					break;
					
				case "exit" :
					try
					{
						System.out.println("Proceed exiting...");
						if (ss.isEnable()) ss.disable();
						sc.close();
					}
					catch(Exception e) 
					{
						//ignore
					}
					finally
					{
						System.out.println("Bye.");
						System.exit(0);
					}
					break;
					
				default :
					System.out.println("Unknown argument '"+ input +"' found !");
					System.out.println("Insert 'help' to display help message or 'exit' to leave.");
					System.out.println("Leaving the software include the disabling of the storage server.");
			}
		}	
	}
}
