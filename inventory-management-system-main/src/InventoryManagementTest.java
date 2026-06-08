import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagementTest {
	
	//Insert Test
    @Test
    void testInsertItem() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(1, "Mouse", "Electronics", 10, 500.0);

        im.inventory.put(item.id, item);

        assertTrue(im.inventory.containsKey(1));
        assertEquals("Mouse", im.inventory.get(1).name);
    }
    
    //Delete Test
    @Test
    void testDeleteItem() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(2, "Keyboard", "Electronics", 5, 1000.0);
        im.inventory.put(item.id, item);

        im.inventory.remove(2);

        assertFalse(im.inventory.containsKey(2));
    }
    
    //Update Test
    @Test
    void testUpdateItem() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(3, "Laptop", "Electronics", 2, 50000.0);
        im.inventory.put(item.id, item);

        Item updated = new Item(3, "Laptop Pro", "Electronics", 3, 70000.0);
        im.inventory.put(3, updated);

        assertEquals("Laptop Pro", im.inventory.get(3).name);
        assertEquals(70000.0, im.inventory.get(3).price);
    }
    
    //Search by ID logic Test
    @Test
    void testSearchByIdLogic() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(4, "Phone", "Electronics", 8, 30000.0);
        im.inventory.put(item.id, item);

        Item result = im.inventory.get(4);

        assertNotNull(result);
        assertEquals("Phone", result.name);
    }
    
    //Price range map test
    @Test
    void testPriceMap() {
        InventoryManagement im = new InventoryManagement();

        Item item1 = new Item(5, "A", "Cat", 1, 100);
        Item item2 = new Item(6, "B", "Cat", 1, 200);

        im.inventory.put(5, item1);
        im.inventory.put(6, item2);

        im.viewForPriceSearch.putIfAbsent(100.0, new java.util.ArrayList<>());
        im.viewForPriceSearch.get(100.0).add(item1);

        assertTrue(im.viewForPriceSearch.containsKey(100.0));
    }
    
    //Low stock map test
    @Test
    void testLowStockMap() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(7, "Book", "Stationery", 2, 150);

        im.inventory.put(7, item);

        im.viewForLowStockSearch.putIfAbsent(2, new java.util.ArrayList<>());
        im.viewForLowStockSearch.get(2).add(item);

        assertTrue(im.viewForLowStockSearch.containsKey(2));
    }
    
    
    //Undo Stack test
    @Test
    void testUndoStack() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(8, "Pen", "Stationery", 5, 50);

        UndoRedoObj obj = new UndoRedoObj("insert", item);
        im.undoStack.push(obj);

        assertFalse(im.undoStack.isEmpty());
        assertEquals("insert", im.undoStack.peek().operation);
    }
    
    //Redo stack test
    @Test
    void testRedoStack() {
        InventoryManagement im = new InventoryManagement();

        Item item = new Item(9, "Notebook", "Stationery", 10, 100.0);

        UndoRedoObj obj = new UndoRedoObj("delete", item);
        im.redoStack.push(obj);

        assertFalse(im.redoStack.isEmpty());
        assertEquals("delete", im.redoStack.peek().operation);
        assertEquals(9, im.redoStack.peek().item.id);
    }
}