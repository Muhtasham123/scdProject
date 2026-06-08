import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.io.Serializable;
import java.util.*;

class Item implements Serializable {
    int id;
    String name;
    String category;
    int quantity;
    double price;

    public Item(int id,String name,String category,int quantity,double price){
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }
}

class UndoRedoObj{
    String operation;
    Item item;

    public UndoRedoObj(String operation,Item item){
        this.operation = operation;
        this.item = item;
    }
}

class InventoryManagement {
    Scanner sc;
    TreeMap<Integer, Item> inventory;
    TreeMap<Double, ArrayList<Item>> viewForPriceSearch;
    TreeMap<Integer, ArrayList<Item>> viewForLowStockSearch;
    Stack<UndoRedoObj> redoStack;
    Stack<UndoRedoObj> undoStack;

    public InventoryManagement() {
        inventory = new TreeMap<>();
        viewForPriceSearch = new TreeMap<>();
        viewForLowStockSearch = new TreeMap<>();
        fileReader();
        redoStack = new Stack<>();
        undoStack = new Stack<>();
        sc = new Scanner(System.in);
    }

    public void insert() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        panel.add(new JLabel("Enter Id : "));
        panel.add(idField);
        panel.add(new JLabel("Enter Name : "));
        panel.add(nameField);
        panel.add(new JLabel("Enter category : "));
        panel.add(categoryField);
        panel.add(new JLabel("Enter Price : "));
        panel.add(priceField);
        panel.add(new JLabel("Enter Quantity : "));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Item details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (inventory.containsKey(id)) {
                    System.out.println("id already exists");
                    JOptionPane.showMessageDialog(null, "Id already exists");
                    return;
                }


                Item i = new Item(id, name, category, quantity, price);
                UndoRedoObj obj = new UndoRedoObj("insert",i);
                undoStack.push(obj);
                inventory.put(id, i);
                viewForPriceSearch.putIfAbsent(price,new ArrayList<>());
                viewForPriceSearch.get(price).add(i);
                viewForLowStockSearch.putIfAbsent(quantity,new ArrayList<>());
                viewForLowStockSearch.get(quantity).add(i);
                fileWriter();
                JOptionPane.showMessageDialog(null, "Item added successfully");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for a number field or missing input");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Canceled");
        }
    }

    public void delete(){
        String id = JOptionPane.showInputDialog(null,"Enter Id : ");

        if(id != null){
            try {
                int parsedId = Integer.parseInt(id);
                Item item = inventory.get(parsedId);

                if (!inventory.containsKey(parsedId)) {
                    JOptionPane.showMessageDialog(null, "Item does not exist");
                    return;
                }

                UndoRedoObj obj = new UndoRedoObj("delete", inventory.get(parsedId));
                undoStack.push(obj);
                inventory.remove(parsedId);
                viewForPriceSearch.get(item.price).remove(item);
                if (viewForPriceSearch.get(item.price).isEmpty()) {
                    viewForPriceSearch.remove(item.price);
                }
                viewForLowStockSearch.get(item.quantity).remove(item);
                if (viewForLowStockSearch.get(item.quantity).isEmpty()) {
                    viewForLowStockSearch.remove(item.quantity);
                }
                fileWriter();
                JOptionPane.showMessageDialog(null, "Item deleted successfully");
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid Id");
            }
        }else{
            JOptionPane.showMessageDialog(null,"Deletion cancelled");
        }

    }

    public void update(){
        String id = JOptionPane.showInputDialog(null,"Enter Id : ");

        if(id != null) {
            try {
                int parsedId = Integer.parseInt(id);
                if (!inventory.containsKey(parsedId)) {
                    JOptionPane.showMessageDialog(null, "Item does not exist");
                    return;
                }

                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField nameField = new JTextField(10);
                JTextField categoryField = new JTextField(10);
                JTextField priceField = new JTextField(10);
                JTextField quantityField = new JTextField(10);

                panel.add(new JLabel("Enter Name : "));
                panel.add(nameField);
                panel.add(new JLabel("Enter category : "));
                panel.add(categoryField);
                panel.add(new JLabel("Enter Price : "));
                panel.add(priceField);
                panel.add(new JLabel("Enter Quantity : "));
                panel.add(quantityField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Enter Item details", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String category = categoryField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    Item i = new Item(parsedId, name, category, quantity, price);
                    Item oldItem = inventory.get(parsedId);
                    UndoRedoObj obj = new UndoRedoObj("update", oldItem);
                    undoStack.push(obj);
                    inventory.put(parsedId, i);

                    viewForPriceSearch.get(oldItem.price).remove(oldItem);
                    if (viewForPriceSearch.get(oldItem.price).isEmpty()) {
                        viewForPriceSearch.remove(oldItem.price);
                    }

                    viewForLowStockSearch.get(oldItem.quantity).remove(oldItem);
                    if (viewForLowStockSearch.get(oldItem.quantity).isEmpty()) {
                        viewForLowStockSearch.remove(oldItem.quantity);
                    }

                    viewForPriceSearch.putIfAbsent(i.price, new ArrayList<>());
                    viewForPriceSearch.get(i.price).add(i);

                    viewForLowStockSearch.putIfAbsent(i.quantity, new ArrayList<>());
                    viewForLowStockSearch.get(i.quantity).add(i);
                    fileWriter();
                    JOptionPane.showMessageDialog(null, "Item updated successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Canceled");
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid or missing input");
            }
        }
    }

    public void undo(){
        if(!undoStack.isEmpty()) {
            UndoRedoObj obj = undoStack.pop();
            UndoRedoObj updatedObj = new UndoRedoObj("update",inventory.get(obj.item.id));

            if (obj.operation.equals("insert")) {
                inventory.remove(obj.item.id);
                redoStack.push(obj);
            }
            if (obj.operation.equals("delete")) {
                inventory.put(obj.item.id, obj.item);
                redoStack.push(obj);
            }else{
                inventory.put(obj.item.id, obj.item);
                redoStack.push(updatedObj);
            }
            fileWriter();
            fileReader();
        }
    }

    public void redo(){
        if(!redoStack.isEmpty()) {
            UndoRedoObj obj = redoStack.pop();
            UndoRedoObj updatedObj = new UndoRedoObj("update",inventory.get(obj.item.id));

            if (obj.operation.equals("insert")) {
                inventory.put(obj.item.id, obj.item);
                undoStack.push(obj);
            }
            else{
                inventory.put(obj.item.id, obj.item);
                undoStack.push(updatedObj);
            }
            if (obj.operation.equals("delete")) {
                inventory.remove(obj.item.id);
                undoStack.push(obj);
            }
            fileWriter();
            fileReader();
        }
    }

    public Object[][] createTableData(TreeMap<Integer,Item>inventory){
        Object[][] data = new Object[inventory.size()][5];
        int row = 0;

        for(Item item : inventory.values()){
            data[row][0] = item.id;
            data[row][1] = item.name;
            data[row][2] = item.category;
            data[row][3] = item.price;
            data[row][4] = item.quantity;
            row++;
        }
        return data;
    }

    public Object[][] createPriceTableData(NavigableMap<Double,ArrayList<Item>>viewForPriceSearch){
        ArrayList<Item> allItems = new ArrayList<>();
        for (ArrayList<Item> list : viewForPriceSearch.values()) {
            allItems.addAll(list);
        }

        Object[][] data = new Object[allItems.size()][5];
        for (int i = 0; i < allItems.size(); i++) {
            Item item = allItems.get(i);
            data[i][0] = item.id;
            data[i][1] = item.name;
            data[i][2] = item.category;
            data[i][3] = item.price;
            data[i][4] = item.quantity;
        }
        return data;
    }

    public Object[][] createLowStockTableData(SortedMap<Integer,ArrayList<Item>>viewForLowStockSearch){
        Object[][] data = new Object[viewForLowStockSearch.size()][5];

        ArrayList<Item>allItems = new ArrayList<>();
        for(ArrayList<Item>list : viewForLowStockSearch.values()){
            allItems.addAll(list);
        }

        for(int i = 0;i<allItems.size();i++){
            data[i][0] = allItems.get(i).id;
            data[i][1] = allItems.get(i).name;
            data[i][2] = allItems.get(i).category;
            data[i][3] = allItems.get(i).price;
            data[i][4] = allItems.get(i).quantity;
        }
        return data;
    }

    public void searchById(){
        String id = JOptionPane.showInputDialog("Enter ID :");
        if(id != null){
            try{
                if(!inventory.containsKey(Integer.parseInt(id))){
                    JOptionPane.showMessageDialog(null,"Item does not exist");
                    return;
                }
                Item item = inventory.get(Integer.parseInt(id));
                JPanel panel = new JPanel();
                panel.add(new JLabel("|ID : " + item.id + "| "));
                panel.add(new JLabel("|Name : " + item.name + "| "));
                panel.add(new JLabel("|Category : " + item.category + "| "));
                panel.add(new JLabel("|Price : " + item.price + "| "));
                panel.add(new JLabel("|Quantity : " + item.quantity + "| "));

                JOptionPane.showMessageDialog(null,panel);
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid Id");
            }
        }
    }

    public TreeMap<Integer, Item> searchByCategory(){
        String category = JOptionPane.showInputDialog("Enter Category");
        if(category != null){
            String processedCategory = category.trim().toLowerCase();
            TreeMap<Integer,Item>categoryMap = new TreeMap<>();

            for(Item item : inventory.values()){
                if(item.category.equalsIgnoreCase(processedCategory)){
                    categoryMap.put(item.id,item);
                }
            }

            return categoryMap;
        }else{
            return null;
        }
    }

    public NavigableMap<Double,ArrayList<Item>>searchByPriceRange(){
        JPanel panel = new JPanel();
        JLabel minPriceLabel = new JLabel("Enter min price : ");
        JLabel maxPriceLabel = new JLabel("Enter max price : ");
        JTextField minPriceField = new JTextField(20);
        JTextField maxPriceField = new JTextField(20);

        panel.add(minPriceLabel);
        panel.add(minPriceField);
        panel.add(maxPriceLabel);
        panel.add(maxPriceField);

        int result = JOptionPane.showConfirmDialog(null,panel,"",JOptionPane.OK_CANCEL_OPTION);

        if(result == JOptionPane.OK_OPTION){
            try{
                Double minPrice = Double.parseDouble(minPriceField.getText());
                Double maxPrice = Double.parseDouble(maxPriceField.getText());

                return viewForPriceSearch.subMap(minPrice,true,maxPrice,true);
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid price");
                return null;
            }
        }else{
            return null;
        }
    }

    public SortedMap<Integer,ArrayList<Item>> searchByQuantity(){
        JPanel panel = new JPanel();
        JLabel quantityLabel = new JLabel("Enter quantity : ");
        JTextField quantityField = new JTextField(20);

        panel.add(quantityLabel);
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null,panel,"",JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            try{
                Integer processedQuantity = Integer.parseInt(quantityField.getText());
                return viewForLowStockSearch.headMap(processedQuantity);

            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid quantity");
                return null;
            }
        }else {
            return null;
        }
    }

    public void fileWriter(){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.dat"))){
            out.writeObject(inventory);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void fileReader(){
        File f = new File("data.dat");
        if(!f.exists())return;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.dat"))){
            inventory = (TreeMap<Integer,Item>) in.readObject();

            viewForPriceSearch.clear();
            viewForLowStockSearch.clear();

            for (Item item : inventory.values()) {
                viewForPriceSearch.putIfAbsent(item.price, new ArrayList<>());
                viewForPriceSearch.get(item.price).add(item);

                viewForLowStockSearch.putIfAbsent(item.quantity, new ArrayList<>());
                viewForLowStockSearch.get(item.quantity).add(item);
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        InventoryManagement im = new InventoryManagement();
        JButton insertBtn = new JButton("Insert Item");
        JButton deleteBtn = new JButton("Delete Item");
        JButton updateBtn = new JButton("Update Item");
        JButton undoBtn = new JButton("Undo");
        JButton redoBtn = new JButton("Redo");
        JButton searchByIdBtn = new JButton("Search By Id");
        JButton searchByCategoryBtn = new JButton("Search By Category");
        JButton searchByPriceBtn = new JButton("Search By Price range");
        JButton searchByQuantityBtn = new JButton("Get Low Stock Items");
        JButton allItemsBtn = new JButton("All Items");

        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());

        frame.add(insertBtn);
        frame.add(deleteBtn);
        frame.add(updateBtn);
        frame.add(undoBtn);
        frame.add(redoBtn);
        frame.add(searchByIdBtn);
        frame.add(searchByCategoryBtn);
        frame.add(searchByPriceBtn);
        frame.add(searchByQuantityBtn);
        frame.add(allItemsBtn);

        String []columnHeaders = {"ID","Name","Category","Price","Quantity"};
        JTable table = new JTable(im.createTableData(im.inventory),columnHeaders);
        table.setPreferredScrollableViewportSize(new Dimension(950,600));

        JScrollPane pane = new JScrollPane(table);
        frame.add(pane);


        insertBtn.addActionListener(e -> {
            im.insert();
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

        deleteBtn.addActionListener(e -> {
            im.delete();
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

        updateBtn.addActionListener(e -> {
            im.update();
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

        undoBtn.addActionListener(e -> {
            im.undo();
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

        redoBtn.addActionListener(e -> {
            im.redo();
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

        searchByIdBtn.addActionListener(e -> {
            im.searchById();
        });

        searchByCategoryBtn.addActionListener(e ->{
            TreeMap<Integer,Item>items = im.searchByCategory();
            if(!items.isEmpty()){
                table.setModel(new DefaultTableModel(im.createTableData(items),columnHeaders));
                table.revalidate();
                table.repaint();
            }else{
                JOptionPane.showMessageDialog(null,"Category not found");
            }
        });

        searchByPriceBtn.addActionListener(e ->{
            NavigableMap<Double,ArrayList<Item>>subView = im.searchByPriceRange();
            if(subView != null) {
                System.out.println(subView);
                table.setModel(new DefaultTableModel(im.createPriceTableData(subView), columnHeaders));
                table.revalidate();
                table.repaint();
            }
        });

        searchByQuantityBtn.addActionListener(e ->{
            SortedMap<Integer,ArrayList<Item>>subView = im.searchByQuantity();
            if(subView != null) {
                System.out.println(subView);
                table.setModel(new DefaultTableModel(im.createLowStockTableData(subView), columnHeaders));
                table.revalidate();
                table.repaint();
            }
        });

        allItemsBtn.addActionListener(e ->{
            table.setModel(new DefaultTableModel(im.createTableData(im.inventory),columnHeaders));
            table.revalidate();
            table.repaint();
        });

    }
}

