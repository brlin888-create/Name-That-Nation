// imports necessary for game
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Scanner;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;	
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import java.io.FileWriter;

import javax.swing.Timer;
import java.util.ArrayList;

/* This is the main class that creates the JFrame adding in the contents
 * of NameThatNationHolder. It sets the frame as 960 by 540 and exits on close. 
 * It cannot be resizable and is set as visible.*/
public class NameThatNation
{
	public NameThatNation()
	{
	}
    public static void main(String [] args)
    {
        NameThatNation ntn = new NameThatNation(); //create instance
        ntn.run();
    }
    public void run()
    {
        JFrame frame = new JFrame("NameThatNation");
        frame.setSize(960, 540); //960 by 540
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setResizable(false);
		NameThatNationHolder ntnh = new NameThatNationHolder(frame);
        frame.getContentPane().add(ntnh);
        frame.setVisible(true);
    }
}

/* NameThatNationHolder is the panel that holds the card layout. 
 * It creates instances of all the panels, sending in the current instance and
 * also info which is the instance for the information class. It has a try-catch
 * method which can be accessed later whenever necessary in other classes. */
class NameThatNationHolder extends JPanel
{
	private Information info;
	private CardLayout cards;
	private JFrame frame;
	public NameThatNationHolder(JFrame frameIn)
	{
		frame = frameIn;
		cards = new CardLayout();
		setLayout(cards);
		info = new Information();
		StartPanel SP = new StartPanel(this, cards, info);
		SettingsPanel sp = new SettingsPanel(this, cards, info);
		InstructionsPanel ip = new InstructionsPanel(this, cards);
		CountryInfoPanel cip = new CountryInfoPanel(this, cards);
		add(SP, "Start");
		add(sp, "Settings");
		add(ip, "Instructions");
		add(cip, "Country Info");		
		cards.show(this, "Start");		
	}
	public void closeWindow() //method used to close the entire JFrame when the exit JButton is clicked
	{
		frame.dispose();
	}
	public Image getMyImage(String pictName) //returns image through a try catch statement
	{
		Image picture = null;
		try
		{
			picture = ImageIO.read(new File(pictName));//get picture from file
		}
		catch(IOException e)
		{
			System.err.println("cannot find file " + pictName + " to read.\n\n\n");//error message
			e.printStackTrace();
		}	
		return picture;
	}
	
}

/* This is the class for the start panel. It includes field variables for the components 
 * 1ike the a timer for the background animation for null layout and the text field for the user to
 * enter the name. The are actionListener classes to check if user has done an action
 * such as clicking a button proceeding to the next panel in the card layout. */
class StartPanel extends JPanel
{
    // Field variables for components in the start panel including JButtons and timers
    private JButton set, intro, facts, play, exit;
    private JTextField name; 
    private CardLayout cards;
    private NameThatNationHolder parent;
    private Information info;
    private double timeRemaining;
    private Timer timer;
    private int counter;
    private JLabel noName;
    // constructor used to mainly initialize field variables
    public StartPanel(NameThatNationHolder parentIn, CardLayout cardsIn, Information infoIn)
    {
        setLayout(null);
        parent = parentIn;
        cards = cardsIn;
        info = infoIn;
        name = new JTextField("Enter Your Name:");
        name.setFont(new Font("monospaced", Font.PLAIN, 20));
        set = new JButton("Settings");
        intro = new JButton("Instructions");
        facts = new JButton("Country Info");
        play = new JButton("Play");
        exit = new JButton("Exit");
        
        noName = new JLabel("Please Enter Your Name");
        noName.setForeground(Color.RED);
        noName.setFont(new Font("monospaced", Font.PLAIN, 30));
        noName.setBounds(300, 450, 450, 50);
        noName.setVisible(false);
        set.addActionListener(new buttons());
        intro.addActionListener(new buttons());
        facts.addActionListener(new buttons());
        
        play.addActionListener(new buttons());
        exit.addActionListener(new exitListener());
        set.setBounds(50, 400, 200, 20);
        intro.setBounds(710, 400, 200, 20);
        facts.setBounds(405, 250, 150, 20);
        play.setBounds(405, 280, 150, 20);
        name.setBounds(380, 425, 200, 25);
        
        exit.setBounds(25, 25, 100, 20);
        timeRemaining = 0.5;
        timer = new Timer(500, new TimerListener());
        timer.start();
        counter = 0;
        add(set);
        add(intro);
        add(facts);
        add(play);
        add(name);
        add(exit);
        add(noName);
    }
    // checks the time left to switch between images, creating an animation
    class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            timeRemaining -= 0.5;
            if (timeRemaining == 0)
            {
                repaint();                
                timeRemaining = 0.5;
            }
        }
    }    
    // paintComponent method for animations
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (counter == 0)
        {
            g.drawImage(parent.getMyImage("pictures/panelImages/World1.png"), 0, 0, 960, 540, this);
            counter = 1;
        }
        else if (counter == 1)
        {
            g.drawImage(parent.getMyImage("pictures/panelImages/World2.png"), 0, 0, 960, 540, this);
            counter = 2;
        }
        else if (counter == 2)
        {
            g.drawImage(parent.getMyImage("pictures/panelImages/World3.png"), 0, 0, 960, 540, this);
            counter = 0;
        }
    }
    // Used to check if Play button is clicked which will show game panels
    class buttons implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String which = ((JButton)e.getSource()).getText();
            
            if(which.equals("Play"))
            {
                if(name.getText().equals("Enter Your Name:") || name.getText().equals(""))
                {
					noName.setVisible(true);
				}
				else
				{
					info.setName(name.getText());
					noName.setVisible(false);
					GamePanel gp = new GamePanel(parent, cards, info);
					parent.add(gp, "Play");
					cards.show(parent, which);
				}
            }
            else
				cards.show(parent, which);
			if(!name.getText().equals("Enter Your Name:") && !name.getText().equals(""))
				noName.setVisible(false);
        }
    }
    // Class used to see if exit button clicked
    class exitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            String exited = ((JButton)evt.getSource()).getText();
            parent.closeWindow();
        }
    }
}

/* This panel is shown when the user clicks the instructions JButton. By clicking here:
 * an image of the gameplay will be shown as well as the instructions in a JScrollPane
 * so that the user understands how to play the game. */ 
class InstructionsPanel extends JPanel
{
	// field variables include cardlayout, instances of other classes for access, and components
	private NameThatNationHolder parent;
	private CardLayout cards;
	private JLabel intros;
	private JButton back;
	private JTextArea instructions;
	public InstructionsPanel(NameThatNationHolder parentIn, CardLayout cardsIn)
    {
        parent = parentIn;
        cards = cardsIn;
        setLayout(new BorderLayout());
        intros = new JLabel("Instructions");
        intros.setFont(new Font("monospaced", Font.BOLD, 30));
        JPanel intro = new JPanel();
        
        intro.add(intros);
        instructions = new JTextArea("Welcome to the instructions panel of NameThatNation!!! To play the game, follow these steps! \n" +
        "If you want to learn about the countries, go to the “CountriesInfo” button which will direct you to the location where you learn more about the countries. \n" +
        "First, you want to go to the settings panel and select the following (color of the background of your gameplay, the size of the image that is displayed, the continents that are in your gameplay which require at least 3, and lastly the difficulty of the levels). \n" +
        "If chosen easy, not that many countries will show and there will be five minutes to answer all the questions. If chosen medium, a little bit more countries will be shown in gameplay and four minutes to answer. " + 
        "If chosen difficult, all countries will be shown in gameplay with only three minutes. There are different power ups which you can use in your gameplay with the goal of maximizing the score. \n" +
        "In the beginning, all of the powerups are faded, meaning they can't be used. They are earned when you get a specific number of questions right in a row (which can only be activated one time only).\n" +
        "Freeze powerup adds 30 seconds to time and enabled when three questions are right in a row. Skip powerup allows to skip the powerup, which requires five questions in a row. Extra life "+
        "requires seven questions correct in a row, and can only be enabled if less than three lives are left. Lastly, the Double Points powerup requires 10 questions in a row and double the points per question. \n" +
        "At the start, every question the user gets right only awards user with 30 points, but every three that are right, 10 points is added to increment counter. User can click and release the right key or click the right button"+
        " to move to the next question. Hints can be used three times during gameplay, and if enabled and question was right, only 10 points will be added no matter increment counter. \n" +
        "After gameplay is done by either running out of time, all countries were answered, or all lives were used up,  user will move on to the correct panel. This is where you can learn what questions you got wrong. "+ 
        "You can move to the end panel, which has many different options such as checking the leaderboard or achievements. Going to the achievements panel would allow you to see what achievements you have unlocked and what to still try to earn!" +
        " Save your score and have fun!!! Good Luck!!!!\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        instructions.setLineWrap(true);
        instructions.setEditable(false);
        instructions.setWrapStyleWord(true);
        instructions.setFont(new Font("serif", Font.BOLD, 20));
        
        JScrollPane jsp = new JScrollPane(instructions, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        back = new JButton("Back");
        back.addActionListener(new button());
        JPanel buttHolder = new JPanel();
        buttHolder.setOpaque(false);
        buttHolder.add(back);
        jsp.setPreferredSize(new Dimension(400, 900));
        jsp.setOpaque(false);
        
        JPanel holder = new JPanel();
        holder.add(jsp);
        holder.setOpaque(false);
        intro.setOpaque(false);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(intro, BorderLayout.NORTH);
        add(holder, BorderLayout.EAST);
        add(buttHolder, BorderLayout.SOUTH);
    }
	//Draws background image
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/InstructionsBG.jpg"), 0, 0, 960, 540, this);
		g.drawImage(parent.getMyImage("pictures/panelImages/Game.png"), 25, 100, 500, 300, this);
	}
	//checks if button clicked to go back to start panel
	class button implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if((JButton)e.getSource() == back)
				cards.show(parent, "Start");
		}
	}
}

/* This class is for the settingsPanel. There are different components such as
 * JCheckBoxes for the user to choose continents (at least three) and also the color
 * of the background for gameplay in three JSliders that control the RGB values. 
 * The JScrollBar can be used to change the size of the image. There are also
 * menu items in a menu bar that can be used to select the difficulty of the level. */
class SettingsPanel extends JPanel
{
	// Field variables include JSlider values, menu bars, instances of other classes, etc.
	private JSlider redAmount;
	private JSlider greenAmount;
	private JSlider blueAmount;
	private CardLayout cards;
	private NameThatNationHolder ntnh;
	private JCheckBox Antarctica; 
	private JCheckBox Australia;
	private JCheckBox SouthAmerica;
	private JCheckBox Africa;
	private JCheckBox Asia;
	private JCheckBox NorthAmerica;
	private JCheckBox Europe; 
	private JMenu difficultyMenu;
	private JMenuItem easyItem, mediumItem, hardItem;
	private JMenuBar difficulty;
	private JScrollBar imageSize;
	private int red, green, blue;
	private JCheckBox[] continentButtons;
	private int sizeChange;
	private JButton backButton;
	private JLabel settings;
	private Information info;
	private String nameDifficulty;
	public SettingsPanel(NameThatNationHolder panelCardsIn, CardLayout cardsIn, Information infoIn)
	{
		setLayout(new BorderLayout(2, 1));
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.setLayout(new GridLayout(1, 2));
		JPanel buttonsAndScroll = new JPanel();
		JPanel slidermenunext = new JPanel();
		slidermenunext.setOpaque(false);
		
		JPanel labelpane = new JPanel();
		labelpane.setOpaque(false);
		settings = new JLabel("Settings");
		settings.setFont(new Font("monospaced", Font.BOLD, 30));
		settings.setForeground(Color.WHITE);
		labelpane.add(settings);
		add(labelpane, BorderLayout.NORTH);
		
		buttonsAndScroll.setLayout(new GridLayout(2, 1));
		JPanel checkboxPane = new JPanel();
		checkboxPane.setOpaque(false);
		checkboxPane.setLayout(new GridLayout(1, 2));
		
		JPanel checkboxLeft = new JPanel();
		JPanel checkboxRight = new JPanel();
		checkboxLeft.setOpaque(false);
		checkboxRight.setOpaque(false);
		checkboxLeft.setLayout(new GridLayout(4, 1));
		checkboxRight.setLayout(new GridLayout(4, 1));
		
		JLabel warning = new JLabel("Choose 3 Continents before going back!");
		warning.setForeground(Color.RED);
		slidermenunext.setLayout(new GridLayout(2, 1));
		
		ntnh = panelCardsIn;
		cards = cardsIn;
		info = infoIn;
		redAmount = new JSlider(0, 255, 173);
		redAmount.setForeground(Color.WHITE);
		greenAmount = new JSlider(0, 255, 216);
		greenAmount.setForeground(Color.WHITE);
		blueAmount = new JSlider(0, 255, 230);
		blueAmount.setForeground(Color.WHITE);
		
		red = 173;
		green = 216;
		blue = 230;
		info.setColor(red, green, blue);
		
		Antarctica = new JCheckBox("Antarctica");
		Australia = new JCheckBox("Australia/Oceania");
		SouthAmerica = new JCheckBox("South America");
		Africa = new JCheckBox("Africa");
		Asia = new JCheckBox("Asia");
		NorthAmerica = new JCheckBox("North America");
		Europe = new JCheckBox("Europe");
		
		checkboxLeft.add(Antarctica);
		checkboxLeft.add(Australia);
		checkboxLeft.add(SouthAmerica);
		checkboxLeft.add(Africa);
		checkboxRight.add(Asia);
		checkboxRight.add(NorthAmerica);
		checkboxRight.add(Europe);
		checkboxRight.add(warning);
		
		checkboxPane.add(checkboxLeft);
		checkboxPane.add(checkboxRight);
		continentButtons = new JCheckBox[]{Antarctica, Australia, SouthAmerica, Africa, Asia, NorthAmerica, Europe};

		redAmount.addChangeListener(new ColorHandler());
		redAmount.setOpaque(false);
		
		greenAmount.addChangeListener(new ColorHandler());
		greenAmount.setOpaque(false);
		blueAmount.addChangeListener(new ColorHandler());
		blueAmount.setOpaque(false);
		
		greenAmount.setMajorTickSpacing(25);
		greenAmount.setPaintTicks(true);
		greenAmount.createStandardLabels(25);
		greenAmount.setPaintLabels(true);
		greenAmount.setOrientation(JSlider.HORIZONTAL);
		
		redAmount.setMajorTickSpacing(25);
		redAmount.setPaintTicks(true);
		redAmount.createStandardLabels(25);
		redAmount.setPaintLabels(true);
		redAmount.setOrientation(JSlider.HORIZONTAL);
		
		blueAmount.setMajorTickSpacing(25);
		blueAmount.setPaintTicks(true);
		blueAmount.createStandardLabels(25);
		blueAmount.setPaintLabels(true);
		blueAmount.setOrientation(JSlider.HORIZONTAL);
		
		JPanel sliderPane = new JPanel();
		sliderPane.setOpaque(false);
		sliderPane.setLayout(new GridLayout(6, 1));
		JLabel redLabel = new JLabel("Change Red Amount");
		JLabel greenLabel = new JLabel("Change Green Amount");
		JLabel blueLabel = new JLabel("Change Blue Amount");
		
		redLabel.setForeground(Color.WHITE);
		greenLabel.setForeground(Color.WHITE);
		blueLabel.setForeground(Color.WHITE);
		sliderPane.add(redLabel);
		sliderPane.add(redAmount);
		sliderPane.add(greenLabel);
		sliderPane.add(greenAmount);
		sliderPane.add(blueLabel);
		sliderPane.add(blueAmount);
		
		JPanel menuandbutton = new JPanel();
		menuandbutton.setOpaque(false);
		menuandbutton.setLayout(new GridLayout(2, 1));
		slidermenunext.add(sliderPane);
		
		Antarctica.addActionListener(new CheckBoxHandler());
		Antarctica.setOpaque(false);
		Antarctica.setForeground(new Color(183, 9, 9));
		Australia.addActionListener(new CheckBoxHandler());
		Australia.setOpaque(false);
		Australia.setForeground(new Color(183, 9, 9));
		SouthAmerica.addActionListener(new CheckBoxHandler());
		SouthAmerica.setOpaque(false);
		SouthAmerica.setForeground(new Color(183, 9, 9));
		Africa.addActionListener(new CheckBoxHandler());
		Africa.setOpaque(false);
		Africa.setForeground(new Color(183, 9, 9));
		Asia.addActionListener(new CheckBoxHandler());
		Asia.setOpaque(false);
		Asia.setForeground(new Color(183, 9, 9));
		NorthAmerica.addActionListener(new CheckBoxHandler());
		NorthAmerica.setOpaque(false);
		NorthAmerica.setForeground(new Color(183, 9, 9));
		Europe.addActionListener(new CheckBoxHandler());
		Europe.setOpaque(false);
		Europe.setForeground(new Color(183, 9, 9));
		
		Antarctica.setEnabled(true);
		Australia.setEnabled(true);
		SouthAmerica.setEnabled(true);
		Africa.setEnabled(true);
		Asia.setEnabled(true);
		NorthAmerica.setEnabled(true);
		Europe.setEnabled(true);
		Antarctica.setSelected(true);
		Australia.setSelected(true);
		SouthAmerica.setSelected(true);
		Africa.setSelected(true);
		Asia.setSelected(true);
		NorthAmerica.setSelected(true);
		Europe.setSelected(true);
		
		easyItem = new JMenuItem("Easy");
		mediumItem = new JMenuItem("Medium");
		hardItem = new JMenuItem("Hard");
		centerPanel.add(slidermenunext);
		centerPanel.add(buttonsAndScroll);
		
		easyItem.addActionListener(new MenuHandler());
		mediumItem.addActionListener(new MenuHandler());
		hardItem.addActionListener(new MenuHandler());
		
		difficultyMenu = new JMenu("            Choose Difficulty          ");
		difficultyMenu.setFont(new Font("serif", Font.BOLD, 30));
		difficulty = new JMenuBar();
		difficultyMenu.add(easyItem);
		difficultyMenu.add(mediumItem);
		difficultyMenu.add(hardItem);
		
		difficulty.add(difficultyMenu);
		imageSize = new JScrollBar(JScrollBar.HORIZONTAL, 2, 1, 1, 3);
		imageSize.addAdjustmentListener(new AdjustmentHandler());
		imageSize.setPreferredSize(new Dimension(300, 100));
		backButton = new JButton("Back");
		backButton.addActionListener(new ButtonListener());
		buttonsAndScroll.setOpaque(false);
		buttonsAndScroll.add(checkboxPane);
		
		JPanel scrollandlabel = new JPanel();
		JPanel scrollpane = new JPanel();
		scrollandlabel.setOpaque(false);
		scrollpane.add(imageSize);
		scrollpane.setOpaque(false);
		scrollandlabel.setLayout(new BorderLayout(1, 2));
		JPanel labelPane = new JPanel();
		labelPane.setOpaque(false);
		JLabel imageLabel = new JLabel("Choose size of your image!");
		labelPane.add(imageLabel);
		imageLabel.setFont(new Font("serif", Font.BOLD, 20));
		imageLabel.setForeground(Color.RED);
		
		scrollandlabel.add(labelPane, BorderLayout.NORTH);
		scrollandlabel.add(scrollpane, BorderLayout.CENTER);
		buttonsAndScroll.add(scrollandlabel);
		
		JPanel menuPane = new JPanel();
		menuPane.add(difficulty);
		menuPane.setOpaque(false);
		menuandbutton.add(menuPane);
		JPanel buttonPane = new JPanel();
		buttonPane.add(backButton);
		buttonPane.setOpaque(false);
		backButton.setPreferredSize(new Dimension(100, 50));
		menuandbutton.add(buttonPane);
		
		slidermenunext.add(menuandbutton);
		difficulty.setPreferredSize(new Dimension(400, 100));
		nameDifficulty = "Easy";
	}
	//Draw background image
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(ntnh.getMyImage("pictures/panelImages/SettingsBackground.png"), 0, 0, 960, 540, this);
	}
	/* This class checks the difficulty selected through the action done */
	class MenuHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			String changed = evt.getActionCommand();
			if (changed.equals("Easy"))
			{
				nameDifficulty = "Easy";
			}
			else if (changed.equals("Medium"))
			{	
				nameDifficulty = "Medium";
			}
			else
			{	
				nameDifficulty = "Hard";
			}
			info.setFile(nameDifficulty);
		}
	}
	// Class used to get value from the JSliders
	class ColorHandler implements ChangeListener
	{
		public void stateChanged(ChangeEvent evt)
		{
			red = redAmount.getValue();
			green = greenAmount.getValue();
			blue = blueAmount.getValue();
			info.setColor(red, green, blue);
		}
	}
	// Class used to check which JCheckBox was selected for continents to include
	class CheckBoxHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			for (int i = 0; i < continentButtons.length; i++)
			{
				if (continentButtons[i].isSelected())
					info.setContinent(i, true);
				else
					info.setContinent(i, false);
			}
		}
	}
	// Used to check the image size set for gameplay
	class AdjustmentHandler implements AdjustmentListener
	{
		public void adjustmentValueChanged(AdjustmentEvent evt)
		{
			sizeChange = evt.getValue();
			info.setImageSize(sizeChange);
		}
	}
	// Used to check if back button was clicked making sure at least three continents were selected
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			int count = 0;
			for (int a = 0; a < 7; a ++)
			{
				if(continentButtons[a].isSelected())
					count += 1;
			}
			if((JButton)evt.getSource() == backButton && count >= 3)
				cards.show(ntnh, "Start");
		}
	}
}

/* This class uses a try-catch to access the hard file
 * whic contains all the information about the countries
 * and takes this information and puts it in a JScrollPane. There 
 * is also a picture of Earth on the left side. */ 
class CountryInfoPanel extends JPanel
{
	private NameThatNationHolder parent;
	private CardLayout cards;
	private JLabel conInfo;
	private JButton proceed;
	private JScrollPane infos;
	public CountryInfoPanel(NameThatNationHolder parentIn, CardLayout cardsIn)
	{
		setLayout(new BorderLayout());
		parent = parentIn;
		cards = cardsIn;
		conInfo = new JLabel("Country Info");
		conInfo.setFont(new Font("monospaced", Font.BOLD, 30));
		proceed = new JButton("Back");
		
		proceed.addActionListener(new next());
		JTextArea cons = new JTextArea(fileIn("files/Read/countriesContinentHard.txt"));
		cons.setLineWrap(true);
		cons.setEditable(false);
		cons.setWrapStyleWord(true);
		cons.setFont(new Font("monospaced", Font.PLAIN, 15));
		infos = new JScrollPane(cons);
		infos.setOpaque(false);
		infos.setPreferredSize(new Dimension(400, 1000));
		
		JPanel butHolder = new JPanel();
		butHolder.add(proceed);
		butHolder.setOpaque(false);
		
		JPanel scroller = new JPanel();
		scroller.add(infos);
		scroller.setOpaque(false);
		JPanel conHolder = new JPanel();
		conHolder.add(conInfo);
		conHolder.setOpaque(false);
		add(conHolder, BorderLayout.NORTH);
		add(scroller, BorderLayout.EAST);
		add(butHolder, BorderLayout.SOUTH);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/CountryInfoBG.jpg"), 0, 0, 960, 540, this);
		g.drawImage(parent.getMyImage("pictures/panelImages/Earth.png"), 70, 85, 350, 350, this);
	}
	// class to check if next button clicked
	class next implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String which = ((JButton)e.getSource()).getText();
			if(which.equals("Back"))
				cards.show(parent, "Start");
		}
		
	}
	// Used to read in files (try-catch) and accessed in other classes too
	public String fileIn(String fileName)
	{
		String read = "";
		Scanner country = null;
		try
		{
			country = new Scanner(new File(fileName)); 
		}
		catch(FileNotFoundException e)
		{
			System.err.println("cannot find file " + fileName + " to read.\n\n\n"); //error message
			e.printStackTrace();
		}
		while(country.hasNextLine())
		{
			read += country.nextLine() + "\n";
		}
		country.close();
		return read;
	}
}	
/* This is the main class for the gameplay. There are lots of components and even
 * arrays that are specifically used for the storing of the names of the countries
 * and the values to decide on what continents to read from the file. There 
 * are also JButtons for the user to select what they think the answer is based 
 * on an image. */
class GamePanel extends JPanel implements KeyListener, MouseListener
{
	private NameThatNationHolder parent;
	private CardLayout cards;
	private Information info;
	private ArrayList<String> countries;
	private String diff;
	private boolean[] getContinents;
	private boolean[] getCountries;
	private Timer timer;
	private JLabel timeLabel, whatNation, manyPoints, hintsLeft, livesLeft;
	private int timeRemaining;
	private JButton exit, op1, op2, op3, op4, useHint;
	private JButton[] bArray;
	private int count;
	private int questionNumber;
	private int num1, num2, num3, order, bCount;
	private JButton bNext;
	private Image[] powers;
	private boolean[] powerindex;
	private boolean[] set;
	private boolean[] firstTime;
	private int counts;
	private int points;
	private int tPoints;
	private int hints;
	private int lives;
	private int cStreak;
	private boolean[] hOrder;
	private boolean[] lOrder;
	private boolean answered;
	private boolean usedHint;
	private boolean freezeCheck;
	private int freezeTimeRemaining;
	private Timer freezeTimer;
	private boolean doubleEnabled;
	private hintsPanel HP;
	private ArrayList<String> conContinent;
	private ArrayList<String> diffCon;
	private boolean isDiff;
	private boolean useAny;
	private int timePassed;
	private boolean useSkip;
	private JLabel completion;
	private JLabel isRight;
	
	public GamePanel(NameThatNationHolder panelCardsIn, CardLayout cardsIn, Information infoIn)
	{
		setLayout(null);
		parent = panelCardsIn;
		cards = cardsIn;
		info = infoIn;
		isDiff = true;
		points = 30;
		tPoints = 0;
		counts = 0;
		hints = 3;
		lives = 3;
		cStreak = 1;
		completion = new JLabel("Progress: 0%");
		isRight = new JLabel("");
		
		usedHint = false;
		useAny = false;
		freezeTimeRemaining = 0;
		freezeCheck = false;
		freezeTimer = new Timer(1000, new freezeTimerListener());
		hintsLeft = new JLabel("Hints: ");
		livesLeft = new JLabel("Lives: ");
		useHint = new JButton("Hint");
		
		hintsLeft.setBounds(650, 150, 100, 75);
		livesLeft.setBounds(650, 260, 100, 75);
		timePassed = 0;
		useHint.addActionListener(new hListener());
		useHint.setBounds(700, 150, 68, 30);
		add(hintsLeft);
		add(useHint);
		add(livesLeft);
		answered = false;
		hOrder = new boolean[] {true, true, true};
		lOrder = new boolean[] {true, true, true};
		
		op1 = new JButton();
		op2 = new JButton();
		op3 = new JButton();
		op4 = new JButton();
		bNext = new JButton("Next");
		bNext.addActionListener(new nListener());
		bNext.setBounds(800, 100, 75, 25);
		doubleEnabled = false;
		useSkip = false;
		set = new boolean[] {false, false, false, false};
		firstTime = new boolean[] {false, false, false, false};
		manyPoints = new JLabel("Points: 0");
		manyPoints.setFont(new Font("Serif", Font.PLAIN, 20));
		manyPoints.setBounds(750, 0, 400, 75);
		add(manyPoints);
		add(bNext);
		
		op1.addActionListener(new bListener());
		op2.addActionListener(new bListener());
		op3.addActionListener(new bListener());
		op4.addActionListener(new bListener());
		diff = "files/Read/countriesContinent" + info.getFileName() + ".txt";
		exit = new JButton("Quit");
		exit.addActionListener(new exitListener());
		whatNation = new JLabel("What Nation is This?");
		whatNation.setFont(new Font("serif", Font.PLAIN, 40));
		completion.setBounds(50, 100, 200, 50);
		isRight.setBounds(50, 200, 200, 50);
		
		completion.setFont(new Font("monospaced", Font.BOLD, 20));
		isRight.setFont(new Font("monospaced", Font.BOLD, 30));
		isRight.setVisible(false);
		exit.setBounds(700, 100, 75, 25);
		whatNation.setBounds(300, -75, 500, 200);
		add(exit);
		add(whatNation);
		bArray = new JButton[] {op1, op2, op3, op4};
		countries = new ArrayList<>();
		diffCon = new ArrayList<>();
		getContinents = info.getContinent();
		timeRemaining = 180;
		conContinent = new ArrayList<>();
		if(info.getFileName().equals("Easy"))
			timeRemaining = 300;
		else if (info.getFileName().equals("Medium"))
			timeRemaining = 240;
		
		timeLabel = new JLabel(formatTime(timeRemaining), JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 25));
		timeLabel.setBounds(10, 10, 200, 100);
		add(timeLabel);
		timer = new Timer(1000, new TimerListener());
        timer.start();
		
		op1.setBounds(25, 400, 200, 75);
		op2.setBounds(250, 400, 200, 75);
		op3.setBounds(475, 400, 200, 75);
		op4.setBounds(700, 400, 200, 75);
		add(op1);
		add(op2);
		add(op3);
		add(op4);
		add(completion);
		add(isRight);
		setFocusable(true);	
		addKeyListener(this);
		addMouseListener(this);
		powers = new Image [] {parent.getMyImage("pictures/panelImages/DoublePoints.png"), parent.getMyImage("pictures/panelImages/ExtraHeart.png"), parent.getMyImage("pictures/panelImages/FreezeTime.png"), parent.getMyImage("pictures/panelImages/Skip.png"), parent.getMyImage("pictures/panelImages/DoublePointsFade.png"), parent.getMyImage("pictures/panelImages/ExtraHeartFade.png"), parent.getMyImage("pictures/panelImages/FreezeTimeFade.png"), parent.getMyImage("pictures/panelImages/SkipFade.png")};
		powerindex = new boolean[] {true, true, true, true, false, false, false, false};
		HP = new hintsPanel();
		playGame();
	}
	// Checks if the hint button is clicked, working with the hintsPanel class to show
	// the new window pop up where the hints will be displayed
	class hListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			hOrder[hints - 1] = false;
			HP.redoHints(questionNumber, countries);
			HP.returnFrame().setVisible(true);
			useHint.setEnabled(false);
			repaint();
			hints --;
			usedHint = true;
			useAny = true;
		}
	}
	// Checks the time left, with if else statement for changing time value showed
	class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			timeRemaining -= 1;
			timePassed += 1;
			info.setRemaining(timePassed);
			if (timeRemaining <= 30)
			{
				timeLabel.setForeground(Color.RED);
			}
			timeLabel.setText(formatTime(timeRemaining));
			if(timeRemaining == 0 || lives == 0 || count == 0)
			{
				timer.stop();
				timeLabel.setText("Game over!");
				bNext.setEnabled(true);
				for (int k = 0; k < 4; k++)
				{
					bArray[k].setEnabled(false);
				}
				if(!answered)
					info.incorrectAnswers(countries.get(questionNumber));
			}
		}
	}
	// class to check for freeze timer powerup
	class freezeTimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			freezeTimeRemaining -= 1;
			if (freezeTimeRemaining == 0)
			{
				freezeTimer.stop();
				freezeCheck = false;
				repaint();
			}
		}
	}
	// check for JButton exit clicked
	class exitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			info.reset();
			cards.show(parent, "Start");
		}
	}	
	// mainly used to fomat the timer that is shown to user
	public String formatTime(int totalSeconds) 
	{
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
	// includes if statements for which power up image to show, as well as country image shown
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(info.getColor());
        int x1 = 700;
        int x2 = 700;
        if (freezeCheck)
		{
			g.drawImage(parent.getMyImage("pictures/panelImages/freezeImage.png"), 0, 0, 960, 540, this);
		}
        Image temp = parent.getMyImage("pictures/countryImages/" + countries.get(questionNumber) + ".jpg");
        g.drawImage(temp, 480-temp.getWidth(null)*info.getImageSize()/6, 200-temp.getHeight(null)*info.getImageSize()/6,
        temp.getWidth(null)*info.getImageSize()/3, temp.getHeight(null)*info.getImageSize()/3, this); 
		g.drawImage(powers[4], 20, 300, 100, 100, this);
		g.drawImage(powers[5], 100, 300, 100, 100, this);
		g.drawImage(powers[6], 180, 300, 100, 100, this);
		g.drawImage(powers[7], 260, 300, 100, 100, this);
        if (set[0] && firstTime[0])
        {
			if (powerindex[0])
				g.drawImage(powers[0], 20, 300, 100, 100, this);
			else if (powerindex[4])
				g.drawImage(powers[4], 20, 300, 100, 100, this);
		}
		if (set[1] && firstTime[1])
		{
			if (powerindex[1])
				g.drawImage(powers[1], 100, 300, 100, 100, this);
			else if (powerindex[5])
				g.drawImage(powers[5], 100, 300, 100, 100, this);
		}
		if (set[2] && firstTime[2])
		{	
			if (powerindex[2])
				g.drawImage(powers[2], 180, 300, 100, 100, this);
			else if (powerindex[6])
				g.drawImage(powers[6], 180, 300, 100, 100, this);
		}
		if (set[3] && firstTime[3])
		{
			if (powerindex[3])
				g.drawImage(powers[3], 260, 300, 100, 100, this);
			else if (powerindex[7])
				g.drawImage(powers[7], 260, 300, 100, 100, this);
		}
		for (int a = 0; a < 3; a ++)
		{
			if(hOrder[a])
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.fillOval(x1, 190, 30, 30);
			x1 += 50;
		}
		for (int b = 0; b < 3; b ++)
		{
			if(lOrder[b])
				g.setColor(Color.RED);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.fillOval(x2, 300, 30, 30);
			x2 += 50;
		}
    }
    // Generate the question with loops to make sure
    // none of the countries are repeated
    public void genQuestion()
    {
		isRight.setVisible(false);
		if(count <= getCountries.length)
			getCountries[questionNumber] = false; //mark as used
		count--;
		HP.returnFrame().setVisible(false);
		if(hints > 0)
			useHint.setEnabled(true);
		bNext.setEnabled(false);
		answered = false;
		usedHint = false;
		for (int k = 0; k < 4; k++)
		{
			bArray[k].setBackground(Color.LIGHT_GRAY);
			bArray[k].setEnabled(true);
		}
		questionNumber = (int)(Math.random() * getCountries.length);
		while (!getCountries[questionNumber]) 
		{
			questionNumber = (int)(Math.random() * getCountries.length);
		}
		do 
		{
			num1 = (int)(Math.random() * getCountries.length);
		} while (num1 == questionNumber);

		do 
		{
			num2 = (int)(Math.random() * getCountries.length);
		} while (num2 == questionNumber || num2 == num1);

		do 
		{
			num3 = (int)(Math.random() * getCountries.length);
		} while (num3 == questionNumber || num3 == num1 || num3 == num2);

		for (int i = 0; i < 4; i++) 
		{
			bArray[i].setText("");
		}

		int[] countryIndices = {questionNumber, num1, num2, num3};
		boolean[] usedSlots = new boolean[4];

		for (int i = 0; i < 4; i++) 
		{
			boolean placed = false;
			while (!placed) 
			{
				order = (int)(Math.random() * 4);
				if (!usedSlots[order]) 
				{
					bArray[order].setText(countries.get(countryIndices[i]));
					usedSlots[order] = true;
					placed = true;
				}
			}
		}
			
		repaint();
	}
    // Try catch for file reading (hard file) and later stored in the new window
    // pop-up through a while loop using hasNext()
    public void playGame()
    {
		Scanner sc = null;
		File inFile = new File(diff);
		String currentContinent = "";
		String currentCountry = "";
		int continentIndex = 0;
		try
		{
			sc = new Scanner(inFile);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Cannot find " + diff + " file.");
			System.exit(1);
		}
		String line;
		while (sc.hasNext())
		{
			line = sc.nextLine();
			if (line.indexOf(" - Continent") != -1)
			{
				currentContinent = line.substring(0, line.indexOf(" -"));
				continentIndex = getContinentIndex(currentContinent);	
			}
			if (line.indexOf("Country:") != -1 && getContinents[continentIndex])
			{
				currentCountry = line.substring(line.indexOf(" ")+1);
				countries.add(currentCountry);
				conContinent.add(currentContinent);
			}
		}
		if (getContinents[0])
		{
			countries.add("Antarctica");
			conContinent.add("Antarctica");
		}

		getCountries = new boolean [countries.size()];
		for (int i = 0; i < getCountries.length; i++)
		{
			getCountries[i] = true;
		} 
		count = getCountries.length + 1;
		genQuestion();
		sc.close();
	}
	// checks the country button clicked by the user and if it is correct or not
	// adding to the streak for powerup implementation
	class bListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String option = ((JButton) e.getSource()).getText();
			info.setTotal();
			answered = true;
			isDiff = true;
			info.setPercent((getCountries.length-(count-1))*100/getCountries.length);
			completion.setText("Progress: " + info.returnPercent() + "%");		
			for (int b = 0; b < 4; b ++)
			{
				if(bArray[b].getText().equals(countries.get(questionNumber)))
					bArray[b].setBackground(Color.GREEN);
				else
					bArray[b].setBackground(Color.RED);
				bArray[b].setEnabled(false);
			}
			requestFocusInWindow();
			if (option.equals(countries.get(questionNumber)))
			{
				isRight.setForeground(new Color(75, 193, 108));
				isRight.setText("Correct!");
				info.setEarn(0);
				info.setCorrect();
				if (usedHint)
				{
					tPoints += 10;
					info.setTime(0);
					info.correctAnswers(5);
				}
				else if (freezeTimeRemaining > 0)
				{
					info.correctAnswers(2);
					tPoints += points;
				}
				else if (doubleEnabled)
				{
					info.correctAnswers(4);
					tPoints += points;
					
				}
				else if (!usedHint)
				{
					info.correctAnswers(0);
					tPoints += points;
				}
				if(!usedHint)
				{
					info.setTime(cStreak);
				}
				counts++;
			}
			else if (!option.equals(countries.get(questionNumber)))
			{
				isRight.setForeground(Color.RED);
				isRight.setText("Incorrect!");
				info.correctAnswers(1);
				counts = 0;
				lOrder[lives-1] = false;
				lives --;
				info.incorrectAnswers(countries.get(questionNumber));
				repaint();
				
			}
			if (counts == 10 && !firstTime[0])
			{
				set[0] = true;
				firstTime[0] = true;
				info.setEarn(2);
				repaint();
			}
			if (counts == 7 && !firstTime[1])
			{
				set[1] = true;
				firstTime[1] = true;
				repaint();
			}
			if (counts == 3 && !firstTime[2])
			{
				set[2] = true;
				firstTime[2] = true;
				repaint();
			}
			if (counts == 5 && !firstTime[3])
			{
				set[3] = true;
				firstTime[3] = true;
				repaint();
			}
			if (counts%3 == 0 && counts > 0 && !usedHint)
			{
				points += 10;
				cStreak += 1;
				info.setStreak(points);
			}
			manyPoints.setText("Points: " + tPoints);
			isRight.setVisible(true);
			bNext.setEnabled(true);
			useHint.setEnabled(false);
			if (usedHint)
			{
				usedHint = false;
			}
			if (count == 1)
			{
				getCountries[questionNumber] = false; //mark as used
				count--;
				info.setPercent(100);
			}
			if (info.getDone() - info.returnIncorrects().size() == 5)
				info.setEarn(1);
			if (info.getCorrect() == 50)
				info.setEarn(3);
			for (int i = 0; i < diffCon.size(); i ++)
			{
				if(conContinent.get(questionNumber).equals(diffCon.get(i)))
					isDiff = false;
			}
			if (isDiff)
				diffCon.add(conContinent.get(questionNumber));
			if (diffCon.size() == 7)
				info.setEarn(4);
			if (info.getDone() - info.returnIncorrects().size() == 50 && info.getFileName().equals("Hard"))
				info.setEarn(14);
			if (counts == 25)
				info.setEarn(15);
			if (!useSkip && counts == countries.size())
				info.setEarn(16);
		}
	}
	// class implementing ActionListener purpose to call method
	// to generate question
	class nListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(lives == 0 || count == 0 || timeRemaining == 0)
			{
				if(!useAny)
					info.setEarn(11);
				if(timePassed <= 60)
					info.setEarn(12);
				info.setP(tPoints);
				CorrectPanel cp = new CorrectPanel(parent, cards, info);
				parent.add(cp, "Correct");
				cards.show(parent, "Correct");
			}
			else
				genQuestion();
		}
	} 
	// method returns the index necessary for boolean array checking
	public int getContinentIndex(String name)
	{
		if (name.equals("Antarctica"))
			return 0;
		else if (name.equals("Australia/Oceania"))
			return 1;
		else if (name.equals("South America"))
			return 2;
		else if (name.equals("Africa"))
			return 3;
		else if (name.equals("Asia"))
			return 4;
		else if (name.equals("North America"))
			return 5;
		else if (name.equals("Europe"))
			return 6;
		else
			return -1;
	}
	// Checks if right arrow is pressed on keyboard to move to next question
	public void keyReleased(KeyEvent evt)
	{
		int keyCode = evt.getKeyCode();
		if(keyCode == KeyEvent.VK_RIGHT)
		{
			if(lives == 0 || count == 0 || timeRemaining == 0)
			{
				if(!useAny)
					info.setEarn(11);
				if(timePassed <= 60)
					info.setEarn(12);
				info.setP(tPoints);
				CorrectPanel cp = new CorrectPanel(parent, cards, info);
				parent.add(cp, "Correct");
				cards.show(parent, "Correct");
			}
			else if(answered)
			{
				genQuestion();
			}
		}
	}
	// Checks specific location of mouse, activating a powerup with necessary
	public void mousePressed(MouseEvent evt) //mouse method check
	{
		int xMouse = evt.getX(); //get x
		int yMouse = evt.getY(); //get y
		if(count > 0 && timeRemaining > 0 && lives > 0)
		{
			if (20 <= xMouse && xMouse <= 120 && 300 <= yMouse && yMouse <= 400 && set[0] && powerindex[0]) //if power up clicked
			{
				powerindex[0] = false;
				powerindex[4] = true;
				points *= 2; //doubles points added
				cStreak += 1;
				info.setStreak(cStreak);
				doubleEnabled = true;
				info.setEarn(5);
				info.setEarn(8);
				useAny = true;
				repaint();
			}
			if (100 <= xMouse && xMouse <= 200 && 300 <= yMouse && yMouse <= 400 && set[1] && powerindex[1]) //if power up clicked
			{
				if(powerindex[1] && lives <= 2)
				{
					lives += 1;
					lOrder[lives-1] = true;
					powerindex[1] = false;
					powerindex[5] = true;
					info.setEarn(9);
					useAny = true;
				}
				repaint();
			}
			if (180 <= xMouse && xMouse <= 280 && 300 <= yMouse && yMouse <= 400 && set[2] && powerindex[2]) //if power up clicked
			{
				powerindex[2] = false;
				powerindex[6] = true;
				timeRemaining += 30;
				freezeCheck = true;
				changeFreezeTime();
				info.setEarn(5);
				info.setEarn(6);
				useAny = true;
				repaint();
			}
			if (260 <= xMouse && xMouse <= 360 && 300 <= yMouse && yMouse <= 400 && set[3] && powerindex[3]) //if power up clicked
			{
				powerindex[3] = false;
				powerindex[7] = true;
				info.correctAnswers(3);
				info.setEarn(5);
				info.setEarn(7);
				useAny = true;
				useSkip = true;
				answered = true;
				if(count > 1)
				{
					genQuestion();
					info.setPercent((getCountries.length-(count))*100/getCountries.length);
					completion.setText("Progress: " + info.returnPercent() + "%");		
					repaint();
				}
				else
				{
					if(timePassed <= 60) //check time left
						info.setEarn(12);
					info.setPercent(100);
					info.setP(tPoints);
					CorrectPanel cp = new CorrectPanel(parent, cards, info); //instance of new class
					parent.add(cp, "Correct");
					cards.show(parent, "Correct");
				}
				
			}
			if(info.returnEarn()[6] && info.returnEarn()[7] && info.returnEarn()[8] && info.returnEarn()[9])
				info.setEarn(10);
			requestFocusInWindow(); //need for keyboard checking
		}
	}
	// change the time left for the freeze time powerup
	public void changeFreezeTime()
	{
		freezeTimeRemaining = 30;
		freezeTimer.start();
	}
	public void keyPressed(KeyEvent evt){} //override
	public void keyTyped(KeyEvent evt){} //override
	public void mouseClicked(MouseEvent evt) {} //override
	public void mouseReleased(MouseEvent evt) {} //override
	public void mouseEntered(MouseEvent evt) {} //override
	public void mouseExited(MouseEvent evt) {} //override
	public void mouseMoved(MouseEvent evt) {} //override
	public void mouseDragged(MouseEvent evt) {} //override
	
}
// Class used to display new JFrame window for hints
class hintsPanel
{
	private hintsHolder hh;
	private JFrame frame;
	public hintsPanel()
	{
		frame = new JFrame("Hints");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setResizable(true);
		hh = new hintsHolder();
        frame.getContentPane().add(hh);
        frame.setVisible(false);

	}
	public void redoHints(int numQ, ArrayList<String> conIn)
	{
		hh.getHint(numQ, conIn);
	}
	public JFrame returnFrame()
	{
		return frame;
	}
}
// Works with hintsPanel but adds in the components for the hintsPanel JFrame
class hintsHolder extends JPanel
{
	private JTextArea hText;
	public hintsHolder()
	{
		hText = new JTextArea("");
		hText.setFont(new Font("serif", Font.PLAIN, 15));
		JScrollPane holder = new JScrollPane(hText);
		holder.setPreferredSize(new Dimension(450, 300));
		hText.setFont(new Font("serif", Font.PLAIN, 15));
		hText.setLineWrap(true);
		hText.setEditable(false);
		hText.setWrapStyleWord(true);
		add(holder);
	}
	// Gets the specific hints needed to user making sure answer isn't revealed
	public void getHint(int qIn, ArrayList<String> countriesIn)
	{
		String result = "";
		String[] newStored = new String[] {"Continent:", "Languages Spoken:", "Famous Landmarks:", "Culture Description:","Geography:", "Other:"};
		String line = "";
		Scanner kb = null;
		boolean bad = false;
		try
		{
			kb = new Scanner(new File("Files/Read/countriesContinentHard.txt"));
		}
		catch(IOException e)
		{
			System.err.println("Cannot find hints file to read");
			e.printStackTrace();
		}

		while(kb.hasNext() && !line.equals("Country: " + countriesIn.get(qIn)) && !line.equals("Continent: Antarctica"))
		{
			line = kb.nextLine();
		}
		while(line.indexOf("Other:") == -1)
		{
			line = kb.nextLine();
			for (int a = 0; a < 6; a++)
			{
				if(line.indexOf(newStored[a]) == 0)
					if(a == 3 || a == 5)
						bad = true;
					else
						bad = false;
						
			}
			if(!bad)
				result += line + "\n";
		}
		kb.close();
		hText.setText(result);
	}
}
// Class for the correct panel showing user the questions that they got wrong
class CorrectPanel extends JPanel implements ActionListener
{
    private ArrayList <Integer> corrects; //arraylist used as size is changed depending on questions answered
    private NameThatNationHolder parent;
    private Information info;
    private CardLayout cards;
    private JButton nextButton;
    private ArrayList <String> incorrectQuestions;
    private JLabel correctLabel;
    private JLabel howMuch;
    public CorrectPanel(NameThatNationHolder parentIn, CardLayout cardsIn, Information infoIn)
    {
        setLayout(new BorderLayout(5, 0));
        parent = parentIn;
        cards = cardsIn;
        info = infoIn;
        corrects = info.returnAnswers();
        incorrectQuestions = info.returnIncorrects();
        nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(150, 50));
        correctLabel = new JLabel("Here are the results of your gameplay!");
        correctLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        correctLabel.setForeground(Color.RED);
        howMuch = new JLabel("Game completed: " + info.returnPercent() + "%");
        howMuch.setFont(new Font("serif", Font.PLAIN, 30));
        JPanel corr = new JPanel();
        corr.add(correctLabel);
        corr.setOpaque(false);
        add(corr, BorderLayout.NORTH);
        JPanel bholder = new JPanel();
        
        bholder.setOpaque(false);
        PaintBoxes paintComponentPane = new PaintBoxes();
        JPanel drawCorrects = new JPanel();
        drawCorrects.setOpaque(false);
        drawCorrects.setLayout(new BorderLayout());
        drawCorrects.add(paintComponentPane, BorderLayout.CENTER);
        
        JTextArea keyArea = new JTextArea("Key: Green - Correct (no hints)\nRed - Incorrect\nCyan - Correct during freeze time\nGray- Skipped question\nYellow - Correct during double points\nBlue - Correct using hint\n"
        + "\nThe draw squares go from left to right, up to down, based on questions answered!");
        keyArea.setFont(new Font("Times New Roman", Font.BOLD, 20));
        keyArea.setLineWrap(true);
		keyArea.setEditable(false);
		keyArea.setWrapStyleWord(true);
        JScrollPane keyPane = new JScrollPane(keyArea);
        keyPane.setPreferredSize(new Dimension(200, 125));
        drawCorrects.add(keyPane, BorderLayout.NORTH);
        
        paintComponentPane.setPreferredSize(new Dimension(350, 475));
        JPanel pane = new JPanel();
        paintComponentPane.setOpaque(false);
        add(drawCorrects, BorderLayout.EAST);
        drawCorrects.setOpaque(false);
        add(pane, BorderLayout.CENTER);
        pane.setLayout(new BorderLayout());
        showCorrects corrects = new showCorrects();
        corrects.setOpaque(false);
        boolean all = true;
		for (int i = 0; i < 17; i ++)
		{
			if(!info.returnEarn()[i] && i != 13)
				all = false;
		}
		if(all)
			info.setEarn(13);
        pane.add(corrects, BorderLayout.CENTER);
        bholder.add(nextButton);
        pane.add(bholder, BorderLayout.SOUTH);
        nextButton.addActionListener(this);
        pane.setOpaque(false);
        EndPanel ep = new EndPanel(parent, cards, info);
		parent.add(ep, "End");
    }
    //draws background image
    public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/CorrectPanelBG.jpg"), 0, 0, 960, 540, this);
	}
	// checks if next button is clicked then show the end panel
    public void actionPerformed(ActionEvent evt)
    {
        String event = evt.getActionCommand();
        if (event.equals("Next"))
            cards.show(parent, "End");
    }
    // paintComponent used to draw the squares the are shown with color
    // corresponding to the color (key)
    class PaintBoxes extends JPanel
    {
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int x = 0;
			int y = 25;
			int counter = 0;
			for (int i = 0; i < corrects.size(); i++)
			{
				counter++;
				if (corrects.get(i) == 0)
					g.setColor(Color.GREEN);
				else if (corrects.get(i) == 1)
					g.setColor(Color.RED);
				else if (corrects.get(i) == 2)
					g.setColor(Color.CYAN);
				else if (corrects.get(i) == 3)
					g.setColor(Color.GRAY);
				else if (corrects.get(i) == 4)
					g.setColor(Color.YELLOW);
				else if (corrects.get(i) == 5)
					g.setColor(Color.BLUE);
				g.fillRect(x, y, 10, 10);
				if (counter % 14 == 0)
				{
					x = 0;
					y += 20;
				}            
				else
					x += 25;
			}
		}
	}
	/*Mainly used to show the incorrect questions. Repaints the question
	 * that the user got wrong everytime they click one of the JButtons
	 * either back or next, with added ActionListener. */
	class showCorrects extends JPanel implements ActionListener
	{
		private int numberIncorrs;
		private JTextArea countryfact;
		private JScrollPane countryFacts;
		private JButton previousQuestion;
		private JButton nextQuestion;
		public showCorrects()
		{
			setLayout(null);
			previousQuestion = new JButton("Previous");
			nextQuestion = new JButton("Next Question");
			previousQuestion.setBounds(50, 350, 150, 30);
			nextQuestion.setBounds(350, 350, 150, 30);
			
			previousQuestion.addActionListener(this);
			nextQuestion.addActionListener(this);
			numberIncorrs = 0;
			countryfact = new JTextArea();
			countryfact.setWrapStyleWord(true);
			countryfact.setEditable(false);
			countryfact.setLineWrap(true);
			if(incorrectQuestions.size() > 0)
				countryfact.setText(showAnswers(incorrectQuestions.get(numberIncorrs)));
			if(incorrectQuestions.size() <= 1)
				nextQuestion.setEnabled(false);
			if(incorrectQuestions.size() == 0)
				countryfact.setText("You didn't get anything Incorrect!");
			countryFacts = new JScrollPane(countryfact);
			countryfact.setFont(new Font("monospaced", Font.PLAIN, 20));
			countryFacts.setBounds(350, 0, 225, 350);
			previousQuestion.setEnabled(false);
			add(countryFacts);
			add(nextQuestion);
			add(previousQuestion);
		}
		//Used to draw image, made sure formatting is right
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(incorrectQuestions.size() > 0)
			{
				Image temp = parent.getMyImage("pictures/countryImages/" + incorrectQuestions.get(numberIncorrs) + ".jpg");
				g.drawImage(temp, 175-temp.getWidth(null)*info.getImageSize()/6, 175 - temp.getHeight(null)*info.getImageSize()/6, temp.getWidth(null)*info.getImageSize()/3, temp.getHeight(null)*info.getImageSize()/3, this);
			}
		}
		//Moves between images, calling repaint() after each
		public void actionPerformed(ActionEvent evt)
		{
			
			if (evt.getActionCommand().equals("Previous"))
			{
				numberIncorrs -= 1;
				nextQuestion.setEnabled(true);
				if(numberIncorrs == 0)
					previousQuestion.setEnabled(false);
				repaint();
			}
			else if (evt.getActionCommand().equals("Next Question"))
			{
				numberIncorrs += 1;
				previousQuestion.setEnabled(true);
				if(numberIncorrs == incorrectQuestions.size()-1)
					nextQuestion.setEnabled(false);
				repaint();
			}
			String countInfo = showAnswers(incorrectQuestions.get(numberIncorrs));
			countryfact.setText(countInfo);
		}
		//Shows the correct answer, along with information of each of them. 
		//This is done by reading a file, then specifically targeting the country.
		public String showAnswers(String countryMissed)
		{
			String result = "";
			Scanner kb = null;
			try
			{
				kb = new Scanner(new File("Files/Read/countriesContinentHard.txt"));
			}
			catch(IOException e)
			{
				System.err.println("Cannot find answers file to read");
				e.printStackTrace();
			}
			String line = "";
			while(kb.hasNext() && !line.equals("Country: " + countryMissed) && !line.equals("Continent: Antarctica"))
			{
				line = kb.nextLine();
			}
			result += line + "\n";
			while(!line.equals("") && kb.hasNext())
			{
				line = kb.nextLine();
				result += line + "\n";
			}
			kb.close();
			return result;
		}
	}	
}
// Endpanel class that mainly directs user to play game another time, save score
// check leaderboard, etc. Also allows the user to see how the score is calculated.
class EndPanel extends JPanel implements ActionListener
{
    private NameThatNationHolder parent;
    private CardLayout cards;
    private Information info;
    private JButton saveScore, checkAchieve, mainMenu, checkLead, checkCorrect;
    private JTextArea area;
    private JLabel completed;
    private JLabel accuracy;
    //Initialize components, set as GridLayout, then adding components
    public EndPanel(NameThatNationHolder parentIn, CardLayout cardsIn, Information infoIn)
    {
        parent = parentIn;
        cards = cardsIn;
        info = infoIn;
        
        setLayout(new BorderLayout());
        JPanel endPaneLabel = new JPanel();
        endPaneLabel.setOpaque(false);
        JLabel endLabel = new JLabel("End Panel");
        endLabel.setForeground(Color.WHITE);
        endPaneLabel.add(endLabel);
        endLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        completed = new JLabel("Time Taken: " + String.format("%02d:%02d", info.getRemaining()/60, info.getRemaining()%60));
		accuracy = new JLabel("Guessing Accuracy: " + (info.getDone()-info.returnIncorrects().size())*100/info.getDone() + "%");
		completed.setFont(new Font("serif", Font.PLAIN, 35));
		accuracy.setFont(new Font("serif", Font.PLAIN, 35));
		
		JPanel infos = new JPanel(new GridLayout(2,1));
		infos.add(completed);
		infos.setOpaque(false);
		infos.add(accuracy);
        JPanel leftAndRightPane = new JPanel();
        leftAndRightPane.setOpaque(false);
        add(endPaneLabel, BorderLayout.NORTH);
        
        add(leftAndRightPane, BorderLayout.CENTER);
        JPanel infoHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoHolder.add(infos);
        infoHolder.setOpaque(false);
        add(infoHolder, BorderLayout.SOUTH);
        leftAndRightPane.setLayout(new GridLayout(1, 2));
        JPanel leftPane = new JPanel();
        leftPane.setOpaque(false);
        JPanel rightPane = new JPanel(new GridLayout(3, 2));
        rightPane.setOpaque(false);
        
        saveScore = new JButton("Save Score");
        mainMenu = new JButton("Main Menu");
        checkAchieve = new JButton("Check Achievements");
        checkLead = new JButton("Check Leaderboard");
        checkCorrect = new JButton("View wrong questions");
        saveScore.setBackground(Color.RED);
        mainMenu.setBackground(Color.ORANGE);
        checkAchieve.setBackground(Color.YELLOW);
        checkLead.setBackground(Color.GREEN);
        checkCorrect.setBackground(Color.PINK);
        
        saveScore.setPreferredSize(new Dimension(175, 50));
        mainMenu.setPreferredSize(new Dimension(175, 50));
        checkAchieve.setPreferredSize(new Dimension(175, 50));
        checkLead.setPreferredSize(new Dimension(175, 50));
        checkCorrect.setPreferredSize(new Dimension(175, 50));
        
        JPanel b1 = new JPanel();
        b1.setOpaque(false);
        JPanel b2 = new JPanel();
        b2.setOpaque(false);
        JPanel b3 = new JPanel();
        b3.setOpaque(false);
        JPanel b4 = new JPanel();
        b4.setOpaque(false);
        JPanel b5 = new JPanel();
        b5.setOpaque(false);
        
        b1.add(saveScore);
        b2.add(mainMenu);
        b3.add(checkAchieve);
        b4.add(checkLead);
        b5.add(checkCorrect);     
        rightPane.add(b1);
        rightPane.add(b2);
        rightPane.add(b3);
        rightPane.add(b4);
        rightPane.add(b5);
        
        saveScore.addActionListener(this);
        mainMenu.addActionListener(this);
        checkAchieve.addActionListener(this);
        checkLead.addActionListener(this);
        checkCorrect.addActionListener(this);
        area = new JTextArea(showCalc());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        
        JScrollPane calcP = new JScrollPane(area);
        calcP.setPreferredSize(new Dimension(400, 200));
        area.setFont(new Font("Times New Roman", Font.BOLD, 20));
        
        leftPane.add(calcP);
        leftAndRightPane.add(leftPane);
        leftAndRightPane.add(rightPane);
    }
    // Draws the background image of the panel
    public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/World.jpg"), 0, 0, 960, 540, this);
	}
    //Shows how the point system is calculated to the user, using a for loop structure
    public String showCalc()
    {
		String result = "";
		for (int i = 0; i < info.getStreak().size(); i ++ )
		{
			if(i > 0)
				if(info.getStreak().get(i) == info.getStreak().get(i-1)*2)
					result += "Double points used, ";
			result += info.getStreak().get(i) + " points x " + info.getTime().get(i) + "\n";
		}
		result += "total points: " + info.getP();
		return result;
	}
    // checks if the save score JButton is clicked with
    // if statements for the specific card shown
    public void actionPerformed(ActionEvent evt)
    {
        String which = evt.getActionCommand();
        if (which.equals("Save Score"))
        {
            writeLeader();
            cards.show(parent, "LeaderBoard");
        }
        else if(which.equals("Main Menu"))
        {
			cards.show(parent, "Start");
			info.reset();
		}
		else if(which.equals("Check Leaderboard"))
		{
			LeaderPanel lp = new LeaderPanel(parent, cards, info);
			parent.add(lp, "Leader");
			cards.show(parent, "Leader");
		}
		else if (which.equals("Check Achievements"))
		{
			Achievements achieve = new Achievements(parent, cards, info);
			parent.add(achieve, "Achievements");
			cards.show(parent, "Achievements");
		}
		else
		{
			cards.show(parent, "Correct");
		}
    }
    //method used to try-catch to add to the leaderboard txt file
    public void writeLeader()
    {
		String line = "";
		String result = "";
		String need = "Easy:";
		PrintWriter pw = null;
		Scanner kb = null;
		boolean used = false;
		try
		{
			kb = new Scanner(new File("Files/Write/LeaderBoard.txt"));	
		}
		catch(IOException e)
		{
			System.err.println("Cannot find LeaderBoard.txt to read from");
			e.printStackTrace();
		}
		
		while(kb.hasNext())
		{
			line = kb.nextLine();
			if(line.equals("") && !used && need.equals(info.getFileName() + ":"))
				result += String.format("%-8s- %s", info.getName(), info.getP() + " pts") + "\n";
			if(line.indexOf(":") != -1)
				need = line;
			if(line.indexOf("-") != -1 && need.equals(info.getFileName() + ":") && !used)
			{
				if(info.getP() >= Integer.parseInt(line.substring(line.indexOf("-")+2, line.indexOf("pts")-1)))
				{
					result += String.format("%-8s- %s", info.getName(), info.getP() + " pts") + "\n";
					used = true;
				}
			}
			result += line + "\n";
		}
		try
		{
			pw = new PrintWriter(new File("Files/Write/LeaderBoard.txt"));	
		}
		catch(IOException e)
		{
			System.err.println("Cannot find LeaderBoard.txt to write to");
			e.printStackTrace();
		}
		pw.print(result);
		kb.close(); //close scanner
		pw.close(); //close scanner
		saveScore.setEnabled(false);
	}
}
/* Main purpose of class to to display the leaderboard. This is done 
 * by reading in the leaderboard file, where the user can add their score
 * which is sorted into their category. This is then set as the content
 * of a JScrollPane, then shown to the user. */
class LeaderPanel extends JPanel implements ActionListener
{
	private NameThatNationHolder parent;
	private CardLayout cards;
	private Information info;
	private JButton back;
	//Initialize variables, making sure words are not cut off, and formatted properly
	//setBounds methods used to set the specific location of where components are shown
	public LeaderPanel(NameThatNationHolder parentIn, CardLayout cardsIn, Information infoIn)
	{
		parent = parentIn;
		cards = cardsIn;
		info = infoIn;
		
		setLayout(null);
		back = new JButton("End Panel");
		back.addActionListener(this);
		back.setBounds(430, 450, 100, 50);
		JTextArea Leader = new JTextArea(readLead());
		Leader.setLineWrap(true);
        Leader.setEditable(false);
        Leader.setWrapStyleWord(true);
		
		JScrollPane lead = new JScrollPane(Leader);
		Leader.setFont(new Font("monospaced", Font.PLAIN, 20));
		lead.setBounds(252, 123, 463, 302);
		
		add(lead);
		add(back);
	}
	//Shows end panel when action is done
	public void actionPerformed(ActionEvent e)
	{
		cards.show(parent, "End");
	}
	//draws the background image for the LeaderBoard Panel
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/LeaderBoardBG.jpg"), 0, 0, 960, 540, this);
	}
	//Trys to read in the leaderboard txt file, then adding to string which is returned
	public String readLead()
	{
		Scanner kb = null;
		String result = "";
		try
		{
			kb = new Scanner(new File("Files/Write/LeaderBoard.txt"));
		}
		catch(IOException e)
		{
			System.err.println("Cannot find LeaderBoard.txt to read from");
			e.printStackTrace();
		}
		while(kb.hasNext())
		{
			result += kb.nextLine() + "\n";
		}
		kb.close();
		return result;
	}
}
/* Contains many achievements which can be clicked on, that allows the user
 * to see what they have unlocked and haven't unlocked. The different
 * achievements have specific requirements that allows the user to unlock them
 * and are kept throughout multiple gameplays. */
class Achievements extends JPanel implements ActionListener
{
	private NameThatNationHolder parent;
	private CardLayout cards;
	private Information info;
	private JButton back, fTime, sStart, ngHere, gWhiz, cPro, uiWisely, fiTime, sAlive, sSkip, dEnabled, gueAll, nhNeeded, sStarter, moNations;
	private JButton hGuessmaster, sSeeker, mLover;
	private JTextArea showAchieve;
	private JButton[] bAchieve;
	/* Initializes components including JScrollPane, that currently has the
	 * achievements whether they are achieved or not. */
	public Achievements(NameThatNationHolder parentIn, CardLayout cardsIn, Information infoIn)
	{
		parent = parentIn;
		cards = cardsIn;
		info = infoIn;
		setLayout(null);
		back = new JButton("Go To End Panel");
		back.addActionListener(this);
		back.setBounds(25, 15, 150, 25);
		fTime = new JButton("Description");
		sStart = new JButton("Description");
		ngHere = new JButton("Description");
		gWhiz = new JButton("Description");
		cPro = new JButton("Description");
		uiWisely = new JButton("Description");
		fiTime = new JButton("Description");
		sAlive = new JButton("Description");
		sSkip = new JButton("Description");
		dEnabled = new JButton("Description");
		gueAll = new JButton("Description");
		nhNeeded = new JButton("Description");
		sStarter = new JButton("Description");
		moNations = new JButton("Description");
		hGuessmaster = new JButton("Description");
		sSeeker = new JButton("Description");
		mLover = new JButton("Description");
		bAchieve = new JButton[] {fTime, sStart, ngHere, gWhiz, cPro, uiWisely, fiTime, sAlive, sSkip, dEnabled, gueAll, nhNeeded, sStarter, moNations,
			hGuessmaster, sSeeker, mLover};
		
		JLabel achieve = new JLabel("Achievements");
		achieve.setForeground(Color.WHITE);
		achieve.setFont(new Font("Monospaced", Font.PLAIN, 30));
		achieve.setBounds(380, 0, 350, 25);
		add(achieve);
		int x = 50;
		int y = 190;
		
		for (int a = 0; a < 10; a++)
		{
			bAchieve[a].setBounds(x,y,125,25);
			bAchieve[a].addActionListener(this);
			add(bAchieve[a]);
			x += 175;
			if(a%5==4)
			{
				x = 50;
				y += 150;
			}
			
		}
		x = 25;
		y = 475;
		for (int b = 10; b < 17; b++)
		{
			bAchieve[b].setBounds(x,y,100,25);
			bAchieve[b].addActionListener(this);
			add(bAchieve[b]);
			x += 130;
		}
		add(back);
	}
	/* Draws the background image for the panel */
	public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/AchievementsBG.png"), 0, 0, 960, 540, this);
	}
	public void actionPerformed(ActionEvent e) //shows end panel
	{
		JButton which = (JButton)e.getSource();
		for (int b = 0; b < 17; b ++)
		{
			if(which == bAchieve[b])
			{
				AchievementHolder ah = new AchievementHolder(b, parent, cards, info);
				parent.add(ah, "aHolder");
				cards.show(parent, "aHolder");
			}
		}
		if(which == back)
			cards.show(parent, "End");
	}
}
/* This class is mainly used to proceed the CardLayout from the Achievement Panel, 
 * to the specific panel that describes more about the achievements. */
class AchievementHolder extends JPanel implements ActionListener
{
	private JLabel name;
	private JLabel description;
	private JLabel state;
	private String[] des;
	private JButton back;
	private NameThatNationHolder parent;
	private CardLayout cards;
	private Information info;
	private int index;
	private JButton bPrevious, bNext;
	/* This constructor initializes all the field variables. Other than that, 
	 * it also sets the specific font which is set in locations so that the 
	 * game looks good. */
	public AchievementHolder(int ind, NameThatNationHolder holderIn, CardLayout cardsIn, Information infoIn)
	{
		parent = holderIn;
		cards = cardsIn;
		info = infoIn;
		index = ind;
		des = new String[] {
			"First Time - Get your first question correct", 
			"Sharp Start - Get 5 correct answers in one session",
			"No Guessing Here - Get your first perfect streak of 10",
			"Geography Whiz - Get 50 correct answers",
			"Continental Pro - Correctly guess at least 1 country from every continent",
			"Use It Wisely - Use any one power-up in a session",
			"Frozen in Time - Use the Freeze Time power-up",
			"Staying Alive - Use the Extra Life power-up while having less than 3 lives",
			"Strategic Skip - Use the Skip power-up",
			"Double Enabled - Use the Double Points power-up",
			"Gotta Use Em' All - Use all power-ups",
			"No Help Needed - Finish a game without using any power-ups or hints",
			"Speedrun Starter - Finish a full game in under 1 minute (on any difficulty)",
			"Master of Nations - Unlock all other achievements",
			"Hardcore Guessmaster - Get 50 correct answers in Hard mode in one session",
			"Streak Seeker - Reach a 25-correct answer streak in any mode",
			"Map Lover - Finish a session by correctly answering all questions",
			
		};
		name = new JLabel(des[index].substring(0, des[index].indexOf("-")-1));
		name.setFont(new Font("serif", Font.PLAIN, 50));
		name.setForeground(Color.WHITE);
		description = new JLabel(des[index].substring(des[index].indexOf("-")+2));
		description.setFont(new Font("serif", Font.PLAIN, 30));
		description.setForeground(Color.WHITE);
		state = new JLabel("Locked");
		if(info.returnEarn()[index])
			state.setText("Unlocked");
		state.setFont(new Font("serif", Font.PLAIN, 25));
		state.setForeground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JPanel hold1 = new JPanel(new GridLayout(3,1));
		name.setOpaque(false);
		description.setOpaque(false);
		state.setOpaque(false);
		hold1.add(name);
		hold1.add(description);
		hold1.add(state);
		bPrevious = new JButton("Previous");
		bNext = new JButton("Next");
		bPrevious.setFont(new Font("monospaced", Font.PLAIN,20));
		bNext.setFont(new Font("monospaced", Font.PLAIN, 20));
		bNext.addActionListener(this);
		bPrevious.addActionListener(this);
		JPanel pHolder = new JPanel();
		JPanel nHolder = new JPanel();
		pHolder.add(bPrevious);
		nHolder.add(bNext);
		pHolder.setOpaque(false);
		nHolder.setOpaque(false);
		JPanel hold2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		hold2.add(hold1);
		back = new JButton("Go To Achievements Panel");
		back.addActionListener(this);
		JPanel hold3 = new JPanel();
		hold3.add(back);
		hold1.setOpaque(false);
		hold3.setOpaque(false);
		hold2.setOpaque(false);
		add(pHolder, BorderLayout.WEST);
		add(nHolder, BorderLayout.EAST);
		add(hold2, BorderLayout.NORTH);
		add(hold3, BorderLayout.SOUTH);
		
		if(index == 0)
			bPrevious.setEnabled(false);
		if(index == 16)
			bNext.setEnabled(false);
	}
	/* This method from ActionListener is used to check the specific JButton that is clicked
	 * to decide which achievement to show. These buttons include both the previous and the next JButtons.
	 * If neither is clicked, will go back to the Achievements Panel. */
	public void actionPerformed(ActionEvent e)
	{
		String which = ((JButton)e.getSource()).getText();
		if(which.equals("Previous"))
		{
			index -= 1;
			anotherAchievement();
			bNext.setEnabled(true);
			repaint();
		}
		else if(which.equals("Next"))
		{
			index += 1;
			anotherAchievement();
			bPrevious.setEnabled(true);
			repaint();
		}
		else
			cards.show(parent, "Achievements");
	}
	/* This paintComponent method is used to draw the image of the locked or unlocked 
	 * image. The image is used to convey whether the achievement has been done. The images
	 * are drawn through the getMyImage() from another class, which makes drawing images easier. */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(parent.getMyImage("pictures/panelImages/ShowAchieveBG.png"), 0, 0, 960, 540, this);
		Image lock = parent.getMyImage("pictures/panelImages/LockedLock.png");
		if(info.returnEarn()[index])
			lock = parent.getMyImage("pictures/panelImages/UnlockedLock.png");
		g.drawImage(lock, 400, 125, lock.getWidth(null), lock.getHeight(null), this);
	}
	/* This method is for showing the description and name of the achivements. 
	 * By including if-else statements, it sets the JButtons as either enabled/disabled. 
	 * This is because there is no previous achievement when on the first achievement and no
	 * next achievement for the last achievement. */
	public void anotherAchievement()
	{
		name.setText(des[index].substring(0, des[index].indexOf("-")-1));
		description.setText(des[index].substring(des[index].indexOf("-")+2));
		if(index == 0)
		{
			bPrevious.setEnabled(false);
		}
		if(index == 16)
		{
			bNext.setEnabled(false);
		}
		if(info.returnEarn()[index])
			state.setText("Unlocked");
		else
			state.setText("Locked");
	}
}
/* Main functions of this class is to store the information necessary for the
 * other classes to access and use. This includes name, color, image size,
 * file name, and continents. */
class Information
{
	private String name;
	private int red, green, blue;
	private String fileName;
	private int sizeOfImage;
	private boolean[] states;
	private String difficulty;
	private ArrayList<Integer> corrections;
	private ArrayList<String> incorr;
	private int pts;
	private ArrayList<Integer> pStreak;
	private ArrayList<Integer> sTime;
	private boolean[] earned;
	private int numCorrect;
	private int percentage;
	private int timeSpent;
	private int questionsDone;
	public Information()
	{
		states = new boolean[]{true, true, true, true, true, true, true};
		// locations are this Antarctica, Australia, South America, Africa, Asia, North America, Europe 
		fileName = "Easy";
		earned = new boolean[17];
		sizeOfImage = 2;
		corrections = new ArrayList<Integer>();
		incorr = new ArrayList<String>();
		pStreak = new ArrayList<Integer>();
		pStreak.add(10);
		pStreak.add(30);
		sTime = new ArrayList<Integer>();
		sTime.add(0);
		sTime.add(0);
		numCorrect = 0;
		percentage = 0;
		questionsDone = 0;
		timeSpent = 0;
		
	}
	/* Methods other than the constructor below are used to store
	 * specific components that need to be accessed by other classes. 
	 * To do this, we used multiple methods where each component has a 
	 * corresponding set method, and a get method, stored using the 
	 * field variables */
	public String getName() //get name of user
	{
		return name;
	}

	public void setName(String nameIn) //sets name of user from textfield
	{
		name = nameIn;
	}

	public void setColor(int r, int g, int b) //sets the color of the background
	{
		red = r;
		green = g;
		blue = b;
	}
	public Color getColor() //return the color for background display on the game panel
	{
		return new Color(red, green, blue);
	}
	public void setFile(String fileIn) //sets the file difficulty
	{
		fileName = fileIn;
	}
	public String getFileName() //gets the file name depending on difficulty
	{
		return fileName;
	}
	public void setImageSize(int imageSize) //sets the image size
	{
		sizeOfImage = imageSize;
	}
	public int getImageSize() //returns the image size wanted
	{
		return sizeOfImage;
	}
	public void setContinent(int index, boolean trueFalse) //sets the state at which a continent was chosen
	{
		states[index] = trueFalse;
	}
	public boolean[] getContinent() //returns the continents that they chose
	{
		return states;
	}
	public void correctAnswers(int corrects) //sees if answers were correct
	{
		corrections.add(corrects);
	}
	public ArrayList <Integer> returnAnswers() //returns arraylist for correct answers (for color shown)
	{
		return corrections;
	}
	public void setP(int pt) //sets points
	{
		pts = pt;
	}
	public int getP() //returns points
	{
		return pts;
	}
	public void incorrectAnswers(String incorrect) //sets the incorrect questions
	{
		incorr.add(incorrect);
	}
	public ArrayList<String> returnIncorrects() //returns the incorrect questions
	{
		return incorr;
	}
	public void setStreak(int value) //set the streak
	{
		pStreak.add(value);
		sTime.add(0);
	}
	public ArrayList <Integer> getStreak() //get the streak
	{
		return pStreak;
	}
	public void setTime(int value) //sets the time left
	{
		sTime.set(value, sTime.get(value) + 1);
	}
	public ArrayList <Integer> getTime() //returns time left
	{
		return sTime;
	}
	public void reset() //called to reset all arraylists
	{
		sTime.clear();
		pStreak.clear();
		corrections.clear();
		incorr.clear();
		sTime.add(0);
		sTime.add(0);
		pStreak.add(10);
		pStreak.add(30);
		questionsDone = 0;
		timeSpent = 0;
		percentage = 0;
	}
	public void setEarn(int ind) //sets earned points
	{
		earned[ind] = true;
	}
	public boolean[] returnEarn() //returns points
	{
		return earned;
	}
	public void setCorrect() //sets correct answers
	{
		numCorrect += 1;
	}
	public int getCorrect() //get correct answers
	{
		return numCorrect;
	}
	public void setPercent(int completed) //sets the percentage of the game that the user has finished
	{
		percentage = completed;
	}
	public int returnPercent() //returns the percentage
	{
		return percentage;
	}
	public void setRemaining(int timeTaken) //sets the remaining time
	{
		timeSpent = timeTaken;
	}
	public void setTotal() //sets the total number of questions the user has done
	{
		questionsDone += 1;
	}
	public int getRemaining() //gets the remaining time of game
	{
		return timeSpent;
	}
	public int getDone() //returns how many questions the user answers (excluding skip)
	{
		return questionsDone;
	}
}
