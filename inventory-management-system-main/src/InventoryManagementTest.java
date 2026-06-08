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
}