/*****************************************
 * Team 20
 * Alexander Kelly - 21359703 - lethalhedgehog (GitHub)
 * Conor Bleakley - 21411422 - CBleakley (GitHub)
 *****************************************/

package backgammon.player;

import backgammon.board.Color;

/**
 * Represents a player in the backgammon game.
 * Each player has a name and an assigned color.
 *
 * @param name  the name of the player
 * @param color the color assigned to the player (RED or BLUE)
 */
public record Player(String name, Color color) { }
