import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PracProjForm extends JFrame {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtNumOfWor;
    private JCheckBox finishedCheckBox;
    private JTextField txtDate;
    private JRadioButton rb1;
    private JRadioButton rb3;
    private JButton previousButton;
    private JButton nextButton;
    private JButton saveButton;
    private JRadioButton rb2;
    private JTextField txtCost;
    public List<PracProj> listOfProjects = new ArrayList<>();
    private int index = 0;

    public PracProjForm() {
        setContentPane(mainPanel);
        setTitle("Projects");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        initMenu();
        addProjects();
        previousButton.addActionListener(e -> prev());
        nextButton.addActionListener(e -> next());
        saveButton.addActionListener(e -> saveToFile());
        ButtonGroup btnGrp = new ButtonGroup();
        btnGrp.add(rb1);
        btnGrp.add(rb2);
        btnGrp.add(rb3);
        display(getProject(index));
    }

    public void addProjects() {
        PracProj p1 = new PracProj("Těstoviny", 2, new BigDecimal(5000), 1, LocalDate.of(1978, 5, 5), Boolean.TRUE);
        PracProj p2 = new PracProj("Zničení mandelinky bramborové", 1500, new BigDecimal(2500000), 2, LocalDate.of(1949, 7, 1), Boolean.TRUE);
        PracProj p3 = new PracProj("Jak slevnit na daních", 5, new BigDecimal(15000), 3, LocalDate.of(2022, 12, 12), Boolean.FALSE);
        listOfProjects.add(p1);
        listOfProjects.add(p2);
        listOfProjects.add(p3);
    }

    public void prev() {
        if (index > 0) {
            index--;
            display(getProject(index));
        }
    }

    public void next() {
        if (index < listOfProjects.size() - 1) {
            index++;
            display(getProject(index));
        }
    }

    public void display(PracProj prj) {
        txtName.setText(prj.getName());
        txtNumOfWor.setText(String.valueOf(prj.getNumberOfWorkers()));
        finishedCheckBox.setSelected(prj.isFinished());
        txtCost.setText(String.valueOf(prj.getProbableCost()));
        txtDate.setText(prj.getDateOfStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        switch (prj.getProjectRating()) {
            case 1 -> rb1.setSelected(true);
            case 2 -> rb2.setSelected(true);
            case 3 -> rb3.setSelected(true);
        }
    }

    public PracProj getProject(int i) {
        return listOfProjects.get(i);
    }

    public void addNewProject() {
        JTextField nameField = new JTextField();
        JTextField numOfWorkersField = new JTextField();
        JTextField probableCostField = new JTextField();
        JComboBox<Integer> projectRatingBox = new JComboBox<>(new Integer[]{1, 2, 3});
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        JCheckBox finishedCheckBox = new JCheckBox("Finished", false);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Number of Workers:"));
        panel.add(numOfWorkersField);
        panel.add(new JLabel("Probable Cost:"));
        panel.add(probableCostField);
        panel.add(new JLabel("Project Rating:"));
        panel.add(projectRatingBox);
        panel.add(new JLabel("Date (dd/MM/yyyy):"));
        panel.add(dateField);
        panel.add(finishedCheckBox);

        int result;
        try {
            result = JOptionPane.showConfirmDialog(null, panel, "Add New Project", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                int numOfWorkers = Integer.parseInt(numOfWorkersField.getText());
                BigDecimal probableCost = new BigDecimal(probableCostField.getText());
                int projectRating = (int) projectRatingBox.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                boolean finished = finishedCheckBox.isSelected();

                PracProj newProject = new PracProj(name, numOfWorkers, probableCost, projectRating, date, finished);
                listOfProjects.add(newProject);
                JOptionPane.showMessageDialog(this, "New Project added");
            }
        } catch (NumberFormatException | DateTimeParseException | NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please ensure all fields are filled in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void showStats() {
        JOptionPane.showMessageDialog(this, "Number of finished projects: " + costOfAllprojects());
    }

    private BigDecimal costOfAllprojects() {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (PracProj pp : listOfProjects) {
            if (pp.isFinished()) {
                totalCost = totalCost.add(pp.getProbableCost());
            }
        }
        return totalCost;
    }

    public void saveToFile() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            if (!selectedFile.getName().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "File Exists", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            WriteIntoFile(selectedFile);
            JOptionPane.showMessageDialog(this, "Successfully saved projects into a text file");
        } else if (result == JFileChooser.CANCEL_OPTION) {
            File newFile = new File("default_project_list.txt");
            WriteIntoFile(newFile);
            JOptionPane.showMessageDialog(this, "Successfully saved projects into a new file: " + newFile.getName());
        }
    }


    public void WriteIntoFile(File selectedFile) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(selectedFile)))) {
            for (PracProj pp : listOfProjects) {
                writer.println(pp.getName() + "#" + pp.getProjectRating() + "#" + (pp.isFinished() ? "yes" : "no") + "#" + pp.getProbableCost() + "#" + pp.getDateOfStart() + "#" + pp.getNumberOfWorkers());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initMenu() {
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);

        JMenu jmP = new JMenu("Project");
        jmb.add(jmP);

        JMenuItem jmA = new JMenuItem("Add another");
        jmP.add(jmA);
        jmA.addActionListener(e -> addNewProject());

        JMenuItem jmS = new JMenuItem("Statistics");
        jmb.add(jmS);
        jmS.addActionListener(e -> showStats());
    }

    public static void main(String[] args) {
        PracProjForm p = new PracProjForm();
        p.setVisible(true);
    }
}
