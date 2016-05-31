package hive.view.customhexes;

import javafx.scene.Group;

/**
 * Created by Kortika on 4/2/16.
 */
public class HexagonGroup extends Group{
    private HiveHexagon hexagon;
    public HexagonGroup (HiveHexagon hiveHexagon){
        if(hiveHexagon == null){
            throw new IllegalArgumentException();
        }
        this.hexagon = hiveHexagon;
        this.getChildren().add(hiveHexagon);
        this.disableProperty().bindBidirectional(hiveHexagon.disableProperty());
        disabledConfig();
    }

    private void disabledConfig (){
        this.disableProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                this.setOpacity(0.5);
            }else{
                this.setOpacity(1);
            }
        }));
    }

    public HiveHexagon getHexagon() {
        return hexagon;
    }
}
