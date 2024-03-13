import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PracProjForm extends JFrame{
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

    public PracProjForm(){
        setContentPane(mainPanel);
        setTitle("Projects");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
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

    public void addProjects(){
        PracProj p1 = new PracProj("Těstoviny", 2, new BigDecimal(5000), 1, LocalDate.of(1978, 5,5), Boolean.TRUE);
        PracProj p2 = new PracProj("Zničení mandelinky bramborové", 1500, new BigDecimal(2500000), 2, LocalDate.of(1949, 7,1), Boolean.TRUE);
        PracProj p3 = new PracProj("Jak slevnit na daních", 5, new BigDecimal(15000), 3, LocalDate.of(2022, 12,12), Boolean.TRUE);
        listOfProjects.add(p1);
        listOfProjects.add(p2);
        listOfProjects.add(p3);
    }

    public void prev(){
        if (index > 0){
            index--;
            display(getProject(index));
        }
    }

    public void next(){
        if (index < listOfProjects.size()-1){
            index++;
            display(getProject(index));
        }
    }

    public void display(PracProj prj){
        txtName.setText(prj.getName());
        txtNumOfWor.setText(String.valueOf(prj.getNumberOfWorkers()));
        finishedCheckBox.setSelected(prj.isFinished());
        txtCost.setText(String.valueOf(prj.getProbableCost()));
        txtDate.setText(String.valueOf(prj.getDateOfStart()));
        switch (prj.getProjectRating()){
            case 1 -> rb1.setSelected(true);
            case 2 -> rb2.setSelected(true);
            case 3 -> rb3.setSelected(true);
        }
    }

    public PracProj getProject(int i){
        return listOfProjects.get(i);
    }

    public void addNewProject(String name, int numberOfWorkers, BigDecimal probableCost, int projectRating, LocalDate dateOfStart, boolean finished){
        PracProj newProject = new PracProj(name, numberOfWorkers, probableCost, projectRating, dateOfStart, finished);
        newProject.setName(txtName.getText());
        newProject.setNumberOfWorkers(Integer.parseInt((txtNumOfWor.getText())));
        newProject.setProbableCost(BigDecimal.valueOf(0));
        newProject.setProjectRating(1);
        newProject.setDateOfStart(LocalDate.now());
        newProject.setFinished(false);
        listOfProjects.add(new PracProj(name, numberOfWorkers, probableCost, projectRating, dateOfStart, finished));
        JOptionPane.showMessageDialog(this, "New Game added");
    }

    public void showStats(){
        JOptionPane.showMessageDialog(this, "Number of finished projects: " + costOfAllprojects());
    }

    private int costOfAllprojects(){
        int count = 0;
        for (PracProj pp : listOfProjects) {
            if (pp.isFinished()){
                count++;
            }
        }
        return count;
    }

    public void saveToFile() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fc.getSelectedFile();
            PracProj pracProj = listOfProjects.get(index);
            txtName.setText(pracProj.getName());
            txtNumOfWor.setText(String.valueOf(pracProj.getNumberOfWorkers()));
            finishedCheckBox.setSelected(pracProj.isFinished());
            txtCost.setText(String.valueOf(pracProj.getProbableCost()));
            txtDate.setText(String.valueOf(pracProj.getDateOfStart()));
            switch (pracProj.getProjectRating()){
                case 1 -> rb1.setSelected(true);
                case 2 -> rb2.setSelected(true);
                case 3 -> rb3.setSelected(true);
            }
            WriteIntoFile(selectedFile);
        }
        JOptionPane.showMessageDialog(this, "Succesfully saved projects into a text file");
    }


    public void WriteIntoFile(File selectedFile){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(selectedFile)))) {
            for (PracProj pp : listOfProjects) {
                writer.print(pp.getName() + "#" + pp.getProjectRating() + "#" + (pp.isFinished() ? "ano" : "ne" + "#" + pp.getProbableCost() + "#" + pp.getDateOfStart() + "#" + pp.getProjectRating() + "\n"));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getLocalizedMessage());
        }
    }

    public void initMenu(){
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);

        JMenu jmP = new JMenu("Project");
        jmb.add(jmP);

        JMenuItem jmA = new JMenuItem("Add another");
        jmP.add(jmA);
        jmA.addActionListener(e-> addNewProject(txtName.getText(), 0, BigDecimal.valueOf(0),1,LocalDate.now(),false));

        JMenuItem jmS = new JMenuItem("Statistics");
        jmb.add(jmS);
        jmS.addActionListener(e-> showStats());
    }

    public static void main(String[] args) {
        PracProjForm p = new PracProjForm();
        p.setVisible(true);
    }
}
