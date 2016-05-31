package hive;

/**
 * Deze klasse mag niet aangepast worden.
 */
public class TransferPiece implements Comparable<TransferPiece> {

    private final char type;
    private final char color;
    private final int order;
    private final int r;
    private final int c;

    /**
     *
     * @param type Afkorting zoals beschreven in de opgave: 'Q', 'A', ...
     * @param color 'w' (wit) of 'b' (zwart)
     * @param order 1, 2 of 3
     * @param r rij-coordinaat
     * @param c kolom-coordinaat
     */
    public TransferPiece(char type, char color, int order, int r, int c) {
        this.type = type;
        this.color = color;
        this.order = order;
        this.r = r;
        this.c = c;
    }

    public TransferPiece(char type, char color, int r, int c) {
        this(type, color, 1, r, c);
    }

    @Override
    public String toString() {
        return r + " " + c + " " + color + type + order;
    }

    @Override
    public int compareTo(TransferPiece tp) {
        if(this.r == tp.r) {
            if (this.c == tp.c) return 0;
            else return this.c > tp.c ? 1 : -1;
        } else {
            return this.r > tp.r ? 1 : -1;
        }
    }
}
