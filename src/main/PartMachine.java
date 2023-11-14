package main;

import java.util.Random;

import data_structures.ListQueue;
import data_structures.SinglyLinkedList;
import interfaces.List;
import interfaces.Queue;

public class PartMachine {

	private int id;
	private CarPart p1;
	private int period;
	private double weightError;
	private int partsProduced;
	private int chanceOfDefective;
	private Queue<Integer> timer;
	private Queue<CarPart> conveyorBelt;

	/**
	 * Creates instance of PartMachine
	 * 
	 * @param id
	 * @param p1
	 * @param period
	 * @param weightError
	 * @param chanceOfDefective
	 */
	public PartMachine(int id, CarPart p1, int period, double weightError, int chanceOfDefective) {
		this.id = id;
		this.p1 = p1;
		this.period = period;
		this.weightError = weightError;
		this.partsProduced = 0;
		this.chanceOfDefective = chanceOfDefective;
		this.timer = new ListQueue<Integer>();

		for (int i = this.period - 1; i >= 0; i--)
			timer.enqueue(i);

		resetConveyorBelt();
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
	 * Gets Timer queue of instance
	 * 
	 * @return timer
	 */
	public Queue<Integer> getTimer() {
		return this.timer;
	}

	/**
	 * Changes timer of instance
	 * 
	 * @param timer
	 */
	public void setTimer(Queue<Integer> timer) {
		this.timer = timer;
	}

	/**
	 * Gets CarPart of instance
	 * 
	 * @return p1
	 */
	public CarPart getPart() {
		return this.p1;
	}

	/**
	 * Changes CarPart of instance
	 * 
	 * @param part1
	 */
	public void setPart(CarPart part1) {
		this.p1 = part1;
	}

	/**
	 * Gets conveyer belt Queue of instance
	 * 
	 * @return
	 */
	public Queue<CarPart> getConveyorBelt() {
		return this.conveyorBelt;
	}

	/**
	 * Changes conveyer belt Queue of instance
	 * 
	 * @param conveyorBelt
	 */
	public void setConveyorBelt(Queue<CarPart> conveyorBelt) {
		this.conveyorBelt = conveyorBelt;
	}

	/**
	 * Gets total amount of part produced by instance
	 * 
	 * @return partsProduced
	 */
	public int getTotalPartsProduced() {
		return this.partsProduced;
	}

	/**
	 * Changes total amount of parts produced by instance
	 * 
	 * @param count
	 */
	public void setTotalPartsProduced(int count) {
		this.partsProduced = count;
	}

	/**
	 * Gets weight error for producing CarPart of instance
	 * 
	 * @return weightError
	 */
	public double getPartWeightError() {
		return this.weightError;
	}

	/**
	 * Changes weight error of instance
	 * 
	 * @param partWeightError
	 */
	public void setPartWeightError(double partWeightError) {
		this.weightError = partWeightError;
	}

	/**
	 * Gets change of CarPart made by instance being defective
	 * 
	 * @return chanceOfDefective
	 */
	public int getChanceOfDefective() {
		return this.chanceOfDefective;
	}

	/**
	 * Changes chance of CarPart being defective
	 * 
	 * @param chanceOfDefective
	 */
	public void setChanceOfDefective(int chanceOfDefective) {
		this.chanceOfDefective = chanceOfDefective;
	}

	/**
	 * Initializes conveyer belt and resets it to null
	 */
	public void resetConveyorBelt() {
		conveyorBelt = new ListQueue<CarPart>();

		for (int i = 0; i < 10; i++)
			conveyorBelt.enqueue(null);
	}

	/**
	 * Loops through conveyorBelt Queue to get any parts present on it
	 * 
	 * @return parts
	 */
	public List<CarPart> getPartsOnConveyorBelt() {
		List<CarPart> parts = new SinglyLinkedList<CarPart>();

		for (int i = 0; i < conveyorBelt.size(); i++) {
			if (conveyorBelt.front() != null)
				parts.add(conveyorBelt.front());

			conveyorBelt.enqueue(conveyorBelt.dequeue());
		}

		return parts;
	}

	/**
	 * Cycles one item of timer Queue when called
	 * 
	 * @return
	 */
	public int tickTimer() {
		Integer temp = timer.front();
		timer.enqueue(timer.dequeue());
		return temp;
	}

	/**
	 * Creates new CarPart with a random weight based on the weight given and its
	 * error, and sets defective given the total amount of parts produced and chance
	 * of defective
	 * 
	 * @return CarPart at beginning of conveyorBelt
	 */
	public CarPart produceCarPart() {
		if (tickTimer() != 0) {
			conveyorBelt.enqueue(null);
			return conveyorBelt.dequeue();
		}

		Random rng = new Random();
		double max = p1.getWeight() + weightError;
		double min = p1.getWeight() - weightError;
		CarPart newPart = new CarPart(p1.getId(), p1.getName(), min + (max - min) * rng.nextDouble(),
				partsProduced % chanceOfDefective == 0);
		conveyorBelt.enqueue(newPart);
		partsProduced += 1;
		return conveyorBelt.dequeue();
	}

	/**
	 * Returns string representation of a Part Machine in the following format:
	 * Machine {id} Produced: {part name} {total parts produced}
	 */
	@Override
	public String toString() {
		return "Machine " + this.getId() + " Produced: " + this.getPart().getName() + " "
				+ this.getTotalPartsProduced();
	}

	/**
	 * Prints the content of the conveyor belt. The machine is shown as |Machine
	 * {id}|. If the is a part it is presented as |P| and an empty space as _.
	 */
	public void printConveyorBelt() {
		// String we will print
		String str = "";
		// Iterate through the conveyor belt
		for (int i = 0; i < this.getConveyorBelt().size(); i++) {
			// When the current position is empty
			if (this.getConveyorBelt().front() == null) {
				str = "_" + str;
			}
			// When there is a CarPart
			else {
				str = "|P|" + str;
			}
			// Rotate the values
			this.getConveyorBelt().enqueue(this.getConveyorBelt().dequeue());
		}
		System.out.println("|Machine " + this.getId() + "|" + str);
	}
}
