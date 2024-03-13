import java.math.BigDecimal;
import java.time.LocalDate;

public class PracProj {
    private String name;
    private int numberOfWorkers;
    private BigDecimal probableCost;
    private int projectRating;
    public LocalDate dateOfStart;
    private boolean finished;

    public PracProj(String name, int numberOfWorkers, BigDecimal probableCost, int projectRating, LocalDate dateOfStart, boolean finished) {
        this.name = name;
        this.numberOfWorkers = numberOfWorkers;
        this.probableCost = probableCost;
        this.projectRating = projectRating;
        this.dateOfStart = dateOfStart;
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public BigDecimal getProbableCost() {
        return probableCost;
    }

    public void setProbableCost(BigDecimal probableCost) {
        this.probableCost = probableCost;
    }

    public int getProjectRating() {
        return projectRating;
    }

    public void setProjectRating(int projectRating) {
        this.projectRating = projectRating;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
