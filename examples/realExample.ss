// This example takes input from a text file and adds it to a dictionary
// From there, a user is able to select and print out the value of a key based on the file input

filename: "examples/realExample.txt"
months()

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
