package server;

import java.time.Instant;

public class Order {
	public double Price; 
	public Side Side;
	public double Size;
	public Instant OrderTime;
	
	public Order(Side side, double price, double size) {
		Price = price;
		Side = side;
		Size = size;
		OrderTime = Instant.now();		
	}
	
	public Order(Order that) {
		this.Price = that.Price;
		this.Side = that.Side;
		this.Size = that.Size; 
		this.OrderTime = that.OrderTime;
	}
}
