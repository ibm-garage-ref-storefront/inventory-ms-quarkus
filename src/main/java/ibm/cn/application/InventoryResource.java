package ibm.cn.application;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ibm.cn.application.model.Inventory;
import ibm.cn.application.repository.InventoryRepository;

@Path("/micro/inventory")
public class InventoryResource {
	
	private final InventoryRepository inventoryRepository;

	public InventoryResource(InventoryRepository inventoryRepository) {
	  this.inventoryRepository = inventoryRepository;
	}
	
	@GET
	@Path("/resource")
    public String getRequest() {
        return "InventoryResource response";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Inventory> getInventoryDetails() {
    	return inventoryRepository.getInventoryDetails();
    }
}