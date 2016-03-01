package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import algorithm.Agent;
import algorithm.LeaderElection;
import algorithm.MyRingList;

public class GUI {

	private int quantity;
	private MyPanel panelWithPicture;
	private MyRingList list;
	private int taskStep;
	private JSplitPane rightSplit;
	private JFrame frame;
	private SwingWorker<Void, Void> sw;
	private JButton setup;
	private JButton stepButtop;
	private JButton go;
	private JButton stop;
	private JTextField quantityField;
	private JComboBox<Object> comboBoxForMode;
	private JTextArea textAreaForList;
	private JRadioButton hand;
	private JTextPane textAreaForTextMode;
	private StringBuilder textModeString;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				UIManager
						.put("OptionPane.messageDialogTitle", "��������������");
				new GUI();
			}
		});
	}

	public GUI() {
		frame = new JFrame("LEADER ELECTION");

		setData();
		createPanelWithPicture();

		ImageIcon image = new ImageIcon("images/leaderIcon.png");
		frame.setIconImage(image.getImage());

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				Object[] options = { "��", "���!" };
				int n = JOptionPane.showOptionDialog(e.getWindow(),
						"������� ����?", "�������������",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (n == 0) {
					e.getWindow().setVisible(false);
					System.exit(0);
				}
			}

		});

		frame.setMinimumSize(new Dimension(1000, 700));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(createMainPanel());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		quantityField.requestFocus();
	}

	private Component createMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(createButtonPanel(), BorderLayout.WEST);
		mainPanel.add(createRightSplit(), BorderLayout.CENTER);
		return mainPanel;
	}

	private Component createRightSplit() {
		rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		rightSplit.setTopComponent(panelWithPicture);
		rightSplit.setDividerLocation(0.9);
		rightSplit.setResizeWeight(0.95);
		textAreaForTextMode = new JTextPane();
		JPanel temp = new JPanel(new BorderLayout());
		temp.add(textAreaForTextMode, BorderLayout.CENTER);
		textAreaForTextMode.setFont(textAreaForTextMode.getFont().deriveFont(
				16f));
		JScrollPane scroll = new JScrollPane(temp);
		rightSplit.setBottomComponent(scroll);
		rightSplit.setVisible(false);
		return rightSplit;
	}

	private void setData() {
		ArrayList<Integer> items = new ArrayList<>();
		list = new MyRingList();
		for (int i = 0; i < quantity; i++) {
			items.add(i + 1);
		}

		while (!items.isEmpty()) {
			Random r = new Random();
			int index = r.nextInt(items.size());
			list.add(new Agent(items.get(index)));
			items.remove(index);
		}
	}

	private JPanel createButtonPanel() {
		JPanel infoPanel = new JPanel(new MigLayout());
		createSetupButton();
		createStepButton();
		createGoButton();
		createStopButton();
		setButtonsVisibility(true, false, false, false);

		JPanel panelForButtons = new JPanel(new MigLayout());
		panelForButtons.add(setup, "sg 1");
		panelForButtons.add(stepButtop, "sg 1, wrap");
		panelForButtons.setBorder(new EmptyBorder(0, 30, 0, 30));
		panelForButtons.add(go, "sg 1");
		panelForButtons.add(stop, "sg 1");

		infoPanel.add(panelForButtons, "wrap");

		JPanel panelForQuantityAndMode = new JPanel(new MigLayout());
		panelForQuantityAndMode.add(new JLabel("<html>������<br>������: "),
				"gap, sg 2");
		quantityField = new JTextField(4);
		panelForQuantityAndMode.add(quantityField, "wrap");
		panelForQuantityAndMode.add(new JLabel("�����: "), "gap, sg 2");
		comboBoxForMode = new JComboBox<>();
		comboBoxForMode.addItem("����������������");
		comboBoxForMode.addItem("���������������");
		panelForQuantityAndMode.add(comboBoxForMode, "wrap");
		panelForQuantityAndMode.setBorder(new EmptyBorder(0, 50, 0, 50));

		infoPanel.add(panelForQuantityAndMode, "wrap");

		JPanel panelForListGenereate = new JPanel(new MigLayout());

		TitledBorder tb = new TitledBorder(new LineBorder(Color.BLACK, 1));
		tb.setTitle("������������ �����");
		tb.setTitleJustification(TitledBorder.CENTER);
		panelForListGenereate.setBorder(new TitledBorder(tb));

		JRadioButton random = new JRadioButton(
				"<html>��������<br>�������������");
		random.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaForList.setEnabled(false);
			}
		});
		hand = new JRadioButton("<html>������<br>�������");
		hand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaForList.setEnabled(true);
				textAreaForList.requestFocus();
			}
		});
		JPanel panelForRandomAndHandle = new JPanel(new MigLayout());
		panelForRandomAndHandle.add(random);
		panelForRandomAndHandle.add(hand, "wrap");

		panelForListGenereate.add(panelForRandomAndHandle, "wrap");

		ButtonGroup gr = new ButtonGroup();
		gr.add(random);
		gr.add(hand);
		random.setSelected(true);

		textAreaForList = new JTextArea(1, 30);
		textAreaForList.setFont(textAreaForList.getFont().deriveFont(12f));
		textAreaForList.setEnabled(false);
		JScrollPane scrollForList = new JScrollPane(textAreaForList);
		scrollForList
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollForList
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelForListGenereate.add(scrollForList);

		infoPanel.add(panelForListGenereate, "wrap");

		return infoPanel;
	}

	private void createStopButton() {
		stop = new JButton(" stop ");
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sw.cancel(true);
				setButtonsVisibility(true, true, true, false);
			}
		});
	}

	private void createGoButton() {
		go = new JButton("  go  ");
		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonsVisibility(false, false, false, true);
				sw = new SwingWorkerForButtonGo<Void, Void>();
				sw.execute();
			}
		});
	}

	private void createStepButton() {
		stepButtop = new JButton(" step ");
		stepButtop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (taskStep == 0) {
					textModeString.append("<b>��������� ���������:</b><br>");
					textAreaForTextMode.setText(textModeString.toString());
				}
				LeaderElection.solve(list, taskStep++);
				textModeString.append("<b>��� " + taskStep + ":</b> "
						+ list.printMsgs() + "<br>");
				textAreaForTextMode.setText(textModeString.toString());
				if (taskStep == quantity) {
					taskStep = 0;
					textModeString
							.append("<b><font size = 6>����� ������!!! Id ������: "
									+ quantity + "</font></b><br>");
					textAreaForTextMode.setText(textModeString.toString());
				}
				updateFrame();
			}
		});
	}

	private void createSetupButton() {
		setup = new JButton("setup");
		setup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					quantity = Integer.valueOf(quantityField.getText());
				} catch (Exception exc) {
					if (exc.getMessage().equals("For input string: \"\"")) {
						JOptionPane.showMessageDialog(null,
								"���������� ������� ������ ������");
					} else {
						JOptionPane
								.showMessageDialog(null,
										"������������ ����: ���������� ������� ������ ����� �����");
						quantityField.setText("");
					}
					quantityField.requestFocus();
					return;
				}
				if (hand.isSelected()) {
					list = new MyRingList();
					String[] items = textAreaForList.getText().split(",");
					LinkedHashSet<Integer> intItems = new LinkedHashSet<>();
					for (int i = 0; i < items.length; i++) {
						try {
							int element = Integer.valueOf(items[i].trim());
							if (!intItems.add(element)) {
								JOptionPane
										.showMessageDialog(
												null,
												"������������ ��������: "
														+ "�� ������ ���� ������������� ���������");
								textAreaForList.requestFocus();
								return;
							}
							if (element <= 0) {
								JOptionPane
										.showMessageDialog(
												null,
												"������������ ��������: "
														+ "�������� ������ ���� ������ ����");
								textAreaForList.requestFocus();
								return;
							}
						} catch (Exception exc) {
							JOptionPane
									.showMessageDialog(
											null,
											"������������ ������ �����: "
													+ "���������� ������� ������ ����� ����� ����� �������");
							textAreaForList.requestFocus();
							textAreaForList.setText("");
							return;
						}
					}
					if (intItems.size() != quantity) {
						JOptionPane.showMessageDialog(null,
								"������������ ������ �����: "
										+ "�������� ���������� ���������");
						textAreaForList.requestFocus();
						return;
					}
					Iterator<Integer> iter = intItems.iterator();
					while (iter.hasNext()) {
						list.add(new Agent(iter.next()));
					}
				} else {
					setData();
					taskStep = 0;
				}
				panelWithPicture.setQuantity(quantity);
				if (comboBoxForMode.getSelectedItem()
						.equals("����������������")) {
					panelWithPicture.setMode(false);
				} else {
					panelWithPicture.setMode(true);
				}
				textAreaForTextMode.setContentType("text/html");
				textModeString = new StringBuilder(
						"<b>������������ �����:</b> " + list + "<br>");
				textAreaForTextMode.setText(textModeString.toString());
				updateFrame();
				stepButtop.setEnabled(true);
				go.setEnabled(true);
				setButtonsVisibility(true, true, true, false);
				rightSplit.setVisible(true);
				if (quantity >= 40) {
					rightSplit.setDividerLocation(0.5);
					rightSplit.setResizeWeight(0.5);
				}
				frame.revalidate();
			}

		});
	}

	private void createPanelWithPicture() {
		panelWithPicture = new MyPanel();
		updateFrame();
	}

	private void updateFrame() {
		panelWithPicture.setList(list);
		panelWithPicture.setStep(taskStep);
		frame.repaint();
	}

	private void setButtonsVisibility(boolean setupVisibility,
			boolean stepVisibility, boolean goVisibility, boolean stopVisibility) {
		setup.setEnabled(setupVisibility);
		stepButtop.setEnabled(stepVisibility);
		go.setEnabled(goVisibility);
		stop.setEnabled(stopVisibility);
	}

	private class SwingWorkerForButtonGo<T, V> extends SwingWorker<T, V> {

		@Override
		protected T doInBackground() throws Exception {
			while (taskStep != quantity) {
				Thread.sleep(2000);
				LeaderElection.solve(list, taskStep++);
				publish();
			}
			taskStep = 0;
			return null;
		}

		@Override
		protected void process(List<V> chunks) {
			if (taskStep == 1) {
				textModeString.append("<b>��������� ���������:</b><br>");
				textAreaForTextMode.setText(textModeString.toString());
			}
			textModeString.append("<b>��� " + taskStep + ":</b> "
					+ list.printMsgs() + "<br>");
			textAreaForTextMode.setText(textModeString.toString());
			updateFrame();
		}

		@Override
		protected void done() {
			updateFrame();
			setButtonsVisibility(true, true, true, false);
			stop.setEnabled(false);
			setup.setEnabled(true);
			stepButtop.setEnabled(true);
			go.setEnabled(true);
			if (taskStep == 0) {
				textModeString
						.append("<b><font size = 6>����� ������!!! Id ������: "
								+ quantity + "</font></b><br>");
				textAreaForTextMode.setText(textModeString.toString());
			}
		}

	}

}
