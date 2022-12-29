package UI.Includes;

import java.awt.*;

public class MyLabel extends Label {
    public MyLabel(String s){
        super(s);
        setPreferredSize(new Dimension(200, 20));
    }

    public MyLabel(){
        super();
        setPreferredSize(new Dimension(200, 20));
    }
}
