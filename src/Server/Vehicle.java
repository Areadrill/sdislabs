package Server;

public class Vehicle {
	private String plate;
	private String ownerName;
	
	public Vehicle(String plate, String name){
		this.plate = plate;
		ownerName = name;
	}
	
	public String getPlate(){
		return plate;
	}
	
	public String getOwner(){
		return ownerName;
	}
}
