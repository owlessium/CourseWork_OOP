package UI.Views;

import Models.DeliveryType;
import Models.Order;
import Models.Product;
import UI.Includes.DropDown;
import UI.Includes.Input;
import UI.Includes.MyLabel;
import db.DBHelper;
import org.sqlite.core.DB;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class OrderView extends JDialog {

    private Order order = null;

    private Button finishBtn = new Button();
    private Button finishOrderBtn = new Button("Закрыть заказ");
    private Button delButton = new Button("Удалить");


    public OrderView(Order order){
        super();
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.order = order;
        initFields();
        finishBtn.setLabel("Изменить");
    }

    public OrderView(){
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

        var productsList = DBHelper.getInstance().getAllProducts();

        var nameBox  = new Input();
        var priceBox = new MyLabel("0");
        var isFinishedLabel = new MyLabel();
        var dateOBox = new MyLabel(LocalDate.now().toString());
        var dateDBox = new Input();
        var timeBox = new Input();
        var addressBox = new Input();
        var deliveryBox = new DropDown<>(DBHelper.getInstance().getAllDeliveryTypes());
        var productsBox = new DropDown<>(productsList);
        var productDesc = new MyLabel();

        deliveryBox.addActionListener(
                (x) -> {
                    var type = (DeliveryType)deliveryBox.getSelectedItem();

                    if (!Objects.equals(type.getName(), "такси")){
                        addressBox.setEditable(false);
                        addressBox.setText("Адрес кондитерской");
                    } else addressBox.setEditable(true);

                    priceBox.setText(String.valueOf(((Product) productsBox.getSelectedItem()).getPrice() + type.getPrice()));
                }
        );

        finishOrderBtn.addActionListener(
                (x) -> {
                    DBHelper.getInstance().finishOrder(this.order);

                    dispose();
                }
        );

        productsBox.addActionListener(
                (x) -> {
                    var product = (Product) productsBox.getSelectedItem();
                    var type = (DeliveryType)deliveryBox.getSelectedItem();

                    priceBox.setText(String.valueOf((product.getPrice() + type.getPrice())));
                    productDesc.setText(product.getDescription());
                }
        );

        deliveryBox.setSelectedIndex(0);
        productsBox.setSelectedIndex(0);

        if (order != null){
            nameBox.setText(order.getClientName());
            priceBox.setText(String.valueOf(order.getSummaryPrice()));
            dateOBox.setText(order.getOrderDate().toString());
            dateDBox.setText(order.getDeliveryDate().toString());
            timeBox.setText(order.getDeliveryTime());
            addressBox.setText(order.getDeliveryAddress());
            deliveryBox.getModel().setSelectedItem(order.getDeliveryType());
            productsBox.getModel().setSelectedItem(order.getProduct());

            if (order.isFinished()){
                isFinishedLabel.setText("Заказ выполнен");
            } else isFinishedLabel.setText("Заказ не выполнен");
        }

        finishBtn.addActionListener((x) -> {
            try {
                if (!checkInputs(container)){
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                    return;
                }
                var orderDate = LocalDate.parse(dateOBox.getText());
                var deliveryDate =  LocalDate.parse(dateDBox.getText());

                if (deliveryDate.isBefore(orderDate)){
                    JOptionPane.showMessageDialog(null, "Неверная дата доставки");
                    return;
                }

                var product = (Product)productsBox.getSelectedItem();

                var isFinished = Objects.equals( isFinishedLabel.getText(), "Заказ выполнен");

                var temp = new Order(
                        nameBox.getText(),
                        LocalDate.parse(dateOBox.getText()),
                        LocalDate.parse(dateDBox.getText()),
                        LocalTime.parse(timeBox.getText()).toString(),
                        addressBox.getText(),
                        (DeliveryType)deliveryBox.getSelectedItem(),
                        isFinished, product
                );

                if (order != null) {
                    temp.setId(order.getId());
                    DBHelper.getInstance().editOrder(temp, temp.getId());
                }
                else {
                    DBHelper.getInstance().addOrder(temp);
                }
                dispose();


            } catch (DateTimeParseException exception){
                JOptionPane.showMessageDialog(null, "Не корректная дата или время!");
            }
        });

        delButton.addActionListener(
                (x) -> {
                    DBHelper.getInstance().deleteOrderById(order.getId());
                    dispose();
                }
        );

        c.fill = GridBagConstraints.VERTICAL;


        c.gridx = 0;
        c.gridy = 0;
        container.add(new Label("Клиент"), c);

        c.gridx = 2;
        c.gridy = 0;
        container.add(isFinishedLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        container.add(new Label("Сум. стоимость"), c);

        c.gridx = 0;
        c.gridy = 2;
        container.add(new Label("Дата заказа"), c);

        c.gridx = 0;
        c.gridy = 3;
        container.add(new Label("Дата доставки"), c);

        c.gridx = 0;
        c.gridy = 4;
        container.add(new Label("Адрес доставки и тип"), c);

        c.gridx = 0;
        c.gridy = 5;
        container.add(new Label(" "), c);

        c.gridx = 0;
        c.gridy = 6;
        container.add(new Label("Изделие"), c);

        c.gridx = 1;
        c.gridy = 0;
        container.add(nameBox, c);

        c.gridx = 1;
        c.gridy = 1;
        container.add(priceBox, c);

        c.gridx = 1;
        c.gridy = 2;
        container.add(dateOBox, c);

        c.gridx = 1;
        c.gridy = 3;
        container.add(dateDBox, c);

        c.gridx = 2;
        c.gridy = 3;
        container.add(timeBox, c);

        c.gridx = 1;
        c.gridy = 4;
        container.add(addressBox, c);

        c.gridx = 2;
        c.gridy = 4;
        container.add(deliveryBox, c);

        c.gridx = 1;
        c.gridy = 6;
        container.add(productsBox, c);

        c.gridx = 1;
        c.gridy = 7;
        container.add(productDesc, c);

        c.gridx = 0;
        c.gridy = 7;
        container.add(finishBtn, c);

        if (order != null) {
            c.gridx = 2;
            c.gridy = 7;
            container.add(delButton, c);

            if(!order.isFinished()) {
                c.gridx = 2;
                c.gridy = 1;
                container.add(finishOrderBtn, c);
            }
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
