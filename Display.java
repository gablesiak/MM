package project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*
 * GUI
 */
public class Display extends JFrame {

    private static final long serialVersionUID = 1L;
    private Grid panel_1 = null;
    private JPanel contentPane;
    private String[] sasiedztwo = new String[]{"Moore", "von Neumann", "Further Moore"};
    private String[] typeInclu = new String[]{"Square", "Round"};

    public Display() {
        setTitle("Multiscale Modeling");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700); //window settings

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


        JMenuItem mntmZamknij = new JMenuItem("Close");
        mntmZamknij.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        JButton mnImportBitMap = new JButton("Import from PNG");
        mnImportBitMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File toRead = fc.getSelectedFile();
                        panel_1.readGrid(ImageIO.read(toRead));
                        System.out.println("Image imported");
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                    repaint();
                }
            }
        });


        JButton mnImportFile = new JButton("Import from File");
        mnImportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {


                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File toRead = fc.getSelectedFile();
                        BufferedInputStream ois = new BufferedInputStream(new FileInputStream(toRead));
                        panel_1.clean();
                        panel_1.readGrid(ois);
                        repaint();
                        ois.close();

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            }


        });


        JButton mnExportBitMap = new JButton("Export to PNG");
        mnExportBitMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {


                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    //1 bitmap without panel
                    try {
                        File toRead = fc.getSelectedFile();
                        Robot robot = new Robot();
                        Point p = panel_1.getLocationOnScreen();
                        BufferedImage bitmap = robot.createScreenCapture(new Rectangle((int) p.getX(), (int) p.getY(), panel_1.getWidth(), panel_1.getHeight()));
                        ImageIO.write(bitmap, "png", toRead);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        JButton mnExportFile = new JButton("Export to File");
        mnExportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        System.out.println("Export started");
                        File toRead = fc.getSelectedFile();
                        BufferedOutputStream oos = new BufferedOutputStream(new FileOutputStream(toRead));
                        oos.write(panel_1.gridAsString().getBytes());
                        oos.flush();
                        oos.close();
                        System.out.println("Export finished");
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
            }
        });

        menuBar.add(mnExportFile);
        menuBar.add(mnExportBitMap);
        menuBar.add(mnImportFile);
        menuBar.add(mnImportBitMap);


        //================================================
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createTitledBorder("Simple Growth"));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBackground(Color.white);
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.LINE_END);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Custom"));
        panel.add(Box.createVerticalGlue());

        final JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox_1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent argP) {
                Core.Config.neigh = comboBox_1.getSelectedIndex();
            }
        });
        comboBox_1.setBackground(new Color(255, 255, 255));
        comboBox_1.setModel(new DefaultComboBoxModel(this.sasiedztwo));

        final JComboBox comboBox_TypeInclu = new JComboBox();
        comboBox_TypeInclu.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox_TypeInclu.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent argP) {
                Core.Config.typeInclu = comboBox_TypeInclu.getSelectedIndex();
            }
        });
        comboBox_TypeInclu.setModel(new DefaultComboBoxModel(this.typeInclu));


        // =================================	window generator	================================

        panel_1 = new Grid();
        panel_1.setBackground(Color.BLUE);
        contentPane.add(panel_1, BorderLayout.CENTER);

        new Thread(panel_1).start();

        // =================================	generating	================================
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });

        JButton btnGenerate = new JButton("GROWTH");
        btnGenerate.setBackground(Color.GREEN);
        btnGenerate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Core.Config.menuSaveMap) {
                    System.out.println("NOT AVAILABLE YET");
                } else {
                    Display.this.panel_1.clearGrid();
                    Display.this.panel_1.naive();
                }
                repaint();
            }
        });

        JLabel lblIloZiaren = new JLabel("Number of grains:");


        final JSpinner spinner = new JSpinner();
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Core.Config.points = (Integer) spinner.getValue();
            }
        });
        spinner.setModel(new SpinnerNumberModel(Core.Config.points, 1, 360, 1));

        JButton btnReset = new JButton("RESET");
        btnReset.setBackground(Color.BLACK);
        btnReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Display.this.panel_1.clean();
                repaint();
            }
        });

        JLabel lblInclusion = new JLabel("Amount of inclusion");
        final JSpinner spinnerInclusion = new JSpinner();
        spinnerInclusion.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Core.Config.amountInclu = (Integer) spinnerInclusion.getValue();
            }
        });


        final JSpinner spinnerInclusionSize = new JSpinner();
        spinnerInclusionSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Core.Config.sizeInclu = (Integer) spinnerInclusionSize.getValue();
            }
        });

        JButton btnInclusion = new JButton("ADD INCLUSION");
        btnInclusion.setBackground(Color.BLACK);
        btnInclusion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Display.this.panel_1.addInclusionAfter();
                repaint();
            }
        });


        final JCheckBox chckInclusion = new JCheckBox("Inclusion Before");
        chckInclusion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("Checked? " + chckInclusion.isSelected());
                Core.Config.inclusionBefore = chckInclusion.isSelected();
            }
        });

        JButton btnSubsc1 = new JButton("Subsctructure");
        btnSubsc1.addActionListener(e -> {
            Display.this.panel_1.clearGrid();
            repaint();
        });

        JLabel lblEmpty = new JLabel(" ");
        JLabel lblEmpty1 = new JLabel(" ");
        JLabel lblEmpty2 = new JLabel(" ");
        btnGenerate.setMaximumSize(new Dimension(200, 200));
        btnReset.setMaximumSize(new Dimension(200, 200));
        comboBox_1.setMaximumSize(new Dimension(200, 200));
        btnInclusion.setMaximumSize(new Dimension(200, 200));
        comboBox_TypeInclu.setMaximumSize(new Dimension(200, 200));
        chckInclusion.setMaximumSize(new Dimension(200, 200));
        spinner.setMaximumSize(new Dimension(200, 200));
        spinnerInclusion.setMaximumSize(new Dimension(200, 200));
        spinnerInclusionSize.setMaximumSize(new Dimension(200, 200));
        JLabel lblSizeIncl = new JLabel("Choose Size of Inclusion");
        JLabel lblTypeInclu = new JLabel("Choose Type of Inclusion");
        JLabel lblEmpty3 = new JLabel(" ");
        btnSubsc1.setMaximumSize(new Dimension(200, 200));


        panel.add(btnSubsc1, BoxLayout.X_AXIS);
        panel.add(btnInclusion, BoxLayout.X_AXIS);
        panel.add(spinnerInclusion, BoxLayout.X_AXIS);
        panel.add(lblInclusion, BoxLayout.X_AXIS);
        panel.add(spinnerInclusionSize, BoxLayout.X_AXIS);
        panel.add(lblSizeIncl, BoxLayout.X_AXIS);
        panel.add(comboBox_TypeInclu, BoxLayout.X_AXIS);
        panel.add(lblTypeInclu, BoxLayout.X_AXIS);

        panel.add(lblEmpty1, BoxLayout.X_AXIS);


        panel.add(spinner, BoxLayout.X_AXIS); //number of grains

        panel.add(lblIloZiaren, BoxLayout.X_AXIS); //number of grains
        panel.add(lblEmpty, BoxLayout.X_AXIS);
        panel.add(comboBox_1, BoxLayout.X_AXIS); //neighb

        panel.add(lblEmpty, BoxLayout.X_AXIS);

        panel.add(lblEmpty2, BoxLayout.X_AXIS);
        panel.add(chckInclusion, BoxLayout.X_AXIS);
        panel.add(btnGenerate, BoxLayout.X_AXIS);

    }

}
