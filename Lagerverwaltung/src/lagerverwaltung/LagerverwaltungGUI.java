package lagerverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LagerverwaltungGUI extends JFrame{

	private Actionlistener actionlistener = new Actionlistener();
	private LagerverwaltungDaten daten = new LagerverwaltungDaten();
	
	//Men�bar
	private JMenuBar menubar;
	private JMenu menuDatei;
	private JMenu menuZurueck;
	private JMenu menuFachauslastung;
	private JMenuItem oeffnenItem;
	private JMenuItem beendenItem;
	private JMenuItem speichernItem;
	private JMenuItem anzeigenLagerinhaltItem;
	private JMenuItem entnehmenItem;
	private JMenuItem einlagernItem;
	private JMenuItem startseiteItem;
	private JMenuItem fachauslastungItem;
	
	//IconMenu
	private JButton oeffnenbtn;
	private JButton beendenbtn;
	private JButton speichernbtn;
	private JButton anzeigenLagerinhaltbtn;
	private JButton entnehmenbtn;
	private JButton einlagernbtn;
	
	//Icon
	private ImageIcon close;
	private ImageIcon open;
	private ImageIcon pallettake;
	private ImageIcon palletstore;
	private ImageIcon save;
	private ImageIcon stock;
	
	//Startoberfl�che
	private Box hilfsbox;
	private JPanel menupanel;
	private JPanel leftpanel;
	private CustomPanel rightpanel;
	
	//ProgressBar
	private CustomProgressBar freierPlatzBar;
	private CustomProgressBar freieFaecherBar;
	private JLabel freierPlatzLabel;
	private JLabel freieFaecherLabel;
	private JLabel ueberschriftLabel;
	private JLabel hilfslabel;
	
	//Farbe
	private Color CustomColor;
	
	//Ergebnisdialog
	private JDialog ergebnisDialog;
	private JLabel teilLabel;
	private JLabel[] ergebnisLabel;
	private JLabel[] ausschriftLabel;
	private JButton btnok;
	private EmptyBorder eborder;
	private Dimension screensize;
	
	
	
	/**
	 * Konstruktor: erzeugt die Graphikoberfl�che bestehend aus Men�, Iconmen�, Auslastung des Lagers und ein Bild
	 * Zus�tzlich werden Eigenschaften des JFrames gesetzt und der ActionListener f�r Buttons erzeugt
	 * 
	 */
	public LagerverwaltungGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.put("ProgressBarUI", "javax.swing.plaf.metal.MetalProgressBarUI");
		} catch (Exception e) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Das Windows Look and Feel konnte leider nicht gesetzt werden.",
				    "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		
		//Eigenschaften		
		this.setLocation(0, 0);
		this.setTitle("Lagerverwaltung");
		this.setMinimumSize(new Dimension(800, 600));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {		
				int i = JOptionPane.showOptionDialog(null, "Wollen Sie das Programm wirklich beenden?",
						"Programm schlie�en?", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, new String[] {"Ja", "Nein"}, "Ja");
				if(i == JOptionPane.YES_OPTION) {
					 actionlistener.speichern();
					 System.exit(0);
				 }
			}
		});
		
		//Men�
		menueErzeugen();
				
		//Startpanels
		startpanelsErzeugen();
		
		//Button f�r Menupanel
		iconmenuErzeugen();
		
		//Style
		stylebtn(oeffnenbtn);
		stylebtn(beendenbtn);
		stylebtn(speichernbtn);
		stylebtn(anzeigenLagerinhaltbtn);
		stylebtn(entnehmenbtn);
		stylebtn(einlagernbtn);
		
		//Hilfsbox
		hilfsboxErzeugen();
		
		//ProgressBar
		progressbarErzeugen();
		
		
		//Actionlistener
		oeffnenItem.addActionListener(e -> {
			try {
				actionlistener.oeffnen(this);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Es kam beim �ffnen der Datei zu einem Fehler, "
						+ "veruschen Sie es erneut oder laden sie eine andere Datei.",
						"Fehler", JOptionPane.ERROR_MESSAGE);
			}
		});
		beendenItem.addActionListener(e -> actionlistener.beenden());
		speichernItem.addActionListener(e -> actionlistener.speichern());
		anzeigenLagerinhaltItem.addActionListener(e -> actionlistener.anzeigenLagerinhalt(this, menupanel, leftpanel, rightpanel, fachauslastungItem));
		entnehmenItem.addActionListener(e -> actionlistener.entnehmen(this));
		einlagernItem.addActionListener(e -> actionlistener.einlagern(this, null, null, null));
		startseiteItem.addActionListener(e -> actionlistener.startseite(this, leftpanel, rightpanel, menupanel, fachauslastungItem));
		fachauslastungItem.addActionListener(e -> actionlistener.fachauslastungDialog(this, rightpanel));
		
		oeffnenbtn.addActionListener(e -> {
			try {
				actionlistener.oeffnen(this);
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(null, "Es kam beim �ffnen der Datei zu einem Fehler, "
						+ "veruschen Sie es erneut oder laden sie eine andere Datei.",
						"Fehler", JOptionPane.ERROR_MESSAGE);
			}
		});
		speichernbtn.addActionListener(e -> actionlistener.speichern());
		anzeigenLagerinhaltbtn.addActionListener(e -> actionlistener.anzeigenLagerinhalt(this, menupanel, leftpanel, rightpanel, fachauslastungItem));
		entnehmenbtn.addActionListener(e -> actionlistener.entnehmen(this));
		einlagernbtn.addActionListener(e -> actionlistener.einlagern(this, null, null, null));
		beendenbtn.addActionListener(e -> actionlistener.beenden());
	
	}
	
	/**
	 * Erzeugt die Panels f�r die Start�bersicht bestehend aus dem Iconmen�, Auslastung des Lagers und ein Bild
	 * Zus�tzlich werden Eigenschaften dieser Panels gesetzt
	 */
	private void startpanelsErzeugen() {
		CustomColor = new Color(80, 80, 80);
		menupanel = new JPanel();
		leftpanel = new JPanel();
		rightpanel = new CustomPanel();
		
		menupanel.setLayout(new BoxLayout(menupanel, BoxLayout.LINE_AXIS));
		leftpanel.setLayout(new GridBagLayout());
		rightpanel.setLayout(new BorderLayout());
		leftpanel.setBorder(BorderFactory.createCompoundBorder((BorderFactory.createLineBorder(Color.BLACK)), new EmptyBorder(0, 20, 0, 20)));
		leftpanel.setBackground(CustomColor);

		Image image = null;
		try {
			image = ImageIO.read(new File("../Lagerverwaltung_AOPII/img/lager.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Beim �ffnen des Bildes f�r das rechte Panel kam es zu einem Fehler, "
					+ "gehen Sie sicher, dass sich das Bild lager.jpg im Ordner img unter Lagerverwaltung_AOPII befindet",
					"Fehler", JOptionPane.ERROR_MESSAGE);
		}
		rightpanel.setImage(image);
		
		this.add(menupanel,gbcErzeugen(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.BOTH));
		this.add(leftpanel, gbcErzeugen(0, 1, 1, 1, 0.25, 0.0, GridBagConstraints.BOTH));
		this.add(rightpanel,gbcErzeugen(1, 1, 1, 1, 0.75, 1.0, GridBagConstraints.BOTH));
	}

	/**
	 * Erzeugt das Men� am oberen linken Rand des Programms: Dabei gibt es als die zwei Hauptmen�s Datei und Zur�ck
	 * Die Hauptmen�s haben dabei noch einzelne Men�unterpunkte
	 */
	private void menueErzeugen() {
		menubar = new JMenuBar();
		menuDatei = new JMenu("Datei");
		menuZurueck = new JMenu("Zur�ck");
		menuFachauslastung = new JMenu("Fachauslastung");
		oeffnenItem = new JMenuItem("Datei laden");
		speichernItem = new JMenuItem("Speichern");
		anzeigenLagerinhaltItem = new JMenuItem ("Lagerinhalt anzeigen");
		entnehmenItem = new JMenuItem("Entnehmen eines Teils");
		einlagernItem = new JMenuItem("Einlagern eines Teils");
		beendenItem = new JMenuItem("Beenden");
		startseiteItem = new JMenuItem("Zur�ck zur Startseite");
		fachauslastungItem = new JMenuItem("Fachauslastung anzeigen");
		menuDatei.add(oeffnenItem);
		menuDatei.add(menuDatei);
		menuDatei.add(speichernItem);
		menuDatei.add(anzeigenLagerinhaltItem);
		menuDatei.add(entnehmenItem);
		menuDatei.add(einlagernItem);
		menuDatei.add(beendenItem);
		menuZurueck.add(startseiteItem);
		menuFachauslastung.add(fachauslastungItem);
		menubar.add(menuDatei);
		menubar.add(menuZurueck);
		menubar.add(menuFachauslastung);
		
		oeffnenItem.setToolTipText("Erm�glicht Ihnen eine Datei zu laden.");
		speichernItem.setToolTipText("Erm�glicht Ihnen eine Datei zu speichern.");
		anzeigenLagerinhaltItem.setToolTipText("Erm�glicht Ihnen den Lagerinhalt anzuzeigen.");
		entnehmenItem.setToolTipText("Erm�glicht Ihnen das Entnehmen eines Teiles.");
		einlagernItem.setToolTipText("Erm�glicht Ihnen das Einlagern eines Teiles.");
		beendenItem.setToolTipText("Erm�glicht Ihnen das Programm zu schlie�en.");
		startseiteItem.setToolTipText("Erm�glicht Ihnen auf die Startseite zur�ckzukehren.");
		fachauslastungItem.setToolTipText("Erm�glicht Ihnen die Auslastung eines Faches zu sehen.");
		
		this.setJMenuBar(menubar);
	}

	/**
	 * Erzeugt das Iconmen�, welches sich auf dem menupanel befindet. Es werden die Icons geladen und die Buttons erzeugt
	 */
	private void iconmenuErzeugen() {
		open = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/open.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH ));
		palletstore = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/palletstore.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH));	
		pallettake = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/pallettake.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH ));
		save = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/diskette.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH ));
		stock = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/stock.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH ));
		close = new ImageIcon(new ImageIcon("../Lagerverwaltung_AOPII/icons/close.png").getImage().getScaledInstance( 55, 55,  java.awt.Image.SCALE_SMOOTH ));
		
		oeffnenbtn = new JButton("Datei �ffnen", open);
		speichernbtn = new JButton("Speichern", save);
		anzeigenLagerinhaltbtn = new JButton("Lagerinhalt anzeigen", stock);
		entnehmenbtn = new JButton("Entnehmen", pallettake);
		einlagernbtn = new JButton("Einlagern", palletstore);
		beendenbtn = new JButton("Beenden", close);
		
		oeffnenbtn.setToolTipText("Erm�glicht Ihnen eine Datei zu laden.");
		speichernbtn.setToolTipText("Erm�glicht Ihnen eine Datei zu speichern.");
		anzeigenLagerinhaltbtn.setToolTipText("Erm�glicht Ihnen den Lagerinhalt anzuzeigen.");
		entnehmenbtn.setToolTipText("Erm�glicht Ihnen das Entnehmen eines Teiles.");
		einlagernbtn.setToolTipText("Erm�glicht Ihnen das Einlagern eines Teiles.");
		beendenbtn.setToolTipText("Erm�glicht Ihnen das Programm zu schlie�en.");
	}
	
	/**
	 * Dient zur Ausrichtung der jeweiligen Iconmen�buttons, sodass diese immer den gleichen Abstand zueiander haben
	 * Geschieht mit Hilfe einer Box und deren createGlue methode
	 */
	private void hilfsboxErzeugen() {
		hilfsbox = Box.createHorizontalBox();
		hilfsbox.setBorder(new EmptyBorder(10, 0, 5, 0));
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(oeffnenbtn);
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(speichernbtn);
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(anzeigenLagerinhaltbtn);
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(entnehmenbtn);
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(einlagernbtn);
		hilfsbox.add(Box.createGlue());
		hilfsbox.add(beendenbtn);
		hilfsbox.add(Box.createGlue());
		menupanel.add(hilfsbox);
	}

	/**
	 * Erzeugt die Progressbars und die Label f�r die Veranschaulichung der Auslastung des Lagers
	 * Die Progressbar und die Labels werden erzeugt und gewisse Eigenschaften gesetzt
	 */
	private void progressbarErzeugen() {
		freierPlatzBar = new CustomProgressBar(8000);
		freieFaecherBar = new CustomProgressBar(800);
		freierPlatzLabel = new JLabel();
		freieFaecherLabel = new JLabel();
		ueberschriftLabel = new JLabel("Aktueller Stand des Lagers:");
		hilfslabel = new JLabel();
		
		freierPlatzBar.setModel(new DefaultBoundedRangeModel(0, 0 , 0, 8000));
		freieFaecherBar.setModel(new DefaultBoundedRangeModel(0, 0, 0, 800));
		freierPlatzBar.setUI(new CustomProgressBarUI());
		freieFaecherBar.setUI(new CustomProgressBarUI());
		freierPlatzBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		freieFaecherBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		freierPlatzBar.setStringPainted(true);
		freieFaecherBar.setStringPainted(true);		
		freierPlatzBar.setBorder(new EmptyBorder(8,0,8,0));
		freieFaecherBar.setBorder(new EmptyBorder(8,0,8,0));
		
		freierPlatzLabel.setBorder(new EmptyBorder(0, 5, 10, 0));
		freieFaecherLabel.setBorder(new EmptyBorder(10, 5, 10, 0));
		freierPlatzLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  15));
		freieFaecherLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  15));
		freierPlatzLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		freieFaecherLabel.setVerticalAlignment(SwingConstants.BOTTOM);

		aktualisierenProgressbar(daten.getOccupied(), daten.getfreieRegalfaecher());
		
		ueberschriftLabel.setBorder(new EmptyBorder(0, 5, 10, 0));
		ueberschriftLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  15));
		ueberschriftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ueberschriftLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		
		leftpanel.add(ueberschriftLabel, gbcErzeugen(0, 0, 1 ,1, 1.0, 0.46, GridBagConstraints.BOTH));
		leftpanel.add(freierPlatzLabel, gbcErzeugen(0, 1, 1, 1, 1.0, 0.01, GridBagConstraints.BOTH));
		leftpanel.add(freierPlatzBar, gbcErzeugen(0, 2, 1, 1, 0.0, 0.01, GridBagConstraints.BOTH));
		leftpanel.add(freieFaecherLabel, gbcErzeugen(0, 3, 1, 1, 0.0, 0.05, GridBagConstraints.BOTH));
		leftpanel.add(freieFaecherBar, gbcErzeugen(0, 4, 1, 1, 0.0, 0.01, GridBagConstraints.BOTH));
		leftpanel.add(hilfslabel, gbcErzeugen(0, 5, 1, 1, 0.0, 0.46, GridBagConstraints.BOTH));
	}

	/**
	 * Setzt die Value der Bars und �ndert den Text der Labels entsprechend der Werte
	 * 
	 * @param freierPlatz die Zahl, wie viele Platz frei sind
	 * @param freieFaecher die Zahl, wie viele F�cher frei sind
	 */
	public void aktualisierenProgressbar(int freierPlatz, int freieFaecher) {
		freieFaecherBar.setValue(800 - freieFaecher);
		freierPlatzBar.setValue(freierPlatz);
		freierPlatzLabel.setText("Belegter Platz: \t"+ freierPlatzBar.getValue() +"/" + freierPlatzBar.getMaximum());
		freieFaecherLabel.setText("Belegte F�cher: \t"+ freieFaecherBar.getValue() +"/" + freieFaecherBar.getMaximum());
	}

	
	/**
	 * Dient zum "Stylen" des jeweiligen Iconmen�buttons, es werden die ben�tigten Eigenschaften gesetzt
	 * 
	 * @see iconmenuErzeugen
	 * 
	 * @param component der Button, welcher diese Eigenschaften erhalten soll
	 */
	private void stylebtn(JButton component) {
		component.setSize(75, 75);
		component.setMargin(new Insets(0,0,0,0));
		component.setBorder(null);
		component.setVerticalTextPosition(SwingConstants.BOTTOM);
		component.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	/**
	 * Erzeugt den Ergebnisdialog, welcher nach dem Einlagern zu sehen ist, abh�ngig von der Gr��e des verwendeten Monitors
	 * 
	 * @param eingabebezeichnung dient zur Ausgabe des Bezeichnung, welche das Teil beim Einlagern erhielt
	 * @param eingabeteilenummer dient zur Ausgabe der Teilenummer, welche das Teil beim Einlagern erhielt
	 * @param ergebnis enth�lt Informationen �ber die Wege, welche das Transportsystem zur�ck gelegt hat
	 */
	public void einlagernErgebnisDialog(JTextField eingabebezeichnung, JTextField eingabeteilenummer, int[] ergebnis) {
		screensize = actionlistener.getScreensize();
		ergebnisDialog = new JDialog();
		teilLabel = new JLabel();
		ergebnisLabel = new JLabel[3];
		ausschriftLabel = new JLabel[3];
		btnok = new JButton("OK");
		eborder = new EmptyBorder(0, 20, 0, 0);
		
		
		if(eingabeteilenummer.getText().length() > 0) {
			teilLabel.setText("Das Teil mit der Bezeichnung " + eingabebezeichnung.getText() + 
				" und der Teilenummer " + eingabeteilenummer.getText() + 
				" wurde erfolgreich eingelagert");
		}
		else {
			teilLabel.setText("Das Teil mit der Bezeichnung " + eingabebezeichnung.getText() + 
					" wurde erfolgreich eingelagert");
		}
		
		for(int i = 0; i < ergebnisLabel.length; i++) {
			ergebnisLabel[i] = new JLabel(ergebnis[i+1] + " Meter");
		}
		
		
		for(int i = 0; i < ausschriftLabel.length; i++) {
			ausschriftLabel[i] = new JLabel();
			ausschriftLabel[i].setBorder(eborder);
		}
		
		ausschriftLabel[0].setText("Der Fahrtweg in x-Richtung betrug: ");
		ausschriftLabel[1].setText("Der Fahrtweg in y-Richtung betrug: ");
		ausschriftLabel[2].setText("Der Fahrtweg in z-Richtung betrug: ");
		
		ergebnisDialog.setTitle("Teil wurde in das Lager aufgenommen.");
		ergebnisDialog.setLayout(new GridBagLayout());
		
		btnok.setBorder(new EmptyBorder(7, 25, 7, 25));		

		ergebnisDialog.add(ausschriftLabel[0], gbcErzeugen(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));
	
		ergebnisDialog.add(ergebnisLabel[0], gbcErzeugen(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ausschriftLabel[1], gbcErzeugen(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ergebnisLabel[1], gbcErzeugen(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ausschriftLabel[2], gbcErzeugen(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ergebnisLabel[2], gbcErzeugen(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(btnok, gbcErzeugen(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.VERTICAL));
		
		ergebnisDialog.add(teilLabel, gbcErzeugen(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.VERTICAL));
		
		ergebnisDialog.setSize(screensize.width, screensize.height);
		ergebnisDialog.setLocationRelativeTo(null);
		ergebnisDialog.setVisible(true);
		
		//ActionListener
		btnok.addActionListener(e -> schliesseErgebnisDialog(ergebnisDialog));
		btnok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "beenden");
		btnok.getActionMap().put("beenden", new EscAction(ergebnisDialog) );
	}

	/**
	 * Erzeugt den Ergebnisdialog, welcher nach dem entnehmen zu sehen ist, abh�ngig von der Gr��e des verwendeten Monitors
	 * 
	 * @param teilenamen enth�lt die Information entweder �ber die Bezeichnung des Teils oder die Teilenummer
	 * @param ergebnis enth�lt Informationen �ber die Wege, welche das Transportsystem zur�ck gelegt hat
	 */
	public void entnehmenErgebnisDialog(String teilenamen, int[] ergebnis) {
		screensize = actionlistener.getScreensize();
		ergebnisDialog = new JDialog();
		teilLabel = new JLabel();
		ergebnisLabel = new JLabel[3];
		ausschriftLabel = new JLabel[3];
		btnok = new JButton("OK");
		eborder = new EmptyBorder(0, 20, 0, 0);
		
		if(pruefeString(teilenamen)) {
			teilLabel.setText("Das Teil mit der Teilenummer " + teilenamen + " wurde erfolgreich entnommen");	
		}
		else {
			teilLabel.setText("Das Teil mit der Bezeichnung " + teilenamen + " wurde erfolgreich entnommen");
		}
		
		for(int i = 0; i < ergebnisLabel.length; i++) {
			ergebnisLabel[i] = new JLabel(ergebnis[i+1] + " Meter");
		}
		
		
		for(int i = 0; i < ausschriftLabel.length; i++) {
			ausschriftLabel[i] = new JLabel();
			ausschriftLabel[i].setBorder(eborder);
		}
		
		ausschriftLabel[0].setText("Der Fahrtweg in x-Richtung betrug: ");
		ausschriftLabel[1].setText("Der Fahrtweg in y-Richtung betrug: ");
		ausschriftLabel[2].setText("Der Fahrtweg in z-Richtung betrug: ");
		
		ergebnisDialog.setTitle("Teil wurde aus dem Lager entnommen.");
		ergebnisDialog.setLayout(new GridBagLayout());
		
		btnok.setBorder(new EmptyBorder(7, 25, 7, 25));

		ergebnisDialog.add(ausschriftLabel[0], gbcErzeugen(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ergebnisLabel[0], gbcErzeugen(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ausschriftLabel[1], gbcErzeugen(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ergebnisLabel[1], gbcErzeugen(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ausschriftLabel[2], gbcErzeugen(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(ergebnisLabel[2], gbcErzeugen(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH));

		ergebnisDialog.add(btnok, gbcErzeugen(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.VERTICAL));
		
		ergebnisDialog.add(teilLabel, gbcErzeugen(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.VERTICAL));
		
		ergebnisDialog.setSize(screensize.width, screensize.height);
		ergebnisDialog.setLocationRelativeTo(null);
		ergebnisDialog.setVisible(true);

		//Actionlistener
		btnok.addActionListener(e -> schliesseErgebnisDialog(ergebnisDialog));
		btnok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "beenden");
		btnok.getActionMap().put("beenden", new EscAction(ergebnisDialog) );		
	}

	
	/**
	 * Versucht den String in eine Zahl umzuwandenl und je nachdem ob dies klappt wird true oder false zur�ck gegeben
	 * 
	 * @param teilenamen enth�lt die Information entweder �ber die Bezeichnung des Teils oder die Teilenummer
	 */
	public boolean pruefeString(String teilenamen) {
		boolean testbestanden;
		try {
			Integer.parseInt(teilenamen);
		    testbestanden = true;
		} catch(NumberFormatException e) {
		      testbestanden = false;
		   }
		   return testbestanden;
	}
	
	/**
	 * Schlie�t das Fenster des Ergebnisdialogs, abh�ngig von dem �bergebenem Parameter
	 * 
	 * @param ergebnisdialog erh�lt Informationen dar�ber, welches Fenster(JDialog) geschlossen werden soll
	 */
	public void schliesseErgebnisDialog(JDialog ergebnisdialog) {
		ergebnisdialog.dispose();
	}
	
	/**
	 * Erzeugt das richtige GridBagConstraints in Abh�ngigkeit von den �bergebenen Parametern
	 * 
	 * @param gridx setzt gridx von gbc auf den jeweiligen Wert
	 * @param gridy setzt gridy von gbc auf den jeweiligen Wert
	 * @param gridwidth setzt gridwidth von gbc auf den jeweiligen Wert
	 * @param gridheight setzt gridheight von gbc auf den jeweiligen Wert
	 * @param weightx setzt weightx von gbc auf den jeweiligen Wert
	 * @param weighty setzt weighty von gbc auf den jeweiligen Wert
	 * @param fill setzt fill von gbc auf den jeweiligen Wert
	 * 
	 * @return GridBagConstraints gbc: wird zur Positionierung im GridBagLayout ben�tigt
	 */
	public GridBagConstraints gbcErzeugen(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.fill = fill;
		return gbc;
	}

}
