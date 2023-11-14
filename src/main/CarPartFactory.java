package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.BasicHashFunction;
import data_structures.HashTableSC;
import data_structures.LinkedStack;
import data_structures.SinglyLinkedList;
import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class CarPartFactory {

	private List<PartMachine> machines;
	private Stack<CarPart> production;
	private Map<Integer, CarPart> partCatalog;
	private Map<Integer, List<CarPart>> inventory;
	private List<Order> orders;
	private Map<Integer, Integer> defectives;

	/**
	 * Creates new instance of CarPartFactory
	 * 
	 * @param orderPath
	 * @param partsPath
	 * @throws IOException
	 */
	public CarPartFactory(String orderPath, String partsPath) throws IOException {
		setupMachines(partsPath);
		setupOrders(orderPath);
		setupInventory();
		setupCatalog();
		this.production = new LinkedStack<CarPart>();
		this.defectives = new HashTableSC<Integer, Integer>(2, new BasicHashFunction());
	}

	/**
	 * Gets machines from instance
	 * 
	 * @return
	 */
	public List<PartMachine> getMachines() {
		return this.machines;
	}

	/**
	 * Changes machines of instance
	 * 
	 * @param machines
	 */
	public void setMachines(List<PartMachine> machines) {
		this.machines = machines;
	}

	/**
	 * Gets production of instance
	 * 
	 * @return production
	 */
	public Stack<CarPart> getProductionBin() {
		return this.production;
	}

	/**
	 * Change production of instance
	 * 
	 * @param production
	 */
	public void setProductionBin(Stack<CarPart> production) {
		this.production = production;
	}

	/**
	 * Gets catalog of instance
	 * 
	 * @return
	 */
	public Map<Integer, CarPart> getPartCatalog() {
		return this.partCatalog;
	}

	/**
	 * Changes catalog of instance
	 * 
	 * @param partCatalog
	 */
	public void setPartCatalog(Map<Integer, CarPart> partCatalog) {
		this.partCatalog = partCatalog;
	}

	/**
	 * Gets inventory of instance
	 * 
	 * @return inventory
	 */
	public Map<Integer, List<CarPart>> getInventory() {
		return this.inventory;
	}

	/**
	 * Changes inventory of instance
	 * 
	 * @param inventory
	 */
	public void setInventory(Map<Integer, List<CarPart>> inventory) {
		this.inventory = inventory;
	}

	/**
	 * Gets Order list of instance
	 * 
	 * @return orders
	 */
	public List<Order> getOrders() {
		return this.orders;
	}

	/**
	 * Changes Order list of instance
	 * 
	 * @param orders
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * Gets defectives map of instance
	 * 
	 * @return defectives
	 */
	public Map<Integer, Integer> getDefectives() {
		return this.defectives;
	}

	/**
	 * Changes defectives of instance
	 * 
	 * @param defectives
	 */
	public void setDefectives(Map<Integer, Integer> defectives) {
		this.defectives = defectives;
	}

	/**
	 * Gets Orders from CSV file and parses it to the Order format, setting
	 * fulfilled as false by default, into an ArrayList for efficient access.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void setupOrders(String path) throws IOException {
		this.orders = new ArrayList<Order>();
		BufferedReader file = new BufferedReader(new FileReader(path));
		String line = file.readLine();

		while ((line = file.readLine()) != null) {
			String[] fields = line.split(",");
			Map<Integer, Integer> parts = new HashTableSC<Integer, Integer>(20, new BasicHashFunction());

			for (String part : fields[2].split("-")) {
				String[] arr = new String[2];
				arr[0] = part.replaceAll("[()]", "").trim().split(" ")[0];
				arr[1] = part.replaceAll("[()]", "").trim().split(" ")[1];
				parts.put(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			}

			Order order = new Order(Integer.parseInt(fields[0]), fields[1], parts, false);

			orders.add(order);
		}

		file.close();
	}

	/**
	 * Gets Orders from CSV file and parses it to the PartMachine format, setting
	 * the CarPart's defective as false by default, into an ArrayList for efficient
	 * access.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void setupMachines(String path) throws IOException {
		this.machines = new ArrayList<PartMachine>();
		BufferedReader file = new BufferedReader(new FileReader(path));
		String line = file.readLine();

		while ((line = file.readLine()) != null) {
			String[] fields = line.split(",");
			CarPart carPart = new CarPart(Integer.parseInt(fields[0]), fields[1], Double.parseDouble(fields[2]), false);

			PartMachine part = new PartMachine(Integer.parseInt(fields[0]), carPart, Integer.parseInt(fields[4]),
					Double.parseDouble(fields[3]), Integer.parseInt(fields[5]));

			machines.add(part);
		}

		file.close();
	}

	/**
	 * Initializes catalog as a map of PartMachine's CartPart ID and PartMachine's
	 * CarPart
	 */
	public void setupCatalog() {
		this.partCatalog = new HashTableSC<Integer, CarPart>(2, new BasicHashFunction());

		for (PartMachine machine : machines)
			partCatalog.put(machine.getPart().getId(), machine.getPart());
	}

	/**
	 * Initializes inventory with the amount of machines and empty LinkedLists given
	 * data will often be added/removed
	 */
	public void setupInventory() {
		this.inventory = new HashTableSC<Integer, List<CarPart>>(machines.size(), new BasicHashFunction());

		for (int i = 1; i <= machines.size(); i++)
			inventory.put(i, new SinglyLinkedList<CarPart>());
	}

	/**
	 * Uses data from the production stack and sorts it into the inventory map if
	 * its not defective or into the defective map otherwise
	 */
	public void storeInInventory() {
		while (!production.isEmpty()) {
			CarPart part = production.pop();

			if (!part.isDefective()) {
				List<CarPart> tempList = inventory.get(part.getId());
				tempList.add(part);
				inventory.put(part.getId(), tempList);
			} else
				defectives.put(part.getId(),
						(defectives.containsKey(part.getId()) ? defectives.get(part.getId()) + 1 : 1));
		}
	}

	/**
	 * Runs each machine every minute minute of everyday. At the end of the day, the
	 * parts produced are sorted into the inventory and after the final day of
	 * production the orders are processed
	 * 
	 * @param days
	 * @param minutes
	 */
	public void runFactory(int days, int minutes) {
		// day cycle
		for (int i = 0; i < days; i++) {
			// minutes cycle
			for (int z = 0; z < minutes; z++) {
				for (PartMachine machine : machines) {
					CarPart part = machine.produceCarPart();

					if (part != null)
						production.push(part);
				}
			}

			for (PartMachine machine : machines) {
				for (CarPart part : machine.getPartsOnConveyorBelt())
					production.push(part);

				machine.resetConveyorBelt();
			}

			storeInInventory();
		}

		processOrders();
	}

	/**
	 * Loops through each order and checks if the quantity of requested parts are
	 * available in the inventory. If there is enough then the order is set to true
	 * and the amount of parts from the order's requested is removed from the
	 * inventory. Otherwise, the order stays false.
	 */
	public void processOrders() {
		for (Order order : orders) {
			boolean flag = true;

			for (int key : order.getRequestedParts().getKeys())
				if (order.getRequestedParts().get(key) > this.inventory.get(key).size()) {
					flag = false;
					break;
				}

			if (flag) {
				order.setFulfilled(true);

				for (int key : order.getRequestedParts().getKeys())
					for (int i = 0; i < order.getRequestedParts().get(key); i++) {
						List<CarPart> temp = this.inventory.get(key);
						temp.remove(0);
						inventory.put(key, temp);
					}
			}
		}
	}

	/**
	 * Generates a report indicating how many parts were produced per machine, how
	 * many of those were defective and are still in inventory. Additionally, it
	 * also shows how many orders were successfully fulfilled.
	 */
	public void generateReport() {
		String report = "\t\t\tREPORT\n\n";
		report += "Parts Produced per Machine\n";
		for (PartMachine machine : this.getMachines()) {
			report += machine + "\t(" + this.getDefectives().get(machine.getPart().getId()) + " defective)\t("
					+ this.getInventory().get(machine.getPart().getId()).size() + " in inventory)\n";
		}

		report += "\nORDERS\n\n";
		for (Order transaction : this.getOrders()) {
			report += transaction + "\n";
		}
		System.out.println(report);
	}

}
