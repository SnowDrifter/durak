package ru.romanov.durak.lobby;

import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romanov.durak.model.GameInvite;
import ru.romanov.durak.websocket.WebSocketService;
import ru.romanov.durak.websocket.message.InviteMessage;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ru.romanov.durak.websocket.message.MessageType.INVITE;
import static ru.romanov.durak.websocket.message.MessageType.REJECT_INVITE;

@Service
public class InviteServiceImpl implements InviteService {

    @Autowired
    private WebSocketService webSocketService;

    private final Map<String, GameInvite> invites = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .asyncExpirationListener((ExpirationListener<String, GameInvite>) (invitee, invite) -> rejectInvite(invitee))
            .build();

    @Override
    public void createInvite(String initiator, String invitee) {
        invites.put(invitee, new GameInvite(initiator, invitee));
        webSocketService.sendMessage(invitee, new InviteMessage(INVITE, initiator));
    }

    @Override
    public GameInvite getInvite(String invitee) {
        return invites.get(invitee);
    }

    @Override
    public void rejectInvite(String invitee) {
        GameInvite invite = invites.remove(invitee);
        webSocketService.sendMessage(invite.getInitiator(), new InviteMessage(REJECT_INVITE));
    }

}
