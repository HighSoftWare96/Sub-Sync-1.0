package gb.en.subSyncGUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SSFrame extends JFrame {

	public static JLabel statusLbl;
	public static JTextField barraRicerca;

	/**
	 * Create the frame.
	 */
	public SSFrame() {
		super();


		// trying to conform the UI to the current system == miracoloso!!
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}

		setTitle("Sub Sync 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(420, 200);
		setResizable(false);
		JPanel nordPnl = new JPanel();
		nordPnl.setLayout(new FlowLayout());
		barraRicerca = new JTextField();
		barraRicerca.setText("Insert the path to the .srt file");
		nordPnl.add(barraRicerca);
		JButton sfogliaBtn = new JButton("Browse");
		sfogliaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// selettore della cartella predefinito di swing
				JFileChooser chooser = new JFileChooser();

				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".srt files", "srt");
				chooser.setFileFilter(filter);

				// apro una cartella di dialogo
				int returnVal = chooser.showOpenDialog(nordPnl);

				// stampo il percorso scelto nella barra a fianco
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					barraRicerca.setText(chooser.getSelectedFile().toString());
				}
			}
		});
		nordPnl.add(sfogliaBtn);

		JPanel centerPnl = new JPanel();
		centerPnl.setLayout(new FlowLayout());
		SpinnerModel modelForSeconds = new SpinnerNumberModel(0, // initial
				-2000, // min
				2000, // max
				1); // step
		JSpinner secondChoice = new JSpinner(modelForSeconds);

		SpinnerModel modelForMSSeconds = new SpinnerNumberModel(0, // initial
				-999, // min
				999, // max
				1); // step

		JSpinner msSecondChoice = new JSpinner(modelForMSSeconds);
		centerPnl.add(new JLabel("Seconds:"));
		centerPnl.add(secondChoice);
		centerPnl.add(new JLabel("MilliSeconds:"));
		centerPnl.add(msSecondChoice);

		JButton enterAction = new JButton("Proceed");
		enterAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// se i valori degli spinner non sono nulli e il file esiste
				if ((Integer) secondChoice.getValue() != 0 || (Integer) msSecondChoice.getValue() != 0) {
					statusLbl.setText("Working...");
					SubSyncGUI.seconds = (int) secondChoice.getValue();
					SubSyncGUI.mSeconds = (int) msSecondChoice.getValue();
					SubSyncGUI.syncronizeFile();
					// statusLbl.setText("End");
				}

			}
		});

		centerPnl.add(enterAction);

		JPanel sudPnl = new JPanel();
		sudPnl.setLayout(new BorderLayout());
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		statusLbl = new JLabel("");
		sudPnl.add(statusLbl, BorderLayout.NORTH);
		sudPnl.add(exit, BorderLayout.SOUTH);

		Container frmContentPane = this.getContentPane();
		frmContentPane.setLayout(new BorderLayout(0, 0));
		frmContentPane.add(nordPnl, BorderLayout.NORTH);
		frmContentPane.add(centerPnl, BorderLayout.CENTER);
		frmContentPane.add(sudPnl, BorderLayout.SOUTH);
		setIconImage(new ImageIcon("icon.png").getImage());
		setVisible(true);

	}

}
