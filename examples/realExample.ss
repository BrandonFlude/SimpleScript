// This example takes input from a text file and adds it to a dictionary directly.
// From there, a user is able to select and print out the value of a key based on the file input
// This example also highlights the use(<func>) option, to use a different function instead of the one called

filename: "examples/realExample.txt"
months<>

func populateDict()
{
	// Open, read, process and close the file
	openfile(filename)
	
	// Populate the dictionary
	for(i: 1; i LESS THAN 13; i++)
	{
		key: i
		value: readfile[i]
		months.add(key, value)
	}
	
	// Close the file
	closefile
	
	// Cycle through dictionary and print values
	for(i: 1; i LESS THAN 13; i++)
	{
		write("For key: "+i)
		months.write(i)
	}
}


func populateDictLongWay()
{
	use(populateDict) // Use new function from file instead, comment line to see this func instead
	months.add(1, "January")
	months.add(2, "February")
	months.add(3, "March")
	months.add(4, "April")
	months.add(5, "May")
	months.add(6, "June")
	months.add(7, "July")
	months.add(8, "August")
	months.add(9, "September")
	months.add(10, "October")
	months.add(11, "November")
	months.add(12, "December")
	
	// Write them out
	months.write(1)
	months.write(2)
	months.write(3)
	months.write(4)
	months.write(5)
	months.write(6)
	months.write(7)
	months.write(8)
	months.write(9)
	months.write(10)
	months.write(11)
	months.write(12)
}

populateDictLongWay()