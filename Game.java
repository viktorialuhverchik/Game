import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;


public class Game {
    
    public static void showMenu(String[] arr) {
        System.out.println("Available moves:");
        for (int i = 0; i < arr.length; i++) {
            System.out.println((i + 1) + " - " + arr[i]);
        }
        System.out.println("0 - exit");
    }

    public static int getUserMove(String[] arr) {
        showMenu(arr);
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your move: ");
        try {
            int numUser = scanner.nextInt();
            if (numUser > arr.length - 1 || numUser < 0) {
                System.out.println("Check your move!");
                return getUserMove(arr);
            }
            return numUser;
        }
        catch (Exception error) {
            System.out.println("Check your move!");
            return getUserMove(arr);
        }
    }
    public static void main(String[] args) {

        if (args.length <= 1) {
            System.out.println("You have entered an insufficient number of values!");
            return;
        } else if (args.length % 2 == 0 ) {
            System.out.println("You entered an even number of values!");
            return;
        }
        
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[256];
        random.nextBytes(bytes);
        String secret = Base64.getEncoder().encodeToString(bytes);

        int numComputer = ThreadLocalRandom.current().nextInt(0, args.length);
        String moveComputer = args[numComputer];

        try {            
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmac.init(secret_key);
       
            String hash = Base64.getEncoder().encodeToString(hmac.doFinal(moveComputer.getBytes()));
            System.out.println("HMAC:");
            System.out.println(hash);
            System.out.println("");
        }
        catch (Exception e){
            System.out.println(e);
        }

        int num = getUserMove(args);
        if (num == 0) {
            System.out.println("Good bye!");
            return; 
        }
        
        String moveUser = args[num-1];        
          
        System.out.printf("Your move: %s \n", moveUser);
        System.out.printf("Computer move: %s \n", moveComputer);

        if (num-1 == numComputer) {
            System.out.println("It's tie!");
        } else {
            boolean numberFound = false;

            for (int i = 1; i <= (args.length - 1) / 2; i++) {
                int value = i + numComputer;

                if (value >= args.length) {
                    value -= args.length;
                }

                if (num-1 == value) {
                    numberFound = true;
                }
            }

            if (numberFound) {
                System.out.println("You win!");
            } else {
                System.out.println("You lose!");
            }
        }

        System.out.println("HMAC secret:");
        System.out.println(secret);
    }
}