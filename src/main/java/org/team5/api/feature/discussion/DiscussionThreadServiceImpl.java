package org.team5.api.feature.discussion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.exceptions.NotFoundException;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.account.AccountRepository;
import org.team5.api.feature.course.Course;
import org.team5.api.feature.course.CourseRepository;

import java.util.UUID;

@Service
public class DiscussionThreadServiceImpl implements DiscussionThreadService {
    private final DiscussionThreadRepository discussionThreadRepository;
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;

    public DiscussionThreadServiceImpl(
            DiscussionThreadRepository discussionThreadRepository,
            CourseRepository courseRepository,
            AccountRepository accountRepository) {
        this.discussionThreadRepository = discussionThreadRepository;
        this.courseRepository = courseRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public DiscussionThread createThread(
            UUID courseId,
            UUID authorId,
            String title,
            String content) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Account author = accountRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (!courseRepository.existsByIdAndMembersId(courseId, authorId)) {
            throw new ForbiddenException("You must be a member of the course to create a thread");
        }

        DiscussionThread thread = new DiscussionThread(
                title.trim(),
                content.trim(),
                author,
                course
        );

        return discussionThreadRepository.save(thread);
    }
}
