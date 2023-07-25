package Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Client");
    private JTextArea messageArea=new JTextArea();
    private JTextField input=new JTextField();
    private Font font=new Font("Robot",Font.PLAIN,20);

    Client() throws IOException {
        socket=new Socket("localhost",4450);
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());
        createGUI();
        handleEvents();
        startReading();
    }

    private void handleEvents() {
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==10){
                    String msg=input.getText();
                    messageArea.append("Me : "+msg+"\n");
                    out.println(msg);
                    out.flush();
                    if(msg.equals("exit") || msg.equals("Exit")){
                        try {
                            socket.close();
                            System.exit(0);
                        } catch (IOException ex) {
                            System.out.println("Connection Closed!!!");
                        }
                    }
                    input.setText("");
                    input.requestFocus();
                }
            }
        });
    }

    public void createGUI() {
        this.setTitle("Client Area");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messageArea.setFont(font);
        input.setFont(font);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(input,BorderLayout.SOUTH);
        input.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVisible(true);
    }

    private void startReading() {
        Runnable r1=()->{
            System.out.println("Reader Started");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit") || msg.equals("Exit")) {
                        System.out.println("Server terminated the Chat!!!");
                        JOptionPane.showMessageDialog(this,"Server terminated the Chat!!!");
                        input.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Server :"+msg+ "\n");
                }
            }
            catch (IOException e) {
                System.out.println("Connection Closed!!");
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}
