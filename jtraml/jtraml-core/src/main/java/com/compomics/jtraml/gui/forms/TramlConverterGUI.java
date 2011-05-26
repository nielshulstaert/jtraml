package com.compomics.jtraml.gui.forms;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.gui.TramlConverterMessaging;
import com.compomics.jtraml.interfaces.FileModel;
import com.compomics.jtraml.model.rowmodel.AgilentQQQImpl;
import com.compomics.jtraml.model.rowmodel.ThermoTSQImpl;
import com.compomics.jtraml.thread.TSVToTRAMLJob;
import com.compomics.jtraml.validation.DataModelValidator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import org.divxdede.swing.busy.FutureBusyModel;
import org.divxdede.swing.busy.JBusyComponent;
import sun.misc.ConditionLock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is a
 */
public class TramlConverterGUI {

    private static Logger logger = Logger.getLogger(TramlConverterGUI.class);


    private JPanel jpanContent;
    private JPanel jpanBottom;
    public JPanel jpanCenter;
    public JRadioButton btnExportThermo;
    public JRadioButton btnExportAgilent;
    public JRadioButton btnExportABI;
    public JRadioButton btnImportTraml;
    public JRadioButton btnExportTraml;
    public JPanel jpanHeader;
    public JLabel lblInputFile;
    public JRadioButton btnImportThermo;
    public JRadioButton btnImportAgilent;
    public JRadioButton btnImportABI;
    public JButton btnConvert;
    public JLabel lblArrow;
    public JPanel jpanArrow;
    public JPanel jpanFiles;
    public JButton btnOutputSelect;
    public JButton btnInputSelect;
    public JLabel lblOutputFile;
    private JBusyComponent iBusyComponent;
    private ApplicationDataModel iDataModel;
    public JFrame iFrame;


    /**
     * Constructs a new SepToTraml instance with a JPanel as a root component.
     */
    public TramlConverterGUI() {

        iDataModel = new ApplicationDataModel();
        iDataModel.setObserver(this);

        iFrame = new JFrame("TramlConverterGUI");
        iFrame.setContentPane($$$getRootComponent$$$());


//        iFrame.pack();
        iFrame.setSize(544, 500);

        iFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        iFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exit();
            }
        });


        // 1) First ask the input file!

        while (iDataModel.hasInputFile() == false) {
            showInputFileDialog();
        }


        iFrame.setVisible(true);


        // Apply the current datamodel.
        getData(iDataModel);

        TramlConverterMessaging.setAnchorComponent($$$getRootComponent$$$());


        setListeners();

    }

    /**
     * Set the listeners for all buttons and
     */
    private void setListeners() {

        btnOutputSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                try {
                    showOutputFileDialog();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

        btnConvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                System.out.println(iDataModel.toString());
                convert();
            }
        });

        btnInputSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                showInputFileDialog();
            }
        });

        btnImportThermo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setImportType(FileTypeEnum.TSV_THERMO_TSQ);
            }
        });

        btnImportAgilent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setImportType(FileTypeEnum.TSV_AGILENT_QQQ);
            }
        });

        btnImportABI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setImportType(FileTypeEnum.TSV_ABI);
            }
        });

        btnImportTraml.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setImportType(FileTypeEnum.TRAML);
            }
        });

        btnExportThermo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setExportType(FileTypeEnum.TSV_THERMO_TSQ);
            }
        });

        btnExportAgilent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setExportType(FileTypeEnum.TSV_AGILENT_QQQ);
            }
        });

        btnExportABI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setExportType(FileTypeEnum.TSV_ABI);
            }
        });

        btnExportTraml.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aActionEvent) {
                iDataModel.setExportType(FileTypeEnum.TRAML);
            }
        });

    }

    /**
     * This method will first validate current settings, and if all is fine - then the conversion will start.
     */
    private void convert() {
        DataModelValidator lValidator = new DataModelValidator(TramlConverterGUI.this);
        if (lValidator.isValidApplicationModel(iDataModel)) {
            FileTypeEnum lImportType = iDataModel.getImportType();

            FileModel lFileModel = null;

            if (lImportType == FileTypeEnum.TSV_THERMO_TSQ) {
                logger.info("starting conversion from Thermo TSQ to TraML");
                lFileModel = new ThermoTSQImpl(iDataModel.getInputFile());

            } else if (lImportType == FileTypeEnum.TSV_AGILENT_QQQ) {
                logger.info("starting conversion from Agilent QQQ to TraML");
                lFileModel = new AgilentQQQImpl(iDataModel.getInputFile());
            }

            TSVToTRAMLJob job = new TSVToTRAMLJob(lFileModel, iDataModel.getInputFile(), iDataModel.getOutputFile());
            Future lSubmit = Executors.newSingleThreadExecutor().submit(job);
            FutureBusyModel lBusyModel = new FutureBusyModel();
            lBusyModel.setFuture(lSubmit);
            lBusyModel.setCancellable(false);
            iBusyComponent.setBusyModel(lBusyModel);

            ConditionLock lConditionLock = new ConditionLock();
            synchronized (lConditionLock) {
                while (lSubmit.isDone() != true) {
                    try {
                        lBusyModel.setDescription(job.getStatus());
                        lConditionLock.wait(1000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                String s = "finished writing \t" + iDataModel.getOutputFile().getName();
                TramlConverterMessaging.confirm("finished conversion");
                logger.debug(s);
            }

        }
    }

    private void showInputFileDialog() {
        JFileChooser lFileChooser = new JFileChooser();
        lFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        lFileChooser.setCurrentDirectory(new File("/Users/kennyhelsens/Java/jtraml/src/test/resources"));

        lFileChooser.setMultiSelectionEnabled(false);
        int lApproveOption = lFileChooser.showOpenDialog(null);
        if (lApproveOption == JFileChooser.APPROVE_OPTION) {
            iDataModel.setInputFile(lFileChooser.getSelectedFile());
        } else if (iDataModel.getInputFile() == null) {
            TramlConverterMessaging.warning("You must specify a input file to convert!!");

        } else {
            // Do nothing special.
        }

    }

    private void showOutputFileDialog() throws IOException {
        JFileChooser lFileChooser = new JFileChooser();
        lFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        lFileChooser.setCurrentDirectory(new File("/Users/kennyhelsens/Desktop"));

        lFileChooser.setMultiSelectionEnabled(false);
        int lApproveOption = lFileChooser.showSaveDialog($$$getRootComponent$$$());
        if (lApproveOption == JFileChooser.APPROVE_OPTION) {
            File lSelectedFile = lFileChooser.getSelectedFile();
            if (lSelectedFile.exists()) {
                boolean lResult = TramlConverterMessaging.ask("The output file allready exists.\nDo you want to overwrite the existing file?");
                if (lResult) {
                    lSelectedFile.delete();
                    lSelectedFile.createNewFile();
                    iDataModel.setOutputFile(lSelectedFile);
                }
            } else {
                lSelectedFile.createNewFile();
                iDataModel.setOutputFile(lSelectedFile);
            }
        } else {
            TramlConverterMessaging.warning("You must specify a output file!!");
        }
    }

    public static void main(String[] args) {
        new TramlConverterGUI();
        //
    }

    /**
     * Exit the converter application.
     */
    private void exit() {
        if (TramlConverterMessaging.ask("Do you really want to exit?")) {
            iFrame.dispose();
            System.exit(0);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        iBusyComponent = new JBusyComponent();
    }


    /**
     * Set the current GUI information to the data model.
     *
     * @param data
     */
    public void setData(ApplicationDataModel data) {

        // persist the import type.
        if (btnImportThermo.isSelected()) {
            data.setImportType(FileTypeEnum.TSV_THERMO_TSQ);
        } else if (btnImportAgilent.isSelected()) {
            data.setImportType(FileTypeEnum.TSV_AGILENT_QQQ);
        } else if (btnImportABI.isSelected()) {
            data.setImportType(FileTypeEnum.TSV_ABI);
        } else if (btnImportTraml.isSelected()) {
            data.setImportType(FileTypeEnum.TRAML);
        }

        // persist the output type.
        if (btnExportThermo.isSelected()) {
            data.setExportType(FileTypeEnum.TSV_THERMO_TSQ);
        } else if (btnExportAgilent.isSelected()) {
            data.setExportType(FileTypeEnum.TSV_AGILENT_QQQ);
        } else if (btnExportABI.isSelected()) {
            data.setExportType(FileTypeEnum.TSV_ABI);
        } else if (btnExportTraml.isSelected()) {
            data.setExportType(FileTypeEnum.TRAML);
        }

    }

    public void getData(ApplicationDataModel data) {

        // Set the input file text label.
        lblInputFile.setText(data.getInputFile().getAbsolutePath());
        File lOutputFile = data.getOutputFile();
        if (lOutputFile != null) {
            lblOutputFile.setText(lOutputFile.getAbsolutePath());
        }

        FileTypeEnum lImportType = data.getImportType();
        setImportRadioButton(lImportType);

        FileTypeEnum lExportType = data.getExportType();
        setExportRadioButton(lExportType);

    }


    private void setExportRadioButton(FileTypeEnum aExportType) {
        if (aExportType == FileTypeEnum.TRAML) {
            btnExportTraml.setSelected(true);
        } else if (aExportType == FileTypeEnum.TSV_THERMO_TSQ) {
            btnExportThermo.setSelected(true);
        } else if (aExportType == FileTypeEnum.TSV_ABI) {
            btnExportABI.setSelected(true);
        } else if (aExportType == FileTypeEnum.TSV_AGILENT_QQQ) {
            btnExportAgilent.setSelected(true);
        }

        iFrame.repaint();

    }

    private void setImportRadioButton(FileTypeEnum aImportType) {
        if (aImportType == FileTypeEnum.TRAML) {
            btnImportTraml.setSelected(true);
        } else if (aImportType == FileTypeEnum.TSV_THERMO_TSQ) {
            btnImportThermo.setSelected(true);
        } else if (aImportType == FileTypeEnum.TSV_ABI) {
            btnImportABI.setSelected(true);
        } else if (aImportType == FileTypeEnum.TSV_AGILENT_QQQ) {
            btnImportAgilent.setSelected(true);
        }

        iFrame.repaint();
    }

    public void update() {
        System.out.println("update called");
        // We need to update the datamodel.
        getData(iDataModel);
        iFrame.repaint();
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanContent = new JPanel();
        jpanContent.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:3dlu:noGrow,fill:d:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        jpanCenter = new JPanel();
        jpanCenter.setLayout(new FormLayout("fill:200px:noGrow,left:4dlu:noGrow,fill:p:grow,left:4dlu:noGrow,fill:200px:noGrow", "center:40px:noGrow,top:5dlu:noGrow,center:180px:grow,fill:d:noGrow,fill:p:noGrow"));
        CellConstraints cc = new CellConstraints();
        jpanContent.add(jpanCenter, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.TOP));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 14));
        label1.setText("TraML converter");
        jpanCenter.add(label1, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(5, 15, 5, 5)));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        jpanCenter.add(panel1, new CellConstraints(1, 3, 1, 3, CellConstraints.DEFAULT, CellConstraints.TOP, new Insets(10, 10, 10, 10)));
        panel1.setBorder(BorderFactory.createTitledBorder("Import"));
        btnImportThermo = new JRadioButton();
        btnImportThermo.setSelected(false);
        btnImportThermo.setText("Thermo");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel1.add(btnImportThermo, gbc);
        btnImportAgilent = new JRadioButton();
        btnImportAgilent.setText("Agilent");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel1.add(btnImportAgilent, gbc);
        btnImportABI = new JRadioButton();
        btnImportABI.setText("ABI");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel1.add(btnImportABI, gbc);
        btnImportTraml = new JRadioButton();
        btnImportTraml.setText("TraML");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel1.add(btnImportTraml, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        jpanCenter.add(panel2, new CellConstraints(5, 3, 1, 3, CellConstraints.DEFAULT, CellConstraints.TOP, new Insets(10, 10, 10, 10)));
        panel2.setBorder(BorderFactory.createTitledBorder("Export"));
        btnExportThermo = new JRadioButton();
        btnExportThermo.setText("Thermo");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(btnExportThermo, gbc);
        btnExportAgilent = new JRadioButton();
        btnExportAgilent.setText("Agilent");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(btnExportAgilent, gbc);
        btnExportABI = new JRadioButton();
        btnExportABI.setText("ABI");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(btnExportABI, gbc);
        btnExportTraml = new JRadioButton();
        btnExportTraml.setSelected(false);
        btnExportTraml.setText("TraML");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(btnExportTraml, gbc);
        jpanArrow = new JPanel();
        jpanArrow.setLayout(new BorderLayout(0, 0));
        jpanCenter.add(jpanArrow, cc.xy(3, 3, CellConstraints.CENTER, CellConstraints.FILL));
        iBusyComponent = new JBusyComponent();
        iBusyComponent.setLayout(new FormLayout("fill:d:noGrow", "center:d:noGrow"));
        jpanArrow.add(iBusyComponent, BorderLayout.WEST);
        lblArrow = new JLabel();
        lblArrow.setBackground(UIManager.getColor("Button.background"));
        lblArrow.setIcon(new ImageIcon(getClass().getResource("/images/right.png")));
        lblArrow.setText("");
        iBusyComponent.add(lblArrow, cc.xy(1, 1));
        jpanHeader = new JPanel();
        jpanHeader.setLayout(new FormLayout("fill:d:grow", ""));
        jpanContent.add(jpanHeader, cc.xy(1, 1));
        jpanFiles = new JPanel();
        jpanFiles.setLayout(new FormLayout("left:478px:grow,left:4dlu:noGrow,fill:d:grow", "center:40px:grow,top:3dlu:noGrow,center:35px:grow,top:3dlu:noGrow,center:40px:grow,top:3dlu:noGrow,center:35px:grow"));
        jpanContent.add(jpanFiles, new CellConstraints(1, 4, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 10)));
        jpanFiles.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        final JLabel label2 = new JLabel();
        label2.setFont(UIManager.getFont("OptionPane.font"));
        label2.setText("input file:");
        jpanFiles.add(label2, new CellConstraints(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(5, 5, 5, 5)));
        final JLabel label3 = new JLabel();
        label3.setFont(UIManager.getFont("OptionPane.font"));
        label3.setText("output file:");
        jpanFiles.add(label3, new CellConstraints(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(5, 5, 5, 5)));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        jpanFiles.add(panel3, new CellConstraints(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(5, 10, 5, 5)));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-3355444)), null));
        lblInputFile = new JLabel();
        lblInputFile.setFont(UIManager.getFont("Slider.font"));
        lblInputFile.setForeground(new Color(-10066330));
        lblInputFile.setText("no file selected");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(lblInputFile, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        jpanFiles.add(panel4, new CellConstraints(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(5, 10, 5, 5)));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-3355444)), null));
        lblOutputFile = new JLabel();
        lblOutputFile.setFont(UIManager.getFont("Slider.font"));
        lblOutputFile.setForeground(new Color(-10066330));
        lblOutputFile.setText("no file selected");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(lblOutputFile, gbc);
        btnInputSelect = new JButton();
        btnInputSelect.setMaximumSize(new Dimension(29, 29));
        btnInputSelect.setMinimumSize(new Dimension(29, 29));
        btnInputSelect.setPreferredSize(new Dimension(29, 29));
        btnInputSelect.setText("...");
        jpanFiles.add(btnInputSelect, new CellConstraints(3, 3, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT, new Insets(5, 5, 5, 5)));
        btnOutputSelect = new JButton();
        btnOutputSelect.setMaximumSize(new Dimension(29, 29));
        btnOutputSelect.setMinimumSize(new Dimension(29, 29));
        btnOutputSelect.setPreferredSize(new Dimension(29, 29));
        btnOutputSelect.setText("...");
        jpanFiles.add(btnOutputSelect, new CellConstraints(3, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.CENTER, new Insets(5, 5, 5, 5)));
        jpanBottom = new JPanel();
        jpanBottom.setLayout(new FormLayout("fill:max(d;4px):grow", "center:d:noGrow"));
        jpanContent.add(jpanBottom, cc.xy(1, 6));
        btnConvert = new JButton();
        btnConvert.setIcon(new ImageIcon(getClass().getResource("/images/star.png")));
        btnConvert.setText("convert!");
        jpanBottom.add(btnConvert, new CellConstraints(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 10)));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(btnExportThermo);
        buttonGroup.add(btnExportAgilent);
        buttonGroup.add(btnExportABI);
        buttonGroup.add(btnExportTraml);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(btnImportThermo);
        buttonGroup.add(btnImportAgilent);
        buttonGroup.add(btnImportABI);
        buttonGroup.add(btnImportTraml);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
