package org.team5.api.feature.reply;

import org.team5.api.feature.account.AccountDto;

import java.time.Instant;
import java.util.UUID;

public class ReplyDto {
    public final UUID id;
    public final String content;
    public final AccountDto author;
    public final Instant createdAt;

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.author = new AccountDto(reply.getAuthor());
        this.createdAt = reply.getCreatedAt();
    }
}