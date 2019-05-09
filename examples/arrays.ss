numbers[]: [10, 5, 7, 1003, 4, 1, 0]
names[]: ["John", "Dave", "Chris", "Wayne", "George", "Stanley", "Brandon", "Danny"]

j: sizeof(numbers)
k: sizeof(names)

write("Unsorted Arrays:")

for(i: 0; i LESS THAN j; i++)
{
	 write(numbers[i])
}

for(i: 0; i LESS THAN k; i++)
{
	write(names[i])
}
write("Sorted Arrays:")

sort(numbers)

for(i: 0; i LESS THAN j; i++)
{
	write(numbers[i])
}

sort(names)

for(i: 0; i LESS THAN k; i++)
{
	write(names[i])
}
