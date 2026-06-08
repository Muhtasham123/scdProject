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
}