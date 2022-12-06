package org.b07boys.walnut.lists;

import org.b07boys.walnut.courses.Course;

public class CourseListAdapter2 extends CourseListAdapter{

    private Course course;

    public CourseListAdapter2(Course course) {
        super(course);
        this.course = course;
    }

    @Override
    public String toString() {

        return course.getCode() + " " + course.getName();
    }
}
