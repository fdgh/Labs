import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	private JTextArea area;
	private final static int PORT = 2154;
	private final static int MAX_NUMBER_OF_THREADS = 3;

	public Server() {
		initUI();
		ExecutorService exec = null;
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(PORT);
			area.append("Wait connect...");
			exec = Executors.newFixedThreadPool(4);
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
		
		 Connect(Socket soket) {
			DataInputStream din = null;
			FileOutputStream fout = null;
			try {
				din = new DataInputStream(soket.getInputStream());
				int filesCount = din.readInt();
				area.append("\n------------------------------------------------------\n");
				area.append("Download " + filesCount + " files\n");

				for (int i = 0; i < filesCount; i++) {
					area.append("Processing " + (i + 1) + "file\n");

					long fileSize = din.readLong(); 

					String fileName = din.readUTF(); 

					area.append("File name: " + fileName + "\n");
					area.append("File size: " + fileSize + " byte\n");

					byte[] buffer = new byte[64 * 1024];
					fout = new FileOutputStream(fileName);
					int count, total = 0;
					
					while ((count = din.read(buffer)) != -1) {
						total += count;
						fout.write(buffer, 0, count);
						if (total == fileSize) {
							break;
						}
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
				e.printStackTrace();
			} finally {
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
		public void run() {			
		}
	}
	

	public static void main(String[] arg) {
		new Server();
	}
}
