package org.team5.api.feature.account;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ConflictException;
import org.team5.api.exceptions.NotFoundException;
import org.team5.api.exceptions.UnauthorizedException;
import org.team5.api.feature.course.Course;
import org.team5.api.feature.course.CourseRepository;
import org.team5.api.feature.discussion.ThreadRepository;
import org.team5.api.feature.reply.ReplyRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService {
    private static final String DELETED_USERNAME = "deleted-user";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;
    private final ThreadRepository threadRepository;
    private final ReplyRepository replyRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CourseRepository courseRepository,
            ThreadRepository threadRepository,
            ReplyRepository replyRepository,
            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
        this.threadRepository = threadRepository;
        this.replyRepository = replyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(String email, String username, String password, boolean isAdmin) {
        if (DELETED_USERNAME.equalsIgnoreCase(username)) {
            throw new ConflictException("Username already exists");
        }
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new ConflictException("Username already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        return accountRepository.save(new Account(email, username, hashedPassword, isAdmin));
    }



    public Account getAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public Account updateAccount(UUID id, String email) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow( () -> new NotFoundException("Account no found with id: " + id));

        if (email != null) {
            existingAccount.setEmail(email);
        }

        return accountRepository.save(existingAccount);
    }

    @Transactional
    public void deleteAccount(UUID id) {
        Account account = getAccount(id);
        if (DELETED_USERNAME.equals(account.getUsername())) {
            throw new BadRequestException("This account cannot be deleted");
        }

        Account deletedUser = getOrCreateDeletedUser();
        threadRepository.reassignAuthor(id, deletedUser);
        replyRepository.reassignAuthor(id, deletedUser);

        for (Course course : courseRepository.findByMembersId(id)) {
            course.getMembers().removeIf(member -> member.getId().equals(id));
        }

        accountRepository.deleteById(id);
    }

    private Account getOrCreateDeletedUser() {
        return accountRepository.findByUsername(DELETED_USERNAME)
                .orElseGet(() -> accountRepository.save(new Account(
                        null,
                        DELETED_USERNAME,
                        passwordEncoder.encode(UUID.randomUUID().toString()),
                        false
                )));
    }

    @Transactional
    public Account updateProfilePicture(UUID id, byte[] profilePicture) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Account no found with id: " + id));

        account.setProfilePicture(profilePicture);
        return accountRepository.save(account);
    }
}
