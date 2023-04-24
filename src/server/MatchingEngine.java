package server;

import java.time.*;
import java.util.*;

public class MatchingEngine {
	private List<Order> _buyOrderBook = new ArrayList<Order>();
	private List<Order> _sellOrderBook = new ArrayList<Order>();
	
	
	public TradeResponse addBuyOrder(Order order) {
		var copy = new Order(order);
		CheckForMatchingSell(order);
		if(order.Size == 0) return new TradeResponse(true, copy.Size, Instant.now(), copy.Price, Side.Buy);
		else {
			_buyOrderBook.add(order);
			_buyOrderBook.sort(new OrderComparer(Side.Buy));
			return  new TradeResponse(false, copy.Size, Instant.now(), copy.Price, Side.Buy);
		}
	}
	
	public TradeResponse addSellOrder(Order order) {
		var copy = new Order(order);
		CheckForMatchingBuy(order);
		if(order.Size == 0) return new TradeResponse(true, copy.Size, Instant.now(), copy.Price, Side.Sell);
		else {
			_sellOrderBook.add(order);
			_sellOrderBook.sort(new OrderComparer(Side.Sell));
			return  new TradeResponse(false, copy.Size, Instant.now(), copy.Price, Side.Sell);
		}
	}

	private void CheckForMatchingSell(Order buy) {
		for(int i = _sellOrderBook.size() - 1; i > -1 ; i--) {
			var sell = _sellOrderBook.get(i);
			if(sell.Price <=  buy.Price) {
				if(sell.Size > buy.Size) {
					sell.Size -= buy.Size;
					buy.Size = 0;
					return;
				}
				else if (buy.Size > sell.Size) {
					buy.Size -= sell.Size;
					_sellOrderBook.remove(i);
					i--;
				}
				else {
					buy.Size = 0; 
					sell.Size = 0;
					_sellOrderBook.remove(i);
					i--;
					return;
				}
			}
		}
		
	}
	
	private void CheckForMatchingBuy(Order sell) {
		for(int i = _buyOrderBook.size() - 1; i > -1 ; i--) {
			var buy = _buyOrderBook.get(i);
			if(buy.Price >=  sell.Price) {
				if(buy.Size > sell.Size) {
					buy.Size -= sell.Size;
					sell.Size = 0;
					return;
				}
				else if (sell.Size > buy.Size) {
					sell.Size -= buy.Size;
					_buyOrderBook.remove(i);
					i--;
				}
				else {
					buy.Size = 0; 
					sell.Size = 0;
					_buyOrderBook.remove(i);
					i--;
					return;
				}
			}
		}
		
	}
	
}


class OrderComparer implements Comparator<Order> {
	private Side _side;
	public OrderComparer(Side side) {
		_side = side;
	}
	
	@Override
	public int compare(Order o1, Order o2) {
		switch(_side) {
			case Buy: 
				if(o1.Price > o2.Price) return 1;
				else if(o1.Price < o2.Price) return -1;
				else if(o1.OrderTime.compareTo(o2.OrderTime) > 0) return 1;
				else return -1;
				
			case Sell:
				if(o1.Price < o2.Price) return 1;
				else if(o1.Price > o2.Price) return -1;
				else if(o1.OrderTime.compareTo(o2.OrderTime) > 0) return 1;
				else return -1;
			
		}
		return 0;
	}
}





