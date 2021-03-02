package ibm.cn.application;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import ibm.cn.application.model.Inventory;
import ibm.cn.application.repository.InventoryRepository;
import ibm.cn.application.repository.InventoryService;

@Path("/micro/inventory")
public class InventoryResource {
	
	private static final Logger LOG = Logger.getLogger(InventoryResource.class);
	
	private final InventoryRepository inventoryRepository;
	private final InventoryService inventoryService;

	public InventoryResource(InventoryRepository inventoryRepository, InventoryService inventoryService) {
	  this.inventoryRepository = inventoryRepository;
	  this.inventoryService = inventoryService;
	}
	
	@GET
	@Path("/resource")
    public String getRequest() {
		LOG.info("/resource endpoint");
        return inventoryService.details();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Traced
    public List<Inventory> getInventoryDetails() {
    	LOG.info("/inventory endpoint");
    	return inventoryRepository.getInventoryDetails();
    }
}