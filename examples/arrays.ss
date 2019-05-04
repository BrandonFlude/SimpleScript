integerArray[]: [2, 1, 3, 4]
stringArray[]: ["c", "b", "a", "d", "e"]
mixedArray[]: ["cat", "box", "dog", 1, 3, 2]

j: sizeof(integerArray)
k: sizeof(stringArray)
l: sizeof(mixedArray)

write("Unsorted Arrays:")

for(i: 0; i LESS THAN j; i++)
{
	 write(integerArray[i])
}

for(i: 0; i LESS THAN k; i++)
{
	write(stringArray[i])
}

for(i: 0; i LESS THAN l; i++)
{
	write(mixedArray[i])
}

write("Sorted Arrays:")

sort(integerArray)

for(i: 0; i LESS THAN j; i++)
{
	write(integerArray[i])
}

sort(stringArray)

for(i: 0; i LESS THAN k; i++)
{
	write(stringArray[i])
}

sort(mixedArray)

for(i: 0; i LESS THAN l; i++)
{
	write(mixedArray[i])
}
