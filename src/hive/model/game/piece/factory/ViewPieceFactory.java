package hive.model.game.piece.factory;

import hive.view.customhexes.HiveHexagon;
import hive.view.customhexes.HexagonGroup;
import hive.model.game.iconmapper.PieceIconMapper;
import hive.model.game.piece.HivePiece;
import hive.model.board.HiveBoard;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class ViewPieceFactory {

    private PieceIconMapper iconMapper;
    private HiveBoard board;

    public ViewPieceFactory(PieceIconMapper iconMapper, HiveBoard board) {
        if (iconMapper == null || board == null) {
            throw new IllegalArgumentException();
        }
        this.iconMapper = iconMapper;
        this.board = board;
    }

    public HexagonGroup createViewPiece(HivePiece hivePiece, int rank, String color) {

        ImageView imageView = new ImageView();
        imageView.setImage(iconMapper.getImage(hivePiece));
        imageView.setFitWidth(100);
        imageView.setFitHeight(120);
        imageView.setScaleX(0.8);
        imageView.setScaleY(0.8);
        imageView.setLayoutX(-50);
        imageView.setLayoutY(-60);
        imageView.setMouseTransparent(true);

        HiveHexagon hiveHexagon = new HiveHexagon(board.getGame(), hivePiece);
        hiveHexagon.setFill(iconMapper.getParser().parseColor(color));
        hiveHexagon.getStyleClass().add("standard-piece");
        HexagonGroup innerGroup = new HexagonGroup(hiveHexagon);
        innerGroup.getChildren().add(imageView);

        for (int i = 1; i <= rank; i++) {
            if (!hivePiece.getType().equals("Q"))
                innerGroup.getChildren().add(rank(i));
        }
        return innerGroup;
    }

    private Circle rank(int rank) {

        int startX = 40;
        int startY = -30;
        int yDelta = 10;

        startY += yDelta * rank;
        Circle circle = new Circle(3);
        circle.setMouseTransparent(true);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        circle.setStrokeWidth(1);
        circle.setLayoutX(startX);
        circle.setLayoutY(startY);
        return circle;
    }


}
