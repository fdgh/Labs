import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
			area.append("Wait connect...");
			exec = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
			while (true) {
				Socket s = null;
				//TODO ask about it
				try {
					s = ss.accept();
					exec.execute(new Connect(s));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (s != null) s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(IOException e) {
			area.append("Can't use " + PORT + " socket");
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
		JScrollPane scroll = new JScrollPane(area);
		f.add(scroll);
		
		f.setAlwaysOnTop(true);
		f.setVisible(true);
	}

	public class Connect implements Runnable{
		 Connect(Socket socket) {
			DataInputStream din = null;
			FileOutputStream fout = null;
			boolean isSuccessful = true;
			try {
				din = new DataInputStream(socket.getInputStream());
				int filesCount = din.readInt();
				area.append("\n------------------------------------------------------\n");
				area.append("Download " + filesCount + " files\n");

				for (int i = 0; i < filesCount; i++) {
					area.append("Processing " + (i + 1) + " file\n");
					long fileSize = din.readLong(); 
					String fileName = din.readUTF(); 
					area.append("File name: " + fileName + "\n");
					area.append("File size: " + fileSize + " byte\n");
					
					fout = new FileOutputStream(fileName);
					int count;
					long total = fileSize;
					
					while (total > 0) {
						int bufferSize = (int) Math.min((long)SIZE_OF_BUFFER, total);
						byte[] buffer = new byte[bufferSize];
						count = din.read(buffer);
						total -= count;
						fout.write(buffer, 0, count);
					}
					fout.flush();
					try {
						fout.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					area.append("Download complete\n---------------------------------\n");
				}
			} catch (Exception e) {
				area.append("Download failed!!!\n");
				isSuccessful = false;
				e.printStackTrace();
			} finally {
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
				try {
					if (din != null)
						din.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (fout != null)
						fout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {}		
	}
	

	public static void main(String[] arg) {
		new Server();
	}
}
