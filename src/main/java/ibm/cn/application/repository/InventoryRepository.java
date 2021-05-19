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
        
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
        	
        	connection = dataSource.getConnection();
        	
            ps = connection.prepareStatement(
                    "select id,stock,price,img_alt,img,name,description from inventorydb.items");
            rs = ps.executeQuery();
            
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
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* Ignored */}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { /* Ignored */}
            }
            if (connection != null) {
                try {
                	connection.close();
                } catch (SQLException e) { /* Ignored */}
            }
        }
        
        return inventoryItems;
 
    }

}
