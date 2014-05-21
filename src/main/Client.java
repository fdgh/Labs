import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Client {
 
    JTextArea area;
    JTextField fieldIP;
    JTextField fieldPort; 
    
    List<String> selectedFiles;
    
    public Client(){
        initUI();        
    }
    
    private void initUI() {

    	 JFrame f = new JFrame("Client");
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         f.setSize(440, 400);
         f.setLayout(new GridBagLayout());
         

         
         area = new JTextArea();
         fieldIP = new JTextField("127.0.0.1");
         fieldPort = new JTextField("2154");
         ((AbstractDocument)fieldPort.getDocument()).setDocumentFilter(
                 new DocumentFilterOnlyDigits()); 
         final JScrollPane scroll = new JScrollPane(area);
         final JButton selectBut = new JButton("Select");
         final JButton hashBut = new JButton("Get MD5 hash of chosen files");
         final JButton sendBut = new JButton("Send");
         sendBut.setEnabled(false);
         hashBut.setEnabled(false);
         JPanel card1 = new JPanel();
         card1.setLayout(new GridLayout(2, 2));
         card1.add(selectBut); 
         card1.add(hashBut);
         card1.add(fieldIP);
         card1.add(fieldPort);  
         JPanel card2 = new JPanel(); 
         card2.setLayout(new BorderLayout());
         card2.add(sendBut, BorderLayout.NORTH);
         card2.add(scroll, BorderLayout.CENTER);
         GridBagConstraints c = new GridBagConstraints(); 
         c.ipady = 30;      
         c.ipadx = f.getWidth();
         c.weightx = f.getWidth(); 
         
         f.add(card1, c);
         c.ipady = f.getHeight() - 100;
         c.ipadx = f.getWidth();
         c.gridy = 2;
         f.add(card2, c);
         f.setVisible(true);

         sendBut.addActionListener(new ActionListener() {
         
             @Override
             public void actionPerformed(ActionEvent arg0) {
            	 Socket socket = null;
            	 try {
            		 int port = Integer.parseInt(fieldPort.getText());
            		 String addres = fieldIP.getText();
            		 InetAddress ipAddress = null;
            		 ipAddress = InetAddress.getByName(addres);
            		 socket = new Socket(ipAddress, port);
            		 sendFiles(selectedFiles, socket); 
            		 area.append("Sended successful");
            	 } catch(Exception e) {
            		 area.append(e.toString());
            		 e.printStackTrace();
            	 } finally {
            		 try {
						if (socket != null) socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	 }
             }
         });
         
         hashBut.addActionListener(new ActionListener() {
             
             @Override
             public void actionPerformed(ActionEvent e) {
             	FileInputStream in = null;
             	try {
             		area.append("Get MD5 hash of chosen files:\n");
             		for(String s :selectedFiles){
             			File f = new File(s);                        
             			in = new FileInputStream(f);
             			long fileSize = f.length();
                     	if (fileSize - (int) fileSize > 0) {
                     		area.append(s + "\nfile is too big");
                     		try {
                         		in.close();
                         	} catch (Exception ex) {
                         		ex.printStackTrace();
                         	}
                     		continue;
                     	}
                     	byte [] buffer = new byte[(int)fileSize];
                     	in.read(buffer);
                     	area.append(s + "\n" + getHashMD5(buffer) + "\n");
                     	try {
                     		in.close();
                     	} catch (Exception ex) {
                     		ex.printStackTrace();
                     	}
             		}
             	} catch (Exception ex) {
             		ex.printStackTrace();
                 }  finally { 
 						try {
 							if (in != null) in.close();
 						} catch (IOException ex) {
 							ex.printStackTrace();
 						}
                 }
             }   
         });  
         selectBut.addActionListener(new ActionListener() {
             
             @Override
             public void actionPerformed(ActionEvent e) {
                 JFileChooser chooser = new JFileChooser();
                 chooser.setMultiSelectionEnabled(true);
                 selectedFiles = new ArrayList<String>();
                 area.setText("");
                 int returnVal = chooser.showOpenDialog(null);
                 
                 if (returnVal == JFileChooser.APPROVE_OPTION){
                     area.append("Chosen files:\n" );
                     File[] file = chooser.getSelectedFiles();
                     for (File d : file){
                         selectedFiles.add(d+"");
                         area.append(d+"\n");
                         
                     }       
                     sendBut.setEnabled(true);
                     hashBut.setEnabled(true);
                 }
             }
         });  
		
	}
    
	private String getHashMD5(byte[] data) throws NoSuchAlgorithmException {
    	MessageDigest m = MessageDigest.getInstance("MD5");
    	m.reset();
    	m.update(data);
    	byte[] digest = m.digest();
    	BigInteger bigInt = new BigInteger(1,digest);
    	String hashText = bigInt.toString(16);
		return hashText;  	
    }
    
    private void sendFiles(List<String> selectedFiles, Socket socket){

        
        int countFiles = selectedFiles.size();
        
        DataOutputStream outD; 
        try{
            outD = new DataOutputStream(socket.getOutputStream());
            
            outD.writeInt(countFiles);
                        
            for(int i = 0; i < countFiles; i++){
                File f = new File(selectedFiles.get(i));
                                
                outD.writeLong(f.length());
                outD.writeUTF(f.getName());
            
                FileInputStream in = new FileInputStream(f);
                byte [] buffer = new byte[64 * 1024];
                int count;
                
                while((count = in.read(buffer)) != -1){
                    outD.write(buffer, 0, count);
                }
                outD.flush();
                in.close();
            }           
            socket.close();         
        }
        catch(IOException e){
            e.printStackTrace();
        }   
    }
    
    class DocumentFilterOnlyDigits extends DocumentFilter
    {   
        @Override
        public void insertString(DocumentFilter.FilterBypass fp
                , int offset, String string, AttributeSet aset)
                                    throws BadLocationException {
        
        	if (checkForDigits(fp, offset,  string, aset))
                super.insertString(fp, offset, string, aset);
            else
                Toolkit.getDefaultToolkit().beep();

        }

        @Override
        public void replace(DocumentFilter.FilterBypass fp, int offset
                        , int length, String string, AttributeSet aset)
                                            throws BadLocationException {
        if (checkForDigits(fp, offset, string, aset))
            super.replace(fp, offset, length, string, aset);
        else
            Toolkit.getDefaultToolkit().beep();

        }
        private boolean checkForDigits(DocumentFilter.FilterBypass fp, int offset,
                  String string, AttributeSet aset) throws BadLocationException {
        	
        	int len = string.length();
            boolean isValidInteger = true;

            for (int i = 0; i < len; i++)
            {
                if (!Character.isDigit(string.charAt(i)))
                {
                    isValidInteger = false;
                    break;
                }
            }
            return isValidInteger;
        }
    }
    
    public static void main(String[] args) {
        new Client();
    }
}
