import UI.Views.MainView;
import db.DBHelper;

public class Main {

    public static void main(String[] args) {
        DBHelper.getInstance().createTables();
        new MainView();
    }
}
