package ibm.cn.application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import ibm.cn.application.repository.InventoryRepository;

@Path("/micro/inventory")
public class InventoryResource {
	
	private final InventoryRepository inventoryRepository;

	public InventoryResource(InventoryRepository inventoryRepository) {
	  this.inventoryRepository = inventoryRepository;
	}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInventoryDetails() {
        Gson gson = new Gson();
        return gson.toJson(inventoryRepository.getInventoryDetails());
    }
}