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


		System.out.println("************************************************** COMPARATORS ***************************************************");

		// 1. Ordiniamo gli utenti per età crescente
		List<User> sortedUsers = users.stream().sorted(Comparator.comparing(user -> user.getAge())).toList();
		sortedUsers.forEach(user -> System.out.println(user));

		// 2. Ordiniamo gli utenti per età decrescente
		List<User> sortedUsersReverseOrder = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).toList();
		// se uso .reversed() non posso usare le lambda devo usare la sintassi con ::
		sortedUsersReverseOrder.forEach(user -> System.out.println(user));

		// 3. Ordiniamo gli utenti per cognome
		List<User> sortedUsersBySurname = users.stream().sorted(Comparator.comparing(user -> user.getSurname())).toList();
		sortedUsersBySurname.forEach(user -> System.out.println(user));

		// 4. Ordiniamo per età e raggruppiamo per città
		Map<String, List<User>> usersGroupedByCitySortedByAge = users.stream().sorted(Comparator.comparing(user -> user.getAge())).collect(Collectors.groupingBy(user -> user.getCity()));
		usersGroupedByCitySortedByAge.forEach((city, userList) -> System.out.println("Città: " + city + ", " + userList));


		System.out.println("************************************************** LIMIT ***************************************************");

		// 1. Ottengo lista 10 utenti più vecchi
		List<User> top10OldUsers = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).limit(10).toList();
		System.out.println(top10OldUsers);

		System.out.println("************************************************** SKIP ***************************************************");
		// 2. Ottengo la lista dei secondi 10 utenti più vecchi (dall'undicesimo al ventesimo)
		List<User> other10OldUsers = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).skip(10).limit(10).toList();
		System.out.println(other10OldUsers);


		System.out.println("************************************************** MAP TO ***************************************************");

		// 1. Calcolo della somma delle età tramite map+reduce
		int totalAges = users.stream().map(user -> user.getAge()).reduce(0, (partialSum, currentAge) -> partialSum + currentAge);
		System.out.println("Somma tramite reduce: " + totalAges);

		// 2. Calcolo della somma delle età tramite collect & summingInt
		int totalAges2 = users.stream().collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println("Somma tramite summingInt: " + totalAges2);

		// 3. Calcolo della somma delle età tramite mapToInt
		int totalAges3 = users.stream().mapToInt(user -> user.getAge()).sum();
		System.out.println("Somma tramite mapToInt: " + totalAges3);

		// 4. Calcolo della media delle età tramite mapToInt
		OptionalDouble average2 = users.stream().mapToInt(user -> user.getAge()).average();
		if (average2.isPresent()) System.out.println("La media è: " + average2.getAsDouble());
		else System.out.println("Non è stato possibile calcolare la media in quanto la lista è vuota!");

		// 5. Calcolo dell'età massima tramite mapToInt
		OptionalInt maxAge = users.stream().mapToInt(user -> user.getAge()).max();
		if (maxAge.isPresent()) System.out.println("L'età massima è: " + maxAge.getAsInt());
		else System.out.println("Non è stato possibile calcolare l'età massima in quanto la lista è vuota!");

		// 6. Calcolo delle statistiche sull'età tramite mapToInt
		IntSummaryStatistics stats = users.stream().mapToInt(user -> user.getAge()).summaryStatistics();
		System.out.println(stats);

	}
}
