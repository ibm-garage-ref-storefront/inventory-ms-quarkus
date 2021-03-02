package ibm.cn.application.repository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryService {
	
	public String details() {
        return "InventoryResource response";
    }

}
