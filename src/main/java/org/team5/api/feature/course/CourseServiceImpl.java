package org.team5.api.feature.course;

import org.springframework.stereotype.Service;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ConflictException;
import org.team5.api.exceptions.NotFoundException;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.account.AccountRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        String joinCode = generateJoinCode();
        return courseRepository.save(new Course(name, joinCode));
    }

    @Override
    public void joinCourse(UUID courseId, UUID accountId, String joinCode) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));

        if (!course.getJoinCode().equals(joinCode)) {
            throw new BadRequestException("Invalid join code");
        }

        if (courseRepository.existsByIdAndMembersId(courseId, accountId)) {
            throw new ConflictException("User is already in course");
        }

        course.getMembers().add(account);
        courseRepository.save(course);
    }

    @Override
    public void leaveCourse(UUID courseId, UUID accountId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));

        if (!courseRepository.existsByIdAndMembersId(courseId, accountId)) {
            throw new ConflictException("User is not in course");
        }

        course.getMembers().remove(account);
        courseRepository.save(course);
    }

    @Override
    public List<Course> getUserCourses(UUID accountId) {
        return courseRepository.findByMembersId(accountId);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    private String generateJoinCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String joinCode;

        do {
            joinCode = IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(chars.charAt(random.nextInt(chars.length()))))
                .collect(Collectors.joining());
        } while (courseRepository.existsByJoinCode(joinCode));

        return joinCode;
    }
}
