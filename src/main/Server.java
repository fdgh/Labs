package main;
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	private JTextArea area;
	private final static int PORT = 2154;
	private final static int MAX_NUMBER_OF_THREADS = 3;
	private final static int SIZE_OF_BUFFER = 64 * 1024;
	
	public Server() {
		initUI();
		ExecutorService exec = null;
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(PORT);
			area.append("Wait connect...\n");
			exec = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
			while (true) {
				Socket s = null;
				try {
					s = ss.accept();
					exec.execute(new Connect(s));
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		} catch(IOException e) {
			area.append("Can't use " + PORT + " socket\n");
			e.printStackTrace();
		} finally {
			try {
				if (ss != null) ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (exec != null) exec.shutdown();
		}
	}
	
	private void initUI() {
		JFrame f = new JFrame("Server");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 350);
		f.setLayout(new BorderLayout());
		
		area = new JTextArea();
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area);
		f.add(scroll);
		
		f.setAlwaysOnTop(true);
		f.setVisible(true);
	}

	public class Connect implements Runnable{
		Socket socket = null;
		StringBuilder downloadHistory;

		Connect(Socket socket) {
			this.socket = socket;
			downloadHistory = new StringBuilder();
		}
		
		@Override
		public void run() {
			boolean hasDownloaded;
		
			hasDownloaded = downloadFiles();
			sendCallbackAndCloseSocket(hasDownloaded);
			printHistory();
		}	
		
		private void sendCallbackAndCloseSocket(boolean isSuccessful) {
			DataOutputStream dout = null;
			try {
				dout = new DataOutputStream(socket.getOutputStream());
				dout.writeBoolean(isSuccessful);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (dout != null) dout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void printHistory() {
			area.append(downloadHistory.toString());
		}
		
		private boolean downloadFiles() {
			DataInputStream din = null;
			FileOutputStream fout = null;
			boolean isSuccessful = false;
			try {
				din = new DataInputStream(socket.getInputStream());
				int filesCount = din.readInt();
				downloadHistory.append("\n---------------------------------\n");
				downloadHistory.append("Download " + filesCount + " files\n");
				downloadHistory.append("Started at " + new Date() + "\n");
				for (int i = 0; i < filesCount; i++) {
					downloadHistory.append("\nProcessing " + (i + 1) + " file\n");
					long fileSize = din.readLong(); 
					String fileName = din.readUTF(); 
					downloadHistory.append("File name: " + fileName + "\n");
					downloadHistory.append("File size: " + fileSize + " byte\n");
					
					fout = new FileOutputStream(fileName);
					int count;
					long total = fileSize;
					byte[] buffer = new byte[SIZE_OF_BUFFER];
					while (total > 0) {
						int bufferSize = (int) Math.min((long) SIZE_OF_BUFFER, total);
						count = din.read(buffer, 0, bufferSize);
						total -= count;
						fout.write(buffer, 0, count);
					}
					fout.flush();
					try {
						fout.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				isSuccessful = true;
				
				downloadHistory.append("\nDownload complete\nEnded at " + new Date());
				downloadHistory.append("\n---------------------------------\n");
				
			} catch (Exception e) {
				downloadHistory.append("\nDownload failed!!!\n");
				e.printStackTrace();
			} finally {
				try {
					if (fout != null) fout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return isSuccessful;
		}
	}
	

	public static void main(String[] arg) {
		new Server();
	}
}
