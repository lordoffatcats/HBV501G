package org.team5.api.feature.course;

import org.springframework.stereotype.Service;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.NotFoundException;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.account.AccountRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public CourseServiceImpl(AccountRepository accountRepository, CourseRepository courseRepository) {
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Course createCourse(String name) {
        // TODO: Generate a join code
        return courseRepository.save(new Course(name, name));
    }

    @Override
    public void joinCourse(UUID courseId, UUID accountId, String joinCode) {
        // TODO: Exceptions
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));

        if (!course.getJoinCode().equals(joinCode)) {
            throw new BadRequestException("Invalid join code");
        }

        if (course.getMembers().contains(account)) {
            throw new BadRequestException("User is already in course");
        }

        course.getMembers().add(account);
        courseRepository.save(course);
    }

    @Override
    public void leaveCourse(UUID courseId, UUID accountId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));

        if (!course.getMembers().contains(account)) {
            throw new BadRequestException("User is not in course");
        }

        course.getMembers().remove(account);
        courseRepository.save(course);
    }

    @Override
    public List<Course> getUserCourses(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));
        return account.getCourses();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
