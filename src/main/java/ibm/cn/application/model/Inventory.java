package ibm.cn.application.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Inventory {
	
	private long id;
    private String name;
    private String description;
    private int price;
    private String img_alt;
    private String img;
    private int stock;
    
	public Inventory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Inventory(long id, String name, String description, int price, String img_alt, String img, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_alt = img_alt;
		this.img = img;
		this.stock = stock;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImg_alt() {
		return img_alt;
	}
	public void setImg_alt(String img_alt) {
		this.img_alt = img_alt;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", img_alt=" + img_alt + ", img=" + img + ", stock=" + stock + "]";
	}
	
	
    
}
