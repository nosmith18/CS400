
public class TestRBTreeAPI {

  
  public static boolean testRemove68() {
    RBTreeAPI test = new RBTreeAPI();
    test.insert(new Vehicle("123456789A1234567", 100, 1990, 10000, "SUV", "Red", false));
    test.insert(new Vehicle("123456789B1234567", 100, 1990, 10000, "Sedan", "Blue", false));
    test.insert(new Vehicle("123456789C1234567", 100, 1990, 10000, "Truck", "Green", false));

    System.out.println(test.toString());
    System.out.println(test.getVehicle("123456789B1234567"));
    
    System.out.println(test.remove("123456789B1234567"));
    System.out.println(test.toString());
    return true;
  }
  
  
  
  
  
  public static void main(String[] args) {
    System.out.println(testRemove68());

  }

}
