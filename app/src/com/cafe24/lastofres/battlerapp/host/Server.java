package com.cafe24.lastofres.battlerapp.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private ServerSocket server;
	private ArrayList<Socket> clients;
	private PrintWriter out;
	private BufferedReader in;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(80);
	}

}
