/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.argseditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.imagegen.JuliaSetImage;
import com.deepnighttwo.gen.imagegen.JuliaSetProgressListener;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.impl.CommonTaskArg;
import com.deepnighttwo.ui.guibuilder.EditorSection;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.componets.Mandelbrot;
import com.deepnighttwo.ui.guibuilder.componets.SpinnerDouble;
import com.deepnighttwo.ui.guibuilder.componets.SpinnerInt;
import com.deepnighttwo.ui.guibuilder.xml.XMLGUIBuilder;
import com.deepnighttwo.util.AppUtilities;
import com.deepnighttwo.util.ResourceUtil;

/**
 * the whole main UI panel
 * 
 * @author mzang
 * 
 */
public class JuliaSetArgsPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JuliaSetPanel juliaPane;

    // private JProgressBar progressBar;
    private JButton help;
    private JButton save;
    private JDialog saveDialog;
    private JDialog logDialog;
    private JDialog helpDialog;
    private JTextArea logArea;
    private JComboBox imageFormat;
    private JCheckBox enableLog;
    private StringBuffer unshowedLog = new StringBuffer(5000);

    private JButton showLog;
    private JuliaSetArgs juliaSetArgs = new JuliaSetArgs();
    private JLabel perfLabel = new JLabel();

    private Insets noSpace = new Insets(0, 0, 0, 0);
    private Insets space = new Insets(5, 0, 0, 0);

    private Map<String, PropertyLine> components = new HashMap<String, PropertyLine>();

    private IGenPostTask logTask = new IGenPostTask() {

        @Override
        public void postTask(JuliaSetImage image) {
            JuliaSetArgs lastArgs = image.getLastJuliaSetArg();
            Dimension size = new Dimension(lastArgs.getImageWidth(), lastArgs.getImageHeight());
            handleLog(System.currentTimeMillis() - image.getStartTime(), size);
        }

        public IGenPostTask getNext() {
            return null;
        }

        @Override
        public void setNext(IGenPostTask next) {
            /*
             * bcz this is a common obj, if next is set here, there is no where to clean it and it will be executed
             * every time with log. If need add a next to a log task, just create a new impl of IGenPostTask
             */
            throw new RuntimeException("Log task has no next task");
        }

    };

    private IGenPostTask saveImageTask = new SaveImageTask();
    private IGenPostTask saveImageTaskWithLog = new SaveImageTask(logTask);

    class SaveImageTask implements IGenPostTask {

        private IGenPostTask next;

        public SaveImageTask() {

        }

        public SaveImageTask(IGenPostTask next) {
            this.next = next;
        }

        @Override
        public void postTask(JuliaSetImage image) {
            saveImage();
            JuliaSetArgs args = juliaPane.getJuliaImage().getLastJuliaSetArg();
            JuliaSetArgs redrawArgs = new JuliaSetArgs();
            redrawArgs.copy(args);
            int width = juliaPane.getWidth();
            int height = juliaPane.getHeight();
            redrawArgs.setImageWidth(width);
            redrawArgs.setImageHeight(height);
            juliaPane.addJuliaTask(new CommonTaskArg(redrawArgs, null, null, next), true);
        }

        public IGenPostTask getNext() {
            return next;
        }

        @Override
        public void setNext(IGenPostTask next) {
            this.next = next;
        }
    }

    private boolean logFlag = false;

    private static String PERF_MSG = ResourceUtil.getString("PERF_MSG");
    private static String PERF_LOG = ResourceUtil.getString("PERF_MSG_LOG") + "\r\n";

    public JuliaSetArgsPanel() {
        init();
    }

    private void init() {
        juliaPane = new JuliaSetPanel(true, juliaSetArgs, logTask);
        juliaPane.getJuliaImage().addJuliaProgressListener(new PreviewJuliaImagePL());
        save = new JButton(ResourceUtil.getString("Save Image"));
        help = new JButton(ResourceUtil.getString("help btn"));
        showLog = new JButton(ResourceUtil.getString("SHOW_DIALOG"));

        imageFormat = new JComboBox();
        {
            imageFormat.addItem("bmp");
            imageFormat.addItem("jpeg");
            imageFormat.addItem("png");
            imageFormat.addItem("gif");
            imageFormat.setSelectedItem("bmp");
        }

        enableLog = new JCheckBox(ResourceUtil.getString("Enable Log"));
        enableLog.setSelected(false);

        save.addActionListener(this);
        showLog.addActionListener(this);
        enableLog.addActionListener(this);
        help.addActionListener(this);

        createSaveDialog();
        createLogDialog();
        createHelpDialog();

        buildMiniGUI();
    }

    private void createSaveDialog() {
        saveDialog = new JDialog();
        Container container = saveDialog.getContentPane();
        container.setLayout(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane();
        container.add(scrollPanel, BorderLayout.CENTER);
        container.add(save, BorderLayout.SOUTH);
    }

    private void createLogDialog() {
        logDialog = new JDialog();
        logDialog.setTitle(ResourceUtil.getString("PERF_DIALOG_TITLE"));
        logArea = new JTextArea();
        logDialog.setSize(600, 700);
        Container container = logDialog.getContentPane();
        container.setLayout(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(logArea);
        container.add(scrollPanel, BorderLayout.CENTER);
        JButton clearLog = new JButton(ResourceUtil.getString("PERF_CLEAR"));
        clearLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                unshowedLog.delete(0, unshowedLog.length());
                logArea.setText("");
            }

        });
        container.add(clearLog, BorderLayout.SOUTH);
    }

    private void createHelpDialog() {
        helpDialog = new JDialog();
        helpDialog.setTitle(ResourceUtil.getString("help_title"));
        helpDialog.setSize(850, 350);
        Container container = helpDialog.getContentPane();
        JLabel helpLbl = new JLabel(ResourceUtil.getString("help"));
        try {
            if (Locale.getDefault().toString().toLowerCase().contains("zh")) {
                helpLbl.setFont(new Font("Microsoft YaHei", Font.PLAIN, 10));
            }
        } catch (Throwable e) {

        }
        container.setLayout(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(helpLbl);
        container.add(scrollPanel, BorderLayout.CENTER);

    }

    private void buildMiniGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(juliaPane);
        add(scrollPanel, gbc);

        gbc.gridx++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        JComponent controlPanel = buildTabControlPanel();
        add(controlPanel, gbc);
    }

    private JPanel buildTabControlPanel() {
        JPanel controlPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setBorder(new TitledBorder("Settings"));

        List<EditorSection> sections = XMLGUIBuilder.parseGUIXML("com/deepnighttwo/ui/argseditor/juliaset.xml");

        for (EditorSection section : sections) {
            JPanel sectionPanel = new JPanel();
            // sectionPanel.setBorder(new
            // TitledBorder(section.getSectionLabel()));
            sectionPanel.setLayout(new GridBagLayout());

            List<PropertyLine> props = section.getProps();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            for (PropertyLine prop : props) {
                prop.setArgsPanel(juliaPane);
                prop.setArgs(juliaSetArgs);

                Component label = prop.getLabel();
                Component content = prop.getContent();
                if (label == null && content != null) {
                    gbc.gridx = 0;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    gbc.gridwidth = 2;
                    sectionPanel.add(content, gbc);
                } else if (label != null && content == null) {
                    gbc.gridx = 0;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    gbc.gridwidth = 2;
                    sectionPanel.add(label, gbc);
                } else {
                    gbc.gridwidth = 1;

                    gbc.gridx = 0;
                    gbc.insets = space;
                    gbc.weightx = 0.0;
                    gbc.weighty = 0.0;
                    sectionPanel.add(label, gbc);

                    gbc.gridx = 1;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    sectionPanel.add(content, gbc);
                }

                gbc.gridy++;

                components.put(prop.getFieldName(), prop);
            }
            gbc.gridx = 0;
            gbc.weighty = 1.0;
            gbc.gridwidth = 2;
            sectionPanel.add(new JPanel(), gbc);

            tabbedPane.addTab(section.getSectionLabel(), sectionPanel);
        }

        try {
            int coreNumber = Runtime.getRuntime().availableProcessors();
            SpinnerInt workThread = (SpinnerInt) components.get("threadCount");
            workThread.setValueForAll(coreNumber);
        } catch (Throwable e) {

        }

        juliaSetArgs.ajustJuliaArgsForImageGen("init");

        IGenPostTask syncConstValuesTask = new IGenPostTask() {

            IGenPostTask next = null;
            SpinnerDouble constX = (SpinnerDouble) components.get("constReal");
            SpinnerDouble constY = (SpinnerDouble) components.get("constImaginary");

            @Override
            public void postTask(JuliaSetImage image) {
                JuliaSetArgs args = image.getLastJuliaSetArg();
                constX.setTrigger(false);
                constY.setTrigger(false);
                constX.getSpinner().setValue(args.getConstReal());
                constY.getSpinner().setValue(args.getConstImaginary());
            }

            @Override
            public IGenPostTask getNext() {
                return next;
            }

            @Override
            public void setNext(IGenPostTask next) {
                this.next = next;
            }

        };

        Mandelbrot mp = (Mandelbrot) components.get("mandelbrot");

        mp.setSyncConstValues(syncConstValuesTask);

        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        controlPanel.add(tabbedPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(buildOperationSettingPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JPanel fill = new JPanel();
        controlPanel.add(fill, gbc);

        return controlPanel;
    }

    private JPanel buildOperationSettingPanel() {

        JPanel operatePanel = new JPanel();
        operatePanel.setLayout(new GridBagLayout());
        operatePanel.setBorder(new TitledBorder(ResourceUtil.getString("Operation")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = noSpace;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;

        gbc.gridwidth = 2;
        operatePanel.add(help, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.insets = space;
        operatePanel.add(imageFormat, gbc);
        gbc.gridx++;
        gbc.weightx = 1.0;
        operatePanel.add(save, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        operatePanel.add(enableLog, gbc);
        gbc.gridx++;
        gbc.weightx = 1.0;
        operatePanel.add(showLog, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        operatePanel.add(perfLabel, gbc);
        perfLabel.setText("  \r\n\r\n    ");

        // gbc.gridy++;
        // gbc.gridx = 0;
        // gbc.weighty = 1.0;
        // operatePanel.add(new ValueZonePanel(juliaPane), gbc);
        return operatePanel;

    }

    class PreviewJuliaImagePL implements JuliaSetProgressListener {

        @Override
        public void progressChanged() {
            juliaPane.repaint();
        }

    }

    private void handleLog(long calculatingTime, Dimension size) {
        String perfMsg = String.format(PERF_MSG, calculatingTime,
                "W=" + juliaPane.getWidth() + ", H=" + juliaPane.getHeight());
        String perfLog = String.format(PERF_LOG, calculatingTime, "W=" + size.getWidth() + ", H=" + size.getHeight());
        perfLabel.setText(perfMsg);
        if (logDialog.isVisible()) {
            logArea.append(perfLog);
        } else {
            unshowedLog.append(perfLog);
        }
    }

    private void saveImage() {
        try {
            String format = imageFormat.getSelectedItem().toString();
            String defaultFileNmae = "r=" + juliaSetArgs.getConstReal() + ", i=" + juliaSetArgs.getConstImaginary()
                    + "." + format;
            File file = new File(defaultFileNmae);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(file);
            int result = fileChooser.showSaveDialog(JuliaSetArgsPanel.this);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(format)) {
                filePath += "." + format;
            }
            BufferedImage image = (BufferedImage) juliaPane.getJuliaImage().getImage();
            ImageIO.write(image, format, new File(filePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void drawAndSaveImage() {
        JuliaSetArgs args = juliaPane.getJuliaImage().getLastJuliaSetArg();
        JuliaSetArgs saveArgs = new JuliaSetArgs();
        saveArgs.copy(args);
        SpinnerInt width = (SpinnerInt) components.get("imageWidth");
        SpinnerInt height = (SpinnerInt) components.get("imageHeight");
        saveArgs.adjustToDeminsion(width.getValue(), height.getValue());
        juliaPane.addRawJuliaTask(new CommonTaskArg(saveArgs, null, null, logFlag ? saveImageTaskWithLog
                : saveImageTask), true);
    }

    private void showTimeLog() {
        logArea.append(unshowedLog.toString());
        unshowedLog.delete(0, unshowedLog.length());
        AppUtilities.showCenterDialog(logDialog);
    }

    private void handleEnableLog() {
        logFlag = enableLog.isSelected();
        juliaPane.setLogFlag(logFlag);
        if (logFlag == false) {
            unshowedLog.delete(0, unshowedLog.length());
            logArea.setText("");
            perfLabel.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            drawAndSaveImage();
        } else if (e.getSource() == showLog) {
            showTimeLog();
        } else if (e.getSource() == enableLog) {
            handleEnableLog();
        } else if (e.getSource() == help) {
            helpDialog.setVisible(true);
        }
    }
}
