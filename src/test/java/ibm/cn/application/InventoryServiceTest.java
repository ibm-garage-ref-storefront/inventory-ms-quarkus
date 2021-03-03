package ibm.cn.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import ibm.cn.application.repository.InventoryService;

public class InventoryServiceTest {
	
	@Test
    public void testInventoryService() {
        InventoryService service = new InventoryService();
        Assertions.assertEquals("InventoryResource response", service.details());
    }

}
