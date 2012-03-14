import com.illposed.osc.*;
import com.illposed.osc.OSCMessage;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class P2 extends JFrame {

	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension sizeOfScreen = toolkit.getScreenSize();
	Font fontTextOne = new Font("Minion Pro", Font.BOLD, 60);
	Font fontTextTwo = new Font("Minion Pro", Font.BOLD, 25);
	JPanel gridBagPanel = new JPanel();
	JPanel rightImagePanel = new JPanel();
	JLabel floorLayout;
	JLabel rightLabel;
	GridBagConstraints c = new GridBagConstraints();
	Container contentPane;// = getContentPane();
	ImageIcon floorMain = new ImageIcon(getClass().getResource("/images/home.png"));
	ImageIcon floorOne = new ImageIcon(getClass().getResource("/images/floor1.png"));
	ImageIcon floorTwo = new ImageIcon(getClass().getResource("/images/floor2.png"));
	ImageIcon floorThree = new ImageIcon(getClass().getResource("/images/floor3.png"));
	ImageIcon floorFour = new ImageIcon(getClass().getResource("/images/floor4.png"));
	ImageIcon thirtyOne = new ImageIcon(getClass().getResource("/images/31.png"));
	ImageIcon thirtyFour = new ImageIcon(getClass().getResource("/images/34.png"));
	ImageIcon fortyOne = new ImageIcon(getClass().getResource("/images/41.png"));
	ImageIcon fiftyThree = new ImageIcon(getClass().getResource("/images/53.png"));
	ImageIcon fiftyNine = new ImageIcon(getClass().getResource("/images/59.png"));
	ImageIcon sixtySeven = new ImageIcon(getClass().getResource("/images/67.png"));
	ImageIcon eightyThree = new ImageIcon(getClass().getResource("/images/83.png"));
	ImageIcon twentyTwelve = new ImageIcon(getClass().getResource("/images/2012.png"));
	static Object value = new Object();
	static String address[] = new String[5];
	OSCMessage messageToBeSentOne = new OSCMessage();// = new OSCMessage(address[0], value);
	OSCMessage messageToBeSentTwo = new OSCMessage();// = new OSCMessage(address[1], value);
	OSCMessage messageToBeSentThree = new OSCMessage();// = new OSCMessage(address[2], value);
	OSCMessage messageToBeSentFour = new OSCMessage();// = new OSCMessage(address[3], value);
	OSCMessage messageToBeSentSlider = new OSCMessage();// = new OSCMessage(address[4], value);
	static Object[] messageArguments = new Object[2];
	static String messageAddress;
	static InetAddress remoteIP;
	static InetAddress receiverIP;
	static OSCPortOut sender;
	static P2 leftHelp;
	static P2 rightHelp;
	int displayWidth;
	int displayHeight; 

	//Size ratio is 1.78

	public P2(int x, int y, int z) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for ( int i = 0; i < devices.length; i++ )
		{
			displayWidth = devices[ i ].getDisplayMode().getWidth();
			displayHeight = devices[ i ].getDisplayMode().getHeight();
			//System.out.println( "Device " + i + " width: " + displayWidth);
			//System.out.println( "Device " + i + " height: " + displayHeight);
		}
		contentPane = getContentPane();
		setTitle("To interact with me use "+ remoteIP);
		setVisible(true);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setSize(displayWidth, displayHeight);
		setSize(1080, 1920);
		if (z == 1)
		{
			LeftDisplay();
		}
		else {
			RightDisplay();
		}
	}

	public void LeftDisplay()
	{
		gridBagPanel.setLayout(new GridBagLayout());

		JLabel addressLabel = new JLabel("To interact with me use "+ remoteIP);
		addressLabel.setHorizontalAlignment(JLabel.CENTER);
		addressLabel.setVerticalAlignment(JLabel.CENTER);
		addressLabel.setFont(fontTextTwo);
		c.weightx = 0.5;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);		
		c.gridx = 0;
		c.gridy = 0;
		gridBagPanel.add(addressLabel, c);

		floorLayout = new JLabel();
		floorLayout.setIcon(floorMain);
		floorLayout.setHorizontalAlignment(JLabel.CENTER);
		floorLayout.setVerticalAlignment(JLabel.CENTER);
		c.weightx = 0.5;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 0;
		gridBagPanel.add(floorLayout, c);

		contentPane.add(gridBagPanel, BorderLayout.PAGE_START);
	}

	public void RightDisplay()
	{
		rightLabel = new JLabel(thirtyOne);
		rightImagePanel.add(rightLabel);
		contentPane.add(rightImagePanel);
	}

	public static void OSCMethod() throws IOException
	{

		int receiverPort = 8000;
		OSCPortIn receiver = new OSCPortIn(receiverPort);

		OSCListener listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				messageArguments = message.getArguments();
				messageAddress = message.getAddress();
				System.out.println("Listener called with address " + messageAddress 
						+ " with a value " + messageArguments[0]);
				try {
					leftHelp.updateLeftDisplay();
					rightHelp.updateRightDisplay();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		receiver.addListener("/Home/1stFloor", listener);
		receiver.addListener("/Home/2ndFloor", listener);
		receiver.addListener("/Home/3rdFloor", listener);
		receiver.addListener("/Home/4thFloor", listener);
		receiver.addListener("/Home/Reset", listener);
		receiver.addListener("/Home/Slider", listener);

		System.out.println("Server is listening on port " + receiverPort + "...");
		receiver.startListening();

		remoteIP = InetAddress.getLocalHost();
		receiverIP = InetAddress.getByName("192.168.1.104"); //.getLocalHost();
		System.out.println("My IP is "+remoteIP);
		int remotePort = 9000;
		sender = new OSCPortOut(receiverIP, remotePort);

	}

	public void updateLeftDisplay() throws IOException
	{
		messageToBeSentOne.setAddress(address[0]);
		messageToBeSentOne.addArgument(value);
		messageToBeSentTwo.setAddress(address[1]);
		messageToBeSentTwo.addArgument(value);
		messageToBeSentThree.setAddress(address[2]);
		messageToBeSentThree.addArgument(value);
		messageToBeSentFour.setAddress(address[3]);
		messageToBeSentFour.addArgument(value);
		messageToBeSentSlider.setAddress(address[4]);
		messageToBeSentSlider.addArgument(value);

		address[0]= "/Home/1stFloor";
		address[1]= "/Home/2ndFloor";
		address[2]= "/Home/3rdFloor";
		address[3]= "/Home/4thFloor";
		address[4]= "/Home/Slider";
		value = "0.0";

		if(messageAddress.contains("/Home/1stFloor"))
		{
			if(messageArguments[0].toString().contains("1.0"))
			{
				floorLayout.setIcon(floorOne);
				sender.send(messageToBeSentTwo);
				sender.send(messageToBeSentThree);
				sender.send(messageToBeSentFour);
			}
			else 
				floorLayout.setIcon(floorMain);
		}
		else if (messageAddress.contains("/Home/2ndFloor"))
		{
			if(messageArguments[0].toString().contains("1.0"))
			{	
				floorLayout.setIcon(floorTwo);
				sender.send(messageToBeSentOne);
				sender.send(messageToBeSentThree);
				sender.send(messageToBeSentFour);
			}
			else 
				floorLayout.setIcon(floorMain);
		}
		else if (messageAddress.contains("/Home/3rdFloor"))
		{
			if(messageArguments[0].toString().contains("1.0"))
			{
				floorLayout.setIcon(floorThree);
				sender.send(messageToBeSentTwo);
				sender.send(messageToBeSentOne);
				sender.send(messageToBeSentFour);
			}
			else 
				floorLayout.setIcon(floorMain);
		}
		else if (messageAddress.contains("/Home/4thFloor"))
		{
			if(messageArguments[0].toString().contains("1.0"))
			{
				floorLayout.setIcon(floorFour);
				sender.send(messageToBeSentTwo);
				sender.send(messageToBeSentThree);
				sender.send(messageToBeSentOne);
			}
			else 
				floorLayout.setIcon(floorMain);
		}
		else if (messageAddress.contains("/Home/Reset"))
		{
			floorLayout.setIcon(floorMain);
			sender.send(messageToBeSentOne);
			sender.send(messageToBeSentTwo);
			sender.send(messageToBeSentThree);
			sender.send(messageToBeSentFour);
			sender.send(messageToBeSentSlider);
		}
	}

	public void updateRightDisplay()
	{
		if(messageAddress.contains("/Home/Slider"))
		{
			if (Double.parseDouble(messageArguments[0].toString()) < 0.125)
				rightLabel.setIcon(thirtyOne);
			else if(Double.parseDouble(messageArguments[0].toString()) >= 0.125 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.25)
				rightLabel.setIcon(thirtyFour);
			else if(Double.parseDouble( messageArguments[0].toString()) >= .25 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.375)
				rightLabel.setIcon(fortyOne);
			else if(Double.parseDouble( messageArguments[0].toString()) >= 0.375 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.50)
				rightLabel.setIcon(fiftyThree);
			else if (Double.parseDouble( messageArguments[0].toString()) >= 0.50 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.625)
				rightLabel.setIcon(fiftyNine);
			else if (Double.parseDouble( messageArguments[0].toString()) >= 0.625 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.75)
				rightLabel.setIcon(sixtySeven);
			else if (Double.parseDouble( messageArguments[0].toString()) >= 0.75 
					&& Double.parseDouble( messageArguments[0].toString()) < 0.99) 
				rightLabel.setIcon(eightyThree);
			else
				rightLabel.setIcon(twentyTwelve);
		}
		else if (messageAddress.contains("/Home/Reset"))
			rightLabel.setIcon(thirtyOne);
	}

	public static void main(String[] args) throws IOException {
		OSCMethod();
		leftHelp = new P2(0, 0, 1);
		rightHelp = new P2(1080, 0, 2);
	}
}