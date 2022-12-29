package UI.Views;

import Models.Order;
import Models.Product;
import Models.Table;
import Models.TableType;
import UI.Includes.Input;
import db.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

public class MainView extends JFrame {

    private final Table table;

    private final Button showOrderTableBtn = new Button("Показать заказы");
    private final Button showProductsTableBtn = new Button("Показать изделия");

    private final Button findButton = new Button("Найти заказ");
    private final Input findBox = new Input();

    private final Button addBtn = new Button("Добавить");


    public MainView() {
        setTitle("Учёт заказов");
        setPreferredSize(new Dimension(1000, 500));

        this.table = initTable();

        initButtons();
        initLayout();

        changeTableState(TableType.ORDERS);
        showOrdersList(DBHelper.getInstance().getAllOrders());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

        pack();
        setVisible(true);
    }

    private void initButtons(){
        addBtn.setMaximumSize(new Dimension(80, 30));

        addBtn.addActionListener(
                (event) -> {
                    switch (table.getTableType()){
                        case ORDERS -> {
                            (new OrderView()).setVisible(true);
                            showOrdersList(DBHelper.getInstance().getAllOrders());
                        }
                        case PRODUCTS -> {
                            (new ProductView()).setVisible(true);
                            showProductsList();
                        }
                    }
                }
        );

        showOrderTableBtn.addActionListener(
                (x) -> {
                    changeTableState(TableType.ORDERS);
                    showOrdersList(DBHelper.getInstance().getAllOrders());
                }
        );

        showProductsTableBtn.addActionListener(
                (x) -> {
                    changeTableState(TableType.PRODUCTS);
                    showProductsList();
                }
        );


        findButton.addActionListener(
                (x) -> {
                    changeTableState(TableType.ORDERS);
                    showOrdersList(DBHelper.getInstance().findOrderByClientName(findBox.getText()));
                }
        );
    }

    private void changeTableState(TableType tableType){
        table.setTableType(tableType);

        switch (tableType){
            case ORDERS -> {
                String[] headers = {"id", "Клиент", "Сумма", "Дата доставки"};
                table.changeHeaders(headers);
            }

            case PRODUCTS -> {
                String[] headers = {"id", "Название", "Вес", "Цена"};
                table.changeHeaders(headers);
            }
        }
    }

    private void initLayout(){
        var container = getContentPane();

        var layout = new GroupLayout(container);
        container.setLayout(layout);

        JScrollPane scrollPane = new JScrollPane(table);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);



        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(showOrderTableBtn)
                        .addComponent(showProductsTableBtn)
                        .addComponent(scrollPane))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(addBtn).addComponent(findBox).addComponent(findButton)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(showOrderTableBtn)
                        .addComponent(showProductsTableBtn)
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(addBtn))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(findBox))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(findButton))
                        )
                )
        );
    }

    private Table initTable(){
        DefaultTableModel model = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        var table = new Table(model);
        table.setSelectionMode(0);
        table.setAutoCreateRowSorter(true);

        table.getSelectionModel().addListSelectionListener(
                (event) -> {
                    if (!event.getValueIsAdjusting()){
                        int selectedRowIndex = table.getSelectedRow();

                        if (selectedRowIndex < 0) return;
                        int id = (int)table.getModel().getValueAt(selectedRowIndex, 0);

                        this.table.clearSelection();

                        switch (table.getTableType()){
                            case ORDERS -> {
                                var result = DBHelper.getInstance().getOrderById(id);
                                (new OrderView(result)).setVisible(true);
                                showOrdersList(DBHelper.getInstance().getAllOrders());
                            }

                            case PRODUCTS -> {
                                var result = DBHelper.getInstance().getProductById(id);
                                (new ProductView(result)).setVisible(true);
                                showProductsList();
                            }
                        }

                    }
                }
        );

        return table;
    }

    private void showOrdersList(List<Order> orders){
        var table = (DefaultTableModel) this.table.getModel();

        table.setRowCount(0);
        for (var order : orders) {
            table.addRow (new Object[] {
                    order.getId(),
                    order.getClientName(),
                    order.getSummaryPrice(),
                    order.getDeliveryDate() + " " + order.getDeliveryTime()
            });
        }
    }

    private void showProductsList(){
        var products = DBHelper.getInstance().getAllProducts();
        var table = (DefaultTableModel) this.table.getModel();

        table.setRowCount(0);
        for (var product : products) {
            table.addRow (new Object[] {
                    product.getId(),
                    product.getName(),
                    product.getWeight(),
                    product.getPrice()
            });
        }
    }
}
