package org.team5.api.feature.reply;

import java.util.UUID;

public interface ReplyService {
    Reply createReply(UUID threadId, UUID accountId, String content);

    Reply updateReply(UUID id, UUID accountId, String content);

    void deleteReply(UUID id, UUID accountId);
}