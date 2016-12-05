package cl.usm.telematica.sigamobile;

/**
 * Created by Pipos on 03-12-2016.
 */

public class Course {
    private String initial;
    private String name;
    private String professor;
    private String parallel;
    private int credits;
    private String score;
    private String evaluation;
    private String status;
    private String period;

    public Course(){}

    public Course(String initial, String name, String professor, String parallel, int credits, String score,
                  String evaluation, String status, String period){
        this.initial = initial;
        this.name = name;
        this.professor = professor;
        this.parallel = parallel;
        this.credits = credits;
        this.score = score;
        this.evaluation = evaluation;
        this.status = status;
        this.period = period;
    }
    public Course(String initial, String name, String professor, String parallel, int credits, String score,
                  String evaluation, String status){
        this.initial = initial;
        this.name = name;
        this.professor = professor;
        this.parallel = parallel;
        this.credits = credits;
        this.score = score;
        this.evaluation = evaluation;
        this.status = status;
        this.period = "";
    }

    public String getPeriod() {return period;}

    public void setPeriod(String period) {this.period = period;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getEvaluation() {return evaluation;}

    public void setEvaluation(String evaluation) {this.evaluation = evaluation;}

    public String getScore() {return score;}

    public void setScore(String score) {this.score = score;}

    public int getCredits() {return credits;}

    public void setCredits(int credits) {this.credits = credits;}

    public String getParallel() {return parallel;}

    public void setParallel(String parallel) {this.parallel = parallel;}

    public String getProfessor() {return professor;}

    public void setProfessor(String professor) {this.professor = professor;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getInitial() {return initial;}

    public void setInitial(String initial) {this.initial = initial;}
}
