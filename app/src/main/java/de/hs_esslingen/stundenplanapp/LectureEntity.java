package de.hs_esslingen.stundenplanapp;

/**
 * Created by admin on 13.06.2017.
 */

public class LectureEntity {
    public LectureEntity(String name, String lecturer, String room, String dayOfWeek,
                         String time, String cancelledOn, String courseOfStudies, String semester,
                         int studentTakesCourse) {
        this.name = name;
        this.lecturer = lecturer;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.cancelledOn = cancelledOn;
        this.courseOfStudies = courseOfStudies;
        this.semester = semester;
        this.studentTakesCourse = studentTakesCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(String cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public String getCourseOfStudies() {
        return courseOfStudies;
    }

    public void setCourseOfStudies(String courseOfStudies) {
        this.courseOfStudies = courseOfStudies;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getStudentTakesCourse() {
        return studentTakesCourse;
    }

    public void setStudentTakesCourse(int studentTakesCourse) {
        this.studentTakesCourse = studentTakesCourse;
    }

    private String name;
    private String lecturer;
    private String room;
    private String dayOfWeek;
    private String time;
    private String cancelledOn; // TO DO: fill by Parser
    private String courseOfStudies;
    private String semester;
    private int studentTakesCourse;
}
