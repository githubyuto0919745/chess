package client;

import java.util.Scanner;

public class Command {
    public Command(){

    }

    public void command (){
        System.out.print("Welcome to 240 chess. Type help to get started!!!");
        Scanner options = new Scanner(System.in);
        System.out.println("Enter your command >>>");

        Scanner actions = new Scanner(System.in);
        System.out.println("LOGGED_OUT >>>");

        System.out.print("register <Username><Password><Email> - to create an account");
        System.out.print("login <Username><Password> - to log in");
        System.out.print("quit - playing chess");
        System.out.print("help - with possible commands");



    }


}
