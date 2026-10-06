package org.team5.api.feature.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.exceptions.NotFoundException;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.account.AccountRepository;
import org.team5.api.feature.course.CourseRepository;
import org.team5.api.feature.discussion.Thread;
import org.team5.api.feature.discussion.ThreadRepository;

import java.util.UUID;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ThreadRepository threadRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public ReplyServiceImpl(
            ReplyRepository replyRepository,
            ThreadRepository threadRepository,
            AccountRepository accountRepository,
            CourseRepository courseRepository) {
        this.replyRepository = replyRepository;
        this.threadRepository = threadRepository;
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public Reply createReply(UUID threadId, UUID accountId, String content) {
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new NotFoundException("Discussion thread not found"));

        Account author = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        UUID courseId = thread.getCourse().getId();

        if (!courseRepository.existsByIdAndMembersId(courseId, accountId)) {
            throw new ForbiddenException("You must be a member of the course to reply to this thread");
        }

        Reply reply = new Reply(
                content.trim(),
                author,
                thread
        );

        return replyRepository.save(reply);
    }

    @Override
    @Transactional
    public Reply updateReply(UUID id, UUID accountId, String content) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reply not found"));

        if (!reply.getAuthor().getId().equals(accountId)) {
            throw new ForbiddenException("You can only edit your own reply");
        }

        reply.setContent(content.trim());

        return replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void deleteReply(UUID id, UUID accountId) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reply not found"));

        if (!reply.getAuthor().getId().equals(accountId)) {
            throw new ForbiddenException("You can only delete your own reply");
        }

        replyRepository.deleteById(id);
    }
}