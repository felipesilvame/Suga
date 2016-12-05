package cl.usm.telematica.sigamobile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pipos on 03-12-2016.
 */

public class CurricularPeriod {
    private List<Course> courses;
    private String period;
    private int credits;
    private int coursesSize;

    public CurricularPeriod(){
        courses = new ArrayList<>();
        credits = 0;
        coursesSize = 0;
        period = "";
    }

    public void addCourse(Course course){
        courses.add(course);
        credits += course.getCredits();
        coursesSize++;
    }

    public void removeCourse(int index){
        credits -= courses.get(index).getCredits();
        courses.remove(index);
        coursesSize--;
    }

    public List<Course> getCourses() {return courses;}

    public void setCourses(ArrayList<Course> courses) {this.courses = courses;}

    public String getPeriod() {return period;}

    public void setPeriod(String period) {this.period = period;}

    public int getCredits() {return credits;}

    public void setCredits(int credits) {this.credits = credits;}

    public int getCoursesSize() {return coursesSize;}

    public void setCoursesSize(int coursesSize) {this.coursesSize = coursesSize;}
}
