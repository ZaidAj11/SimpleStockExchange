package server;

import java.time.Instant;

public class TradeResponse {
	public boolean IsTraded;
	public double LotsTraded;
	public Instant TradeTime;
	public double TradePrice;
	public Side TradeSide;
	
	public TradeResponse(boolean isTraded, double lotsTraded, Instant tradeTime, double tradePrice, Side tradeSide) {
		IsTraded = isTraded; 
		LotsTraded = lotsTraded;
		TradeTime = tradeTime; 
		TradePrice = tradePrice;
		TradeSide = tradeSide; 
	}
	
	public TradeResponse() {}
	
	public String toString() {
		String out = "";
		if(IsTraded) {
			out += TradeSide == Side.Buy ? "Bought " : "Sold ";
			out += LotsTraded + " lots @ " + TradePrice;
		}
		else {
			out += "Added " + TradeSide.toString() + " order for " + LotsTraded + " @ " + TradePrice;
		
		}
		

 		return out;
	}
}