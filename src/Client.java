import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ClientSide implements ActionListener {
    static JPanel panel1;
    JTextField text;
    static JFrame f = new JFrame();
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;

    public ClientSide(){
        f.setLayout(null);
        // Header
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setBounds(0,0,400,55);
        panel.setLayout(null);
        f.add(panel);

        // Adding Back button on panel.
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image image = imageIcon.getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT);
        ImageIcon imageIconModified = new ImageIcon(image);
        JLabel backButton = new JLabel(imageIconModified);
        backButton.setBounds(5,15,25,25);
        panel.add(backButton);

        // Adding Profile Picture on header.
        ImageIcon imageIcon1 = new ImageIcon(ClassLoader.getSystemResource("icons/female.png"));
        Image image1 = imageIcon1.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1Modified = new ImageIcon(image1);
        JLabel profile1 = new JLabel(imageIcon1Modified);
        profile1.setBounds(40,5,50,50);
        panel.add(profile1);

        // Adding Video Button on header.
        ImageIcon imageIcon2 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image image2 = imageIcon2.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon imageIcon2Modified = new ImageIcon(image2);
        JLabel videoButton = new JLabel(imageIcon2Modified);
        videoButton.setBounds(250,15,30,30);
        panel.add(videoButton);

        // Adding Phone Button on header.
        ImageIcon imageIcon3 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image image3 = imageIcon3.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon imageIcon3Modified = new ImageIcon(image3);
        JLabel phoneButton = new JLabel(imageIcon3Modified);
        phoneButton.setBounds(300,15,30,30);
        panel.add(phoneButton);

        // Adding More Button on header.
        ImageIcon imageIcon4 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image image4 = imageIcon4.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon imageIcon4Modified = new ImageIcon(image4);
        JLabel moreButton = new JLabel(imageIcon4Modified);
        moreButton.setBounds(350,15,30,30);
        panel.add(moreButton);

        // Adding Username.
        JLabel name = new JLabel("Milie");
        name.setBounds(110,10,100,18);
        name.setForeground(Color.white); // Changing text colour.
        name.setFont(new Font("SAN_SARIF", Font.BOLD, 18)); // Font Family, Font type, Fnt Size
        panel.add(name);

        // Adding Status
        JLabel status = new JLabel("Online");
        status.setBounds(110,28,100,18);
        status.setForeground(Color.green); // Changing text colour.
        status.setFont(new Font("SAN_SARIF", Font.ITALIC, 12)); // Font Family, Font type, Fnt Size
        panel.add(status);

        // Adding panel for text body.
        panel1 = new JPanel();
        panel1.setBounds(5,60,380,490);
        f.add(panel1);

        // Text Field
        text = new JTextField();
        text.setBounds(5,555,280,40);
        text.setFont(new Font("SAN_SARIF", Font.PLAIN,15));
        f.add(text);

        // Send Button
        JButton send = new JButton("Send");
        send.setBounds(290,555, 96, 40);
        send.setFont(new Font("SAN_SARIF", Font.BOLD,15));
        send.addActionListener(this);
        f.add(send);


        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });


        f.setSize(400,600);
        f.setLocation(800,100);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.lightGray);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        try {
            String data = text.getText();

            JPanel panel2 = formatLabel(data);

            panel1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(panel2, BorderLayout.LINE_END);
            // Line 121 and 123 aligns message to right.
            vertical.add(right); // If there are multiple messages then they will be aligned one below another.
            vertical.add(Box.createVerticalStrut(15));

            panel1.add(vertical, BorderLayout.PAGE_END);

            dout.writeUTF(data);

            // When send button is clicked. After that textbox should look empty
            text.setText("");

            // Methods used to repaint frame.
            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e){
            System.out.println("Error Sending Message!");
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String data){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel fetchedData = new JLabel("<html> <p style=\"width:120px\">" + data + "</p> </html>");
        fetchedData.setFont(new Font("Tahoma",Font.PLAIN,15));
        fetchedData.setBackground(Color.gray);
        fetchedData.setOpaque(true); // To set background color.
        fetchedData.setBorder(new EmptyBorder(15,15,15,50)); // To add padding.

        panel.add(fetchedData);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");

        JLabel time = new JLabel();
        time.setText(sdf.format(calendar.getTime()));

        panel.add(time);

        return panel;
    }
}

public class Client extends ClientSide{
    public static void main(String[] args) {
        ClientSide clientSide = new ClientSide();

        try{
            Socket socket = new Socket("localhost",9999);

            DataInputStream din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while (true){
                panel1.setLayout(new BorderLayout());
                String message = din.readUTF();
                JPanel panel = formatLabel(message);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                panel1.add(vertical, BorderLayout.PAGE_END);

                f.validate();
            }

        }
        catch (Exception e1){
            e1.printStackTrace();
        }
    }
}