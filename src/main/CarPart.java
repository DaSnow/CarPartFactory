package main;

public class CarPart {

	private int id;
	private String name;
	private double weight;
	private boolean isDefective;

	/**
	 * Creates new instance of CarPart
	 * 
	 * @param id
	 * @param name
	 * @param weight
	 * @param isDefective
	 */
	public CarPart(int id, String name, double weight, boolean isDefective) {
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.isDefective = isDefective;
	}

	/**
	 * Gets ID of instance
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Changes ID of instance
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets name of instance
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Changes name of instance
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets weight of instance
	 * 
	 * @return weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Changes weight of instance
	 * 
	 * @param weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Gets condition of instance
	 * 
	 * @return
	 */
	public boolean isDefective() {
		return this.isDefective;
	}

	/**
	 * Changes condition of instance
	 * 
	 * @param isDefective
	 */
	public void setDefective(boolean isDefective) {
		this.isDefective = isDefective;
	}

	/**
	 * Returns the parts name as its string representation
	 * 
	 * @return (String) The part name
	 */
	public String toString() {
		return this.getName();
	}
}