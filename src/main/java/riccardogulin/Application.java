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

		// 1. Raggruppiamo gli utenti minorenni per città
		Map<String, List<User>> usersByCity = users.stream().filter(user -> user.getAge() < 18).collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, usersList) -> System.out.println("Citta: " + city + ", " + usersList));

		// 2. Raggruppiamo gli utenti per età
		Map<Integer, List<User>> usersByAge = users.stream().collect(Collectors.groupingBy(user -> user.getAge()));
		usersByAge.forEach((age, usersList) -> System.out.println("Età: " + age + ", " + usersList));

		// 3. Concateniamo tutti i nomi ed i cognomi in un'unica stringa: "Aldo Baglio. Giovanni Storti. Giacomo Poretti. ...."
		String namesAndSurnames = users.stream().map(user -> user.getName() + " " + user.getSurname()).collect(Collectors.joining(". "));
		System.out.println(namesAndSurnames);

		// 4. Concateniamo tutte le età
		String ages = users.stream().map(user -> "" + user.getAge()).collect(Collectors.joining(". "));
		System.out.println(ages);

		// 5. Calcolo della somma di tutte le età
		int total = users.stream().collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println("La somma delle età è: " + total);

		// 6. Calcolo la media delle età
		double average = users.stream().collect(Collectors.averagingInt(user -> user.getAge()));
		System.out.println("La media delle età è: " + average);

		// 7. Raggruppiamo gli utenti per città e calcoliamo l'età media per ogni città
		Map<String, Double> averageAgePerCity = users.stream()
				.collect(
						Collectors.groupingBy(
								user -> user.getCity(),
								Collectors.averagingInt(user -> user.getAge())
						)
				);
		averageAgePerCity.forEach((city, averageAge) -> System.out.println("Città: " + city + ", " + averageAge));

		// 8. Raggruppiamo gli utenti per città e calcoliamo tutta una serie di statistiche su quegli utenti, come età massima, età minima, media delle età....
		Map<String, IntSummaryStatistics> statsPerCity = users.stream().collect(
				Collectors.groupingBy(
						user -> user.getCity(),
						Collectors.summarizingInt(user -> user.getAge())
				)
		);

		statsPerCity.forEach((city, stats) -> System.out.println("Città: " + city + ", " + stats));

	}
}
