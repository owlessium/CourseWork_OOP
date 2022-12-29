package UI.Views;

import Models.Product;
import UI.Includes.Input;
import db.DBHelper;

import javax.swing.*;
import java.awt.*;

public class ProductView extends JDialog {

    private Product product = null;

    private Button finishBtn = new Button();
    private Button delButton = new Button("Удалить");

    public ProductView(Product product){
        super();
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.product = product;
        initFields();
        finishBtn.setLabel("Изменить");
    }

    public ProductView(){
        super();
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        initFields();
        finishBtn.setLabel("Добавить");
    }

    private void initFields(){
        setMinimumSize(new Dimension(500, 200));

        var container = getContentPane();

        GridBagConstraints c = new GridBagConstraints();

        var layout = new GridBagLayout();
        container.setLayout(layout);


        var nameBox  = new Input();
        var descBox = new Input();
        var weightBox = new Input();
        var countBox = new Input();
        var priceBox = new Input();

        if (product != null){
            nameBox.setText(product.getName());
            descBox.setText(String.valueOf(product.getDescription()));
            weightBox.setText(String.valueOf(product.getWeight()));
            countBox.setText(String.valueOf(product.getCount()));
            priceBox.setText(String.valueOf(product.getPrice()));
        }

        finishBtn.addActionListener((x) -> {
            if (!checkInputs(container)){
                JOptionPane.showMessageDialog(null, "Заполните все поля!");
                return;
            }


            var temp = new Product(
                    nameBox.getText(),
                    descBox.getText(),
                    Float.parseFloat(weightBox.getText()),
                    Integer.parseInt(countBox.getText()),
                    Float.parseFloat(priceBox.getText())
            );

            if (product != null) {
                temp.setId(product.getId());
                DBHelper.getInstance().editProduct(temp, temp.getId());
            }
            else {
                DBHelper.getInstance().addProduct(temp);
            }
            dispose();
        });

        delButton.addActionListener(
                (x) -> {
                    DBHelper.getInstance().deleteProductById(product.getId());
                    dispose();
                }
        );

        c.fill = GridBagConstraints.VERTICAL;

        c.gridx = 0;
        c.gridy = 0;
        container.add(new Label("Название"), c);

        c.gridx = 0;
        c.gridy = 1;
        container.add(new Label("Описание"), c);

        c.gridx = 0;
        c.gridy = 2;
        container.add(new Label("Вес"), c);

        c.gridx = 0;
        c.gridy = 3;
        container.add(new Label("Количество"), c);

        c.gridx = 0;
        c.gridy = 4;
        container.add(new Label("Цена"), c);

        c.gridx = 1;
        c.gridy = 0;
        container.add(nameBox, c);

        c.gridx = 1;
        c.gridy = 1;
        container.add(descBox, c);

        c.gridx = 1;
        c.gridy = 2;
        container.add(weightBox, c);

        c.gridx = 1;
        c.gridy = 3;
        container.add(countBox, c);

        c.gridx = 1;
        c.gridy = 4;
        container.add(priceBox, c);

        c.gridx = 1;
        c.gridy = 5;
        container.add(finishBtn, c);

        if (product != null) {
            c.gridx = 1;
            c.gridy = 6;
            container.add(delButton, c);
        }

        pack();
    }

    private boolean checkInputs(Container container) {
        var components =  container.getComponents();

        for (int i=0; i<components.length; i++) {
            if (components[i] instanceof Input){
                if (((Input) components[i]).getText().isEmpty()) return false;
            }
        }

        return true;
    }
}
