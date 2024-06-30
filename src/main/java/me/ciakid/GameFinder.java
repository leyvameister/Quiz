package me.ciakid;

import me.ciakid.exception.NoArenasAvailableException;
import me.ciakid.game.Game;

public interface GameFinder {

    Game findAvailable() throws NoArenasAvailableException;

}
