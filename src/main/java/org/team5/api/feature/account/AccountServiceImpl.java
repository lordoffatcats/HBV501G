package org.team5.api.feature.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService {
    private static final String DELETED_USERNAME = "deleted-user";

    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;
    private final ThreadRepository threadRepository;
    private final ReplyRepository replyRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CourseRepository courseRepository,
            ThreadRepository threadRepository,
            ReplyRepository replyRepository,
            JwtEncoder jwtEncoder,
            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
        this.threadRepository = threadRepository;
        this.replyRepository = replyRepository;
        this.jwtEncoder = jwtEncoder;
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

    public String authenticate(String username, String password) {
        if (username.equals(DELETED_USERNAME)) {
            throw new UnauthorizedException("Invalid username or password");
        }

        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty()) {
            throw new UnauthorizedException("Invalid username or password");
        }

        Account existingAccount = account.get();
        if (!passwordEncoder.matches(password, existingAccount.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return createJwt(existingAccount);
    }

    private String createJwt(Account account) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(account.getId().toString())
                .claim("username", account.getUsername())
                .claim("isAdmin",account.isAdmin())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
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
