package zoo;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

/**
 * All the zoo commands should go in this file
 * These functions will call the actual worker function in other classes
 * @author damongeorge
 * @author anthonyniehuser
 *
 */
@ShellComponent
public class ZooCommands {

    public Availability adminCheck() { 
        return Session.admin
            ? Availability.available()
            : Availability.unavailable("Admin priveleges required");
    }
   
    @ShellMethod(value="Employee Functions: list, search, add, update, view, train", key={"employees", "emp"} )
    @ShellMethodAvailability("adminCheck") 
    public void employeeCommands(	
    		@ShellOption(help="List all employees")boolean list, 
    		@ShellOption(help="View the given employee's details", defaultValue="__NULL__") String view, 
    		@ShellOption(help="Add a new employee") boolean add, 
			@ShellOption(help="Update an employee's information", defaultValue="__NULL__")String update,
			@ShellOption(help="Search employees by their details", defaultValue="__NULL__")String searchEmployee, 
			@ShellOption(help="Search employees by the animals they handle", defaultValue="__NULL__")String searchAnimal, 
			@ShellOption(help="Search employees by the enclosures they work in", defaultValue="__NULL__")String searchEnclosure,
			@ShellOption(help="Update an employee's training", defaultValue="__NULL__")String train
			 ) {
    
    	if(list) {
    		EmployeeRepo.listAllEmployees();
    	} else if (view != null) {
    		EmployeeRepo.viewEmployee(view);
    		
    	} else if (add) {
    		String values[] = new String[8];
    		if(!InputHandler.getEmployeeInfo(values))
    			return;
    		
    		EmployeeRepo.addEmployee(values);
    	} else if (update != null) {
			if(!EmployeeRepo.employeeExists(update)) {
    			System.out.println("User " + update + " doesn't exist! ");
    			return;
    		} 
			
			String values[] = new String[8];
			if(!InputHandler.getEmployeeInfo(values))
    			return;
    		EmployeeRepo.updateEmployee(update, values);
    		
    	} else if (searchEmployee != null) {
    		EmployeeRepo.searchEmployeesByEmployee(searchEmployee);
    	} else if (searchAnimal != null) {
    		EmployeeRepo.searchEmployeesByAnimal(searchAnimal);
    	} else if (searchEnclosure != null) {
    		EmployeeRepo.searchEmployeesByEnclosure(searchEnclosure);
    	} else if (train != null) {
    		if(!EmployeeRepo.employeeExists(train)){
    			System.out.println("Employee does not exist!");
    			return;
    		}
    		String values[] = new String[3];
    		if(!InputHandler.getTrainingInfo(values))
    			return;
    		
    		EmployeeRepo.trainEmployee(train, values);
    		
     	}else {
    		System.out.println("Please select an option!" );
    	}
    }
    
    @ShellMethod(value="Animal Functions: list, search, add, update, view", key={"animals", "an"} )
    public void animalCommands(	
    		@ShellOption(help="List all animals")boolean list, 
    		@ShellOption(help="View the details of the animal with the given id", defaultValue="__NULL__") String view, 
    		@ShellOption(help="Add a new animal") boolean add, 
			@ShellOption(help="Update an animal's information", defaultValue="__NULL__")String update,
			@ShellOption(help="Search animals by their details", defaultValue="__NULL__")String searchAnimal, 
			@ShellOption(help="Search animals by their enclosures", defaultValue="__NULL__")String searchEnclosure, 
			@ShellOption(help="Search animals by their handlers", defaultValue="__NULL__")String searchEmployee
			 ) {
    
    	if(list) {
    		AnimalRepo.listAllAnimals();
    	} else if (view != null) {
    		AnimalRepo.viewAnimal(view);
    	} else if (add) {
    		String values[] = new String[6];    		
    		
    		if(!InputHandler.getAnimalInfo(values, true))
    			return;
    		
    		AnimalRepo.addAnimal(values);
    		
    	}else if (update != null) {
    		String values[] = new String[5];
    		if(!update.matches("\\d+")) {
    			System.out.println("Please input a number for the animal id");
    			return;
    		}
    		if(!AnimalRepo.animalExists(update)) {
    			System.out.println("Animal #" + update + " doesn't exist! ");
    			return;
    		}
    		
    		if(!InputHandler.getAnimalInfo(values, false))
    			return;
    		
    		AnimalRepo.updateAnimal(update, values);
    		
    	} else if (searchAnimal != null) {
    		AnimalRepo.searchAnimalsByAnimal(searchAnimal);
    	} else if (searchEnclosure != null) {
    		AnimalRepo.searchAnimalsByEnclosure(searchEnclosure);
    	} else if (searchEmployee != null) {
    		AnimalRepo.searchAnimalByEmployee(searchEmployee);
    	} else {
    		System.out.println("Please select an option!" );
    	}
    }
    
    @ShellMethod(value="Enclosure Functions: list, add, open, close", key={"enclosures", "enc"} )
    public void enclosureCommands(	
    		@ShellOption(help="List all enclosures") boolean list,
    		@ShellOption(help="Add a new enclosure") boolean add,
    		@ShellOption(help="Open the given enclosures", defaultValue="__NULL__") String open,
    		@ShellOption(help="Close the given enclosures", defaultValue="__NULL__") String close
			 ) {
    
    	if(list) {
    		EnclosureRepo.listAllEnclosures();
    	} else if (add) {
    		String values[] = new String[4];
    		if(!InputHandler.getEnclosureInfo(values))
    			return;
    		
    		EnclosureRepo.addEnclosure(values);
    		
    	} else if (open != null) {
    		if(!open.matches("\\d+")) {
    			System.out.println("Please input a number for the enclosure id");
    			return;
    		}
    		if(!EnclosureRepo.enclosureExists(open)) {
    			System.out.println("Enclosure #" + open + " doesn't exist! ");
    			return;
    		}
    		
    		EnclosureRepo.setOpen(open, true);
    		
    	} else if (close != null) {
    		if(!close.matches("\\d+")) {
    			System.out.println("Please input a number for the enclosure id");
    			return;
    		}
    		if(!EnclosureRepo.enclosureExists(close)) {
    			System.out.println("Enclosure #" + close + " doesn't exist! ");
    			return;
    		}
    		
    		EnclosureRepo.setOpen(close, false);
    	} else {
    		System.out.println("Please select an option!" );
    	}
    }
    
    @ShellMethod(value="Species Functions: list, add, info", key={"species", "sp"} )
    public void speciesCommands(	
    		@ShellOption(help="List all species") boolean list,
    		@ShellOption(help="Add a new species") boolean add,
    		@ShellOption(help="Feeding Info", defaultValue="__NULL__") String info 
    		) {
    
    	if(list) {
    		SpeciesRepo.listAllSpecies();
    	} else if (add) {
    		String values[] = new String[4];
    		
    		InputHandler.getSpeciesInfo(values);
    		
    		SpeciesRepo.addSpecies(values);
    	}else if(info!=null){
    		if(!SpeciesRepo.speciesExists(info)){
    			System.out.println("This species does not exist!");
    			return;
    		}
    		SpeciesRepo.speciesHandlerAndFoodStats(info);
    	} else {
    		System.out.println("Please select an option!" );
    	}
    }
    
    @ShellMethod(value="Food Functions: list, add, purchase, feed", key={"food", "fd"} )
    public void foodCommands(	
    		@ShellOption(help="List all foods") boolean list,
    		@ShellOption(help="Add a new food") boolean add,
    		@ShellOption(help="Add new quantity of given food", defaultValue="__NULL__") String purchase,
    		@ShellOption(help="Feed the given animal", defaultValue="__NULL__") String feed
    		) {
    
    	if(list) {
    		FoodRepo.listAllFood();
    	} else if (add) {
    		String values[] = new String[6];
    		if(!InputHandler.getFoodInfo(values)){
    			return;
    		}
    		
    		FoodRepo.addFood(values);
    	
    	} else if (purchase != null) { 
    		if(!purchase.matches("\\d+")){
    			System.out.println("Must input a number for food_id");
    			return;
    		}
    		if(!FoodRepo.foodExists(purchase)){
    			System.out.println("Food does not exist.");
    			return;
    		}
    		String quantity[] = new String[1];
    		if(!InputHandler.getFoodQuanity(quantity))
    			return;
    		
    		FoodRepo.buyFood(purchase, quantity[0]);
    		
    	} else if (feed != null) {
    		if(!feed.matches("\\d+")){
    			System.out.println("Must input a number for food_id.");
    			return;
    		}
    		if(!FoodRepo.foodExists(feed)){
    			System.out.println("Food does not exist.");
    			return;
    		}
    		String animal[] = new String[1];
    		if(!InputHandler.getAnimalID(animal))
    			return;
    		
    		FoodRepo.feedAnimal(animal[0], feed);
    		
    	} else {
    		System.out.println("Please select an option!" );
    	}
    }
    
    @ShellMethod(value="Statistic Functions: salaries, handlers, species, enclosures,\n\t\t\t\t\t  food", key={"stat", "stats"} )
    public void statCommands(	
    		@ShellOption(help="Salary Statistics") boolean salaries,
    		@ShellOption(help="Number of trainings per employee") boolean handlers,
    		@ShellOption(help="Species Statistics") boolean species,
    		@ShellOption(help="Enclosure Statistics") boolean enclosures,
    		@ShellOption(help="Food Statistics") boolean food
    		) {
    	if(salaries) {
    		StatsRepo.salaryStats();
    	} else if (handlers) {
    		StatsRepo.handlerStats();
    	} else if (species) {
    		StatsRepo.speciesStats();
    	} else if (enclosures) {
    		StatsRepo.enclosureStats();
    	} else if (food) {
    		StatsRepo.foodStats();
    	} else {
    		System.out.println("Please select an option!" );
    	}
    }
}
