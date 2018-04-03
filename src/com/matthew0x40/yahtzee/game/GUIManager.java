package com.matthew0x40.yahtzee.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Settings;
import com.matthew0x40.yahtzee.models.scoreboard.Scoreboard;
import com.matthew0x40.yahtzee.models.scoreboard.ScoringOption;

/**
 * This class manages the GUI, the swing elements.
 * This is the view of an MVC pattern.
 */
public class GUIManager {
	protected JFrame frame;
	protected JPanel main;
	protected JPanel scorepanel;
	protected JPanel dicepanel;
	protected JPanel rollpanel;
	protected JButton rollbutton;
	
	protected JButton sb_button;
	protected JComboBox<String> sides_chooser;
	protected JComboBox<String> dice_chooser;
	protected JPanel scoreopts;
	
	protected final Color main_color = Color.decode("#58A836");
	
	protected static BufferedImage die1 = null;
	protected static BufferedImage die2 = null;
	protected static BufferedImage die3 = null;
	protected static BufferedImage die4 = null;
	protected static BufferedImage die5 = null;
	protected static BufferedImage die6 = null;
	
	protected Game1PlayerGUIController control;
	
	public ArrayList<MyButton> discard = new ArrayList<>();
	public ArrayList<MyButton> keep = new ArrayList<>();
	
	static {
		try {
			die1 = ImageIO.read(new File("src/die-1.png"));
			die2 = ImageIO.read(new File("src/die-2.png"));
			die3 = ImageIO.read(new File("src/die-3.png"));
			die4 = ImageIO.read(new File("src/die-4.png"));
			die5 = ImageIO.read(new File("src/die-5.png"));
			die6 = ImageIO.read(new File("src/die-6.png"));
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Initializes the GUImanager and creates the JFrame.
	 * Use the init() method to pack and show the JFrame.
	 */
	public GUIManager(Game1PlayerGUIController control) {
		this.control = control;
		
		frame = new JFrame("Yahtzee");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1300,900));
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.decode("#ebebeb")));
			UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.BLACK));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// oh well
		}
		
		scorepanel = createScorePanel();
		frame.add(scorepanel, BorderLayout.EAST);
		
		main = createMainPanel();
		frame.add(main, BorderLayout.CENTER);
	}
	
	/**
	 * Makes the frame visible
	 */
	public void show() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Creates the main panel, this panel
	 * contains the roll button and dice.
	 * @return JPanel
	 */
	private JPanel createMainPanel() {
		JPanel main = new JPanel();
		main.setLayout(new GridLayout(2, 1));
		main.setBackground(main_color);

		JPanel dicepanel2 = new JPanel();
		dicepanel = new JPanel();

		dicepanel2.setBackground(main_color);
		dicepanel.setBackground(main_color);
		
		dicepanel2.add(dicepanel);
		main.add(dicepanel2);
		
		rollpanel = new JPanel();
		rollpanel.setBackground(main_color);
		
		rollbutton = new JButton("Roll!");
		rollbutton.setBackground(Color.WHITE);
		rollbutton.setPreferredSize(new Dimension(100,50));
		rollpanel.add(rollbutton);
		
		main.add(rollpanel);
		
		return main;
	}
	
	public void clearDicePanel() {
		discard.forEach(button -> {
			button.d = null;
			button.repaint();
		});
		keep.forEach(button -> {
			button.d = null;
			button.repaint();
		});
	}
	
	/**
	 * Reset the dice panel for a new game.
	 * This method should be called from the Controller.
	 * @param s the settings to use
	 */
	public void resetDicePanel(Settings s) {
		dicepanel.removeAll();
		dicepanel.setLayout(new GridLayout(2, s.dice_per_hand));
		
		discard.clear();
		keep.clear();
		
		for (int i = 0; i < s.dice_per_hand; i++) {
			MyButton die = createDieButton();
			die.index = i;
			die.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					MyButton b = (MyButton) e.getSource();
					if (b.d != null) {
						keep.get(b.index).d = b.d;
						control.moveToKeep(b.d);
						
						b.d = null;
						keep.get(b.index).repaint();
					}
				}
				
			});
			
			die.setBackground(Color.decode("#f5e3e3"));
			dicepanel.add(die);
			discard.add(die);
		}
		
		for (int i = 0; i < s.dice_per_hand; i++) {
			MyButton die = createDieButton();
			die.index = i;
			die.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					MyButton b = (MyButton) e.getSource();
					if (b.d != null) {
						discard.get(b.index).d = b.d;
						control.moveToDiscard(b.d);
						
						b.d = null;
						discard.get(b.index).repaint();
					}
				}
				
			});
			
			dicepanel.add(die);
			keep.add(die);
		}
	}
	
	/**
	 * Creates a Die button
	 * @return
	 */
	private MyButton createDieButton() {
		MyButton button = new MyButton();
    	button.setFont(new Font("Open Sans", Font.BOLD, 17));
    	button.setFocusPainted(false);
    	button.setPreferredSize(new Dimension(100,100));
    	button.setBorder(new LineBorder(main_color, 5));
    	button.setContentAreaFilled(false);
    	button.setRolloverEnabled(false);
    	button.setMargin(new Insets(0,0,0,0));
    	button.setOpaque(true);
    	button.setForeground(Color.BLACK);
    	button.setBackground(Color.WHITE);
    	button.setText("");
		return button;
	}
	
	/**
	 * Custom JButton class, for the purpose of
	 * having dice drawn on top the "plate".
	 */
	@SuppressWarnings("serial")
	public static class MyButton extends JButton {
		public Dice d = null;
		public int index = 0;
		
		public MyButton() {
			super();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (d != null) {
				Graphics2D g2d = (Graphics2D) g;
				
				BufferedImage diei = null;
				if (d.number == 1) {
					diei = die1;
				} else if (d.number == 2) {
					diei = die2;
				} else if (d.number == 3) {
					diei = die3;
				} else if (d.number == 4) {
					diei = die4;
				} else if (d.number == 5) {
					diei = die5;
				} else if (d.number == 6) {
					diei = die6;
				}
				
				if (diei == null) {
					g2d.setColor(Color.WHITE);
					g2d.fillRoundRect(20, 20, 60, 60, 7, 7);
					g2d.setColor(Color.BLACK);
					g2d.drawRoundRect(20, 20, 60, 60, 7, 7);
					g2d.drawString(Integer.toString(d.number), 45, 55);
				} else {
					g2d.drawImage(diei, 20, 20, 60, 60, null);
				}
			}
		}
		
	}
	
	/**
	 * Creates the score panel, this panel contains the settings
	 * and scoring options
	 * @return JPanel
	 */
	private JPanel createScorePanel() {
		JPanel scorepanel = new JPanel();
		scorepanel.setBackground(Color.decode("#f5f5f5"));
		scorepanel.setLayout(new BoxLayout(scorepanel, BoxLayout.Y_AXIS));
		scorepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		{
			JLabel title = new JLabel("INFOBOX");
			title.setPreferredSize(new Dimension(400, 20));
			title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			title.setFont(new Font("Open Sans", Font.BOLD, 17));
			title.setForeground(Color.decode("#373737"));
			
			title.setAlignmentX(Component.LEFT_ALIGNMENT);
			scorepanel.add(title);
		}
		{
			JPanel toolbar = new JPanel();
			toolbar.setMaximumSize(new Dimension(9999, 39));
			toolbar.setLayout(new FlowLayout());
			toolbar.setBackground(Color.decode("#eaeaea"));

			sb_button = new JButton("Show scoreboard");
			sb_button.setFocusPainted(false);
			sb_button.setFocusable(false);
			sb_button.setBorderPainted(false);
			toolbar.add(sb_button);
			
			String[] sides_list = { "6 sides", "8 sides", "12 sides"};
			sides_chooser = new JComboBox<>(sides_list);
			sides_chooser.setBackground(Color.WHITE);
			sides_chooser.setSelectedIndex(0);
			sides_chooser.setFocusable(false);
			toolbar.add(sides_chooser);

			String[] dice_list = { "5 dice", "6 dice", "7 dice"};
			dice_chooser = new JComboBox<>(dice_list);
			dice_chooser.setBackground(Color.WHITE);
			dice_chooser.setSelectedIndex(0);
			dice_chooser.setFocusable(false);
			toolbar.add(dice_chooser);
			
			toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
			scorepanel.add(toolbar);
		}
		{
			JLabel title = new JLabel("YOUR SCORE OPTIONS");
			title.setPreferredSize(new Dimension(400, 40));
			title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			title.setFont(new Font("Open Sans", Font.BOLD, 17));
			title.setForeground(Color.decode("#373737"));
			
			title.setAlignmentX(Component.LEFT_ALIGNMENT);
			scorepanel.add(title);
		}
		{
			JPanel scoreoptsParent = new JPanel();
			scoreoptsParent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			scoreoptsParent.setBackground(Color.decode("#eaeaea"));
			
			scoreopts = new JPanel();
			scoreopts.setBackground(Color.decode("#eaeaea"));
			scoreopts.setLayout(new BoxLayout(scoreopts, BoxLayout.Y_AXIS));
			
			scoreoptsParent.add(scoreopts);

			scoreoptsParent.setAlignmentX(Component.LEFT_ALIGNMENT);
			scorepanel.add(scoreoptsParent);
		}
		
		return scorepanel;
	}
	
	/**
	 * Reset the score options.
	 * This method should be called from the controller.
	 * @param s the settings to use
	 */
	public void resetScoreOpts(Settings s) {
		EventQueue.invokeLater(() -> {
			scoreopts.removeAll();
			JLabel tbd = new JLabel("TBD");
			tbd.setForeground(Color.decode("#999999"));
			tbd.setFont(new Font("Open Sans", Font.BOLD, 17));
			scoreopts.add(tbd);
			
			scoreopts.repaint();
			scoreopts.revalidate();
		});
	}
	
	public void showGameOver() {
		EventQueue.invokeLater(() -> {
			scoreopts.removeAll();
			
			JLabel tbd = new JLabel(
					"<html>Game Over.<br/>Click \"Show Scoreboard\" above<br/>to see your score.</html>");
			tbd.setForeground(Color.decode("#999999"));
			tbd.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
			tbd.setFont(new Font("Open Sans", Font.BOLD, 17));
			scoreopts.add(tbd);
			
			JButton replayButton = new JButton("Replay?");
			replayButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					control.restart();
				}
				
			});
			scoreopts.add(replayButton);
			
			scoreopts.repaint();
			scoreopts.revalidate();
		});
	}

	public void showOptions(List<ScoringOption> options) {
		if (options.size() == 0) {
			showGameOver();
			return;
		}
		
		EventQueue.invokeLater(() -> {
			scoreopts.removeAll();
			scoreopts.repaint();
			
			options.forEach(opt -> {
				JPanel container = new JPanel();
				container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
				container.setBackground(new Color(0,0,0,0));
				container.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
				
				String text = "<html>Score <b>" + opt.value + "</b> on the <b>" + opt.display_name + "</b> line</html>";
				JButton button = new JButton(text);
				
				button.setFont(new Font("Open Sans", Font.PLAIN, 14));
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						control.receiveScoringOption(opt);
					}
					
				});
				
				button.setMargin(new Insets(5, 5, 5, 5));
				button.setAlignmentX(Component.LEFT_ALIGNMENT);
				container.add(button);
				
				container.setAlignmentX(Component.LEFT_ALIGNMENT);
				scoreopts.add(container);
			});
			
			scoreopts.repaint();
			scoreopts.revalidate();
		});
	}

	public void showScoreboardWindow(Scoreboard scoreboard) {
		JFrame show = new JFrame("Scoreboard");
		show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		show.setPreferredSize(new Dimension(1000,700));
		
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		int total = 0;
		
		List<ScoringOption> scored = scoreboard.toScoreList();
		for (ScoringOption score : scored) {
			JLabel label = new JLabel("<html><b>" + score.display_name + ":</b> " + score.value + "</html>");
			label.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
			label.setAlignmentX(Component.LEFT_ALIGNMENT);
			main.add(label);
			
			total += score.value;
		}
		
		JLabel total_sum = new JLabel("<html><b>Total Sum:</b> "+total+"</html>");
		total_sum.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
		total_sum.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(total_sum);
		
		show.add(main);
		
		show.pack();
		show.setLocationRelativeTo(null);
		show.setVisible(true);
	}
}
