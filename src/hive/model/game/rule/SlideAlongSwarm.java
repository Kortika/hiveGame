package hive.model.game.rule;

import hive.model.game.move.Move;
import hive.model.board.HiveBoard;

/**
 * Created by Kortika on 4/9/16.
 */
public class SlideAlongSwarm implements Rule {


    /**
     * The piece will check if the given move can be made by sliding along the wall
     * of the swarm.
     *           _ _
     *         /     \
     *    _ _ / dest  \ _ _
     *  /     \       /     \
     * /  left \ _ _ / right \
     * \  occ  /     \ occ   /
     *  \ _ _ / moving\ _ _ /
     *        \ piece /
     *         \ _ _ /
     *
     *  If right is occupied and left is unoccupied, the move is legal and the hive piece will hug
     *  along the right side.
     *  If left is occupied and right is unoccupied, the move is legal and the hive piece will hug
     *  along the left side.
     *
     *  If both of them are occupied the hive piece is blocked from reaching the destination and the
     *  move cannot be made.
     *
     * @param move The move to be made by a particular piece.
     * @param board The board on which the pieces are being played and the swarm needed for the check
     *              is located.
     * @return true if the move can be made otherwise false.
     */
    @Override
    public boolean checkMovable(Move move, HiveBoard board) {
        SideOccupiedCheck rightSideOccupied = new SideOccupiedCheck(true);
        SideOccupiedCheck leftSideOccupied = new SideOccupiedCheck(false);
        boolean rightSideSlide = rightSideOccupied.checkMovable(move, board) && !leftSideOccupied.checkMovable(move, board);
        boolean leftSideSlide = !rightSideOccupied.checkMovable(move, board) && leftSideOccupied.checkMovable(move, board);
        return rightSideSlide || leftSideSlide;
    }


}
