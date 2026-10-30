package org.team5.api.feature.discussion;

import org.team5.api.feature.reply.ReplyDto;

import java.util.List;

public class ExtendedThreadDto extends ThreadDto {
    public final List<ReplyDto> replies;

    public ExtendedThreadDto(Thread thread) {
        super(thread);
        this.replies = thread.getReplies().stream().map(ReplyDto::new).toList();
    }
}
