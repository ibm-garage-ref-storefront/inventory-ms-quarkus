package ibm.cn.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import ibm.cn.application.model.Inventory;

@ApplicationScoped
public class InventoryRepository {
	
	private final DataSource dataSource;
	
	public InventoryRepository(DataSource dataSource) {
	    this.dataSource = dataSource;
	}
	
	public List<Inventory> getInventoryDetails() {

        List<Inventory> inventoryItems = new ArrayList<>();
        
        try {
        	
        	Connection connection = dataSource.getConnection();
        	
            PreparedStatement ps = connection.prepareStatement(
                    "select id,stock,price,img_alt,img,name,description from inventorydb.items");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Inventory item = new Inventory();
                item.setId(rs.getLong("id"));
                item.setName(rs.getString("name"));
                item.setStock(rs.getInt("stock"));
                item.setPrice(rs.getInt("price"));
                item.setImg_alt(rs.getString("img_alt"));
                item.setImg(rs.getString("img"));
                item.setDescription(rs.getString("description"));
                inventoryItems.add(item);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return inventoryItems;
 
    }

}
