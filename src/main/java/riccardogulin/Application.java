package riccardogulin;

import com.github.javafaker.Faker;
import riccardogulin.entities.User;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) {
		Supplier<ArrayList<User>> randomUserListSupplier = () -> {
			ArrayList<User> randomUsers = new ArrayList<>();
			Faker faker = new Faker(Locale.ITALIAN);
			Random rndm = new Random();
			for (int i = 0; i < 100; i++) {
				randomUsers.add(new User(faker.lordOfTheRings().character(),
						faker.name().lastName(),
						rndm.nextInt(0, 100),
						faker.lordOfTheRings().location()));
			}
			return randomUsers;
		};

		ArrayList<User> users = randomUserListSupplier.get();
		users.forEach(user -> System.out.println(user));

		System.out.println("*********************************** COLLECTORS *******************************************");

		// 1. Raggruppiamo gli utenti per città
		Map<String, List<User>> usersByCity = users.stream().filter(user -> user.getAge() < 18).collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, usersList) -> System.out.println("Citta: " + city + ", " + usersList));

	}
}
