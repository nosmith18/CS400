// --== CS400 File Header Information ==--
// Name: Yun Su Um
// Email: um5@wisc.edu
// Team: MA
// Role: Front End Developer 1
// TA: Harit Vishwakarma
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.util.*;

public class Driver{
    static Inventory inventory = new Inventory();

    public static void main (String[] args){
        System.out.println("\n\n--------------------------------------------------------");
        System.out.println("   Welcome to the Grocery Store Inventory Checker");
        System.out.println("--------------------------------------------------------\n");
        
        System.out.println("Importing all items in the inventory...");

        try{
            DataHandler.loadAll(inventory,"./Inventory/inventory.csv");
        }catch(Exception e){
            System.out.println("Error occured while importing inventory:");
            System.out.println(e.getMessage());
            return;
        }
        
        help();

        Scanner sc = new Scanner(System.in);
        String[] input = sc.nextLine().trim().split(" ");

        while(!input[0].equals("quit")){
            switch(input[0]){
                case "help":
                    help();
                    break;
                case "items":
                    items();
                    break;
                case "search":
                    search(input);
                    break;
                case "remove":
                    remove(input);
                    DataHandler.saveAll(inventory, "./Inventory/inventory.csv");
                    break;
                case "add":
                    add(input);
                    DataHandler.saveAll(inventory, "./Inventory/inventory.csv");
                    break;
            
                default:
                    System.out.println("Error: " + input[0] + " is an unrecognized commmand.");
                    System.out.println("Use 'help' to see a list of valid commands.");
            }
            input = sc.nextLine().trim().split(" ");
        }
    }

    protected static void help(){
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Commands:");
        System.out.println("help: displays this command list");
        System.out.println("search: <item> displays whether the item exists in the inventory");
        System.out.println("items: displays a list of all items in the inventory");
        System.out.println("add: <name> <price> <stock> <aisle> adds the given item to the inventory");
        System.out.println("remove: <name> <number> if in inventory, removes the given item");
        System.out.println("quit: exit the application");
        System.out.println("--------------------------------------------------------");
    }

    protected static int search(String[] input){
        if(input.length < 2){
            System.out.println("The command is not formatted correctly. Please try again");
            return -1;
        }
        StringBuilder query = new StringBuilder();
        
        for(int i = 1; i < input.length; i++){
             query.append(input[i]);
             if(i!=input.length-1){
                 query.append(" ");
             }
        }
        
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(Item item : inventory.getMatching(query.toString())){
            sb.append(item.getName());
            for(int i = item.getName().length(); i < 30; i++){
                sb.append(" ");
            }
            sb.append("$");
            sb.append(item.getPrice());
            for(int i = String.valueOf(item.getPrice()).length(); i < 10; i++){
                sb.append(" ");
            }
            sb.append("Aisle: "+item.getAisle());
            for(int i = item.getAisle().length(); i < 10; i++){
                sb.append(" ");
            }
            sb.append(item.getStock());
            sb.append(" left\n");
            count++;
        }
        if(sb.length()==0){
            System.out.println("Couldn't find any matching items in the inventory");
            System.out.println("-----------------------------------------------------------");
            return 0;
        }
        System.out.println("\n\n             ------ Search Result ------\n\n");
        System.out.println(sb.toString());
        System.out.println("-----------------------------------------------------------");
        return count;
    }

    protected static int items(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n             ------ Current Inventory ------\n\n\n");
        Iterator<Item> it = inventory.iterator();
        int count = 0;
        while(it.hasNext()){
            Item item = it.next();
            sb.append(item.getName());
            for(int i = item.getName().length(); i < 30; i++){
                sb.append(" ");
            }
            sb.append("$");
            sb.append(item.getPrice());
            for(int i = String.valueOf(item.getPrice()).length(); i < 10; i++){
                sb.append(" ");
            }
            sb.append("Aisle: "+item.getAisle());
            for(int i = item.getAisle().length(); i < 10; i++){
                sb.append(" ");
            }
            sb.append(item.getStock());
            sb.append(" left\n");
            count++;
        }
        sb.append("-----------------------------------------------------------");
        System.out.println(sb.toString());
        return count;
    }

    protected static boolean add(String[] input){
        //If invalid number of parameters
        if(input.length < 4){
            System.out.println("Error. Insufficient information given. Please try again.");
            System.out.println("-----------------------------------------------------------");
            return false;
        }
        
        StringBuilder name = new StringBuilder();
        
        for(int i = 1; i < input.length-3; i++){
             name.append(input[i]);
             if(i!=input.length-4){
                 name.append(" ");
             }
        }
        
        try{
            Item item = new Item(name.toString(), Double.parseDouble(input[input.length-3]),
            Integer.parseInt(input[input.length-2]), name.toString().toLowerCase()+".png", 
            input[input.length-1]);
            inventory.add(item);
        } catch (Exception e){
            System.out.println("******* Error while adding the item *********");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("******* Successfully added the item! *********\n");
        System.out.println("-----------------------------------------------------------");
        return true;
    }

    protected static boolean remove(String[] input){
        //If invalid number of parameters
        if(input.length < 3){
            System.out.println("Error. The input format is incorrect. Please type 'help'");
            System.out.println("-----------------------------------------------------------\n");
            return false;
        }

        StringBuilder name = new StringBuilder();
        
        for(int i = 1; i < input.length-1; i++){
             name.append(input[i]);
             if(i!=input.length-2){
                 name.append(" ");
             }
        }
        for(Item item : inventory.getMatching(name.toString())){
            if(item.getName().equalsIgnoreCase(name.toString())){
                if((item.getStock()-Integer.parseInt(input[input.length-1])) < 0){
                    System.out.println("Error. You cannot remove more than the items availible.");
                    System.out.println("-----------------------------------------------------------");
                    return false;
                }
                if((item.getStock()-Integer.parseInt(input[input.length-1])) == 0){
                    inventory.remove(name.toString());
                }else{
                    item.setStock(item.getStock()-Integer.parseInt(input[input.length-1]));
                }
                System.out.println("Successfully removed the stock.");
                System.out.println("-----------------------------------------------------------");
                return  true;
            }
        }
            System.out.println("Error. The item does not exist in the inventory");
            System.out.println("-----------------------------------------------------------");
            return false;
    }
    
}