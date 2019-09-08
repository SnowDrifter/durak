package ru.romanov.durak.lobby;

import ru.romanov.durak.model.GameInvite;

public interface InviteService {

    void createInvite(String initiator, String invitee);

    GameInvite getInvite(String invitee);

    void rejectInvite(String invitee);

}
