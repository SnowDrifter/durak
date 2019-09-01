package ru.romanov.durak.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameInvite {

    private final String initiator;
    private final String invitee;

}
