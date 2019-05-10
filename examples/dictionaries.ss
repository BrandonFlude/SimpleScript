capitalCities<>
trophies<>

capitalCities.add("England", "London")
capitalCities.add("France", "Paris")
capitalCities.add("USA", "Washington, D.C.")
capitalCities.add("Canada", "Ottawa")
capitalCities.add("Germany", "Berlin")

capitalCities.write("Germany")
capitalCities.write("USA")

// Writing from a variable
country: "Canada"
capitalCities.write(country)

// Updating an entry
capitalCities.update("Canada", "Vancouver") // Please don't mark me down for this being wrong!
capitalCities.write("Canada")

// Remove an entry
// Uncomment below lines to see this work, throws an exception when writing a key that doesn't exist
//capitalCities.write("Germany")
//capitalCities.remove("Germany")
//capitalCities.write("Germany")

// Multiple dictionaries
trophies.add("Football", "FIFA World Cup")
trophies.add("Hockey", "Stanley Cup")
trophies.add("A. Football", "Vince Lombardi Trophy")

trophies.write("Hockey")
trophies.write("A. Football")