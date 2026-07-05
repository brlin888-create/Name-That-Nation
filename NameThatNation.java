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

/* The goal of this project is to create a game where the user will have different levels of difficulty and hopefully be able to choose
the correct country based on the shape that is shown. The user can choose from three levels of difficulty, easy, medium, and hard.
The game will be set in a 960 by 540 pixel window. It cannot be resizable and is set as visible to the user*/

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

/* NameThatNationHolder is the panel that holds the card layout. It creates instances of all the panels, sending in 
the current instance and also information which is then used in the information class. It also has a try catch
method which can be accessed later whenever necessary in other classes.*/

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

/* Now work on the class for the start panel. This is the class foor the start panel
It includes field variables for the components including the timer for the planned background image. 
The background animation is still being worked on, should be an Earth image rotating though. 
The user will enter the name, the actionListener classes will check if the user has done an action
such as clicking a button which will proceed to the next panel in the card layout.*/

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

/* Next, I will create a panel that will show the instructions after the JButton is clicked. By clicking, 
an image of the gameplay will be shown as well as the instructions in a JScrollPane. JScrollPane needs to be 
utilized as the text displayed needs to be scrollable -> they are quite long. User will the nunderstand how the 
game will work and play the game properly. */

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
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            int count = 0;
            for (int a = 0; a < 7; a++)
            {
                if (continentButtons[a].isSelected())
                    count++;
            }
            if ((JButton)evt.getSource() == backButton && count >= 3)
                cards.show(ntnh, "Start");
        }
    }
}

/* This class uses a try-catch to access the hard file
 * which contains all the information about the countries
 * and takes this information and puts it in a JScrollPane.
 * There is also a picture of Earth on the left side. */

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
		conInfo.setFont(new Font("monospaced", Font.Bold, 30));
		proceed = negit w JButton("Back");

		proceed.addActionListener(new next());
		JTextArea cons = new JTextArea(fileIn("files/Read/countriesContinentHard.txt");
		cons.setLineWrap;
		
	}
}