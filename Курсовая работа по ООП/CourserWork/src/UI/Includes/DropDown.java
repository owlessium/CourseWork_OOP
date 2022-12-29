package UI.Includes;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DropDown<T> extends JComboBox<T> {
    public DropDown(List<T> options){
        super((T[]) options.toArray());
        setPreferredSize(new Dimension(200, 20));
    }
}
